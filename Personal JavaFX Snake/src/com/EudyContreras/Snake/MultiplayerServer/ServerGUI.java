package com.EudyContreras.Snake.MultiplayerServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

/**
 * This class is the server graphical user interface
 * @author Nicolai Jaynes, Eudy Contreras, Danyal Mahmood, Marcus Vazdekis
 */

public class ServerGUI {

	private JPanel mainPanel = new JPanel(new BorderLayout());
	private JPanel centerPanel = new JPanel(new BorderLayout());
	private JPanel centerNorth = new JPanel(new GridLayout(2,2));
	private JPanel northEast = new JPanel(new BorderLayout());
	private JPanel lowerNorth = new JPanel(new BorderLayout());
	private JPanel northPanel = new JPanel(new BorderLayout());
	private JPanel southPanel = new JPanel(new BorderLayout());
	private JPanel southEast = new JPanel(new GridLayout());
	private JFrame window = new JFrame("Server interface");
	private JLabel label = new JLabel("Server console", JLabel.CENTER);
	private JLabel portLabel = new JLabel(" Port: ", JLabel.CENTER);
	private JButton clear = new JButton("Clear log");
	private JButton viewOnline = new JButton("View online users");
	private JButton viewOffline = new JButton("View offline users");
	private JButton viewAll = new JButton("View all users");
	private JButton exit = new JButton("Exit");
	private JButton send = new JButton("Send Command");
	private JButton start = new JButton("Start server");
	private JButton choosePath = new JButton("Choose log path");
	private JButton userRecords = new JButton("Information");
	private JTextField portField = new JTextField("6666", JTextField.CENTER);
	private JToggleButton chatLog = new JToggleButton("Chatlog: off");
	private JScrollPane scroll = new JScrollPane();
	private JTextField command = new JTextField();
	private JFileChooser pathChooser = new JFileChooser();
	private JTextArea log = new JTextArea();
	private SimpleDateFormat timestamp;
	private Boolean showLog = false;
	private ClientManager manager;
	private InfoHandler infoHandler;
	private MultiplayerServer server;
	private String serverLogPath = "C:\\";
	private String chatLogPath = "C:\\";
	private int port;

	/**
	 * Constructor which initializes the interface of the GUI. It also
	 * creates a new client manager and a new chatserver object
	 */
	public ServerGUI() {
		this.InitializeInterface();
		this.manager = new ClientManager(this);
		this.server = new MultiplayerServer(this,manager);
		this.infoHandler = new InfoHandler(this,manager,server);
	}

	/**
	 * This method initializes the interface by populating the panels
	 * and adding listeners to buttons
	 */
	public void InitializeInterface(){
		this.portLabel.setPreferredSize(new Dimension(50,0));
		this.northEast.add(portLabel, BorderLayout.WEST);
		this.northEast.add(getPortField(), BorderLayout.CENTER);
		this.centerNorth.add(userRecords);
		this.centerNorth.add(viewAll);
		this.centerNorth.add(viewOffline);
		this.centerNorth.add(viewOnline);
		this.centerNorth.add(northEast);
		this.centerNorth.add(chatLog);
		this.lowerNorth.add(start, BorderLayout.CENTER);
		this.scroll = new JScrollPane(log);
		this.timestamp = new SimpleDateFormat("HH:mm:ss");
		this.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.label.setPreferredSize(new Dimension(50,30));
		this.log.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.log.setEditable(false);
		this.centerPanel.add(scroll);
		this.southEast.add(clear);
		this.southEast.add(choosePath);
		this.southPanel.add(send, BorderLayout.WEST);
		this.southPanel.add(command, BorderLayout.CENTER);
		this.southPanel.add(southEast, BorderLayout.EAST);
		this.southPanel.add(exit, BorderLayout.SOUTH);
		this.northPanel.add(label, BorderLayout.NORTH);
		this.northPanel.add(centerNorth, BorderLayout.CENTER);
		this.northPanel.add(lowerNorth, BorderLayout.SOUTH);
		this.mainPanel.add(northPanel, BorderLayout.NORTH);
		this.mainPanel.add(centerPanel, BorderLayout.CENTER);
		this.mainPanel.add(southPanel, BorderLayout.SOUTH);
		this.getWindow().add(mainPanel);
		this.getWindow().setSize(800, 600);
		this.getWindow().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getWindow().setLocationRelativeTo(null);
		this.getWindow().setVisible(true);
		this.pathChooser();
		this.addListeners();
	}
	public void addListeners(){
		Listener listener = new Listener();
		this.clear.addActionListener(listener);
		this.viewAll.addActionListener(listener);
		this.viewOnline.addActionListener(listener);
		this.viewOffline.addActionListener(listener);
		this.send.addActionListener(listener);
		this.exit.addActionListener(listener);
		this.userRecords.addActionListener(listener);
		this.chatLog.addActionListener(listener);
		this.start.addActionListener(listener);
		this.choosePath.addActionListener(listener);
	}
	public void pathChooser() {
		pathChooser.setCurrentDirectory(null);
		pathChooser.setDialogTitle("Choose log file destination");
		pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		pathChooser.setAcceptAllFileFilterUsed(false);
		setChatLogPath(""+pathChooser.getCurrentDirectory());
		setServerLogPath(""+pathChooser.getCurrentDirectory());
	}
	public void openPathChooser(){
		if (pathChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
			setChatLogPath(""+pathChooser.getSelectedFile());
			setServerLogPath(""+pathChooser.getSelectedFile());
			server.logToConsole("Current directory: " + pathChooser.getCurrentDirectory());
			server.logToConsole("Selected directory: " + pathChooser.getSelectedFile());
		} else {
			server.logToConsole("No path selected");
		}
	}
	/**
	 * This method starts the server.
	 */

