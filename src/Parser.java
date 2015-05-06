import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;


public class Parser implements Constants {
	
		/* 
		 * Initialize Variables
		 */
	
		String sequence;
		Stack<String> input = new Stack<String>();
		Stack<String> rules = new Stack<String>();
		boolean error_recovery;
		boolean error_found;
		boolean rejected;
		int error_count;
		VariableMap variable_map;
	
		public Parser(String file, boolean err) {
			
			variable_map = new VariableMap();
			String unformatted_string = null;
			error_recovery = err;
			error_found = false;
			rejected = false;
			error_count = 0;
			
			/* 
			 * Read text file as one string
			 */
			
			try {
			  unformatted_string = read_file(file, Charset.defaultCharset() );
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			sequence = unformatted_string.replaceAll("\\s", "");
			
			/* 
			 * Push end of stack symbol ($) and Start symbol and into rules stack
			 */
			
			rules.push(variables[variable_number - 1]);
			rules.push(variables[0]);
			
			/*
			 * Push end of stack symbol and all input symbols from read string into input stack
			 */
			input.push(variables[variable_number - 1]);
			for(int i = sequence.length() - 1; i >= 0; i--) {
				input.push(Character.toString(sequence.charAt(i)));
			}
		}

		/*
		 * Return text file data as single string
		 */
		static String read_file(String path, Charset encoding) 
				  throws IOException {
				  byte[] encoded = Files.readAllBytes(Paths.get(path));
				  return new String(encoded, encoding);
		}
		
		/*
		 * Print contents of current input and rules stack on one line
		 */
		public void print_stacks() {
			
			for(int i = input.size() - 1; i >= 0; i--) {
				System.out.print(input.get(i));
			}
			System.out.print("\t");
			for(int i = rules.size() - 1; i >= 0; i--) {
				System.out.print(rules.get(i));
			}
			System.out.println();
	
		}

		/*
		 * Returns true if given argument is an element of the terminal set specified
		 * in the Constants interface
		 */
		public static boolean check_terminal(String arg) {
			for(int i = 0; i < terminal_number; i++) {
				if(arg.compareTo(terminals[i]) == 0) {
					return true;
				}
			}
			return false;
		}
		
	
		public void parse() {
			
			boolean parse_end = false;
			print_stacks();
			/*
			 * Main parsing loop
			 */
			while(parse_end == false) {

				// Get next input token from input stack
				String scanned = input.get(input.size() - 1);
				
				// get updated stack from scanned token and current variable on the rules stack
			    rules = get_rule(scanned, rules.get(rules.size() - 1), rules);
			    
			    // if error recovery is not on
			    if(error_recovery == false) {
			    	
			    	// No rule found for given input, therefore input is invalid
			    	if(rules == null) {
				    	System.out.println("REJECTED");
				    	break;
				    }
			    	// if both input and rules stack contain only the end of stack symbol
			    	// then input is valid
				    if(scanned.compareTo(rules.get(rules.size() - 1)) == 0 && scanned.compareTo("$") == 0) {
						parse_end = true;
						System.out.println("ACCEPTED");
						break;
					}
				    
				    
			    }
			    // if error_recovery is turned on
			    else {
			    	
			    	// if both stacks contain only the end of stack symbol, and no errors have occured during parsing
			    	// then input is valid
			    	if(scanned.compareTo(rules.get(rules.size() - 1)) == 0 && scanned.compareTo("$") == 0 &&
			    			rejected == false){
			    		parse_end = true;
						System.out.println("ACCEPTED");
						break;
			    	}
			    	// else if an error occurred during parsing, or either stack is of size 0 while the other is not
			    	// input is invalid
			    	else if (scanned.compareTo(rules.get(rules.size() - 1)) == 0 && scanned.compareTo("$") == 0 ||
			    			(input.size() == 0 && rules.size() > 0) | 	(input.size() > 0 && rules.size() == 0) &&
			    			rejected == true) {
			    		System.out.println("REJECTED");
			    		System.out.println("Number of errors: " + error_count);
			    		parse_end = true;
				    	break;
			    	}
			    }
			    
			    print_stacks();
			}
		}
				
		/*
		 * Returns an updated rules stack from the given input token
		 */
		public Stack<String> get_rule(String terminal, String variable, Stack<String> prevRules) {
			
			Stack<String> temp_stack = variable_map.update_stack(variable, terminal, prevRules, error_found);
			boolean input_popped = false;
			if(error_recovery == true) {
				// Error occurred for current input token
				if(temp_stack == null) {
					
					if(error_found == false) {
						error_count++;
					}

					// If the current variable on the rules stack contains a follow set, or is null
					// pop the input stack to continue reading until valid token found
					
					for(Variable s : variable_map.get_variables()) {
						if(s.get_follow().isEmpty() == false && s.get_variable().compareTo(variable) == 0
								&& input.size() > 0) {
							input.pop();
							input_popped = true;
							break;
						}
					}
					
					if(input_popped == false || variable.compareTo("$") == 0  && error_found == false) {
						input.pop();
						input_popped = true;
					}
					error_found = true;
					rejected = true;
				}
				else {
					// no errors found
					error_found = false;
					prevRules = temp_stack;
				}
			}
			// If error recovery not turned on, simply update stack
			else {
				prevRules = temp_stack;
			}
			
			// If the current variable on top of rules stack is a terminal
			// pop the input stack
			if( (check_terminal(variable) && input.size() > 0 && input_popped == false) || (error_found == true && input_popped == false) ) {
				input.pop();
			}
			return prevRules;
			
		}
	

}
