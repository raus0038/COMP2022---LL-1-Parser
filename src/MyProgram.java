
public class MyProgram {

	public static void main(String[] args) {
			int argc = args.length;
			if(argc >= 1) {
				Parser LL = new Parser(args[0]);
				LL.parse();
			}
			
			
			
			
	}

}
