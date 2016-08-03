package com.EudyContreras.Snake.MultiplayerClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
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
 * This class is the graphical user interface for the protected chat
 * which can be opened within the client UI.
 * @author Nicolai Jaynes, Danyal Mahmood, Marcus Vazdekis, Eudy Contreras
 */

public class PrivateChatGUI extends JPanel implements Observer{

	private static final long serialVersionUID = 1L;
	private LinkedList<JCheckBox> onlineUsers;
	private JFrame window = new JFrame("Online Users");
	private JPanel chatPanel = new JPanel(new BorderLayout());
	private JPanel userPanel = new JPanel();
	private JPanel northPanel = new JPanel(new BorderLayout());
	private JPanel southPanel = new JPanel(new BorderLayout());
	private JPanel centerSouth = new JPanel(new GridLayout(1,2));
	private JPanel centerPanel = new JPanel(new BorderLayout());
	private JPanel lowerSouth = new JPanel(new BorderLayout(10,10));
	private JPanel labelPanel = new JPanel(new BorderLayout());
	private JButton showImage = new JButton("Show image");
	private JButton declineButton = new JButton("Decline image");
	private JButton startChat = new JButton("Start private chat");
	private JButton sendMessage = new JButton("Send Message");
	private JButton exitButton = new JButton("Hide Window");
	private JButton sendImage = new JButton("send Image");
	private JButton saveImage = new JButton("Save image");
	private JLabel title = new JLabel("List of users", JLabel.CENTER);
	private JLabel chat = new JLabel("private messages", JLabel.CENTER);
	private JTextArea chatLog = new JTextArea();
	private JScrollPane scroll = new JScrollPane(getUserPanel());
	private JScrollPane chatScroll = new JScrollPane(chatLog);
	private JFileChooser chooseFile = new JFileChooser();
	private JTextField messageField = new JTextField("Write message here!");
	private JDialog confirmImageFrame = new JDialog(new JFrame(),"Confirm window", ModalityType.APPLICATION_MODAL);
	private GridLayout userLayout = new GridLayout(20,1,5,5);
	private SimpleDateFormat timeStamp;
	private PrivateChatHandler chatHandler;
	private ClientGUI userInterface;
	private String path;
	private ButtonListener btnListener = new ButtonListener();

