import os
import glob
import subprocess

def main():
    directory = 'tests'
    pattern = os.path.join(directory, '*.java')
    java_files = glob.glob(pattern)
    
    for file_path in java_files:
        print(f'Reading file: {file_path}')
        try:
            with open(file_path, 'r') as file:
                lines = file.readlines()
            
            # Dictionary to store the type of line information
            line_info = {i: None for i in range(3, len(lines) - 1)}
            brace_groups = []  # To store the pairs of opening and closing braces
            stack = []  # To keep track of nested structures
            
            for line_num in range(3, len(lines) - 1):
                line = lines[line_num - 1].strip()
                line = remove_comments(line)
                if line.endswith('{'):
                    line_info = get_statement(line, line_info, line_num)
                    stack.append(line_num)
                elif line.endswith('}'):
                    line_info[line_num] = '}'
                    if stack:
                        start_brace = stack.pop()
                        brace_groups.append((start_brace, line_num))
                elif line.endswith(';'):
                    line_info[line_num] = len(stack) if stack else False
            
            edges = []  # To store the edges for the graph
            conditionals = 0  # To keep track of the number of conditionals processed
            
            for line_num in range(3, len(lines) - 1):
                if line_info[line_num] == False:
                    next_valid_line = find_next_valid_line(line_num, line_info)
                    if next_valid_line:
                        edges = create_edge(line_num, next_valid_line, edges)
                
                if line_info[line_num] == True:
                    next_valid_line = find_next_valid_line(line_num, line_info)
                    while next_valid_line and line_info.get(next_valid_line) in ['ei', 'e']:
                        next_valid_line = find_outside_valid_line(next_valid_line, line_info, conditionals, brace_groups)
                    if next_valid_line:
                        edges = create_edge(line_num, next_valid_line, edges)
                
                if line_info[line_num] == 'i':
                    if_edges = if_statement(line_info, line_num, conditionals, brace_groups)
                    edges.extend(if_edges)
                    conditionals += 1
                
                if line_info[line_num] == 'ei':
                    elseif_edges = else_if_statement(line_info, line_num, conditionals, brace_groups)
                    edges.extend(elseif_edges)
                    conditionals += 1
                
                if line_info[line_num] == 'e':
                    conditionals += 1
            
            edges_file_path = f"{file_path[:-5]}_edges.dot"
            with open(edges_file_path, 'w') as edges_file:
                edges_file.write("strict digraph {\n")
                for entry in edges:
                    edges_file.write(entry + ";\n")
                edges_file.write("}")
            output_svg_path = f"{file_path[:-5]}_edges.svg"
            subprocess.run(["dot", "-Tsvg", edges_file_path, "-o", output_svg_path], check=True)
            os.remove(edges_file_path)
            
        except Exception as e:
            print(f'Error reading file {file_path}: {e}')

def else_if_statement(line_info, line_num, og_conditionals, brace_groups):
    """
    Handle the logic for else if statements.
    
    Args:
    line_info (dict): Dictionary containing information about each line.
    line_num (int): The current line number.
    og_conditionals (int): The original count of conditionals processed.
    brace_groups (list): List of pairs of opening and closing braces.
    
    Returns:
    list: List of edges to be added to the graph.
    """
    edges = []
    conditionals = og_conditionals
    if conditionals >= len(brace_groups):
        return edges  # No more brace groups to process
    start_line, end_line = brace_groups[conditionals]
    next_valid_line = find_next_valid_line(line_num, line_info)
    
    if line_info.get(next_valid_line) == True:
        edges = create_edge(line_num, next_valid_line, edges)
        
    valid_end_line = find_next_valid_line(end_line, line_info)

    if line_info.get(valid_end_line) == False:
        edges = create_edge(line_num, valid_end_line, edges)
    elif line_info.get(valid_end_line) in ['ei']:
        edges = create_edge(line_num, valid_end_line, edges)
    elif line_info.get(valid_end_line) in ['e']:
        valid_end_line = find_next_valid_line(valid_end_line, line_info)
        edges = create_edge(line_num, valid_end_line, edges)
    
    return edges

def if_statement(line_info, line_num, og_conditionals, brace_groups):
    """
    Handle the logic for if statements.
    
    Args:
    line_info (dict): Dictionary containing information about each line.
    line_num (int): The current line number.
    og_conditionals (int): The original count of conditionals processed.
    brace_groups (list): List of pairs of opening and closing braces.
    
    Returns:
    list: List of edges to be added to the graph.
    """
    edges = []
    conditionals = og_conditionals
    if conditionals >= len(brace_groups):
        return edges  # No more brace groups to process
    start_line, end_line = brace_groups[conditionals]
    
    next_valid_line = find_next_valid_line(line_num, line_info)
    
    if line_info.get(next_valid_line) == True:
        edges = create_edge(line_num, next_valid_line, edges)
        
    valid_end_line = find_next_valid_line(end_line, line_info)
    if line_info.get(valid_end_line) == False:
        edges = create_edge(line_num, valid_end_line, edges)
    
    if line_info.get(valid_end_line) in ['ei']:
        edges = create_edge(line_num, valid_end_line, edges)
    elif line_info.get(valid_end_line) in ['e']:
        valid_end_line = find_next_valid_line(valid_end_line, line_info)
        edges = create_edge(line_num, valid_end_line, edges)

    return edges

def get_statement(line, line_info, line_num):
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
        line_info[line_num] = 'i'
    elif line.startswith('else if'):
        line_info[line_num] = 'ei'
    elif line.startswith('else'):
        line_info[line_num] = 'e'
    return line_info

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
        if line_info.get(i) is not None and line_info.get(i) != '}':
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
    while valid_end_line and line_info.get(valid_end_line) in ['ei', 'e']:
        if conditionals >= len(brace_groups):
            return None
        start_line, end_line = brace_groups[conditionals]
        valid_end_line = find_next_valid_line(end_line, line_info)
        conditionals += 1
    return valid_end_line

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

def create_edge(start, end, edges):
    """
    Create an edge between two lines for the graph.
    
    Args:
    start (int): The starting line number.
    end (int): The ending line number.
    edges (list): List of edges for the graph.
    
    Returns:
    list: Updated list of edges.
    """
    edges.append(f'"Line {start}" -> "Line {end}"')
    return edges

if __name__ == "__main__":
    main()
