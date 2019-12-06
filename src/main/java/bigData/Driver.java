package bigData;

import java.io.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import data_producer.DataProducer;
import data_producer.LineProducer;
import jobs.SetCount;
import jobs.WordCount;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.Component;

public class Driver {
	private static JFrame appFrame;
	private static JTable setTable;
	private static String wordCountInputFilepath = "";
	private static String wordCountOutputFilepath = "";
	private static String setCountInputFilepath = "";
	private static String setCountOutputFilepath = "";
	private static JTextField wordCountRegex;
	private static JLabel wordCountRuntimeLabel;
	private static JLabel setCountRuntimeLabel;
	private static JButton setCountStartButton;
	private static JButton wordCountStartButton;
	
	public static void main(String[] args) throws IOException{
		appFrame = new JFrame("Selection");
		appFrame.setMinimumSize(new Dimension(800, 550));
		appFrame.setTitle("Text Analyzer");
	    appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    appFrame.pack();
	    appFrame.setLocationRelativeTo(null);
	    appFrame.getContentPane().setLayout(new BorderLayout(0, 0));
	    
	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    tabbedPane.setName("tabPane");
	    appFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	    
	    /*
	     * 
	     * Set Count UI
	     */
	    JPanel wordCountPanel = new JPanel();
	    tabbedPane.addTab("Word Count", null, wordCountPanel, null);
	    wordCountPanel.setLayout(new BorderLayout(0, 0));
	    
	    JPanel wordCountNorthPane = new JPanel();
	    wordCountPanel.add(wordCountNorthPane, BorderLayout.NORTH);
	    wordCountNorthPane.setLayout(new BoxLayout(wordCountNorthPane, BoxLayout.X_AXIS));
	    
	    JPanel inputPane = new JPanel();
	    wordCountNorthPane.add(inputPane);
	    
	    JButton wordCountFileInput = new JButton("Select Input File");
	    wordCountFileInput.setPreferredSize(new Dimension(160, 23));
	    wordCountFileInput.setMinimumSize(new Dimension(117, 23));
	    wordCountFileInput.setMaximumSize(new Dimension(117, 23));
	    inputPane.add(wordCountFileInput);
	    
	    JLabel label = new JLabel("Input File:");
	    inputPane.add(label);
	    
	    JLabel wordCountInputFilename = new JLabel("N/A");
	    inputPane.add(wordCountInputFilename);
	    
	    JPanel outputPane = new JPanel();
	    wordCountNorthPane.add(outputPane);
	    
	    JButton wordCountFileOutput = new JButton("Select Output File");
	    wordCountFileOutput.setPreferredSize(new Dimension(160, 23));
	    outputPane.add(wordCountFileOutput);
	    
	    JLabel label_2 = new JLabel("Output File:");
	    outputPane.add(label_2);
	    
	    JLabel wordCountOutputFilename = new JLabel("N/A");
	    outputPane.add(wordCountOutputFilename);
	    
	    JPanel panel = new JPanel();
	    wordCountPanel.add(panel, BorderLayout.CENTER);
	    
	    JLabel lblRegularExpression = new JLabel("Regular Expression: ");
	    panel.add(lblRegularExpression);
	    
	    wordCountRegex = new JTextField();
	    wordCountRegex.setMinimumSize(new Dimension(100, 20));
	    wordCountRegex.setPreferredSize(new Dimension(100, 20));
	    panel.add(wordCountRegex);
	    wordCountRegex.setColumns(10);
	    
	    JPanel wordCountSouthPane = new JPanel();
	    wordCountPanel.add(wordCountSouthPane, BorderLayout.SOUTH);
	    
	    wordCountStartButton = new JButton("Start");
	    wordCountStartButton.setPreferredSize(new Dimension(125, 23));
	    wordCountSouthPane.add(wordCountStartButton);
	    
	    JLabel lblRuntime = new JLabel("Runtime: ");
	    wordCountSouthPane.add(lblRuntime);
	    
	    wordCountRuntimeLabel = new JLabel("");
	    wordCountRuntimeLabel.setPreferredSize(new Dimension(100, 14));
	    wordCountSouthPane.add(wordCountRuntimeLabel);
	    
	    
	    wordCountFileInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int action = fileChooser.showOpenDialog(null);
				if (action == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String filename = selectedFile.getName();
					wordCountInputFilename.setText(filename);
					wordCountInputFilepath = selectedFile.getAbsolutePath();
				}
			}
	    	
	    });
	    
	    wordCountFileOutput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int action = fileChooser.showOpenDialog(null);
				if (action == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String filename = selectedFile.getName();
					wordCountOutputFilename.setText(filename);
					wordCountOutputFilepath = selectedFile.getAbsolutePath();
				}
			}
		});
	    
	    wordCountStartButton.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
	    		String regex = wordCountRegex.getText().trim();
	    		if (regex.isEmpty()) {
	    			JOptionPane.showMessageDialog(appFrame, "You must enter a regular expression.");
	    		}
	    		else if (validateSelectedFiles(wordCountInputFilepath, wordCountOutputFilepath)) {
	    			runWordCount(wordCountInputFilepath, wordCountOutputFilepath, regex);
	    		}
	    	}
	    });
	    
	    
	    
	    /*
	     * 
	     * Set Count UI
	     */
	    JPanel setCountPanel = new JPanel();
	    tabbedPane.addTab("Set Count", null, setCountPanel, null);
	    setCountPanel.setLayout(new BorderLayout(0, 0));
	    
	    JScrollPane setPane = new JScrollPane();
	    setPane.setBorder(new LineBorder(new Color(130, 135, 144)));
	    setPane.setPreferredSize(new Dimension(300, 100));
	    setPane.setMinimumSize(new Dimension(300, 100));
	    setCountPanel.add(setPane, BorderLayout.CENTER);
	    
	    DefaultTableModel setTableModel = new DefaultTableModel(
	    		new Object[][] { {"", ""}, },
	    		new String[] { "Set Name", "Set Words" }
	    );
	    setTable = new JTable(setTableModel);
	    setTable.setBorder(new LineBorder(new Color(0, 0, 0)));
	    setPane.setViewportView(setTable);
	    
	    JPanel northPane = new JPanel();
	    setCountPanel.add(northPane, BorderLayout.NORTH);
	    northPane.setLayout(new BoxLayout(northPane, BoxLayout.X_AXIS));
	    
	    JPanel inputFilePane = new JPanel();
	    inputFilePane.setAlignmentX(Component.LEFT_ALIGNMENT);
	    northPane.add(inputFilePane);
	    
	    JButton inputFileButton = new JButton("Select Input File");
	    inputFileButton.setPreferredSize(new Dimension(160, 23));
	    inputFileButton.setMinimumSize(new Dimension(117, 23));
	    inputFileButton.setMaximumSize(new Dimension(117, 23));
	    inputFilePane.add(inputFileButton);
	    
	    JLabel inputLabel = new JLabel("Input File:");
	    inputFilePane.add(inputLabel);
	    
	    JLabel inputFilenameLabel = new JLabel("N/A");
	    inputFilePane.add(inputFilenameLabel);
	    
	    JPanel outputFilePane = new JPanel();
	    outputFilePane.setAlignmentX(Component.RIGHT_ALIGNMENT);
	    northPane.add(outputFilePane);
	    
	    JButton outputFileButton = new JButton("Select Output File");
	    outputFileButton.setPreferredSize(new Dimension(160, 23));
	    outputFilePane.add(outputFileButton);
	    
	    JLabel outputLabel = new JLabel("Output File:");
	    outputFilePane.add(outputLabel);
	    
	    JLabel outputFilenameLabel = new JLabel("N/A");
	    outputFilePane.add(outputFilenameLabel);
	    
	    JPanel southPane = new JPanel();
	    setCountPanel.add(southPane, BorderLayout.SOUTH);
	    
	    JButton addSetButton = new JButton("Add Set");
	    addSetButton.setPreferredSize(new Dimension(125, 23));
	    southPane.add(addSetButton);
	    
	    JButton removeSetButton = new JButton("Remove Set");
	    removeSetButton.setPreferredSize(new Dimension(125, 23));
	    southPane.add(removeSetButton);
	    
	    setCountStartButton = new JButton("Start");
	    setCountStartButton.setPreferredSize(new Dimension(125, 23));
	    southPane.add(setCountStartButton);
	    
	    JLabel label_1 = new JLabel("Runtime: ");
	    southPane.add(label_1);
	    
	    setCountRuntimeLabel = new JLabel("");
	    setCountRuntimeLabel.setPreferredSize(new Dimension(100, 14));
	    southPane.add(setCountRuntimeLabel);
	    appFrame.setVisible(true);		
    	    
	    inputFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int action = fileChooser.showOpenDialog(null);
				if (action == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String filename = selectedFile.getName();
					inputFilenameLabel.setText(filename);
					setCountInputFilepath = selectedFile.getAbsolutePath();
				}
			}
	    	
	    });
	    
	    outputFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int action = fileChooser.showOpenDialog(null);
				if (action == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String filename = selectedFile.getName();
					outputFilenameLabel.setText(filename);
					setCountOutputFilepath = selectedFile.getAbsolutePath();
				}
			}
		});
	    
	    addSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setTableModel.addRow(new Object[] {"", ""});
			}
	    });
	    
	    removeSetButton.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent arg0) {
	    		int selectedIndex = setTable.getSelectedRow();
	    		while (selectedIndex > -1) {
	    			setTableModel.removeRow(selectedIndex);
	    			selectedIndex = setTable.getSelectedRow();
	    		}
			}
	    });
	    
	    setCountStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TableCellEditor editor = setTable.getCellEditor();
				if (editor != null) {
					editor.stopCellEditing();
				}
				List<Set<String>> wordSets = new ArrayList<>();
				int rows = setTableModel.getRowCount();
				for (int i = 0; i < rows; i++) {
					Object value = setTableModel.getValueAt(i, 1);
					String setWords = value.toString();
					List<String> words = Arrays.asList(setWords.split(","));
					Set<String> wordSet = new HashSet<String>();
					wordSet.addAll(words);
					wordSets.add(wordSet);
				}
				if (validateSelectedFiles(Driver.setCountInputFilepath, Driver.setCountOutputFilepath)) {
					runSetCount(Driver.setCountInputFilepath, Driver.setCountOutputFilepath, wordSets);
				}
			}
	    });
	}
	
	private static boolean validateSelectedFiles(String inputPath, String outputPath) {
		boolean valid = true;
		File input = new File(inputPath);
		boolean inputExists = input.exists();
		File output = new File(outputPath);
		boolean outputExists = output.exists();
		if (!inputExists && !outputExists) {
			valid = false;
			JOptionPane.showMessageDialog(appFrame, "You must select an input file and output file.");
		}
		else if (!inputExists) {
			valid = false;
			JOptionPane.showMessageDialog(appFrame, "You must select an input file.");
		}
		else if (!outputExists) {
			valid = false;
			JOptionPane.showMessageDialog(appFrame, "You must select an output file.");
		}
		return valid;
	}
	
	private static void runWordCount(String inputPath, String outputPath, String regex){
		Runnable task = () -> {
			long start = System.currentTimeMillis();
			try {
				wordCountStartButton.setEnabled(false);
				DataProducer<String> producer = new LineProducer(1024 * 100000);
				WordCount wc = new WordCount(producer, 20, regex, inputPath, outputPath);
				wc.execute();
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(appFrame, e.getMessage());
				wordCountStartButton.setEnabled(true);
			}
			long runtimeMillis = System.currentTimeMillis() - start;
			long seconds = runtimeMillis / 1000;
			wordCountRuntimeLabel.setText(seconds > 0 ? seconds + " s" : runtimeMillis + " ms");
			wordCountStartButton.setEnabled(true);
		};
		Thread wordCountThread = new Thread(task);
		wordCountThread.start();
	}
	
	private static void runSetCount(String inputPath, String outputPath, List<Set<String>> wordSets) {
		Runnable task = () -> {
			long start = System.currentTimeMillis();
			try {
				setCountStartButton.setEnabled(false);
				DataProducer<String> producer = new LineProducer(1024 * 100000);
				SetCount setCount = new SetCount(producer, wordSets, inputPath, outputPath);
				setCount.execute();
			}
			catch (Exception e) {
				if (e.getMessage() != null && !e.getMessage().isEmpty()) {
					JOptionPane.showMessageDialog(appFrame, e.getMessage());
				}
				setCountStartButton.setEnabled(true);
			}
			long runtimeMillis = System.currentTimeMillis() - start;
			long seconds = runtimeMillis / 1000;
			setCountRuntimeLabel.setText(seconds > 0 ? seconds + " s" : runtimeMillis + " ms");
			setCountStartButton.setEnabled(true);
		};
		Thread setCountThread = new Thread(task);
		setCountThread.start();
	}
}
