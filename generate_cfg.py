import sys, os
import glob
import subprocess
import logging
import re

# Setup logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def main():
    directory = 'tests'
    pattern_java = os.path.join(directory, '*.java')
    pattern_txt = os.path.join(directory, '*.txt')
    java_files = glob.glob(pattern_java)
    txt_files = glob.glob(pattern_txt)
    
    if len(java_files) != len(txt_files):
        logging.error("Mismatch in number of .java and .txt files.")
        return

    java_path = java_files[0]
    txt_path = txt_files[0]  # Assuming you want to process the first txt file as well.
    total_lines = 0
    brace_count = 0
    start_line = 0
    with open(java_path, 'r') as java_file:
        lines = java_file.readlines()
        for i in range(len(lines)):
            line = lines[i].strip()
            if line.endswith('{') and brace_count <= 2:
                brace_count += 1
                start_line += 1
                total_lines += 1
            elif brace_count <= 2:
                start_line += 1
                total_lines += 1
            elif brace_count >= 2:
                total_lines += 1
    
    parse_bug_probabilities(txt_path, 'probabilities.txt', total_lines)
    try:
        with open(java_path, 'r') as java_file, open('probabilities.txt', 'r') as txt_file:
            lines = java_file.readlines()
            txt_lines = txt_file.readlines()
            
            # Dictionary to store the type of line information and color
            line_info = {i: (None, None) for i in range(start_line, total_lines)}
            brace_groups = []  # To store the pairs of opening and closing braces
            stack = []  # To keep track of nested structures
            
            for line_num in range(start_line, total_lines):
                line = lines[line_num - 1].strip()
                txt_line = txt_lines[line_num - 1].strip()
                line = remove_comments(line)
                txt_color = get_color_value(txt_line)
                if line.endswith('{'):
                    line_info = get_statement(line, line_info, line_num, txt_color)
                    stack.append(line_num)
                elif line.endswith('}'):
                    line_info[line_num] = ('}', txt_color)
                    if stack:
                        start_brace = stack.pop()
                        brace_groups.append((start_brace, line_num))
                elif line.endswith(';'):
                    check = return_check(line)
                    if check:
                        line_info[line_num] = ('r', txt_color)
                    else:
                        line_info[line_num] = (len(stack), txt_color) if stack else (False, txt_color)
            brace_groups = sorted(brace_groups, key=lambda x: x[0])  # Sort brace groups based on starting brace
            edges = []  # To store the edges for the graph
            nodes = set()  # To store unique nodes for the graph
            conditionals = 0  # To keep track of the number of conditionals processed
            loop_ends = []
            for line_num in range(start_line, total_lines - 1):
                if line_info.get(line_num) is not None:
                    if line_info[line_num][0] == False:
                        next_valid_line = find_next_valid_line(line_num, line_info)
                        if next_valid_line:
                            edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
                    elif isinstance(line_info.get(line_num)[0], int):
                        next_valid_line = find_next_valid_line(line_num, line_info)
                        while next_valid_line and line_info.get(next_valid_line) is not None and line_info.get(next_valid_line)[0] in ['ei', 'e']:
                            next_valid_line = find_outside_valid_line(next_valid_line, line_info, conditionals, brace_groups)
                        if next_valid_line:
                            if line_num not in loop_ends:
                                edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
                    elif line_info[line_num][0] == 'i':
                        if_edges, nodes = if_statement(line_info, line_num, conditionals, brace_groups, nodes)
                        edges.extend(if_edges)
                        conditionals += 1
                    elif line_info[line_num][0] == 'ei':
                        elseif_edges, nodes = else_if_statement(line_info, line_num, conditionals, brace_groups, nodes)
                        edges.extend(elseif_edges)
                        conditionals += 1
                    elif line_info[line_num][0] == 'e':
                        conditionals += 1
                    elif line_info[line_num][0] == 'w':
                        while_edges, nodes = while_loop(line_info, line_num, conditionals, brace_groups, nodes)
                        loop_end = loop_check(line_info, line_num, conditionals, brace_groups)
                        loop_ends.append(loop_end)
                        edges.extend(while_edges)
                        conditionals += 1
                    elif line_info[line_num][0] == 'f':
                        for_edges, nodes = for_loop(line_info, line_num, conditionals, brace_groups, nodes)
                        loop_end = loop_check(line_info, line_num, conditionals, brace_groups)
                        loop_ends.append(loop_end)
                        edges.extend(for_edges)
                        conditionals += 1
                        
            # Define the paths for the output files
            base_name = os.path.splitext(os.path.basename(java_path))[0]
            edges_file_path = os.path.join(directory, f"{base_name}_edges.dot")
            output_svg_path = os.path.join(directory, f"{base_name}_edges.svg")
            # Write edges to the .dot file
            with open(edges_file_path, 'w') as edges_file:
                edges_file.write("strict digraph {\n")
                for node in sorted(nodes):  # Sort to ensure consistent output
                    edges_file.write(node + "\n")
                for entry in edges:
                    edges_file.write(entry + ";\n")
                edges_file.write("}")

            # Create the SVG file using the dot command
            subprocess.run(["dot", "-Tsvg", edges_file_path, "-o", output_svg_path], check=True)

    except Exception as e:
        exc_type, exc_obj, exc_tb = sys.exc_info()
        fname = os.path.split(exc_tb.tb_frame.f_code.co_filename)[1]
        print(exc_type, fname, exc_tb.tb_lineno)


