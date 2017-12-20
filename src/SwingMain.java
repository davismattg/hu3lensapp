//import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;


















import java.awt.GridLayout;


//import javax.swing.JLabel;
import javax.swing.JButton;

//import Main.SerialPortReader;




















import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.*;

import jssc.SerialPort; 
import jssc.SerialPortEvent; 
import jssc.SerialPortEventListener; 
import jssc.SerialPortException;
import jssc.SerialPortList;

import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.Font;
import javax.swing.JCheckBox;


public class SwingMain extends JFrame implements TableModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3618079154712536294L;
	
	static String[] portNames;
	
	private JScrollPane lensListScroller;
	private static JProgressBar progressBar;
	private JPanel listPane;
	
	public static JTable workListTable;
	public static JTable databaseTable;
	
	private static JLabel connectedLabel;
	private static JLabel workListLabel;
	private static JLabel databaseLabel;
//	private static JLabel workListNumLensesLabel;
	private static JLabel fileNameLabel;
	
	private static JButton btnImportFromHu;
	private static JButton btnExportToHu;
	private static JButton btnSelectNone;
	private static JButton btnSelectAll;
	private static JButton btnImportFile;
	private static JButton btnExportFile;
	
//	private static Component buttonMargin;
	
	public static WorkListTableModel workListTableModel;
	public static DatabaseTableModel databaseTableModel;
	
	static File lensFile;
	static TimerTask ping;
	static TimerTask idlePing;
	static int pingResponseCounter = 0;
	
	static boolean startLensRx = false;
	static boolean lensSendMode = false;
	static boolean lensDone = false;
	static boolean numLensesSent = false;
	static boolean clearedForImport = false;
	
	static SerialPort serialPort;	
	static boolean portOpened = false;
	static boolean isConnected = false;
	static boolean lensReceiveMode = false;
	
	static int baudRate = 19200;
	static int currentLens = 0;
	static ArrayList<String> lensArray = new ArrayList<String>();
	static ArrayList<String> lensesToSend = new ArrayList<String>();
    static List<Map<String, String>> lensMap = new ArrayList<Map<String, String>>();
    
    static Integer numLenses;
    static Integer numLensesToSend;
	static String[] workListColumnNames = {"Manufacturer", "Series", "Focal Length(s)", "Serial/Note", "F", "I", "Z", "A", "B", "C", "Select"};
	static String[] databaseColumnNames = {"Manuf.", "Series", "FLength(s)", "Serial", "Cal", "My Lists"};
	
	static int[] workListColumnWidths = {100, 100, 100, 75, 50, 50, 50, 50, 50, 50, 50};
	static int[] databaseColumnWidths = {70, 70, 70, 50, 50, 50};
	
	static Vector lensWorkListData;
	static Vector databaseData;
	
	static Object[] importDialogOptions = {"Replace", "Add", "Cancel"};
	static String importDialogMessage = "There are already lenses in the work list.\n Would you like to replace or add to these lenses?";
	static String importDialogTitle = "Import Lenses from HU3";
	
	/* Initialize the String[] holding the lens manufacturer and series names */
	static String[] lensManufNames = {"Angenieux", "Canon", "Cooke", "Fujinon", "Leica", "Panavision", "Zeiss", "Other"};
	
	static String[] lensSeriesAngenieux = {"Optimo", "Rouge", "HR", "Other"};
	static String[] lensSeriesCanon = {"Cinema Prime", "Cinema Zoom", "Other"};
	static String[] lensSeriesCooke = {"S4", "S5", "Panchro", "Zoom", "Other"};
	static String[] lensSeriesFujinon = {"Premier Zoom", "Alura Zoom", "Prime", "Other"};
	static String[] lensSeriesLeica = {"Summilux Prime", "Other"};
	static String[] lensSeriesPanavision = {"Primo Prime", "Primo Zoom", "Anamorphic Prime", "Anamorphic Zoom", "P70 Prime", "Other"};
	static String[] lensSeriesZeiss = {"Master Prime", "Ultra Prime", "Compact Prime", "Zoom", "Other"};
	static String[] lensSeriesOther = {"Prime", "Zoom"};
	
	static String[][] lensSeriesArray = {lensSeriesAngenieux, lensSeriesCanon, lensSeriesCooke, lensSeriesFujinon, lensSeriesLeica, lensSeriesPanavision, lensSeriesZeiss, lensSeriesOther};
	
	static String[] lensSeriesPrime = {"Cinema Prime", "S4", "S5", "Panchro", "Prime", "Summilux Prime", "Primo Prime", "Anamorphic Prime", "P70 Prime", "Master Prime", "Ultra Prime", "Compact Prime"};
	
	/* Declare the Map used to relate mens series with the correct manufacturer */
	static Map lensSeries = new HashMap<String, String[]>();

	private static ArrayList<Lens> lensObjectArray = new ArrayList<>();
    private static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> lensPositionMap = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();

	static String connectedString = "Connected";
	static String disconnectedString = "Disconnected";
	
	static byte[] SYN = {0x16};
	static byte[] SOH = {0x01};
	static byte[] ENQ = {0x05};
	static byte[] SO = {0x0E};
	static byte[] LF = {0x0A};
	static byte[] CR = {0x0D};
	static byte[] STX = {0x02};
	static byte[] EOT = {0x04};
	static byte[] ACK = {0x06};
	static byte[] NAK = {0x15};
	static byte[] init_dl = {0x11, 0x05};
	static byte[] init_ul = {0x01, 0x05};
	static byte[] ACK_SYN = {0x06, 0x16};
	
	static String EOTStr = new String(EOT);
	static String ACKStr = new String(ACK);
	static String NAKStr = new String(NAK);
	static String LFStr = new String(LF);
	static String CRStr = new String(CR);
	
	static StringBuilder lensSBuilder = new StringBuilder("");
	
	static byte[] RxBuffer;
	
	static Timer mainTimer = new Timer();
	static Timer idleTimer = new Timer();
	
	static boolean timerActive = false;
	static boolean idleTimerActive = false;
	
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmAbout;
	private JMenuItem mntmImport;
	private JMenuItem mntmExport;
	private JMenuItem mntmQuit;
	private JMenu mnEdit;
	private JMenuItem mntmDuplicate;
	private Component rigidArea;
	private JPanel databasePane;
	private JPanel importExportPane;
	private JPanel workListPane;
	private JLabel lblNewLabel;
	private JScrollPane scrollPane;
	private JPanel buttonPane;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private Component rigidArea_1;
	private JButton btnSelectAll_1;
	private JButton btnSelectNone_1;
	private JPanel selectLensesPane;
	private JProgressBar progressBar_1;
	private JLabel lblNoHuConnected;
	private Component rigidArea_2;
	private Component rigidArea_3;
	private JLabel lblNewLabel_1;
	private JPanel lensManufAndSeriesPane;
	private JLabel lensManufLabel;
	private JLabel lensSeriesLabel;
	private JPanel lensManufPane;
	private JPanel lensSeriesPane;
	private static JComboBox lensManufComboBox;
	private static JComboBox lensSeriesComboBox;
	private static DefaultComboBoxModel lensSeriesComboBoxModel;
	private JPanel lensFocalAndSerialPane;
	private JPanel lensFocalLengthPane;
	private JPanel lensSerialPane;
	private JLabel lensFocalLengthLabel;
	private JLabel lensSerialLabel;
	private JTextField lensFocal1TextField;
	private JTextField lensFocal2TextField;
	private JPanel lensFocalLengthInnerPane;
	private JLabel lensFocalDashLabel;
	private JLabel lensFocalMMLabel;
	private JTextField lensSerialTextField;
	private JScrollPane databaseScroller;
	private JPanel lensPrimeZoomPanel;
	private static JCheckBox checkBoxPrime;
	private static JCheckBox checkBoxZoom;
	
	/**
	 * Create the frame.
	 */
	public SwingMain() {
		setBounds(new Rectangle(100, 100, 1000, 800));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Set the Application Title */
		this.setTitle("HU3 Lens Management");
		
		/* Initialize the HashMap used to store the lens manufacturer/series combinations */
		initializeLenses();
		
		importExportPane = new JPanel();
		importExportPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		importExportPane.setLayout(new BoxLayout(importExportPane, BoxLayout.LINE_AXIS));
		
		btnImportFile = new JButton("Import...");
		btnImportFile.setToolTipText("Import Preston Lens file, CSV, Excel");
		importExportPane.add(btnImportFile);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		rigidArea.setMinimumSize(new Dimension(10, 10));
		rigidArea.setMaximumSize(new Dimension(10, 10));
		importExportPane.add(rigidArea);
		
		btnExportFile = new JButton("Save");
		importExportPane.add(btnExportFile);
		
		databasePane = new JPanel();
		databasePane.setBorder(new EmptyBorder(0, 10, 10, 0));
		databasePane.setLayout(new BoxLayout(databasePane, BoxLayout.Y_AXIS));
		
		workListPane = new JPanel();
		workListPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		workListPane.setLayout(new BoxLayout(workListPane, BoxLayout.PAGE_AXIS));
		
		fileNameLabel = new JLabel("");
		workListPane.add(fileNameLabel);
		
		/* Work List Table */
		workListTableModel = new WorkListTableModel(workListColumnNames, new Vector());
		workListTable = new JTable(workListTableModel);
		workListTable.setAutoCreateRowSorter(true);
		workListTable.getModel().addTableModelListener(this);
		
		lensListScroller = new JScrollPane(workListTable);
		workListPane.add(lensListScroller);
		workListTable.setFillsViewportHeight(true);
		lensListScroller.setAlignmentX(LEFT_ALIGNMENT);
		
		
		/* Set up the custom column widths for the work list table */
		TableColumn col = null;
		for (int i=0; i < workListColumnNames.length; i++) {
			col = workListTable.getColumnModel().getColumn(i);
			col.setPreferredWidth(workListColumnWidths[i]);
		}
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmAbout = new JMenuItem("About");
		mnFile.add(mntmAbout);
		
		mntmImport = new JMenuItem("Import...");
		mnFile.add(mntmImport);
		
		mntmExport = new JMenuItem("Export...");
		mnFile.add(mntmExport);
		
		mntmQuit = new JMenuItem("Quit");
		mnFile.add(mntmQuit);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmDuplicate = new JMenuItem("Duplicate");
		mnEdit.add(mntmDuplicate);
		
		
		/* Initialize the file chooser to import a lens file */
		final JFileChooser fc = new JFileChooser();

		/* Set up the container for all the UI components and add them to it */
		getContentPane().add(importExportPane, BorderLayout.NORTH);
		getContentPane().add(databasePane, BorderLayout.WEST);
		
		databaseLabel = new JLabel("Lens Database");
		databaseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		databaseLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		databasePane.add(databaseLabel);
		
		lensManufAndSeriesPane = new JPanel();
		lensManufAndSeriesPane.setBorder(new EmptyBorder(0, 0, 10, 0));
		databasePane.add(lensManufAndSeriesPane);
		lensManufAndSeriesPane.setLayout(new BoxLayout(lensManufAndSeriesPane, BoxLayout.X_AXIS));
		
		lensManufPane = new JPanel();
		lensManufPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		lensManufPane.setBorder(new EmptyBorder(0, 0, 0, 10));
		lensManufAndSeriesPane.add(lensManufPane);
		lensManufPane.setLayout(new BoxLayout(lensManufPane, BoxLayout.Y_AXIS));
		
		lensManufLabel = new JLabel("Manufacturer");
		lensManufLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lensManufPane.add(lensManufLabel);
		
		lensManufComboBox = new JComboBox(lensManufNames);
		lensManufComboBox.setMaximumSize(new Dimension(130, 20));
		lensManufComboBox.setMinimumSize(new Dimension(130, 20));
		lensManufComboBox.setAlignmentY(Component.TOP_ALIGNMENT);
		lensManufPane.add(lensManufComboBox);
		
		lensSeriesPane = new JPanel();
		lensManufAndSeriesPane.add(lensSeriesPane);
		lensSeriesPane.setLayout(new BoxLayout(lensSeriesPane, BoxLayout.Y_AXIS));
		
		lensSeriesLabel = new JLabel("Series");
		lensSeriesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lensSeriesPane.add(lensSeriesLabel);
		
		lensSeriesComboBox = new JComboBox();
		lensSeriesComboBox.setMinimumSize(new Dimension(130, 20));
		lensSeriesComboBox.setMaximumSize(new Dimension(130, 20));
		lensSeriesPane.add(lensSeriesComboBox);
		lensSeriesComboBoxModel = new DefaultComboBoxModel((String[]) lensSeries.get(lensManufNames[0]));
		lensSeriesComboBox.setModel(lensSeriesComboBoxModel);
		
		lensFocalAndSerialPane = new JPanel();
		lensFocalAndSerialPane.setBorder(new EmptyBorder(0, 0, 10, 0));
		databasePane.add(lensFocalAndSerialPane);
		lensFocalAndSerialPane.setLayout(new BoxLayout(lensFocalAndSerialPane, BoxLayout.X_AXIS));
		
		lensFocalLengthPane = new JPanel();
		lensFocalLengthPane.setBorder(new EmptyBorder(0, 0, 0, 10));
		lensFocalAndSerialPane.add(lensFocalLengthPane);
		lensFocalLengthPane.setLayout(new BoxLayout(lensFocalLengthPane, BoxLayout.Y_AXIS));
		
		lensFocalLengthLabel = new JLabel("Focal Length(s)");
		lensFocalLengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lensFocalLengthPane.add(lensFocalLengthLabel);
		
		lensFocalLengthInnerPane = new JPanel();
		lensFocalLengthPane.add(lensFocalLengthInnerPane);
		lensFocalLengthInnerPane.setLayout(new BoxLayout(lensFocalLengthInnerPane, BoxLayout.X_AXIS));
		
		lensFocal1TextField = new JTextField();
		lensFocal1TextField.setMinimumSize(new Dimension(50, 20));
		lensFocalLengthInnerPane.add(lensFocal1TextField);
		lensFocal1TextField.setMaximumSize(new Dimension(50, 20));
		lensFocal1TextField.setColumns(10);
		
		lensFocalDashLabel = new JLabel("-");
		lensFocalDashLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lensFocalDashLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		lensFocalLengthInnerPane.add(lensFocalDashLabel);
		
		lensFocal2TextField = new JTextField();
		lensFocal2TextField.setMinimumSize(new Dimension(50, 20));
		lensFocal2TextField.setMaximumSize(new Dimension(50, 20));
		lensFocalLengthInnerPane.add(lensFocal2TextField);
		lensFocal2TextField.setColumns(10);
		
		lensFocalMMLabel = new JLabel("mm");
		lensFocalMMLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
		lensFocalLengthInnerPane.add(lensFocalMMLabel);
		
		lensSerialPane = new JPanel();
		lensSerialPane.setBorder(new EmptyBorder(0, 0, 0, 10));
		lensFocalAndSerialPane.add(lensSerialPane);
		lensSerialPane.setLayout(new BoxLayout(lensSerialPane, BoxLayout.Y_AXIS));
		
		lensSerialLabel = new JLabel("Serial/Note");
		lensSerialLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lensSerialPane.add(lensSerialLabel);
		
		lensSerialTextField = new JTextField();
		lensSerialTextField.setMinimumSize(new Dimension(50, 20));
		lensSerialTextField.setMaximumSize(new Dimension(50, 20));
		lensSerialPane.add(lensSerialTextField);
		lensSerialTextField.setColumns(10);
		
		lensPrimeZoomPanel = new JPanel();
		lensFocalAndSerialPane.add(lensPrimeZoomPanel);
		lensPrimeZoomPanel.setLayout(new BoxLayout(lensPrimeZoomPanel, BoxLayout.Y_AXIS));
		
		checkBoxPrime = new JCheckBox("Prime");
		lensPrimeZoomPanel.add(checkBoxPrime);
		
		checkBoxZoom = new JCheckBox("Zoom");
		lensPrimeZoomPanel.add(checkBoxZoom);
		
		/* Database Table */
		databaseTableModel = new DatabaseTableModel(databaseColumnNames, new Vector());
		databaseTable = new JTable(databaseTableModel);
		databaseTable.setPreferredSize(new Dimension(400, 0));
		databaseTable.setPreferredScrollableViewportSize(new Dimension(400, 500));
		databaseTable.setFillsViewportHeight(true);
		databaseTable.setAutoCreateRowSorter(true);
		databaseTable.getModel().addTableModelListener(this);
		
		databaseScroller = new JScrollPane(databaseTable);
		databaseScroller.setPreferredSize(new Dimension(400, 500));
		databaseScroller.setMaximumSize(new Dimension(400, 500));
		databasePane.add(databaseScroller);
		getContentPane().add(workListPane, BorderLayout.CENTER);
		
		/* Set up column widths for the database table */
		TableColumn dbCol = null;
		for (int j=0; j < databaseColumnNames.length; j++) {
			dbCol = databaseTable.getColumnModel().getColumn(j);
			dbCol.setPreferredWidth(databaseColumnWidths[j]);
		}
		
		buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		
		btnImportFromHu = new JButton("Import From HU3");
		buttonPane.add(btnImportFromHu);
		
		rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_1.setMinimumSize(new Dimension(0, 10));
		rigidArea_1.setMaximumSize(new Dimension(0, 10));
		buttonPane.add(rigidArea_1);
		
		connectedLabel = new JLabel("No HU3 Connected");
		buttonPane.add(connectedLabel);
		
		rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_2.setMinimumSize(new Dimension(0, 10));
		rigidArea_2.setMaximumSize(new Dimension(0, 10));
		buttonPane.add(rigidArea_2);
		
		btnExportToHu = new JButton("Export To HU3");
		buttonPane.add(btnExportToHu);
		
		btnImportFromHu.setEnabled(false);
		btnExportToHu.setEnabled(false);
		
		rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_3.setMinimumSize(new Dimension(0, 10));
		rigidArea_3.setMaximumSize(new Dimension(0, 10));
		buttonPane.add(rigidArea_3);
		
		progressBar = new JProgressBar();
		progressBar.setMinimumSize(new Dimension(10, 20));
		progressBar.setMaximumSize(new Dimension(250, 20));
		buttonPane.add(progressBar);
		getContentPane().add(buttonPane, BorderLayout.EAST);
		
		/* Click handler for Import from HU3 Button */
		btnImportFromHu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("import clicked");
				
				/* Check if there's already lenses stored in the work list */
				if (lensObjectArray.size() > 0) {
					System.out.println("ask the user if they'd like to add or replace lenses in work list");
					
					/* Show an option dialog to the user to make their choice */
					int n = JOptionPane.showOptionDialog(getContentPane(), importDialogMessage, importDialogTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, importDialogOptions, importDialogOptions[0]);  
					System.out.println("user chose: " + n);
					
					/* Process their button click */
					switch(n) {
						case 0:		// "Replace", so clear the current lenses
							workListTableModel.clearAll();
							lensArray.clear();
							lensObjectArray.clear();
							
							clearedForImport = true;
							break;
						case 1:		// "Add", so don't need to clear the lenses
							workListTableModel.clearAll();
							lensObjectArray.clear();
							clearedForImport = true;
							System.out.println("lensArray: " + lensArray);
							break;
						case 2:		// "Cancel", do nothing
							clearedForImport = false;
							break;
					}
				}
				else {		// nothing in the work list, so g'head and import
					clearedForImport = true;
				}
				
				if (clearedForImport) {					// flag in case there were already lenses in the work list
					try {
						serialPort.writeBytes(init_dl);		// initiate the download of data from HU3
					} catch (SerialPortException e) {
						e.printStackTrace();
					}
		            isConnected = true;						// set the flags used when parsing the incoming data
					lensReceiveMode = true;
				}
			}
		});
		
		/* Click handler for Export to HU3 Button */
		btnExportToHu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("export clicked. Add dialog to select add/replace on HU3");
				lensesToSend.clear();
				numLensesToSend = 0;
				
				try {
					lensesToSend = prepareLensesForTransmit();
					lensSendMode = true;
					serialPort.writeBytes(init_ul);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
				
				isConnected = true;				
			}
		});
		
		/* Hide the progress bar since there's no HU3 connected upon startup*/
		progressBar.setVisible(false);
		progressBar.setStringPainted(true);
		
		selectLensesPane = new JPanel();
		selectLensesPane.setBorder(new EmptyBorder(0, 10, 10, 10));
		selectLensesPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		btnSelectAll = new JButton("Select All");
		selectLensesPane.add(btnSelectAll);
		
		btnSelectNone = new JButton("Select None");
		selectLensesPane.add(btnSelectNone);
		getContentPane().add(selectLensesPane, BorderLayout.SOUTH);
		
		/* Click handler for Select All Button */
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("select all clicked");
				selectLenses(true);
			}
		});
		
		/* Click handler for Select None Button */
		btnSelectNone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("select none clicked");
				selectLenses(false);
			}
		});		
		
		/* Click handler for Import File Button */
		btnImportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Import file");
				
				int returnVal = fc.showOpenDialog(SwingMain.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					lensFile = fc.getSelectedFile();
					System.out.println("Open the file: " + lensFile.getName());
					
					importLensFile(lensFile);
				}
			}
		});
		
		/* Click handler for Export File Button */
		btnExportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Export File");
				
				int returnVal = fc.showSaveDialog(SwingMain.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File lensFileOut = fc.getSelectedFile();
					lensesToSend = prepareLensesForTransmit();
					
					System.out.println("Save work list to file: " + lensFileOut.getName());
					
					exportLensFile(lensFileOut, lensesToSend);
				}
			}
		});	
		
		/* ActionListener for Lens Manufacturer ComboBox */
		lensManufComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Lens manuf ComboBox event");
				JComboBox cb = (JComboBox) e.getSource();
				String manuf = (String) cb.getSelectedItem();
				updateLensSeriesComboBox(manuf);
			}
		});
		
		/* ActionListener for Lens Series ComboBox */
		lensSeriesComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Lens series ComboBox event");
				JComboBox cb = (JComboBox) e.getSource();
				String series = (String) cb.getSelectedItem();
				updatePrimeOrZoomCheckBoxes(series);
			}
		});
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		portNames = SerialPortList.getPortNames();										// get the list of available serial ports
		
		/* If no serial ports available, the program can't communicate with the HU3. But it can still do file-related functions */
		if (portNames.length == 0) {
			System.out.println("No serial ports available :(");
			// TODO: Make this alert the user via alert or similar
		}
		
		/* There's at least one available serial port, so try to set it up */
		else {
			for (int i=0; i < portNames.length; i++) {
//				serialPort = new SerialPort(portNames[i]);
				System.out.println(portNames[i]);
			}
		}
	    serialPort = new SerialPort("COM1"); 											// declare the port. TODO: make the program dynamically scan port list and check for correct one						
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain frame = new SwingMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
			        portOpened = serialPort.openPort();																	// Open port
			        if (portOpened) {																					// if the port opened OK
				        serialPort.setParams(baudRate, 8, 1, 0);														// Set up port
				        int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;					// Prepare mask
				        serialPort.setEventsMask(mask);																	// Set mask
				        serialPort.addEventListener(new SerialPortReader());											// Add SerialPortEventListener, called when diff events happen
				      
				        startTimer("main", 500, 100);
			        }
			    }
			    catch (SerialPortException ex) {
			        System.out.println(ex);
			    }
			}
		});
	}
	
	/** This method fires when the data in the work list table changes. This happens when a lens is selected/de-selected
	 * for transmission to the HU3. This method also updates the Lens object array's isSelected property 
	 */
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int col = e.getColumn();
		
