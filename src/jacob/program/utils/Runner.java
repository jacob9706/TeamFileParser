package jacob.program.utils;

import javax.swing.JFrame;

public class Runner {
	
	public void run(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
	
}