def else_if_statement(line_info, line_num, og_conditionals, brace_groups, nodes):
    """
    Handle the logic for else if statements.

    Args:
        line_info (dict): Dictionary containing information about each line.
        line_num (int): The current line number.
        og_conditionals (int): The original count of conditionals processed.
        brace_groups (list): List of pairs of opening and closing braces.
        nodes (set): Set of nodes for the graph.

    Returns:
        tuple: List of edges to be added to the graph and updated set of nodes.
    """
    edges = []
    conditionals = og_conditionals
    if conditionals >= len(brace_groups):
        return edges, nodes  # No more brace groups to process
    start_line, end_line = brace_groups[conditionals]
    next_valid_line = find_next_valid_line(line_num, line_info)
    
    if next_valid_line is not None and isinstance(line_info.get(next_valid_line, (None,))[0], int):
        edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
        
    valid_end_line = find_next_valid_line(end_line, line_info)
    if valid_end_line is not None and line_info.get(valid_end_line, (None,))[0] == False:
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    elif valid_end_line is not None and line_info.get(valid_end_line, (None,))[0] in ['ei', 'r', 'i', 'w', 'f']:
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    elif valid_end_line is not None and line_info.get(valid_end_line, (None,))[0] in ['e']:
        valid_end_line = find_next_valid_line(valid_end_line, line_info)
        if valid_end_line is not None:
            edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    
    return edges, nodes

def if_statement(line_info, line_num, conditionals, brace_groups, nodes):
    """
    Handle the logic for if statements.

    Args:
        line_info (dict): Dictionary containing information about each line.
        line_num (int): The current line number.
        conditionals (int): The original count of conditionals processed.
        brace_groups (list): List of pairs of opening and closing braces.
        nodes (set): Set of nodes for the graph.

    Returns:
        tuple: List of edges to be added to the graph and updated set of nodes.
    """
    edges = []
    if conditionals >= len(brace_groups):
        return edges, nodes  # No more brace groups to process
    start_line, end_line = brace_groups[conditionals]
    
    next_valid_line = find_next_valid_line(line_num, line_info)
    if next_valid_line is not None and isinstance(line_info.get(next_valid_line, (None,))[0], int):
        edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
    if next_valid_line is not None and line_info.get(next_valid_line, (None,))[0] in ['i']:
        edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
        cond_start_line, cond_end_line = brace_groups[conditionals]
        
        next_valid_line = find_next_valid_line(cond_end_line, line_info)
        if next_valid_line is not None and (line_info.get(next_valid_line, (None,))[0] == conditionals + 1 or line_info.get(next_valid_line, (None,))[0] in ['ei', 'w', 'f', 'r', 'i']):
            edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)  
    elif next_valid_line is not None and line_info.get(next_valid_line, (None,))[0] in ['r']:
        edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
    
    
    valid_end_line = find_next_valid_line(end_line, line_info)
    
    if valid_end_line is not None and line_info.get(valid_end_line, (None,))[0] == False:
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    elif valid_end_line is not None and isinstance(line_info.get(valid_end_line, (None,))[0], int):
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    
    if valid_end_line is not None and line_info.get(valid_end_line, (None,))[0] in ['ei', 'w', 'f', 'r', 'i']:
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    
    elif valid_end_line is not None and line_info.get(valid_end_line, (None,))[0] in ['e']:
        next_brace = find_next_brace(end_line, line_info)
        if next_brace > end_line and next_brace < valid_end_line:
             next_start, next_end = brace_groups[conditionals+1]
             valid_end_line = find_next_valid_line(next_end,line_info)
             if valid_end_line is not None:
                edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
        else:
            valid_end_line = find_next_valid_line(valid_end_line, line_info)
            if valid_end_line is not None:
                edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    return edges, nodes