//		System.out.println("event detected at row " + row + ", col " + col);
		
		TableModel model = (TableModel) e.getSource();
		if (col == 9) {		// the "Select" column
			boolean data = (Boolean) model.getValueAt(row,  col);					// get the updated isSelected value
			lensObjectArray.get(row).setIsSelected(data);							// update the Lens object with the new value
		}		
	}
	
	/** This method pre-populates the ComboBox for lens manuf and series names */
	private static void initializeLenses() {
		for (int i=0; i < lensManufNames.length; i++) {
			lensSeries.put(lensManufNames[i], lensSeriesArray[i]);
		}
	}
	
	/** This method either selects or de-selects all lenses in the work list. Only lenses that are selected
	 * in the work list are transmitted to the HU3
	 */
	private static void selectLenses(boolean allLenses) {		
		for (int i = 0; i < workListTableModel.getRowCount(); i++) {
			workListTableModel.setValueAt(allLenses, i, 9);
			lensObjectArray.get(i).setIsSelected(allLenses);
		}
	}
	
	/** This method updates the Lens Series ComboBox dropdown when the user changes the manufacturer ComboBox dropdown
	 * 
	 */
	private static void updateLensSeriesComboBox(String manuf) {
		int position = Arrays.asList(lensManufNames).indexOf(manuf);
		
		if (position == -1) {
			
		}
		else {
			lensSeriesComboBoxModel.removeAllElements();
			String[] newSeriesOptions = (String[]) lensSeries.get(manuf);
			for (String series : newSeriesOptions) {
				lensSeriesComboBoxModel.addElement(series);
			}
		}
	}
	
	/** This method updates the Prime/Zoom checkboxes in the Database panel according to whether the selected lens series is a prime or zoom.
	 * If it's "Other", both check boxes are cleared for the user to specify the type themselves.
	 */
	private static void updatePrimeOrZoomCheckBoxes(String series) {
		if (series == "Other") {
			System.out.println("Other series, deselect both");
			checkBoxPrime.setEnabled(true);
			checkBoxPrime.setSelected(false);
			checkBoxZoom.setEnabled(true);
			checkBoxZoom.setSelected(false);
			
		}
		else if (Arrays.asList(lensSeriesPrime).indexOf(series) == -1) {
			System.out.println("lens is a zoom, check the zoom box");
			checkBoxPrime.setEnabled(false);
			checkBoxPrime.setSelected(false);
			checkBoxZoom.setEnabled(false);
			checkBoxZoom.setSelected(true);
		}
		else {
			System.out.println("lens is a prime, check the prime box");
			checkBoxPrime.setEnabled(false);
			checkBoxPrime.setSelected(true);
			checkBoxZoom.setEnabled(false);
			checkBoxZoom.setSelected(false);
		}
		
	}
	
	/** Buffer the input from the serial port so we are always dealing with complete packets
	 * 
	 * @param bytes
	 * @return
	 */
	private static String buildLensPacket(byte[] bytes) {
	    String text = bytesToText(bytes);
	    if (text.contains(bytesToText(EOT))) {        //EOT is sent after transfer is complete
	        System.out.println("EOT detected. Returning EOT");
	        lensSBuilder.setLength(0);      // clear the buffer
	        return EOTStr;
	    }
	    else {          // if no newline character detected, add response to the buffer
	        lensSBuilder.append(text);
	        if (text.contains("\r")) {
	            String lensString = lensSBuilder.toString();
	            lensSBuilder.setLength(0);
	            return lensString;          // if newline detected, add the text to the buffer, then return the whole buffer
	        } else {
	            return "";
	        }
	    }
	}
		
	/** This method converts a byte array to a string
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToText(byte[] bytes) {
		final String text;
		if (bytes != null) {
			text = new String(bytes);
		}
		else {
			text = "";
		}
	    return text;
	}
	
	/** This This method looks at all the lenses in the work list (represented by lensObjectArray) and checks if each one
	 is "selected" by looking at the isSelected property of each Lens. If it is, it adds the data string to an 
	 ArrayList which is returned and used in the method "receiveLensData" below. */
	private ArrayList<String> prepareLensesForTransmit() {
		ArrayList<String> selectedLenses = new ArrayList<>();
		
		for (Lens lens : lensObjectArray) {							// iterate over the Lens array and check if each one is selected for transmit
			if (lens.isSelected) {
				selectedLenses.add(lens.getDataString());
			}
		}
		
		numLensesToSend = selectedLenses.size();					// the max number of lenses to send to the HU3
		
		return selectedLenses;
	}
		
	/** This method processes the incoming data on the serial port. The SerialPortReader class determines whether
	 * this method or transmitLensData is called when there's data available on the serial port.
	 */
	private static void receiveLensData(String text) throws SerialPortException {
		System.out.println("receiveLensData: " + text);
		System.out.println("isConnected: " + isConnected);
		System.out.println("lensReceiveMode: " + lensReceiveMode);
		
		if (!isConnected) {
			if (text.contains("Hand")) {
	            System.out.println("Hand detected");
	            
	            if (idleTimerActive) {
	            	pingResponseCounter = 0;
	            }
	            else {
	            	startTimer("idle", 500, 200);
	            }
	            
	            updateConnectionStatus(true);
			}
		}
		else {
			if (lensReceiveMode) {		// flag indicating we're in the process of receiving (set by Import button)
				if (timerActive || idleTimerActive) {
					startTimer("none", 0, 0);
				}
				
				if (!startLensRx) {		// not ready for import, need to receive the number of lenses from HU3
		            if (!text.contains("Hand")) {
						serialPort.writeBytes(ACK);
			            startLensRx = true;														// ready for import
			            String trimmedString = text.replaceAll("[^\\w]", "");					// HU3 sends number of lenses
			            System.out.println("trimmedString: " + trimmedString);
			            numLenses = Integer.valueOf(trimmedString, 16);							// String to int	
			            System.out.println("Number of lenses detected: " + numLenses);
			            
			            progressBar.setVisible(true);
			            progressBar.setMaximum(numLenses);
			            lensWorkListData = new Vector(numLenses);
		            }
			    } 
				else {
			        if (text.contains(EOTStr) && currentLens >= numLenses) {								// final lens was received from HU3
			            System.out.println("EOT detected");
			            serialPort.writeBytes(ACK_SYN);														// tell the HU3 we received the last lens
			            lensReceiveMode = false;
			            startLensRx = false;
			            currentLens = 0;
			            progressBar.setVisible(false);
			            
			            startTimer("idle", 500, 200);

			            addLensesToWorkList();																// import the raw data to the work list and underlying arrays
			        } else {			// regular RX of each lens
			            System.out.println("Lens " + currentLens + " of " + numLenses + ": " + text);
			            
			            lensArray.add(stripChars(text));
			            progressBar.setValue(currentLens);
			            currentLens += 1;
			            serialPort.writeBytes(ACK);
			        }
			
			    }
			}
			else {
				if (text.contains("Hand")) {
		            System.out.println("Hand detected");
		            
		            if (idleTimerActive) {
		            	pingResponseCounter = 0;
		            }
//		            else {
//		            	startTimer("idle", 500, 200);
//		            }
		            
//		            updateConnectionStatus(true);
				}
			}
		}
	}
	
	/** This method is the main serial communication method for sending lenses from the PC to the HU3.
	 * It processes the incoming responses from the HU3 and sends the appropriate command/data.
	 */
    private static void transmitLensData(String text) throws SerialPortException {
    	System.out.println("transmitLensData: " + text);
    	
    	if (lensSendMode) {			// flag indicating we want to transmit lenses to the HU3
    		if (timerActive || idleTimerActive) {
				startTimer("none", 0, 0);
			}
    		if (text.contains(ACKStr)) {																	// HU3 responded with ACK to indicate successful receipt of message
                if (!lensDone) {																			// flag indicating whether we're still transmitting lenses
                    if (numLensesSent) {																	// flag indicating whether we've sent the number of lenses to the HU3 (required first part of comms)
                        System.out.println("ACK. Index: " + currentLens + " of " + numLensesToSend);
                        if (currentLens < numLensesToSend) {												// keep going until the end of the lens array
                            byte[] stx = {0x02};															// special STX for lens 
                            byte[] etx0 = {0x0A};
                            byte[] etx1 = {0x0D};															// special ETC for lens receiving
                            
                            String lensInfo = stripChars(lensesToSend.get(currentLens));
                            
                            byte[] lensBytes = lensInfo.getBytes();
                            byte lastByte = lensBytes[lensBytes.length - 1];
                            byte secondToLastByte = lensBytes[lensBytes.length - 2];
                            
                            System.out.println("lens string: " + lensInfo + "$$");
                            System.out.println("2nd to last byte: " + secondToLastByte);
                            System.out.println("last byte: " + lastByte);
                            
                            serialPort.writeBytes(stx);														// write to the serial port	
                            serialPort.writeBytes(stripChars(lensInfo).getBytes());
                            
                            if (!(secondToLastByte == etx0[0])) {
                            	System.out.println("adding LF");
                            	serialPort.writeBytes(etx0);
                            }
                            
                            if (!(lastByte == etx1[0])) {
                            	System.out.println("adding CR");
                            	serialPort.writeBytes(etx1);
                            }
                            
//                            serialPort.writeBytes(etx);
                            
                            progressBar.setValue(currentLens);												// update the progress bar
                            currentLens += 1;																// increment the lens counter
                        } else if (currentLens == numLensesToSend) {												// after sending the final lens
                            System.out.println("Done sending lenses. Sending EOT");
                            serialPort.writeBytes(EOT);														// tell the HU3 we're done transmitting
                            lensDone = true;																// set the flag indicating we're done transmitting
                            currentLens = 0;																// reset the lens counter
                            numLensesSent = false;															// reset the flag
                            
                            startTimer("idle", 500, 200);
                        }
                    }
                    else {		// start by sending the HU3 the number of lenses in the list
                        String numLensesHexString = Integer.toHexString(numLensesToSend).toUpperCase();		// convert to Hex
                        System.out.println("Sending number of lenses: " + numLensesHexString + " (" + numLensesToSend + ")");
                        
                        byte[] stx = {0x0E};																// special STX character for lens transmission (Shift Out - for HU3 to shift memory)
                        byte[] etx = {0x0A, 0x0D};															// special ETX character for lens transmission (LF, CR)
                        byte[] numLensesBytes = convertToASCII(numLensesHexString);
                        
                        System.out.println("numLensesBytes: " + Arrays.toString(numLensesBytes));
                        int packageLength = stx.length + etx.length + numLensesBytes.length;
                        
                        byte[] sendNumLenses = new byte[packageLength];
                        
                        System.arraycopy(stx, 0, sendNumLenses, 0, stx.length);
                        System.arraycopy(numLensesBytes, 0, sendNumLenses, stx.length, numLensesBytes.length);
                        System.arraycopy(etx, 0, sendNumLenses, stx.length + numLensesBytes.length, etx.length);
                        
                        System.out.println(Arrays.toString(sendNumLenses));
                        
                        serialPort.writeBytes(sendNumLenses);
                        
                        progressBar.setVisible(true);														// activate the progress bar
			            progressBar.setMaximum(numLensesToSend);											// scale the progress bar
			            progressBar.setValue(currentLens);													// set the current progress value
			            
                        numLensesSent = true;																// set the flag
                    }
                }
                else {		// last lens successfully transmitted
                    System.out.println("HU3 successfully received lenses");
                    lensSendMode = false;																	// reset the flag, which is set by pressing the Export to HU3 button
                    lensDone = false;																		// reset the flag
                    currentLens = 0;																		// reset the lens counter
		            progressBar.setVisible(false);															// hide the progress bar
                }
            }
            else if (text.contains(NAKStr)) {		// HU3 didn't receive the string or it was corrupted
                System.out.println("NAK received from HU3. Re-sending lens " + currentLens);
                serialPort.writeBytes(ACK);																	// acknowledge 
                serialPort.writeBytes(lensArray.get(currentLens).getBytes());								// resend the current lens again
            }
        }
    }
    
 // import the lens file from the text file into an array that can be sent to the HU3
    private void importLensFile(File lensFile) {
        System.out.println("Importing the lens file: " + lensFile.toString());
        String fileType = getExtension(lensFile.getName());
        System.out.println("File extension: " + fileType);
        
        switch (fileType) {
        case "lens":
	        BufferedReader reader = null;
	        lensArray.clear();
	
	        try {
	            FileInputStream lensIn = new FileInputStream(lensFile);
	            reader = new BufferedReader(
	                    new InputStreamReader(lensIn));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (line.length() > 0) {
	                    System.out.println("Reading lens line: " + line);
	                    lensArray.add(stripChars(line));
	                }
	            }
	            
	            if (lensArray.size() > 0) {
	//                lensFileLoaded = true;
	                numLenses = lensArray.size();
	                currentLens = 0;
	                
	                setFileNameLabel(lensFile.getName());
	                addLensesToWorkList();
	            }
	
	            System.out.println("lensArray loaded successfully. NumLenses: " + numLenses);
	
	        } catch (Exception ex) {
	            System.out.println("importLensFile(): " + ex);
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                }   catch (Exception e) {
	                    System.out.println("reader exception: " + e);
	                }
	            }
	        }
	        break;
        case "csv":
        	System.out.println("import CSV file");
        	break;
        }
    }
    
    /** This method saves the lenses that are currently selected in the work list to a text file.
     * The file is chosen by the user in the Save dialog box.
     * @param file
     */
    private void exportLensFile(File file, ArrayList<String> lenses) {
    	System.out.println("Exporting lenses to file: " + file.toString());
    	System.out.println("Number of lenses to export: " + lenses.size());

        try {
            FileOutputStream fos = new FileOutputStream(file);
            for (String lens : lenses) {
                try {
                	String lensWithLFCR = lens + LFStr + CRStr;
                    fos.write(lensWithLFCR.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fos.close();
                currentLens = 0;
                System.out.println("File saved successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
				
	/*
	 * This method adds all the lenses stored in lensArray to the Work List table in the UI. It also calls the method
	 * parseLensLine to create the ArrayList of Lens objects that is the master List to store all changes made to any lens.
	 */
	private static void addLensesToWorkList() {
		for (int i=0; i < lensArray.size(); i++) {
			/* Initialize the Vector that populates the Work List Table row */
			Vector tableRow = new Vector(workListColumnNames.length);
			
			/* Get the data string from the lens array */
			String str = lensArray.get(i);
			
			/* Parse the data string into a Lens object and add it to the list of Lens objects */
			Lens thisLens = parseLensLine(str, i, true);
			lensObjectArray.add(i, thisLens);
			
			/* add the strings from the Lens object to the Object[][] that the table model was declared with */
			tableRow.add(thisLens.manufacturer);
			tableRow.add(thisLens.series);
			tableRow.add(constructFocalLengthString(thisLens.focalLength1, thisLens.focalLength2));
			tableRow.add(thisLens.serial + thisLens.note);
			tableRow.add(thisLens.calibratedF);
			tableRow.add(thisLens.calibratedI);
			tableRow.add(thisLens.calibratedZ);
			tableRow.add(thisLens.myListA);
			tableRow.add(thisLens.myListB);
			tableRow.add(thisLens.myListC);
			tableRow.add(thisLens.isSelected);
			
			/* Add the row to the JTable model which will populate the table */
			workListTableModel.addRow(tableRow);
        }
		
//		workListNumLensesLabel.setText("" + numLenses + " Lenses");
//		System.out.println("lensArray: " + lensArray);
//		System.out.println("lensObjectArray: " + lensObjectArray);
	}
		
		
	/*
	 * BIG method that parses the raw data string from the HU3 or file and returns a shiny new Lens Object 
	 */
	private static Lens parseLensLine(String line, int index, boolean isNewLens) {
//		System.out.println("parse line #" + index + ": " + line);
		
		Lens lensObject = new Lens(index);
		
		byte[] bytes = line.getBytes();
		
		/* Lens status (calibrated, myList, etc) */
        byte[] status1 = Arrays.copyOfRange(bytes, 14, 16);                               			// bytes 15 and 16 (ASCII bytes) are the first (hex) status byte
        HashMap<String, boolean[]> statusMap = convertLensStatus(status1);
        lensObject.setCalibratedF(statusMap.get("calibrated")[0]);
        lensObject.setCalibratedI(statusMap.get("calibrated")[1]);
        lensObject.setCalibratedZ(statusMap.get("calibrated")[2]);
        lensObject.setMyListA(statusMap.get("myList")[0]);
        lensObject.setMyListB(statusMap.get("myList")[1]);
        lensObject.setMyListC(statusMap.get("myList")[2]);
        lensObject.setIsSelected(isNewLens);

        /* Lens Manufacturer and Type */
        byte[] status2 = Arrays.copyOfRange(bytes, 16, 18);                                         // bytes 17 and 18 (ASCII bytes) are the second (hex) status byte
        HashMap<String, Object> nameAndTypeMap = convertManufName(status2);
        lensObject.setManufacturer((String) nameAndTypeMap.get("manufacturer"));
        lensObject.setSeries((String) nameAndTypeMap.get("series"));

        /* Focal length(s) */
        String focal1 = line.substring(18, 22);                                                     // bytes 19-22 (ASCII bytes) are the first (hex) focal length byte
        String focal2 = line.substring(22, 26);                                                     // bytes 23-26 (ASCII bytes) are the second (hex) focal length byte
        
        lensObject.setFocalLength1(convertFocalLength(focal1));
        lensObject.setFocalLength2(convertFocalLength(focal2));

        /* Serial number */
        String serial = line.substring(26, 30);
        String convertedSerial = convertSerial(serial);
        lensObject.setSerial(convertedSerial);

        /* Note */
        String lensName = line.substring(0, 14);                                                    // get the substring that contains the note (& serial & focal lengths)
        int noteBegin;
        String lensNote;
        if (convertedSerial.length() > 0) {                                                         // serial string present, look for it in the lens name
            noteBegin = lensName.indexOf(convertedSerial) + convertedSerial.length();               // set the index to separate the lens serial and note
        }
        else {
            noteBegin = lensName.indexOf("mm") + 2;                                                 // no serial present, so anything after "mm" is considered the note
        }

        lensNote = lensName.substring(noteBegin).trim();                                            // grab the note using the index determined above
        lensObject.setNote(lensNote);                                                               // set the note property of the lens object

        /* Data String (raw String that gets sent to HU3) */
        lensObject.setDataString(stripChars(line));

        return lensObject;
	}
	
	/* This method starts or stops the timers to ping the HU3 over the serial port.
	 * This is how we can tell if the HU3 was disconnected.
	 */
	private static void startTimer(String timerType, int delay, int interval) {
		Timer timer = new Timer();
		TimerTask task = null;
		
		System.out.println("timer type: " + timerType);
		switch (timerType) {
			case "idle":
				if (timerActive) {
					mainTimer.cancel();
				}
				idleTimer = new Timer();
		        idlePing = new TimerTask() {
		        	public void run() {
		        		timerActive = false;
		            	idleTimerActive = true;
		            	
		            	if (pingResponseCounter < 10) {															// less than 10 pings without a response
			        		try {
								serialPort.writeBytes(SYN);														// write the SYN character to the serial port
								pingResponseCounter += 1;														// increment the ping response counter
//								System.out.println("Pinged HU3 " + pingResponseCounter + " times");
							} catch (SerialPortException e) {
								e.printStackTrace();
							}
		            	}
		            	else {																					// 10 pings without a response, so declare the HU3 disconnected
		            		updateConnectionStatus(false);
		            		pingResponseCounter = 0;															// reset the ping counter
		            	}
		        	}
		        };
				timer = idleTimer;				
				task = idlePing;
				
				System.out.println("Starting idle timer");
				break;
			case "main":
				System.out.println("main timer detected");
				if (idleTimerActive) {
					idleTimer.cancel();
				}
				
				mainTimer = new Timer();
				ping = new TimerTask() {
		        	public void run() {
		        		timerActive = true;
		        		idleTimerActive = false;
		        		try {
							serialPort.writeBytes(SYN);														// write the SYN character to the serial port
//							System.out.println("SYN written to serial port");
						} catch (SerialPortException e) {
							e.printStackTrace();
						}
		        	}
		        };
				timer = mainTimer;
				task = ping;
				
				System.out.println("Starting main timer");
				break;
			case "none":
				timer = null;
				if (timerActive) {
					mainTimer.cancel();
					timerActive = false;
				}
				
				if (idleTimerActive) {
					idleTimer.cancel();
					idleTimerActive = false;
				}
				
				System.out.println("Stopping all timers");
				break;
		}
		
		if (timer != null && task != null) {
			timer.scheduleAtFixedRate(task, delay, interval);
		}
		
		else {
			System.out.println("Timer or task is null. Unable to start task.");
		}       
	}
	
	/** This method checks the first, and final two characters in the lens string to see if they're STX, LF, or CR.
	 * If so, it strips those characters and returns a new string with just the lens data.
	 * @param lens the string representing lens data received from HU3/file
	 * @return out (potentially) trimmed string 
	 */
	private static String stripChars(String lens) {
		String out;
		byte[] inBytes = lens.getBytes();
		int begin = 0;
		int end = inBytes.length;
		
		/* Check the first character against STX */
		if (inBytes[0] == STX[0]) {
//			System.out.println("STX found at beginning");
			begin += 1;
		}
		
//		/* Check the last character for either CR or LF */
//		if (inBytes[inBytes.length - 1] == CR[0] || inBytes[inBytes.length - 1] == LF[0]) {
//			System.out.println("CR or LF found at end");
//			end -= 1;
//			
//			/* If CR found, check the next character for LF */
//			if (inBytes[inBytes.length - 2] == LF[0]) {
//				System.out.println("LF found after CR");
//				end -= 1;
//			}
//		}
		
		/* Create a new byte[] with the appropriate indices */
		byte[] outBytes = Arrays.copyOfRange(inBytes, begin, end);
		
		/* Stringify */
		out = new String(outBytes);
		
		return out;
	}
	
	// use the hex characters to parse the lens calibration status and if it's a member of any lists
    // just follow mirko's lens data structure //
    private static HashMap<String, boolean[]> convertLensStatus(byte[] bytes) {
        /* Initialize variables. lensStatusMap is return value, containing a value for keys "calibrated" and "myList" */
        HashMap<String, boolean[]> lensStatusMap = new HashMap<String, boolean[]>();
        boolean FCal = false;
        boolean ICal = false;
        boolean ZCal = false;
        boolean myListA = false;
        boolean myListB = false;
        boolean myListC = false;
        boolean[] calArray = new boolean[3];
        boolean[] listArray = new boolean[3];

        // check the first byte to determine the status
        switch (bytes[0]) {
            case 70:    // F
                FCal = true;
                myListC = true;
                myListB = true;
                break;
            case 69:    // E
                FCal = true;
                myListC = true;
                break;
            case 68:    // D
                FCal = true;
                myListB = true;
                break;
            case 67:    // C
                FCal = true;
                break;
            case 66:    // B
                myListC = true;
                myListB = true;
                break;
            case 65:    // A
                myListC = true;
                break;
            case 57:    // 9
                myListB = true;
                break;
            default:        // 8 => no list, F not calibrated. Default case
                break;
        }

        // check the second byte to determine the status
        switch (bytes[1]) {
            case 70: case 69:  // F & E (since we don't care about the Z bit)
                myListA = true;
                ICal = true;
                ZCal = true;
                break;
            case 68: case 67: // D & C
                myListA = true;
                ICal = true;
                break;
            case 66: case 65:   // B & A
                myListA = true;
                ZCal = true;
                break;
            case 57: case 56:   // 9 & 8
                myListA = true;
                break;
            case 55:case 54:    // 7 & 6
                ICal = true;
                ZCal = true;
                break;
            case 53:case 52:    // 5 & 4
                ICal = true;
                break;
            case 51:case 50:    // 3 & 2
                ZCal = true;
                break;
            default:
                break;
        }

        // build the boolean arrays
        calArray[0] = FCal;
        calArray[1] = ICal;
        calArray[2] = ZCal;

        listArray[0] = myListA;
        listArray[1] = myListB;
        listArray[2] = myListC;

        // add to the HashMap and return
        lensStatusMap.put("calibrated", calArray);
        lensStatusMap.put("myList", listArray);

        return lensStatusMap;
    }
    
    
    /* This method accepts a status byte as input and returns a map of the lens' manufacturer name and series as strings.
    It calls the methods bytesToLensManuf and bytesToLensType to determine each of those values   */
    private static HashMap<String, Object> convertManufName(byte[] status) {
        HashMap<String, Object> lensManufAndTypeMap = new HashMap<>();
        String manufName = (String) bytesToLensManuf(status).get("manufacturer");
        String manufSeries = (String) bytesToLensSeries(status).get("series");
        int manufPos = (int) bytesToLensManuf(status).get("groupPos");
        int seriesPos = (int) bytesToLensSeries(status).get("seriesPos");

        lensManufAndTypeMap.put("manufacturer", manufName);
        lensManufAndTypeMap.put("series", manufSeries);
        lensManufAndTypeMap.put("manufPosition", manufPos);
        lensManufAndTypeMap.put("seriesPosition", seriesPos);

        return lensManufAndTypeMap;
    }
    
    /* This method accepts a status byte as input and returns the lens manufacturer and group position within the ListView according to that status byte */
    private static HashMap<String, Object> bytesToLensManuf(byte[] status) {
        HashMap<String, Object> manufNameAndPosition = new HashMap<>();
        String name;
        int groupPos;
        switch (status[0]) {
            case 48:
                name = "Angenieux";
                groupPos = 0;
                break;
            case 49:
                name = "Canon";
                groupPos = 1;
                break;
            case 50:
                name = "Cooke";
                groupPos = 2;
                break;
            case 51:
                name = "Fujinon";
                groupPos = 3;
                break;
            case 52:
                name = "Leica";
                groupPos = 4;
                break;
            case 53:
                name = "Panavision";
                groupPos = 5;
                break;
            case 54:
                name = "Zeiss";
                groupPos = 6;
                break;
            default:
                name = "Other";
                groupPos = 7;
                break;
        }

        manufNameAndPosition.put("manufacturer", name);
        manufNameAndPosition.put("groupPos", groupPos);
        return manufNameAndPosition;
    }
	    
	    /* This method accepts a status byte as input and returns the lens series according to that status byte
        The type is dependent on the manufacturer name as well which is why there are two switch statements. */
    private static HashMap<String, Object> bytesToLensSeries(byte[] status) {
        HashMap<String, Object> seriesAndPosition = new HashMap<>();
        String manufType;
        int seriesPos;
        switch (status[0]) {
            case 48:
                switch (status[1]) {
                    case 48:
                        manufType = "Optimo";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "Rouge";
                        seriesPos = 1;
                        break;
                    case 50:
                        manufType = "HR";
                        seriesPos = 2;
                        break;
                    case 51:
                        manufType = "Other";
                        seriesPos = 3;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 3;
                        break;
                }
                break;
            case 49:
                switch (status[1]) {
                    case 48:
                        manufType = "Cinema Prime";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "Cinema Zoom";
                        seriesPos = 1;
                        break;
                    case 50:
                        manufType = "Other";
                        seriesPos = 2;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 2;
                        break;
                }
                break;
            case 50:
                switch (status[1]) {
                    case 48:
                        manufType = "S4";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "S5";
                        seriesPos = 1;
                        break;
                    case 50:
                        manufType = "Panchro";
                        seriesPos = 2;
                        break;
                    case 51:
                        manufType = "Zoom";
                        seriesPos = 3;
                        break;
                    case 52:
                        manufType = "Other";
                        seriesPos = 4;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 4;
                        break;
                }
                break;
            case 51:
                switch (status[1]) {
                    case 48:
                        manufType = "Premier Zoom";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "Alura Zoom";
                        seriesPos = 1;
                        break;
                    case 50:
                        manufType = "Prime";
                        seriesPos = 2;
                        break;
                    case 51:
                        manufType = "Other";
                        seriesPos = 3;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 3;
                        break;
                }
                break;
            case 52:
                switch (status[1]) {
                    case 48:
                        manufType = "Summilux Prime";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "Other";
                        seriesPos = 1;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 1;
                        break;
                }
                break;
            case 53:
                switch (status[1]) {
                    case 48:
                        manufType = "Primo Prime";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "Primo Zoom";
                        seriesPos = 1;
                        break;
                    case 50:
                        manufType = "Anam. Prime";
                        seriesPos = 2;
                        break;
                    case 51:
                        manufType = "Anam. Zoom";
                        seriesPos = 3;
                        break;
                    case 52:
                        manufType = "P70 Prime";
                        seriesPos = 4;
                        break;
                    case 53:
                        manufType = "Other";
                        seriesPos = 5;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 5;
                        break;
                }
                break;
            case 54:
                switch (status[1]) {
                    case 48:
                        manufType = "Master Prime";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "Ultra Prime";
                        seriesPos = 1;
                        break;
                    case 50:
                        manufType = "Compact Prime";
                        seriesPos = 2;
                        break;
                    case 51:
                        manufType = "Zoom";
                        seriesPos = 3;
                        break;
                    case 52:
                        manufType = "Other";
                        seriesPos = 4;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 4;
                        break;
                }
                break;
            default:
                switch (status[1]) {
                    case 48:
                        manufType = "Prime";
                        seriesPos = 0;
                        break;
                    case 49:
                        manufType = "Zoom";
                        seriesPos = 1;
                        break;
                    default:
                        manufType = "";
                        seriesPos = 0;
                        break;
                }
                break;
        }

        seriesAndPosition.put("series", manufType);
        seriesAndPosition.put("seriesPos", seriesPos);
        return seriesAndPosition;
    }
    
    /* Method that accepts String of lens focal length (in hex representation, 4 characters) and returns that value as a (decimal) integer */
    private static int convertFocalLength(String focal) {
        int focalHex = 0x0;
        focalHex = Integer.parseInt(focal, 16);        
    	return focalHex;
    }

    /* Method to build the correctly formatted focal length(s) String depending on if the lens is a zoom or prime (focalLength2 == 0) */
    public static String constructFocalLengthString(int fL1, int fL2) {
        if (fL2 > 0) {                                                                     // fL2 > 0 implies zoom lens
            return String.valueOf(fL1) + "-" + String.valueOf(fL2) + "mm";
        }
        return String.valueOf(fL1) + "mm";                                                                          // prime lens, so just return the first FL
    }
    
    /* Method that accepts a String of the lens serial number (in hex representation, 4 characters) and returns that value as a (decimal) integer */
    private static String convertSerial(String serial) {
        int serialInDecimal = Integer.parseInt(serial, 16);                                         // convert from hex to decimal
        if (serialInDecimal > 0) {                                                                  // if serial > 0, user entered a serial for this lens
            return Integer.toString(serialInDecimal);
        }
        return "";                                                                                  // no serial entered, return empty string
    }
    
    /* Method to update the UI when the connection to the HU3 is either established or lost */
    private static void updateConnectionStatus(boolean connected) {
    	String labelText;
    	
    	isConnected = connected;											// update the global flag indicating whether the HU3 is present
    	
    	if (connected) {		// set the connected string label
    		labelText = connectedString;
    	}
    	else {					// set the disconnected string label
    		labelText = disconnectedString;
    	}
    	
    	/* Update the UI */
    	connectedLabel.setText(labelText);
        btnImportFromHu.setEnabled(connected);
        btnExportToHu.setEnabled(connected);
    }
    
    /* Method to update the lens file name label with the currently loaded file */
    private static void setFileNameLabel(String fileName) {
    	String trimmedName = fileName.split(".lens")[0];
    	fileNameLabel.setText("File: " + trimmedName + " (" + numLenses + ")");
    }
    
    /* Method that converts a Hex string to ASCII */
    private static byte[] convertToASCII(String str) {
    	byte[] out = new byte[2];
    	char[] charArray = str.toCharArray();
    	
    	if (charArray.length == 1) {
    		out[0] = 0x30;
    		out[1] = getASCIIByte(charArray[0]);
    	}
    	else {
    		out[0] = getASCIIByte(charArray[0]);
    		out[1] = getASCIIByte(charArray[0]);
    	}
    	
    	return out;
    }		

    /* Method to convert a single hex character to its ASCII number */
	private static byte getASCIIByte(char c) {
		byte out = 0x30;
		
		switch(c) {
			case '0':
				out = 0x30;
				break;
			case '1':
				out = 0x31;
				break;
			case '2':
				out = 0x32;
				break;
			case '3':
				out = 0x33;
				break;
			case '4':
				out = 0x34;
				break;
			case '5':
				out = 0x35;
				break;
			case '6':
				out = 0x36;
				break;
			case '7':
				out = 0x37;
				break;
			case '8':
				out = 0x38;
				break;
			case '9':
				out = 0x39;
				break;
			case 'A':
				out = 0x41;
				break;
			case 'B':
				out = 0x42;
				break;
			case 'C':
				out = 0x43;
				break;
			case 'D':
				out = 0x44;
				break;
			case 'E':
				out = 0x45;
				break;
			case 'F':
				out = 0x46;
				break;
		}
		
		return out;
	}
	
	public static String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        
        int index = fileName.lastIndexOf(".");
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (index == -1) {
            return "";
        } 
        else if (index > p) {
            return fileName.substring(index + 1);
        }
        else {
        	return "";
        }
    }
		
	/*
	 * In this class must implement the method serialEvent, through it we learn about 
	 * events that happened to our port. But we will not report on all events but only 
	 * those that we put in the mask. In this case the arrival of the data and change the 
	 * status lines CTS and DSR
	 */
	
	static class SerialPortReader implements SerialPortEventListener {
	
	    public void serialEvent(SerialPortEvent event) {
	        if(event.isRXCHAR()){																//If data is available
	        	try {
					RxBuffer = serialPort.readBytes();
					String lensString = buildLensPacket(RxBuffer);
//					System.out.println("data received: " + lensString + "$$");
					
//					if (lensReceiveMode) {
						if (lensString.length() > 0) {
							receiveLensData(lensString);
							System.out.println("lensString: " + lensString.trim());
						}
//					}
					
					// boolean toggled true when you click "Export lenses to HU3"
	                if (lensSendMode) {
	                	String text = new String(RxBuffer, Charset.forName("UTF-8"));
	                    transmitLensData(text);
	                }
	                
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	}
}
