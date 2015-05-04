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
	
		public Parser(String file) {
			String unformatted_string = null;
			
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
		
		public boolean checkTerminal(String arg) {
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
			
			while(parse_end == false && debugCounter < 1000) {
				
			
				print_stacks();
		
				String scanned = input.get(input.size() - 1);
				String next_terminal = new String();
				if(input.size() - 2 >= 0) {
					next_terminal = input.get(input.size() - 2);
				}
				
				if(scanned.compareTo(rules.get(rules.size() - 1)) == 0 && scanned.compareTo("$") == 0) {
					parse_end = true;
					System.out.println("ACCEPTED");
					break;
				}
				
				if(checkTerminal(rules.get(rules.size() - 1))) {
					input.pop();
				}
				
			    rules = getRule(scanned, next_terminal, rules.get(rules.size() - 1), rules);
			    
			    if(rules == null) {
			    	System.out.println("REJECTED");
			    	break;
			    }
		
				debugCounter++;
			}
			
		}
		
		
		public ArrayList<String> save_rules(ArrayList<String> to_save, Stack<String> save_stack) {
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
		
		
		public Stack<String> getRule(String terminal, String next_terminal, String variable, Stack<String> prevRules) {
			
			Stack<String> newRule = prevRules;
			ArrayList<String> prev_rule_parts = new ArrayList<String>();
			// Rule Checking
			
			if(checkTerminal(terminal) && checkTerminal(variable) && terminal.compareTo(variable) == 0) {
				newRule.pop();
				return newRule;
			}
			
			if (checkTerminal(terminal) && checkTerminal(variable) && terminal.compareTo(variable) != 0) {
				newRule = null;
				System.out.println(terminal + " was found but expected " + variable);
				return newRule;
				
			}

			if(terminal.compareTo("$") == 0 && checkTerminal(variable)) {
				newRule = null;
				System.out.println(terminal + " was not found ");
				return newRule;
			}
			
			
			
			if(variable.compareTo("S") == 0) {
				if(terminal.compareTo("i") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("L");
					newRule.push("E");
					return newRule;
				}
				
				else if(terminal.compareTo("x") == 0 || terminal.compareTo("y") == 0 ) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("L");
					newRule.push("A");
					return newRule;
				}
				else {
					System.out.println(terminal + " was found but expecting i or x or y");
					newRule = null;
					return newRule;
				}
			}
		
			if(variable.compareTo("E") == 0) {
				if(terminal.compareTo("i") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("R");
					newRule.push("}");
					newRule.push("S");
					newRule.push("{");
					newRule.push(")");
					newRule.push("C");
					newRule.push("(");
					newRule.push("f");
					newRule.push("i");
		
					return newRule;
				}
				else {
					System.out.println(terminal + " found but expected i");
					newRule = null;
					return newRule;
				}
			}
			
			if(variable.compareTo("C") == 0) {
				if(terminal.compareTo("x") == 0 || terminal.compareTo("y") == 0 ) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("Q");
					newRule.push("O");
					newRule.push("V");
					return newRule;
				}
				else if (terminal.compareTo("a") == 0 || terminal.compareTo("b") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("Q");
					newRule.push("O");
					newRule.push("T");
					return newRule;
				}
				else {
					System.out.println(terminal + " found but expected x or y or a or b");
					newRule = null;
					return newRule;
				}
			}
			
			if(variable.compareTo("V") == 0) {
				if(terminal.compareTo("x") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("x");
					return newRule;
				}
				else if (terminal.compareTo("y") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("y");
					return newRule;
				}
				else {
					System.out.println(terminal + " found but expected x or y");
					newRule = null;
					return newRule;
				}
			}
			
			if(variable.compareTo("T") == 0) {
				if(terminal.compareTo("a") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("a");
					return newRule;
				}
				else if(terminal.compareTo("b") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("b");
					return newRule;
				}
				else {
					System.out.println(terminal + " found but expected a or b");
					newRule = null;
					return newRule;
				}
			}
			
			if(variable.compareTo("O") == 0) {
				if(terminal.compareTo("<") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("<");
					return newRule;
				}
				
				else if(terminal.compareTo(">") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push(">");
					return newRule;
				}
				else {
					System.out.println(terminal + " found but expected > or <");
					newRule = null;
					return newRule;
				}
			}
			
			if(variable.compareTo("Q") == 0) {
				if(terminal.compareTo("a") == 0 || terminal.compareTo("b") == 0 ) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("T");
					return newRule;
				}
				
			}
			
			if(variable.compareTo("A") == 0) {
				if(terminal.compareTo("x") == 0 || terminal.compareTo("y") == 0 ) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push(";");
					newRule.push("T");
					newRule.push("=");
					newRule.push(":");
					newRule.push("V");
					return newRule;
				}
				else {
					System.out.println(terminal + " found but expected x or y");
					newRule = null;
					return newRule;
				}
				
			}
			
			if(variable.compareTo("L") == 0) {
				if(terminal.compareTo("x") == 0 || terminal.compareTo("y") == 0 ) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("L");
					newRule.push("A");
					return newRule;
				}
				else if (newRule.get(newRule.size() - 2).compareTo("$") == 0 && terminal.compareTo("$") != 0 || 
						checkTerminal(terminal) != true && terminal.compareTo("$") != 0) {
					System.out.println(terminal + " found but expected null or x or y");
					newRule = null;
					return newRule;
				}
				else {
					newRule.pop();
					return newRule;
				}
				
			}
			
			if(variable.compareTo("R") == 0) {
				if(terminal.compareTo("e") == 0) {
					newRule.pop();
					prev_rule_parts = save_rules(prev_rule_parts, newRule);
					newRule = restore_rules(prev_rule_parts, newRule);
					newRule.push("}");
					newRule.push("S");
					newRule.push("{");
					newRule.push("e");
					newRule.push("s");
					newRule.push("l");
					newRule.push("e");
					return newRule;
				}
				else {
					newRule.pop();
					return newRule;
				}
				
			}
			
			if(checkTerminal(terminal) && checkTerminal(next_terminal) && terminal.compareTo(next_terminal) == 0
					&& terminal.compareTo("{") != 0 && terminal.compareTo("}") != 0 &&
					terminal.compareTo("(") != 0 && terminal.compareTo(")") != 0 ) {
				newRule = null;
				System.out.println(terminal + " is duplicated");
				return newRule;
			}
			
			
			return newRule;
		}
	

}
