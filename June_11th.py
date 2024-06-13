import os
import glob
import subprocess

def main():
    directory = 'tests'
    pattern = os.path.join(directory, '*.java')
    java_files = glob.glob(pattern)
    
    # Iterate over each Java file
    for file_path in java_files:
        print(f'Reading file: {file_path}')
        try:
            # Open and read all lines from the file
            with open(file_path, 'r') as file:
                lines = file.readlines()
            
            line_info = {i: None for i in range(3, len(lines) - 1)}
            brace_groups = []
            stack = []
            semicolon_lines = []
            
            # Process the lines, skipping the first two and the last two
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
                    if stack:
                        line_info[line_num] = True
                    else:
                        line_info[line_num] = False
                    semicolon_lines.append(line_num)
            
            edges = []
            conditionals = 0
            
            for line_num in range(3, len(lines) - 1):
                # If the current line ends in a ';' outside of a conditional
                if line_info[line_num] == False:
                    next_valid_line = find_next_valid_line(line_num, line_info)
                    if next_valid_line:
                        edges.append(f'"line {line_num}" -> "line {next_valid_line}"')
                
                # If the current line ends in a ';' within a conditional
                if line_info[line_num] == True:
                    next_valid_line = find_next_valid_line(line_num, line_info)
                    while line_info.get(next_valid_line) in ['ei', 'e']:
                        next_valid_line = find_outside_valid_line(next_valid_line, line_info, conditionals, brace_groups)
                    if next_valid_line:
                        edges.append(f'"line {line_num}" -> "line {next_valid_line}"')
                
                # If the current line is an if statement
                if line_info[line_num] == 'i':
                    if_edges = if_statement(line_info, line_num, conditionals, brace_groups)
                    edges.extend(if_edges)
                    conditionals += 1
                
                #If the current line is an else if statement
                
                    
                    
            # Create the edges file and write the edges
            edges_file_path = f"{file_path[:-5]}_edges.dot"  # Assuming the file extension is .java
            with open(edges_file_path, 'w') as edges_file:
                edges_file.write("strict digraph {\n")
                for entry in edges:
                    edges_file.write(entry + ";\n")
                edges_file.write("}")
            
            # Run the command to generate the SVG file using dot
            output_svg_path = f"{file_path[:-5]}_edges.svg"
            subprocess.run(["dot", "-Tsvg", edges_file_path, "-o", output_svg_path], check=True)
            os.remove(edges_file_path)
            
        except Exception as e: 
            print(f'Error reading file {file_path}: {e}')


def if_statement(line_info, line_num, conditionals, brace_groups):
    originalConditionals = conditionals
    edges = []
    start_line, end_line = brace_groups[conditionals]
    valid = True
    next_valid_line = find_next_valid_line(line_num, line_info)
    # If the next line is within the conditional
    if line_info.get(next_valid_line) == True:
        edges.append(f'"line {line_num}" -> "line {next_valid_line}"')
    # If the next line is outside of the conditional
    valid_end_line = find_next_valid_line(end_line, line_info)
    if line_info.get(valid_end_line) == False:
        edges.append(f'"line {line_num}" -> "line {valid_end_line}"')
    # If the next line is outside of the conditional and is another conditional
    elif line_info.get(valid_end_line) in ['ei']:
        while (line_info.get(valid_end_line) in ['ei']):
            conditionals += 1
            start_line, end_line = brace_groups[conditionals]
            valid_end_line = find_next_valid_line(end_line, line_info)
        edges.append(f'"line {line_num}" -> "line {valid_end_line}"')    
    #Check to see if there is an else if
    
    
            
    
    return edges

def get_statement(line, line_info, line_num):
    if line.startswith('if'):
        line_info[line_num] = 'i'
    elif line.startswith('else if'):
        line_info[line_num] = 'ei'
    elif line.startswith('else'):
        line_info[line_num] = 'e'
    return line_info

def find_next_valid_line(line_num, line_info):
    for i in range(line_num + 1, max(line_info.keys()) + 1):
        if line_info.get(i) is not None and line_info.get(i) != '}':
            return i
    return None

def find_outside_valid_line(valid_end_line, line_info, conditionals, brace_groups):
    while (line_info.get(valid_end_line) in ['ei', 'e']):
            conditionals += 1
            start_line, end_line = brace_groups[conditionals]
            valid_end_line = find_next_valid_line(end_line, line_info)
    return valid_end_line

def remove_comments(line):
    if '//' in line:
        line = line.split('//')[0].strip()
    return line
        
if __name__ == "__main__":
    main()