def while_loop(line_info, line_num, conditionals, brace_groups, nodes):
    """
    Handle the logic for while loops.

    Args:
        line_info (dict): Dictionary containing information about each line.
        line_num (int): The current line number.
        conditionals (int): The original count of conditionals processed.
        brace_groups (list): List of pairs of opening and closing braces.
        nodes (set): Set of nodes for the graph.

    Returns:
        tuple: List of edges to be added to the graph and updated set of nodes.
    """
    edges = []
    if conditionals >= len(brace_groups):
        return edges, nodes  # No more brace groups to process
    start_line, end_line = brace_groups[conditionals]
    next_valid_line = find_next_valid_line(line_num, line_info)
    valid_end_line = find_outside_valid_line(line_num, line_info, conditionals, brace_groups)
    prev_valid_line = find_prev_valid_line(end_line, line_info)
    if prev_valid_line is not None:
        edges, nodes = create_edge(prev_valid_line, line_num, edges, nodes, line_info)
    if valid_end_line is not None:
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    if next_valid_line is not None and isinstance(line_info.get(next_valid_line, (None,))[0], int):
        edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
    
    return edges, nodes

def for_loop(line_info, line_num, conditionals, brace_groups, nodes):
    """
    Handle the logic for for loops.

    Args:
        line_info (dict): Dictionary containing information about each line.
        line_num (int): The current line number.
        conditionals (int): The original count of conditionals processed.
        brace_groups (list): List of pairs of opening and closing braces.
        nodes (set): Set of nodes for the graph.

    Returns:
        tuple: List of edges to be added to the graph and updated set of nodes.
    """
    edges = []
    if conditionals >= len(brace_groups):
        return edges, nodes  # No more brace groups to process
    start_line, end_line = brace_groups[conditionals]
    next_valid_line = find_next_valid_line(line_num, line_info)
    valid_end_line = find_outside_valid_line(line_num, line_info, conditionals, brace_groups)
    prev_valid_line = find_prev_valid_line(end_line, line_info)

    if valid_end_line is not None:
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    if prev_valid_line is not None:
        if line_info.get(prev_valid_line, (None,))[0] != 'r':
            edges, nodes = create_edge(prev_valid_line, line_num, edges, nodes, line_info)
    if valid_end_line is not None:
        edges, nodes = create_edge(line_num, valid_end_line, edges, nodes, line_info)
    if next_valid_line is not None:
        if isinstance(line_info.get(next_valid_line, (None,))[0], int) or line_info.get(next_valid_line, (None,))[0] in ['ei', 'r', 'i', 'w', 'f']:
            edges, nodes = create_edge(line_num, next_valid_line, edges, nodes, line_info)
    
    return edges, nodes

def get_statement(line, line_info, line_num, txt_color):
    """
    Determine the type of statement and update line_info accordingly.

    Args:
        line (str): The line of code.
        line_info (dict): Dictionary containing information about each line.
        line_num (int): The current line number.

    Returns:
        dict: Updated line_info dictionary.
    """
    if line.startswith('if'):
        line_info[line_num] = ('i', txt_color)
    elif line.startswith('else if'):
        line_info[line_num] = ('ei', txt_color)
    elif line.startswith('else'):
        line_info[line_num] = ('e', txt_color)
    elif line.startswith('while'):
        line_info[line_num] = ('w', txt_color)
    elif line.startswith('for'):
        line_info[line_num] = ('f', txt_color)
    return line_info

def loop_check(line_info, line_num, conditionals, brace_groups):
    start_line, end_line = brace_groups[conditionals]
    loop_end = find_prev_valid_line(end_line, line_info)
    if line_info[loop_end][0] in ['r', 'e']:
        return
    else:
        return find_prev_valid_line(end_line, line_info)
    

def find_next_brace(line_num, line_info):
    for i in range(line_num + 1, max(line_info.keys()) + 1):
        if line_info.get(i, (None,))[0] == '}':
            return i
    return None
def find_next_valid_line(line_num, line_info):
    """
    Find the next valid line number that contains relevant information.

    Args:
        line_num (int): The current line number.
        line_info (dict): Dictionary containing information about each line.

    Returns:
        int: The next valid line number.
    """
    
    for i in range(line_num + 1, max(line_info.keys()) + 1):
        if line_info.get(i, (None,))[0] is not None and line_info.get(i, (None,))[0] != '}':
            return i
    return None

def find_prev_valid_line(line_num, line_info):
    """
    Find the previous valid line number that ends in a ;.

    Args:
        line_num (int): The current line number.
        line_info (dict): Dictionary containing information about each line.

    Returns:
        int: The previous valid line number.
    """
    for i in range(line_num - 1, 0, -1):
        if line_info.get(i, (None,))[0] is not None and line_info.get(i, (None,))[0] != '}' and line_info.get(i, (None,))[0] != 'e':
            return i
    return None

