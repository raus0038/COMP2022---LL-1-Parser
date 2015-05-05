import java.util.ArrayList;
import java.util.HashMap;


public class Variable {
	
	String variable;
	ArrayList<String> rules;
	HashMap<String, String> first;
	ArrayList<String> follow;
	
	
	public Variable(String variable_identifier) {
		
		first = new HashMap<String, String>();
		follow = new ArrayList<String>();
		
		variable = variable_identifier;
		
		switch(variable) {
		case "S":
			  first.put("i", "EL");
			  first.put("x", "AL");
			  first.put("y", "AL");
			  break;
		case "L":
			  first.put("x", "AL");
			  first.put("y", "AL");
			  follow.add("$");
			  follow.add("x");
			  follow.add("y");
			  follow.add("}");
			  break;
		case "E":
			  first.put("i", "if(C){S}R");
			  break;
		case "R":
			  first.put("e", "else{S}");
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
			  
		
		}
		
	}
	
	public String get_rule(String terminal) {
		return first.get(terminal);
	}
	
	public ArrayList<String> get_follow() {
		return follow;
	}
	
	public String get_variable() {
		return variable;
	}
	
	

}
