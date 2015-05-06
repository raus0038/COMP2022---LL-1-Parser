import java.util.ArrayList;
import java.util.Stack;

/*
 * VariableMap class to hold information regarding all variables of
 * the LL(1) grammar.
 */
public class VariableMap implements Constants {
	
	/*
	 * Declare variables
	 */
	ArrayList<Variable> variable_list;
	
	public VariableMap() {
		
		variable_list = new ArrayList<Variable>();
		
		/*
		 * Initialize variable_list to contain all possible variables
		 * of the grammar as specified in the Constants interface
		 */
		for(int i = 0; i < variable_number; i++) {
			variable_list.add(new Variable(variables[i]));
		}
	}
	
	/*
	 * Returns given variable
	 */
	public Variable get_variable(String variable_identifier) {
		for(Variable s : variable_list) {
			if(s.get_variable().compareTo(variable_identifier) == 0) {
				return s;
			}
		}
		
		return null;
	}
	
	/*
	 * Helper functions used to reverse contents of a stack to allow for 
	 * updated rules to be added to the top of the stack
	 */
	public ArrayList<String> save_rules(Stack<String> save_stack) {
		ArrayList<String> prev_rule_parts = new ArrayList<String>();
		while(save_stack.size() > 0) {
			prev_rule_parts.add(save_stack.pop());
		}
		
		return prev_rule_parts;
	}
	
	public Stack<String> restore_rules(ArrayList<String> rules, Stack<String> to_restore) {
		to_restore = new Stack<String>();
		for(int i = rules.size() - 1; i >= 0; i--) {
			to_restore.push(rules.get(i));
		}
		
		return to_restore;
	}
	
	public Stack<String> reverse_stack(Stack<String> to_reverse) {
		ArrayList<String> rules = save_rules(to_reverse);
		Stack<String> new_stack = restore_rules(rules, to_reverse);
		return new_stack;
	}
	
	
	/*
	 * Returns true of given string argument is an element of the terminal
	 * set as specified in the Constants interface
	 */
	public static boolean check_terminal(String arg) {
		for(int i = 0; i < terminal_number; i++) {
			if(arg.compareTo(terminals[i]) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Returns an updated stack according to given variable and terminal
	 */
	public Stack<String> update_stack(String variable, String terminal, Stack<String> old_stack, boolean err) {
		
		boolean isTerminal = check_terminal(variable);
		
		// If the variable used is not an element of the terminal set
		if(isTerminal == false) {
			
			// Retrieve variables information from the variable map
			Variable current_variable = get_variable(variable);
			
			// If no such variable exists then an error has occurred
			if(current_variable == null) {
				return null;
			}
			
			// Retrieve the rule given by using the input terminal as a first symbol for the variable
			String rule = current_variable.get_rule(terminal);
			
			// If no such rule exists
			if(rule == null) {
				
					// Check if the current variable has a follow set ( "L", "R" ) 
					if(current_variable.get_follow().isEmpty() != true) {
						ArrayList<String> follow = current_variable.get_follow();
						int valid_follow = 0;
						
						// Check if current terminal is an element of the variable's follow set
						for(String s : follow) {
							if(s.compareTo(terminal) == 0) {
								valid_follow++;
							}
						}
						
						// If no such match occurs, an error has occurred
						if(valid_follow == 0) {
							// 'err' is used to prevent repeated error statements from occurring
							if(err == false) {
								System.out.print(terminal + " was found but expected ");
								current_variable.print_follow();
							}
							old_stack = null;
						}
						else {
							old_stack.pop();
						}
						
						return old_stack;
					}
					if(err == false) {
						System.out.print(terminal + " was found but expected ");
						current_variable.print_rules();
						System.out.println();
					}
					return null;
			}
			
			// If rule does exist, pop variable from top of stack, reverse stack 
			// and add updated rules to the top of stack
			
			old_stack.pop();
			old_stack = reverse_stack(old_stack);
			
			for(int i =  rule.length() - 1; i >= 0; i--) {
				old_stack.push(Character.toString(rule.charAt(i)));
			}
	
		}
		
		// If the variable is a terminal
		else {
			// If the current input token and the variable do not match, an error has occured
			if(variable.compareTo(terminal) != 0) {
				if(err == false) {
					System.out.println(terminal + " was found but expected " + variable + ",");
				}
				return null;
			}
			// Else pop terminal from the rules stack (input terminal will be popped in the Parser class)
			else {
				old_stack.pop();
			}
		}
		
		return old_stack;
	}

}