	public String getTime(){
		return timestamp.format(new Date());
	}

	/**
	 * Shows message in the log
	 * @param log The message to be shown
	 */
	public void showOnlog(String log){
		String time = timestamp.format(new Date()) + ":  " + log;
		logEvent(time + "\n");
	}
	public void addSpace(){
		logEvent("\n");
	}
	/**
	 * Shows server event and appends it to the log
	 * @param log The server event in string form
	 */
	protected String showServerEvent(String log){
		try{
			String time = getTime() + ":  " + log;
			logEvent(time + "\n");
			return time;
		}catch(NullPointerException e){
			e.printStackTrace();
			return "ERROR: Server tried to show a null event.";
		}
	}
	/**
	 * Message shown in server log
	 * @param logEvent
	 */
	public void logEvent(String logEvent){
		log.append(logEvent);
		log.setCaretPosition(log.getText().length() - 1);
	}

	public void clearLog(String status, String user){
		log.setText("");
	}

	/*
	 * Sends message to every user on server
	 */
	public void send(String message) {
		if (!message.isEmpty()) {
			try {
				showOnlog("Server: " + message);
				manager.sendPackage("Server: " + message);
				command.setText("");
			} catch (ClassNotFoundException | IOException e) {
				server.logToConsole("failed to send message to clients");
				e.printStackTrace();
			}
		}
	}
	public String getServerLogPath() {
		return serverLogPath;
	}
	public void setServerLogPath(String serverLogPath) {
		this.serverLogPath = serverLogPath;
	}
	public String getChatLogPath() {
		return chatLogPath;
	}
	public void setChatLogPath(String chatLogPath) {
		this.chatLogPath = chatLogPath;
	}


	public void showNotification(String text) {
		DialogUtility.showMessageDialog(getWindow(),"Notification", text, "OK");
	}


	public void setShowClientLog(Boolean state){
		this.showLog = state;
	}
	public Boolean getShowClientLog() {
		return showLog;
	}
	private class Listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == clear){
				clearLog("online",command.getText());
			}
			else if(e.getSource() == viewAll){
				infoHandler.showAllUsers();
			}
			else if(e.getSource() == viewOnline){
				infoHandler.showOnlineUsers();
			}
			else if(e.getSource() == viewOffline){
				infoHandler.showOfflineUsers();
			}
			else if(e.getSource() == choosePath){
				openPathChooser();
			}
			else if(e.getSource() == chatLog){
				if(chatLog.isSelected()){
					setShowClientLog(true);
					chatLog.setText("Chatlog: on");
				}
				else{
					setShowClientLog(false);
					chatLog.setText("Chatlog: off");
				}
			}
			else if(e.getSource() == send){
				send(command.getText());
			}
			else if(e.getSource() == userRecords){
				infoHandler.authentificationList();
			}
			else if(e.getSource() == start){
				infoHandler.startUp();
			}
			else if(e.getSource() == exit){
				infoHandler.exit();
			}
		}
	}
	public void setPort(int port){
		this.port = port;
	}
	public int getPort(){
		return port;
	}
	public static void main(String[] args) {
		new ServerGUI();
	}

	public JTextField getPortField() {
		return portField;
	}

	public void setPortField(JTextField portField) {
		this.portField = portField;
	}

	public JFrame getWindow() {
		return window;
	}

	public void setWindow(JFrame window) {
		this.window = window;
	}

}