def find_outside_valid_line(valid_end_line, line_info, conditionals, brace_groups):
    """
    Find the next valid line outside of the current conditional block.

    Args:
        valid_end_line (int): The current valid end line number.
        line_info (dict): Dictionary containing information about each line.
        conditionals (int): The count of conditionals processed.
        brace_groups (list): List of pairs of opening and closing braces.

    Returns:
        int: The next valid line number outside of conditionals.
    """
    while valid_end_line and line_info.get(valid_end_line, (None,))[0] in ['e', 'w', 'f']:
        if conditionals >= len(brace_groups):
            return None
        start_line, end_line = brace_groups[conditionals]
        valid_end_line = find_next_valid_line(end_line, line_info)
        if valid_end_line is not None and line_info.get(valid_end_line, (None,))[0] in ['i', 'r', 'f', 'w', 'ei']:
            break
        conditionals += 1
    return valid_end_line

def return_check(line):
    """
    Checks if the given line of code starts with the keyword 'return'.

    Args:
        line (str): A string representing a line of code.

    Returns:
        bool: True if the line starts with 'return', False otherwise.
    """
    return line.strip().startswith('return')

def get_color_value(line):
    """
    Gets a value for the color based on the txt file

    Args:
        line (str): A string representing the error value of the line of code.

    Returns:
        str: the hex color value of the line of code.
    """
     #yellow-Red Gradient
    line = float(line)
    if line <= .1:
        return "#fffecf"
    elif line <= 0.2 and line > 0.1:
        return "#fdedb2"
    elif line <= 0.3 and line > 0.2:
        return "#fbdc95"
    elif line <= 0.4 and line > 0.3:
        return "#f9cb78"
    elif line<= 0.5 and line > 0.4:
        return "#f8b95b"
    elif line <= 0.6 and line > 0.5:
        return "#f6a73e"
    elif line <= 0.7 and line > 0.6:
        return "#ef7e2e"
    elif line <= 0.8 and line > 0.7:
        return "#e7541e"
    elif line <= 0.9 and line > 0.8:
        return "#e02a0f"
    elif line <= 1.0 and line > 0.9:
        return "#d90000"
def remove_comments(line):
    """
    Remove inline comments from a line of code.

    Args:
        line (str): The line of code.

    Returns:
        str: The line without comments.
    """
    if '//' in line:
        line = line.split('//')[0].strip()
    return line

def parse_bug_probabilities(input_file_path, output_file_path, total_lines):
    # Initialize dictionaries with all counts set to 0, starting from line 1
    false_count = {i: 0 for i in range(1, total_lines + 1)}
    total_false = 0
    true_count = {i: 0 for i in range(1, total_lines + 1)}
    total_true = 0
    
    with open(input_file_path, 'r') as file:
        for line in file:
            line = line.strip()
            if line.startswith('test'):
                # Reset list for each new line
                repeats = [False] * (total_lines + 1)
                
                parts = line.split()
                if parts[-1] == 'Fail':
                    total_false += 1
                    for part in parts:
                        if part.isdigit():
                            num = int(part)
                            if 1 <= num <= total_lines and not repeats[num]:
                                false_count[num] += 1
                                repeats[num] = True
                
                elif parts[-1] == 'Pass':
                    total_true += 1
                    for part in parts:
                        if part.isdigit():
                            num = int(part)
                            if 1 <= num <= total_lines and not repeats[num]:
                                true_count[num] += 1
                                repeats[num] = True

    probabilities = [0.0] * total_lines
    for line_num in range(1, total_lines + 1):
        percent_fail = false_count[line_num] / total_false if total_false > 0 else 0
        percent_true = true_count[line_num] / total_true if total_true > 0 else 0
        if percent_fail + percent_true > 0:
            prob = percent_fail / (percent_fail + percent_true)
        else:
            prob = 0.0
        probabilities[line_num - 1] = prob

    min_prob = min(probabilities)
    max_prob = max(probabilities)

    if max_prob != min_prob:
        probabilities = [(prob - min_prob) / (max_prob - min_prob) for prob in probabilities]
    else:
        probabilities = [0.0 for _ in probabilities]

    with open(output_file_path, 'w') as file:
        for prob in probabilities:
            file.write(f'{prob}\n')


def create_edge(start, end, edges, nodes, line_info):
    """
    Create an edge between two lines for the graph.

    Args:
        start (int): The starting line number.
        end (int): The ending line number.
        edges (list): List of edges for the graph.
        nodes (set): Set of nodes for the graph.

    Returns:
        tuple: Updated list of edges and set of nodes.
    """
    useless, color = line_info[start]
    useless2, color2 = line_info[end]
    nodeStart = f'Line{start}[label="Line {start}", shape=ellipse, style=filled, fillcolor="{color}"];'
    nodeEnd = f'Line{end}[label="Line {end}", shape=ellipse, style=filled, fillcolor="{color2}"];'
    
    nodes.add(nodeStart)
    nodes.add(nodeEnd)

    edge = f'"Line{start}" -> "Line{end}"'
    edges.append(edge)
    return edges, nodes

if __name__ == "__main__":
    main()
