package jacob.program.main;

import jacob.program.gui.FileParser;
import jacob.program.utils.Runner;

public class Main {
	
	private static Runner runner = new Runner();
	
	public static void main(String[] argvs) {
		runner.run(new FileParser(), 360, 110);
	}
	
}
