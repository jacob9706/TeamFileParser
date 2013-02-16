package jacob.program.gui;

import jacob.program.utils.DatabaseCreator;
import jacob.program.utils.Input;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;	
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileParser extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private String
	file = "",
	directory = "";
	
	private boolean 
	inputSelected = false,
	ouputSelected = false;
	
	private List<Input> parsedData = new ArrayList<Input>();

	private JButton 
	inputFileButton = new JButton("Input File"),
	outputFolderButton = new JButton("Output Folder"),
	runButton = new JButton("Run");
	
	private JTextField
	inputFileField = new JTextField(),
	outputFolderField = new JTextField(),
	outputMessageField = new JTextField();
	
	public FileParser() {
		JPanel buttonPanel = new JPanel();
		
		inputFileButton.addActionListener(new OpenListener());
		buttonPanel.add(inputFileButton);
		
		outputFolderButton.addActionListener(new OutputListener());
		buttonPanel.add(outputFolderButton);
		
		runButton.addActionListener(new RunListener());
		runButton.setEnabled(false);
		buttonPanel.add(runButton);
		
		inputFileField.setEditable(false);
		inputFileField.setText("Select A Input File");
		
		outputFolderField.setEditable(false);
		outputFolderField.setText("Select An Output Folder");
		
		outputMessageField.setEditable(false);
		
		Container container = getContentPane();
		container.add(buttonPanel, BorderLayout.NORTH);
		container.add(outputFolderField, BorderLayout.LINE_END);
		container.add(inputFileField, BorderLayout.LINE_START);
		container.add(outputMessageField, BorderLayout.SOUTH);
	}
	
	private class OpenListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			
			int rval = c.showOpenDialog(FileParser.this);
			
			if (rval == JFileChooser.APPROVE_OPTION) {
				file = c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName();
				inputSelected = true;
				checkIfReady();
				parseInput(file);
			}
		}
		
	}
	
	private class OutputListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int rval = c.showSaveDialog(FileParser.this);
			if (rval == JFileChooser.APPROVE_OPTION) {
				directory = c.getSelectedFile().toString();
				outputFolderField.setText("Output: " + directory);
				ouputSelected = true;
				checkIfReady();
			}
		}
		
	}
	
	private class RunListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (parsedData.size() <= 0) {
				showPopUpMessage("No Mathces Found!");
				return;
			}
			DatabaseCreator dbc = null;
			try {
				dbc = new DatabaseCreator(directory, parsedData);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			outputMessageField.setText("...Processing...");
			
			if (dbc.execute()) {
				outputMessageField.setText("Creation Successful");
			}
			else {
				outputMessageField.setText("Creation Failed");
			}
			
		}
		
	}
	
	private void checkIfReady() {
		runButton.setEnabled(inputSelected && ouputSelected);
	}
	
	private void parseInput(String file) {
		File inputFile = new File(file);
		Scanner input = null;
		try {
			input = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			showPopUpMessage("Could not find file \"" + file + "\"");
		}
		
		if (input != null) {
			int
			linesFound = 0,
			matchesFound = 0;
			
			while (input.hasNextLine()) {
				// Get the current line
				String currentLine = input.nextLine();
				
				// If there is a quote we don't want to parse it
				if (currentLine.contains("\""))
					continue;
				
				// Split into array
				String[] data = currentLine.split(",");
				
				// If the data is correct
				if (data.length == 3) {
					linesFound++;
					
					parsedData.add(new Input(data[0], data[1], data[2]));
					
					if (linesFound % 6 == 0) {
						matchesFound++;
					}
				}
			}

			inputFileField.setText(matchesFound + " possible matches to process.");
		}
		
//		test(parsedData);
		
		input.close();
	}
	
	private void showPopUpMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
//	private void test(List<Input> list) {
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
//	}
	
}
