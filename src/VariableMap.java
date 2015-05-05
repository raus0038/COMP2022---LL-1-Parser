import java.util.ArrayList;
import java.util.Stack;


public class VariableMap implements Constants {
	
	ArrayList<Variable> variable_list;
	
	public VariableMap() {
		
		variable_list = new ArrayList<Variable>();
		
		for(int i = 0; i < variable_number - 1; i++) {
			variable_list.add(new Variable(variables[i]));
		}
	}
	
	public Variable get_variable(String variable_identifier) {
		for(Variable s : variable_list) {
			if(s.get_variable().compareTo(variable_identifier) == 0) {
				return s;
			}
		}
		
		return null;
	}
	
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
	
	
	public static boolean checkTerminal(String arg) {
		for(int i = 0; i < terminal_number; i++) {
			if(arg.compareTo(terminals[i]) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public Stack<String> update_stack(String variable, String terminal, Stack<String> old_stack) {
		
		boolean isTerminal = checkTerminal(variable);
		
		if(isTerminal == false) {
			Variable current_variable = get_variable(variable);
			
			if(current_variable == null) {
				return null;
			}
			
			String rule = current_variable.get_rule(terminal);
			
			if(rule == null) {
					if(current_variable.get_follow().isEmpty() != true) {
						return update_stack_follow(current_variable, terminal, old_stack);
					}
					System.out.println(terminal + " was found but expected " + rule);
					return null;
			}
			
			old_stack.pop();
			old_stack = reverse_stack(old_stack);
			
			for(int i =  rule.length() - 1; i >= 0; i--) {
				old_stack.push(Character.toString(rule.charAt(i)));
			}
	
		}
		else {
			if(variable.compareTo(terminal) != 0) {
				System.out.println(terminal + " was found but expected " + variable);
				return null;
			}
			else {
				old_stack.pop();
			}
		}
		
		return old_stack;
	}
	
	public Stack<String> update_stack_follow(Variable current, String terminal,  Stack<String> old_stack) {
		
		if(current.get_follow().isEmpty() == false) {
			for(int i = 0; i < current.get_follow().size(); i++) {
				if(current.get_follow().get(i).compareTo(terminal) == 0) {
					old_stack.pop();
					return old_stack;
				}
			}
			
			System.out.print(terminal + " was found but expected ");
			for(String s : current.get_follow()) {
				System.out.print(s + ", ");
			}
			System.out.println();
			return null;
		}
		
		return null;
	}

}
