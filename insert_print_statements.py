import os
import glob
import logging

def main():
    logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
    directory = 'init_print_statements'
    pattern_java = os.path.join(directory, '*.java')
    java_files = glob.glob(pattern_java)
    
    print(f"Current working directory: {os.getcwd()}")
    print(f"Looking for Java files with pattern: {pattern_java}")
    print(f"Found Java files: {java_files}")

    if not java_files:
        logging.error("No Java files found. Please check the directory and file pattern.")
        return
    
    for java_path in java_files:
        with open(java_path, 'r') as java_file:
            lines = java_file.readlines()

        brace_count = 0
        start_line = 0
        total_lines = 0
        output_lines = []

        for i in range(len(lines)):
            line = lines[i].strip()
            if line.startswith('else {') or line == '}':
                output_lines.append(lines[i])
                continue
            
            if line.endswith('{') and brace_count <= 2:
                brace_count += 1
                start_line += 1
                total_lines += 1

            print(brace_count)
            
            if line and not line.startswith(('else', '}')) and not line.startswith('print(') and brace_count >= 2:
                output_lines.append(f'print("{i + 1}"); {lines[i]}')
            else:
                output_lines.append(lines[i])

            total_lines += 1

        logging.info(f'File: {java_path}, Start Line: {start_line}, Total Lines: {total_lines}')
        
        # Write code with print statements to file
        with open(java_path, 'w') as java_file:
            java_file.writelines(output_lines)

if __name__ == "__main__":
    main()
