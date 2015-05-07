/*
 * COMP2022 - Formal Languages and Logic Assignment 2
 * 
 * LL(1) Parser
 * 
 * @author Rhys Austin
 * 
 */

public class MyProgram {

	public static void main(String[] args) {
		int argc = args.length;
		
		// Check if correct argument size is given
		if(argc >= 1) {
			boolean error_recovery = false;
			
			// Check if error recovery argument given
			if(argc >= 2) {
				if(args[1].compareTo("-e") == 0) {
					error_recovery = true;
				}
			}
			
			// Create new LL(1) Parser using given text file and error_recovery argument
			Parser LL = new Parser(args[0], error_recovery);
			// Parse text file
			LL.parse();
		}
	}
}