	/**
	 * Constructor which populates the panels, adds listeners and creates a list of
	 * online users that you can start a protected chat with
	 * @param ui
	 * @param name
	 */
	public PrivateChatGUI(ClientGUI ui, String name) {
		this.userInterface = ui;
		this.onlineUsers = new LinkedList<>();
		this.timeStamp = new SimpleDateFormat("HH:mm:ss");
		this.userInterface.getClient().addObserver(this);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension((int) (ui.getWidth()*.7 + ui.getWidth()*.6), ui.getHeight()));
		this.populatePanels();
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.chatHandler = new PrivateChatHandler(this);
		this.addListeners();
		chatHandler.populateList();
	}

	/**
	 * This method populates the panels in the protected chat GUI
	 */
	public void populatePanels(){
		this.exitButton.setPreferredSize(new Dimension(50,30));
		this.title.setPreferredSize(new Dimension((int) (userInterface.getWidth()*.5),20));
		this.getUserPanel().setPreferredSize(new Dimension((int) (userInterface.getWidth()*.5), 0));
		this.messageField.setPreferredSize(new Dimension(0,40));
		this.messageField.addMouseListener(new ClickListener());
		this.labelPanel.add(title, BorderLayout.WEST);
		this.labelPanel.add(chat, BorderLayout.CENTER);
		this.northPanel.add(labelPanel, BorderLayout.SOUTH);
		this.northPanel.add(startChat, BorderLayout.CENTER);
		this.northPanel.setPreferredSize(new Dimension(50, 55));
		this.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.chatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.chatLog.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.chatLog.setEditable(false);
		this.chatPanel.add(chatScroll);
		this.getUserPanel().setLayout(userLayout);
		this.centerPanel.add(scroll, BorderLayout.WEST);
		this.centerPanel.add(chatScroll, BorderLayout.CENTER);
		this.centerSouth.add(getSendMessage());
		this.centerSouth.add(getSendImage());
		this.lowerSouth.add(messageField);
		this.southPanel.setPreferredSize(new Dimension(100, 100));
		this.southPanel.add(centerSouth, BorderLayout.CENTER);
		this.southPanel.add(lowerSouth, BorderLayout.NORTH);
	}
	/**
	 * Method that gives the user the choice to save the image that has been sent to him or her
	 */
	public void saveReceivedImage(){
		File newImage = new File("Received Image."+"png");
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(newImage);
        int choice = chooser.showSaveDialog(PrivateChatGUI.this);
        if (choice == JFileChooser.APPROVE_OPTION) {
        	newImage = chooser.getSelectedFile();
            try {
                ImageIO.write(userInterface.getClient().getReceivedPicture(),"png", newImage);
            } catch (IOException e) {
            	showOnGroupChat("Unable to save the image!");
            }
        }
    }
	/**
	 * This method opens the file chooser when you want to send an image.
	 */
	public void openFileChooser(){
        int returnVal = chooseFile.showOpenDialog(PrivateChatGUI.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooseFile.getSelectedFile();
            path = file.getAbsolutePath();
            chatHandler.sendObjectToSelected(userInterface.getClient().loadImage(ClientGUIHandler.pathFormatter(path)));
            logEvent(path);
        }
    }

	/**
	 * This method opens a dialog where the user can choose to view
	 * an image that has been sent to them
	 * @param sender
	 * @param image
	 */
	public void newImageDialog(String sender,BufferedImage image){
		String[] options = { "Yes please!", "No thanks" };
		if (window.isVisible()) {
			int answer = DialogUtility.showConfirmationDialog(userInterface.getWindow(), "You have received a new image! Show options?",
					"New Image!", options);
			if (answer == DialogUtility.YES_OPTION) {
				userInterface.showPrivateChat();
				confirmImage(sender,image);
			}
		} else {
			confirmImage(sender,image);
		}
	}
	/**
	 * This method will update the list of online users when someone logs in or out
	 */
	public void update(Observable obs, Object obj) {
		Double command = (Double)obj;
		if(command == 1.0){
			chatHandler.populateList();
		}
		else if(command == 2.0){

		}
		else if(command == 3.0){

		}
		else if(command == 4.0){

		}
		else if(command == 10.0){
			chatHandler.populateList();
		}
	}
	public void logEvent(String log){
		System.out.println("OnlineUsersUI: " + log);
	}
	/**
	 * Shows a message in a group chat
	 * @param txt Message to be shown
	 */
	public void showOnGroupChat(String txt){
		String time = timeStamp.format(new Date()) + ":  " + txt;
		appendEvent(time + "\n");
	}

	public void setTimeStamp(String log){
		String time = timeStamp.format(new Date()) + ":  " + log;
		appendEvent(time + "\n");
	}
	public void appendEvent(String log){
		chatLog.append(log);
		chatLog.setCaretPosition(chatLog.getText().length() - 1);
	}

	public void clearPanel(){
		for(int i = 0; i<onlineUsers.size(); i++){
			getUserPanel().removeAll();
		}
	}
	public void addListeners(){
		for(JToggleButton user: onlineUsers){
			user.addActionListener(btnListener);
		}
		this.startChat.addActionListener(btnListener);
		this.getSendMessage().addActionListener(btnListener);
		this.getSendImage().addActionListener(btnListener);
		this.messageField.addActionListener(btnListener);
		this.showImage.addActionListener(btnListener);
		this.declineButton.addActionListener(btnListener);
		this.saveImage.addActionListener(btnListener);
	}
	/**
	 * Tells user that he has a new image that has been sent to him or her.
	 * @param sender
	 * @param image
	 */
	public void confirmImage(String sender, BufferedImage image){
		JPanel panel = new JPanel(new GridLayout(2,1));
		JLabel label = new JLabel("new Image from: " +sender+ "!", JLabel.CENTER);
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
		confirmImageFrame.validate();
		confirmImageFrame.setModal(true);
	}

	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i<onlineUsers.size(); i++){
				if(onlineUsers.get(i).isSelected()){
					onlineUsers.get(i).setSelected(true);
				}
			}
			if(e.getSource() == startChat){
				chatHandler.sendConfirmationToSelected();
			}
			else if(e.getSource() == getSendMessage()){
				if(!messageField.getText().isEmpty()){
					chatHandler.sendTextToSelected(messageField.getText());
					messageField.setText("");
				}
			}
			else if(e.getSource() == getSendImage()){
				openFileChooser();
			}
			else if(e.getSource() == saveImage){
				if(userInterface.getClient().getReceivedPicture()!=null)
				saveReceivedImage();
				confirmImageFrame.dispose();
			}
			else if (e.getSource() == showImage){
				userInterface.showImage(userInterface.getClient().getReceivedPicture());
			}
			else if (e.getSource() == declineButton){
				confirmImageFrame.dispose();
			}
			else if(e.getSource() == messageField){
				if(!messageField.getText().isEmpty()){
					chatHandler.sendMessageToSelectedUsers(messageField.getText());
					messageField.setText("");
				}
			}
		}
	}
	public class ClickListener implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == messageField){
				messageField.setText("");
			}
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}

	}
	public JFrame getFrame(){
		return window;
	}
	public LinkedList<JCheckBox> getOnlineUsers() {
		return onlineUsers;
	}

	public void setOnlineUsers(LinkedList<JCheckBox> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public ClientGUI getUserInterface() {
		return userInterface;
	}

	public void setUserInterface(ClientGUI userInterface) {
		this.userInterface = userInterface;
	}

	public JPanel getUserPanel() {
		return userPanel;
	}

	public JButton getSendImage() {
		return sendImage;
	}

	public JButton getSendMessage() {
		return sendMessage;
	}

    public void Start(){
    	window.add(this);
    	window.pack();
    	window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	window.setLocation(userInterface.getWindow().getX()-window.getWidth(), userInterface.getWindow().getY());
    	window.setBackground(Color.BLACK);
    	window.setVisible(false);
    }

}
