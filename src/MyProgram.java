
public class MyProgram {

	public static void main(String[] args) {
			int argc = args.length;
			if(argc >= 1) {
				boolean error_recovery = false;
				if(argc >= 2) {
					if(args[1].compareTo("-e") == 0) {
						error_recovery = true;
					}
				}
				Parser LL = new Parser(args[0], error_recovery);
				LL.parse();
			}
		
	}

}
