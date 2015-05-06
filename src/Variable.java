import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * Variable class to hold information regarding each individual variable of the 
 * LL(1) Grammar
 */

public class Variable {
	
	/*
	 * Initialize Variables
	 */
	String variable;
	ArrayList<String> rules;
	HashMap<String, String> first;
	ArrayList<String> follow;
	
	
	public Variable(String variable_identifier) {
		
		// Set first and follow sets, the first set is a Hash Map as each valid symbol has a corresponding rule
		first = new HashMap<String, String>();
		follow = new ArrayList<String>();
		
		variable = variable_identifier;
		
		// Switch to define the first / follow sets for given variable
		switch(variable) {
		case "S":
			  first.put("i", "EL");
			  first.put("x", "AL");
			  first.put("y", "AL");
			  break;
		case "L":
			  first.put("x", "AL");
			  first.put("y", "AL");
			  follow.add("x");
			  follow.add("y");
			  follow.add("}");
			  follow.add("$");
			  break;
		case "E":
			  first.put("i", "if(C){S}R");
			  break;
		case "R":
			  first.put("e", "else{S}");
			  follow.add("x");
			  follow.add("y");
			  follow.add("}");
			  follow.add("$");
			  break;
		case "A":
			  first.put("x", "V:=T;");
			  first.put("y", "V:=T;");
			  break;
		case "Q":
			  first.put("a", "T");
			  first.put("b", "T");
			  first.put("x", "V");
			  first.put("y", "V");
			  break;
		case "V":
			  first.put("x", "x");
			  first.put("y", "y");
			  break;
		case "T":
			  first.put("a", "a");
			  first.put("b", "b");
			  break;
		case "C":
			  first.put("x", "VOQ");
			  first.put("y", "VOQ");
			  first.put("a", "TOQ");
			  first.put("b", "TOQ");
			  break;
		case "O":
			  first.put(">", ">");
			  first.put("<", "<");
			  break;
		case "$":
			  first.put("$", "$");
			  break;
			  
		
		}
		
	}
	
	/*
	 * Helper functions to return and display class information
	 */
	public String get_rule(String terminal) {
		return first.get(terminal);
	}
	
	public void print_rules() {
		for(String value : first.keySet()) {
			System.out.print( value + ", ");
		}
	}
	
	public void print_follow() {
		for(String value : follow) {
			System.out.print( value + ", ");
		}
		System.out.println();
	}
	
	public ArrayList<String> get_follow() {
		return follow;
	}
	
	public String get_variable() {
		return variable;
	}
	
	

}
