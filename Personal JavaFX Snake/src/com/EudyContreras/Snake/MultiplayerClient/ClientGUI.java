package com.EudyContreras.Snake.MultiplayerClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import com.EudyContreras.Snake.DataPackage.InfoPack;

/**
 * This class is the client graphical user interface
 * @author Nicolai Jaynes, Danyal Mahmood, Marcus Vazdekis, Eudy Contreras
 *
 */

public class ClientGUI extends JPanel{


	private static final long serialVersionUID = 1L;
	private static JFrame window = new JFrame("Chat Client");
	private JPanel panelLog = new JPanel(new BorderLayout(10,10));
	private JPanel northLog = new JPanel(new GridLayout(2,1));
	private JPanel centerLog = new JPanel(new GridLayout(2,1,5,5));
	private JPanel bottomLog = new JPanel(new BorderLayout());
	private JPanel upperCenterLog = new JPanel(new BorderLayout());
	private JPanel lowerCenterLog = new JPanel(new BorderLayout());
	private JPanel centerPanel = new JPanel(new BorderLayout());
	private JPanel northPanel = new JPanel(new BorderLayout());
	private JPanel southPanel = new JPanel(new BorderLayout());
	private JPanel northWest = new JPanel(new BorderLayout());
	private JPanel eastPanel = new JPanel(new BorderLayout());
	private JPanel westPanel = new JPanel(new BorderLayout());
	private JPanel upperNorth = new JPanel(new GridLayout(1,3));
	private JPanel lowerNorth = new JPanel(new BorderLayout(10,10));
	private JPanel middleNorth = new JPanel(new GridLayout(1,3));
	private JPanel optionsPanel = new JPanel(new BorderLayout());
	private JPanel upperSouth = new JPanel(new BorderLayout());
	private JDialog confirmImageFrame = new JDialog(new JFrame(),"Confirm window", ModalityType.APPLICATION_MODAL);
	private JButton showImage = new JButton("Show image");
	private JButton declineButton = new JButton("Decline image");
	private JButton saveImage = new JButton("Save image");
	private JButton clear = new JButton("Clear History");
	private JButton logIn = new JButton("Log in");
	private JButton sendMessage = new JButton("Send");
	private JButton showUsers = new JButton("Show private chat");
	private JButton button = new JButton("Log in");
	private JButton fileExplorer = new JButton("Send Image");
	private JButton exitButton = new JButton("Exit");
	private JButton cancel = new JButton("Cancel");
	private JToggleButton notifications = new JToggleButton("Notifications: off");
	private JLabel lblIP = new JLabel(" IP :  ");
	private JLabel port = new JLabel(" Port : ");
	private JLabel status = new JLabel(" Connection status: Offline");
	private JLabel userInfo = new JLabel("Logged in as: ", JLabel.CENTER);
	private JLabel label = new JLabel("Please enter your details", JLabel.CENTER);
	private JLabel user = new JLabel("  Username: ");
	private JLabel serverMessage = new JLabel("", JLabel.CENTER);
	private JLabel password = new JLabel("  Password: ");
	private JPasswordField userPasswordField = new JPasswordField();
	private JTextField messageField = new JTextField("");
	private JTextField userNameField = new JTextField();
	private JTextField portField = new JTextField("6666");
	private JTextField ipField = new JTextField("localhost");
	private JTextArea textArea = new JTextArea();
	private JScrollPane scroll = new JScrollPane();
	private JFileChooser chooseFile = new JFileChooser();
	private ClientGUIHandler handler;
	private String IP_ADDRESS;
	private JDialog frame;
	private ChatClient client;
	private PrivateChatGUI onlineUsers;
	private SimpleDateFormat timeStamp;
	private BufferedImage image;
	private Boolean showNotifications = false;
	private Boolean connected = false;
	private String userName;
	private String passWord;
	private String path;
	private int amountOfQueuedMessages = 0;
	public String userInformation;

