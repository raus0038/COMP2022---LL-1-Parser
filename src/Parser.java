import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;


public class Parser implements Constants {
	

		String sequence;
		Stack<String> input = new Stack<String>();
		Stack<String> rules = new Stack<String>();
		boolean error_recovery;
		boolean error_found;
		boolean rejected;
		VariableMap variable_map;
	
		public Parser(String file, boolean err) {
			
			variable_map = new VariableMap();
			String unformatted_string = null;
			error_recovery = err;
			error_found = false;
			rejected = false;
			try {
			  unformatted_string = readFile(file, Charset.defaultCharset() );
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			sequence = unformatted_string.replaceAll("\\s", "");
			
			rules.push(variables[variable_number - 1]);
			rules.push(variables[0]);
			
			input.push(variables[variable_number - 1]);
			for(int i = sequence.length() - 1; i >= 0; i--) {
				input.push(Character.toString(sequence.charAt(i)));
			}
			
		
			
		}

		static String readFile(String path, Charset encoding) 
				  throws IOException {
				  byte[] encoded = Files.readAllBytes(Paths.get(path));
				  return new String(encoded, encoding);
		}
		
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
		
		
		public static boolean checkTerminal(String arg) {
			for(int i = 0; i < terminal_number; i++) {
				if(arg.compareTo(terminals[i]) == 0) {
					return true;
				}
			}
			return false;
		}
		
		@SuppressWarnings("null")
		public void parse() {
			
			boolean parse_end = false;
			int debugCounter = 0;
			Stack<String> previous_stack = rules;
			
			while(parse_end == false) {
				
				
				
				previous_stack = rules;
				print_stacks();
		
				if(input.size() == 0) {
					
				}
				String scanned = input.get(input.size() - 1);
				String next_terminal = new String();
				if(input.size() - 2 >= 0) {
					next_terminal = input.get(input.size() - 2);
				}

				if(checkTerminal(rules.get(rules.size() - 1))) {
					input.pop();
				}	
			
			    rules = getRule(scanned, next_terminal, rules.get(rules.size() - 1), rules);
			    if(error_recovery == false) {
			    	
			    	if(rules == null) {
				    	System.out.println("REJECTED");
				    	break;
				    }
			    	
				    if(scanned.compareTo(rules.get(rules.size() - 1)) == 0 && scanned.compareTo("$") == 0) {
						parse_end = true;
						System.out.println("ACCEPTED");
						break;
					}
				    
				    
			    }
			    else {
			    	if(scanned.compareTo(rules.get(rules.size() - 1)) == 0 && scanned.compareTo("$") == 0 &&
			    			rejected == false){
			    		parse_end = true;
						System.out.println("ACCEPTED");
						break;
			    	}
			    	else if (scanned.compareTo(rules.get(rules.size() - 1)) == 0 && scanned.compareTo("$") == 0 ||
			    			(input.size() == 0 && rules.size() > 0) | 	(input.size() > 0 && rules.size() == 0) &&
			    			rejected == true) {
			    		System.out.println("REJECTED");
			    		parse_end = true;
				    	break;
			    	}
			    }
			    
				
				
				debugCounter++;
			}
			
		}
				
		
		public Stack<String> getRule(String terminal, String next_terminal, String variable, Stack<String> prevRules) {
			
			
			
			Stack<String> temp_stack = variable_map.update_stack(variable, terminal, prevRules, error_found);
			
			if(error_recovery == true) {
				if(temp_stack == null) {
					print_stacks();
					input.pop();
					
					error_found = true;
					rejected = true;
				}
				else {
					error_found = false;
					prevRules = temp_stack;
				}
			}
			else {
				prevRules = temp_stack;
			}
			
			return prevRules;
			
		}
	

}
