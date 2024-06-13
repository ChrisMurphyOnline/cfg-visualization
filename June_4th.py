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
            
            # Initialize variables for condition tracking
            startConditional = None
            endConditional = None
            validLines = []
            graph = []
            
            # Process the lines, skipping the first two and the last two
            for lineNum, line in enumerate(lines[2:-2], start=3):
                line = line.strip()
                if line:  # Ignore empty lines
                    validLines, startConditional, endConditional = parse_line(line, lineNum, startConditional, endConditional, validLines)
                    if startConditional is not None and endConditional is not None:
                        graph.append(f'"line {startConditional}" -> "line {endConditional}"')
                        # Reset after finding a complete condition
                        startConditional = None
                        endConditional = None
            
            # Print the valid line ranges
            for i in range(len(validLines) - 1):
                graph.append(f'"line {validLines[i]}" -> "line {validLines[i+1]}"')
            
            # Create the edges file and write the graph entries
            edges_file_path = f"{file_path[:-5]}_edges.dot"  # Assuming the file extension is .java
            with open(edges_file_path, 'w') as edges_file:
                edges_file.write("strict digraph {")
                for entry in graph:
                    edges_file.write(entry + "\n")
                edges_file.write("}")
            
            # Run the command to generate the SVG file using dot
            output_svg_path = f"{file_path[:-5]}_edges.svg"
            subprocess.run(["dot", "-Tsvg", edges_file_path, "-o", output_svg_path], check=True)
            #os.remove(edges_file_path)
        except Exception as e:
            print(f'Error reading file {file_path}: {e}')

def parse_line(line, lineNum, startConditional, endConditional, validLines):
    if line.startswith('if ('):
        startConditional = lineNum - 1
    elif line.startswith('}'):
        endConditional = lineNum + 1
    else:
        validLines.append(lineNum)
    return validLines, startConditional, endConditional
        
if __name__ == "__main__":
    main()