	/**
	 * Constructor which populates the panels in this GUI, creates a dateformat object,
	 * adds listeners, sets the layout, modifies all buttons to false (meaning they can't be
	 * pressed initially) and creates a ChatClient object
	 */
	public ClientGUI(){
		this.populatePanels();
		this.timeStamp = new SimpleDateFormat("HH:mm:ss");
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(450,600));
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
		this.addListeners();
		this.modifyButtons(false);
		this.client = new ChatClient(IP_ADDRESS,Integer.valueOf(getPortField().getText()), getUserNameField().getText(), this);
		this.handler = new ClientGUIHandler(this,client);
	}

	/**
	 * This method populates the panels in the GUI
	 */
	public void populatePanels(){
		this.status.setPreferredSize(new Dimension(200,50));
		this.textArea.setEditable(false);
		this.lowerNorth.setPreferredSize(new Dimension(50,30));
		this.port.setPreferredSize(new Dimension(50,20));
		this.optionsPanel.add(port, BorderLayout.WEST);
		this.optionsPanel.add(getPortField(), BorderLayout.CENTER);
		this.upperNorth.add(optionsPanel, BorderLayout.CENTER);
		this.upperNorth.add(getShowUsers(), BorderLayout.EAST);
		this.upperNorth.add(getLogIn(), BorderLayout.WEST);
		this.lowerNorth.add(status, BorderLayout.WEST);
		this.lowerNorth.add(userInfo, BorderLayout.EAST);
		this.northWest.add(lblIP, BorderLayout.WEST);
		this.northWest.add(ipField, BorderLayout.CENTER);
		this.middleNorth.add(northWest);
		this.middleNorth.add(notifications);
		this.middleNorth.add(clear);
		this.northPanel.add(upperNorth, BorderLayout.NORTH);
		this.northPanel.add(middleNorth, BorderLayout.CENTER);
		this.northPanel.add(lowerNorth, BorderLayout.SOUTH);
		this.textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.scroll = new JScrollPane(textArea);
		this.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.centerPanel.add(scroll);
		this.upperSouth.add(sendMessage, BorderLayout.WEST);
		this.upperSouth.add(messageField, BorderLayout.CENTER);
		this.upperSouth.add(fileExplorer, BorderLayout.EAST);
		this.southPanel.add(upperSouth, BorderLayout.NORTH);
		this.southPanel.add(exitButton, BorderLayout.SOUTH);
	}

	/**
	 * This method opens the login window in which you enter your username and password
	 */
	public void openLogInWindow(){
		if(!connected){

		getServerMessage().setForeground(Color.RED);
		northLog.add(label);
		northLog.add(getServerMessage());
		getServerMessage().setText("");
		northLog.setPreferredSize(new Dimension(300,60));

		panelLog.setPreferredSize(new Dimension(300,190));
		panelLog.add(northLog, BorderLayout.NORTH);
		panelLog.add(centerLog, BorderLayout.CENTER);
		panelLog.add(bottomLog, BorderLayout.SOUTH);

		user.setPreferredSize(new Dimension(100,50));
		password.setPreferredSize(new Dimension(100,50));
		getUserNameField().setPreferredSize(new Dimension(200,50));
		getUserPasswordField().setPreferredSize(new Dimension(200,50));

		upperCenterLog.add(user,BorderLayout.WEST);
		upperCenterLog.add(getUserNameField(),BorderLayout.CENTER);
		lowerCenterLog.add(password,BorderLayout.WEST);
		lowerCenterLog.add(getUserPasswordField(),BorderLayout.CENTER);

		centerLog.add(upperCenterLog, BorderLayout.NORTH);
		centerLog.add(lowerCenterLog, BorderLayout.CENTER);
		bottomLog.add(button, BorderLayout.CENTER);
		bottomLog.add(cancel, BorderLayout.EAST);
		frame = new JDialog(new JFrame(), "Log in window", ModalityType.APPLICATION_MODAL);
		frame.add(panelLog);
		frame.pack();
		frame.setLocation(getWindow().getX()+getWindow().getWidth()/2-frame.getWidth()/2, getWindow().getY()+getWindow().getHeight()/2 - frame.getHeight()/2);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		panelLog.repaint();
		frame.repaint();
		frame.revalidate();
		getUserNameField().setFocusable(true);
		}
		if(connected){
			Double command = 1.0;
			client.sendPackage(command);
		}
	}

	/**
	 * This method modifies the buttons to enabled or not enabled according to what
	 * boolean value you pass as a parameter
	 * @param state
	 */
	public void modifyButtons(boolean state){
		sendMessage.setEnabled(state);
		fileExplorer.setEnabled(state);
		getShowUsers().setEnabled(state);
		messageField.setEditable(state);
	}

	/**
	 * This method sends a message in the form of a string
	 * @param txt
	 */
	public void sendMessage(String txt){
		String message = getUserName() + ": "+ txt;
		client.sendPackage(message);
		client.setSendMessage(txt);
	}

	/**
	 * This method opens a window where you confirm that you have recieved an image
	 * @param image
	 */
	public void confirmImage(BufferedImage image){
		JPanel panel = new JPanel(new GridLayout(2,1));
		JLabel label = new JLabel("new Image!", JLabel.CENTER);
		JPanel south = new JPanel(new GridLayout(2,1));
		JPanel upperSouth = new JPanel(new GridLayout(1,2));
		panel.add(label, BorderLayout.NORTH);
		upperSouth.add(showImage, BorderLayout.WEST);
		upperSouth.add(saveImage, BorderLayout.EAST);
		south.add(upperSouth);
		south.add(declineButton);
		panel.add(south, BorderLayout.SOUTH);
		confirmImageFrame.add(panel);
		confirmImageFrame.pack();
		confirmImageFrame.setLocationRelativeTo(window);
		confirmImageFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		confirmImageFrame.setVisible(true);
		confirmImageFrame.setResizable(false);
		confirmImageFrame.revalidate();
		confirmImageFrame.repaint();
		confirmImageFrame.setAlwaysOnTop(true);
		confirmImageFrame.setModal(true);
	}

	/**
	 * This method tests if user is online or not.
	 * If so the user will be welcomed to the chatroom
	 * with a greeting message. Otherwise the connection
	 * status text will show that user is offline
	 */
	public void runStatus(){
		if(client.isConnected()){
			status.setText(" Connection status: Online");
			setTimeStamp("Welcome to the chatroom!");
			modifyButtons(true);
		}
		else if(!client.isConnected()){
			status.setText(" Connection status: Offline");
			userInfo.setText("Logged in as: ");
		}
	}

	public void DialogBox(String text){
		JOptionPane pane = new JOptionPane("Hello");
	    JDialog d = pane.createDialog((JFrame)null, "Title");
	    d.setModal(true);
	    d.setModalityType(ModalityType.APPLICATION_MODAL);
	    d.setLocation(10,10);
	    d.setVisible(true);

	}
	public PrivateChatGUI getOnlineUsers(){
		return onlineUsers;
	}

	/**
	 * This method shows the time and date plus a string message
	 * @param txt
	 */
	public void showOnChat(String txt){
		String time = timeStamp.format(new Date()) + ":  " + txt;
		logEvent(time + "\n");
	}

	public void setTimeStamp(String log){
		String time = timeStamp.format(new Date()) + ":  " + log;
		logEvent(time + "\n");
	}

	/**
	 * This method shows a logevent in the text area
	 * @param log
	 */
	public void logEvent(String log){
		textArea.append(log);
		textArea.setCaretPosition(textArea.getText().length() - 1);
	}
	public String getUserName(){
		return userName;
	}
	public JFrame getWindow(){
		return window;
	}
	public ChatClient getClient(){
		return client;
	}
	public void setConnection(boolean state){
		connected = state;
	}

	public void addListeners(){
		Listener listener = new Listener();
		button.addActionListener(listener );
		getLogIn().addActionListener(listener);
		sendMessage.addActionListener(listener);
		messageField.addActionListener(listener);
		getShowUsers().addActionListener(listener);
		getPortField().addActionListener(listener);
		fileExplorer.addActionListener(listener);
		getUserNameField().addActionListener(listener);
		getUserPasswordField().addActionListener(listener);
		exitButton.addActionListener(listener);
		ipField.addActionListener(listener);
		showImage.addActionListener(listener);
		declineButton.addActionListener(listener);
		cancel.addActionListener(listener);
		clear.addActionListener(listener);
		notifications.addActionListener(listener);
		saveImage.addActionListener(listener);
	}

	/**
	 * This method turns an array of characters to a string
	 * @param characters
	 * @return
	 */
	public String charsToString(char[] characters){
		String newString = "";
		for(int i = 0; i<characters.length; i++){
			newString += characters[i];
		}
		return newString;
	}
	/**
	 * This method gives the user the option to save an
	 * image that he or she has recieved
	 */
	public void saveReceivedImage(){
		File newImage = new File("Received Image."+"png");
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(newImage);
        int rval = chooser.showSaveDialog(ClientGUI.this);
        if (rval == JFileChooser.APPROVE_OPTION) {
        	newImage = chooser.getSelectedFile();
            try {
                ImageIO.write(client.getReceivedPicture(),"png", newImage);
            } catch (IOException e) {
            	showOnChat("Unable to save the image!");
            }
        }
    }

	/**
	 * This method shows an image that has been sent to the user
	 * @param img
	 */
	public void showImage(BufferedImage img){
		JDialog frame = new JDialog(new JFrame(), ModalityType.APPLICATION_MODAL);
		try{
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(img)));
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setAlwaysOnTop(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}catch(NullPointerException e){
			showOnChat("Failed to display the received image!");
		}
	}

	/**
	 * This method opens the file chooser
	 */
    public void openFileChooser(){
        int returnVal = chooseFile.showOpenDialog(ClientGUI.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooseFile.getSelectedFile();
            path = file.getAbsolutePath();
            client.sendPictureMessage(client.loadImage(ClientGUIHandler.pathFormatter(path)), handler.setImageName(path));
        }
    }

    /**
     * This method shows tells the user that another user
     * has started a private chat with him or her. Then gives
     * the user the option to respond
     * @param pack
     */
	public void privateChatNotification(InfoPack pack){
		if(showNotifications){
			if(!onlineUsers.getFrame().isVisible()){
			String sender = pack.getID();
			String[] options = { "Yes, please", "No, thanks" };
			int answer = DialogUtility.showConfirmationDialog(window,
					sender + ": has started a private chat with you! Would you like to respond? ", "Notification", options);
			if (answer == DialogUtility.YES_OPTION) {
				showPrivateChat();
			}
		}
		}
	}

	/**
	 * This method notifies the user that another user has sent
	 * a private message to him or her. It the gives the user the
	 * option to view it. It could either be one message or more
	 * messages that are queued.
	 * @param pack
	 */
	public void showNotificationMessage(InfoPack pack) {
		if (amountOfQueuedMessages <= 1) {
			if (showNotifications) {
				if (!onlineUsers.getFrame().isVisible()) {
					String sender = pack.getID();
					String[] options = { "Yes, please", "No, thanks" };
					int answer = DialogUtility.showConfirmationDialog(window,
							"New private message from: " + sender + "! Would you like to view it?", "Notification",
							options);
					if (answer == DialogUtility.YES_OPTION) {

						showPrivateChat();
					}
				}
			}
		} else {
			if (showNotifications) {
				if (!onlineUsers.getFrame().isVisible()) {
					String[] options = { "Yes, please", "No, thanks" };
					int answer = DialogUtility.showConfirmationDialog(window,
							"You have: " + amountOfQueuedMessages
									+ " queued messages! Would you like to open the private chat?",
							"Notification", options);
					amountOfQueuedMessages = 0;
					if (answer == DialogUtility.YES_OPTION) {

						showPrivateChat();
					}
				}
			}
		}
	}
	public void setInboxSize(int amount) {
		this.amountOfQueuedMessages  = amount;

	}
	public void setShowNotifications(Boolean showNotifications){
		this.showNotifications = showNotifications;
	}
	public void showPrivateChat() {
		onlineUsers.getFrame().setLocation(getWindow().getX()-getWidth(), getWindow().getY());
		onlineUsers.getFrame().setVisible(true);
		getShowUsers().setText("Hide private chat");
	}
	public void hidePrivateChat() {
		onlineUsers.getFrame().setVisible(false);
		getShowUsers().setText("Show private chat");
	}
    public void initializePrivateChat(){
    	onlineUsers = new PrivateChatGUI(this, "Online Users");
    	onlineUsers.Start();
		Double command2 = 3.0;
		client.sendPackage(command2);
    }
    public void clearHistory(){
    	textArea.setText("");
    	showOnChat("Your chat history has been cleared");
    }
	public void setImage(BufferedImage img){
		this.image = img;
	}
	public BufferedImage getImage(){
		return image;
	}
	public class Listener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == getLogIn()){
				openLogInWindow();
			}
			else if(e.getSource() == button){
				if(getUserNameField().getText().length() != 0 && getUserPasswordField().getPassword().length!=0){
					client.setIP_Address(ipField.getText());
					handler.tryConnection();
				}
				else if(getUserNameField().getText().length() == 0 && getUserPasswordField().getPassword().length!=0){
					getServerMessage().setText("please enter a username");
				}
				else if(getUserNameField().getText().length()!=0 && getUserPasswordField().getPassword().length == 0){
					getServerMessage().setText("please enter a password");
				}
				else if(getUserNameField().getText().length() == 0 && getUserPasswordField().getPassword().length == 0){
					getServerMessage().setText("please enter a username and password");
				}
			}
			else if(e.getSource() == notifications){
				if(notifications.isSelected()){
					setShowNotifications(true);
					notifications.setText("Notifications: on");
				}
				else{
					setShowNotifications(false);
					notifications.setText("Notifications: off");
				}
			}
			else if(e.getSource() == ipField){

			}
			else if(e.getSource() == saveImage){
				if(client.getReceivedPicture()!=null)
				saveReceivedImage();
				confirmImageFrame.dispose();
			}
			else if(e.getSource() == clear){
				clearHistory();
			}
			else if(e.getSource() == cancel){
				getLogIn().setText("Log in");
				frame.dispose();
				modifyButtons(false);
			}
			else if(e.getSource() == showImage){
				showImage(client.getReceivedPicture());
			}
			else if(e.getSource() == declineButton){
				confirmImageFrame.dispose();
			}
			else if(e.getSource() == messageField){
				if(!messageField.getText().isEmpty()){
					sendMessage(messageField.getText());
					messageField.setText("");
				}
			}
			else if(e.getSource() == sendMessage){
				sendMessage(messageField.getText());
				messageField.setText("");
			}
			else if(e.getSource() == fileExplorer){
				openFileChooser();
			}
			else if(e.getSource() == getShowUsers()){
				if(!onlineUsers.getFrame().isVisible()){
					showPrivateChat();
				}
				else{
					hidePrivateChat();
				}
			}
			else if(e.getSource() == exitButton){
				Double command = 1.0;
				client.sendPackage(command);
				System.exit(0);
			}
		}
	}
	public JLabel getServerMessageLabel() {
		return getServerMessage();
	}
	/**
	 *
	 * @param status
	 * @param scenario
	 */
	public void showServerMessage(String status, String scenario){
		if(status.equals("new")){
			DialogUtility.showMessageDialog(window, "Welcome!", scenario+ "\n" + "Username: "+getUserName()+"\nPassword: "+getPassWord(), "Ok thanks!");
		}
		else if(status.equals("old")){
			DialogUtility.showMessageDialog(window, "Welcome!", scenario, "Ok thanks!");
		}
		Double command = 2.0;
		client.sendPackage(command);
	}

	/**
	 * This method closes and disposes the log in window
	 */
	public void closeLogInWindow(String status, String scenario) {
		userInfo.setText("Logged in as: " + getUserNameField().getText()+ " ");
		frame.dispose();
		runStatus();
		getLogIn().setEnabled(true);
		getLogIn().setText("Log out");
		connected = true;
		showServerMessage(status,scenario);
	}
	public static void main(String[]args){
		ClientGUI ui = new ClientGUI();
		window.add(ui);
		window.pack();
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	public JButton getLogIn() {
		return logIn;
	}

	public void setLogIn(JButton logIn) {
		this.logIn = logIn;
	}

	public JButton getShowUsers() {
		return showUsers;
	}

	public void setShowUsers(JButton showUsers) {
		this.showUsers = showUsers;
	}

	public JTextField getPortField() {
		return portField;
	}

	public void setPortField(JTextField portField) {
		this.portField = portField;
	}

	public JTextField getUserNameField() {
		return userNameField;
	}

	public void setUserNameField(JTextField userNameField) {
		this.userNameField = userNameField;
	}

	public JLabel getServerMessage() {
		return serverMessage;
	}

	public void setServerMessage(JLabel serverMessage) {
		this.serverMessage = serverMessage;
	}

	public JPasswordField getUserPasswordField() {
		return userPasswordField;
	}

	public void setUserPasswordField(JPasswordField userPasswordField) {
		this.userPasswordField = userPasswordField;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

}
