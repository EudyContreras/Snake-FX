package com.EudyContreras.Snake.MultiplayerClient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Observable;

import javax.imageio.ImageIO;

import com.EudyContreras.Snake.DataPackage.ServerResponse;
/**
 * This class represents a client that can connect to a
 * server.
 * @author Eudy Contreras, Marcus Vazdekis, Nicolai Jaynes
 *
 */

public class ChatClient extends Observable{

	private LinkedList<Object> inbox;
	private LinkedList<String> groupOfUsers;
	private LinkedList<String> onlineUsers;
	private LinkedList<String> disconnectedUser;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ByteArrayOutputStream byteOutput;
	private CommandHandler handler;
	private BufferedImage receivedPicture;
	private BufferedImage sendPicture;
	private String IP_Address;
	private Socket clientSocket;
	private Thread connect;
	private Object object;
	private String userName;
	private ClientGUI GUI;
	private String name, lastname;
	private String receivedMessage;
	private String sendMessage;
	private String messageFromServer;
	private String response;
	private String userInformation;
	private Double serverCommand;
	private File file;
	private boolean connected;
	private boolean sending;
	private boolean selectedUser;
	private boolean away;
	private boolean run = true;
	private boolean justLoggedIn = true;
	private int port;
	/**
	 * Constructor which takes an IP address which a connection will be attempted to be
	 * make with, a port to be used for the connection, the user name of this client
	 * and the graphical user interface used by this client application
	 * @param IP_Address
	 * @param port
	 * @param userName
	 * @param GUI
	 */
	public ChatClient(String IP_Address, int port, String userName, ClientGUI GUI) {
		this.inbox = new LinkedList<>();
		this.groupOfUsers = new LinkedList<>();
		this.onlineUsers = new LinkedList<>();
		this.disconnectedUser = new LinkedList<>();
		this.byteOutput = new ByteArrayOutputStream();
		this.IP_Address = IP_Address;
		this.userName = userName;
		this.GUI = GUI;
		this.port = port;
		this.IP_Address = IP_Address;
		this.handler = new CommandHandler(this,GUI);
	}
	public ChatClient() {

	}
	public void allowToRun(){
		if(run)
			return;
		run = true;
	}
	public void stopRunning(){
		if(!run)
			return;
		run = false;
	}
	public void changeNotification(){
		setChanged();
	}
	/**
	 * This method attempts a connection to a sever
	 * with specific port and IP address.
	 * @throws IOException
	 */
	public void connect() throws IOException {
		try{
			clientSocket = new Socket(IP_Address, port);
			setConnected(true);
			startThread();
		}catch(SocketException e){
			showEvent("Unable to connect to a server!");
			GUI.getServerMessageLabel().setText("Server is currently offline!");
		}

	}
	/**
	 * Starts the server thread
	 */
	public void startThread(){
		if(connect==null){
			this.connect = new Thread(new ConnectionHandler(this));
		this.connect.start();
		}
	}
	/**
	 * Stops the server thread
	 */
	public void stopThread(){
		if(connect!=null){
			this.connect.interrupt();
		}
	}
	/**
	 * Sends an object to the server through an output stream.
	 * @param obj
	 */
	public void sendPackage(Object obj){
		try {
			if(isConnected() && clientSocket.isConnected()){
			output.writeObject(obj);
			output.flush();
			}
		} catch (IOException e) {
			showEvent("Failed to write package stream " + e.getMessage());
			//e.printStackTrace();
		}
	}
	/**
	 * Sends a text to the server
	 * @param message
	 */
	public void sendTextMessage(String message){
		try {
			if(isConnected()){
			output.writeObject(message);
			output.flush();
			}
		} catch (IOException e) {
			showEvent("Failed to write message stream " + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Sends a buffered image as a byte array
	 * @param image
	 */
	public void sendPictureMessage(BufferedImage image, String imageName){
		byte[] imageByteArray  = ImageToByte(image);
		ServerResponse pack = new ServerResponse(userName,5.0,imageByteArray,imageName,true);
		try {
			output.writeObject(pack);
			output.flush();
		} catch (IOException e) {
			showEvent("Failed to write image to stream " + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Sends an objects to specified user, containing various types of information.
	 * @param sender
	 * @param receiver
	 * @param message
	 * @param command
	 * @param image
	 */
	public void sendPrivateObject(String sender, String receiver, String message, Double command, BufferedImage image) {
		byte[] imageByteArray  = ImageToByte(image);
		ServerResponse pack = new ServerResponse(sender,receiver,message,command,imageByteArray);
		try {
			output.writeObject(pack);
			output.flush();
		} catch (IOException e) {
			showEvent("Failed to write image to stream " + e.getMessage());
			e.printStackTrace();
		}

	}
	/**
	 * Converts a buffered image into a byte array
	 * @param image
	 * @return
	 */
	public byte[] ImageToByte(BufferedImage image){
		byte[] imageInByte = null;
		try{
			if(image!=null) {
				ImageIO.write(image, "jpg", byteOutput);
				imageInByte = byteOutput.toByteArray();
				byteOutput.flush();
			}
		}catch(IOException | IllegalArgumentException e ){
			showEvent("Unabled to convert image to byte array " +e.getMessage());
			e.printStackTrace();
		}
		try{
		return  imageInByte;
		}
		finally{
			byteOutput.reset();
		}
	}
	/**
	 * Converts a byte array into a Buffered image.
	 * @param data
	 * @return
	 */
	public BufferedImage byteToImage(byte[] data){
		BufferedImage newImage = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			newImage = ImageIO.read(inputStream);
			inputStream.close();
		} catch (IOException e) {
			showEvent("Unable to convert byt to image"+ e.getMessage());
		}
		return newImage;
	}

	/**
	 * Closes this client's connection and closes
	 * all the streams.
	 */
	public void closeConnection() {
		try {
			if(output!=null && input!=null && clientSocket!=null){
				output.close();
				input.close();
				clientSocket.close();
				GUI.setConnection(false);
				showEvent("Connection has been successfully terminated ");
			}
		} catch (IOException e) {
			showEvent("Something went wrong while attempting to close the connection " +e.getMessage());
			e.printStackTrace();
		}
		finally{
			output = null;
			input = null;
			clientSocket = null;
			connect = null;
		}
	}
	/**
	 * Closes this client's output stream
	 */
	public void closeOutputStream() {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException e) {
			showEvent("Something went wrong while attempting to close the outputStream " + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * loads a returns a buffered image
	 * @param path
	 * @return
	 */
	public BufferedImage loadImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException | NullPointerException e ) {
			showEvent("Unable to read buffered image" + e.getMessage());
		}
		try {
			return image;
		} finally {
			image.flush();
		}

	}
	/**
	 * Shows client events on the console
	 * @param event
	 */
	public void showEvent(String event){
		System.out.println("CLIENT :" + event);
	}
	/**
	 * Method responsible for handling all objects
	 * received by this client. This method will determine the
	 * nature of the received and then send it further for
	 * processing.
	 * @param obj
	 */
	public void handleObject(Object obj) {
		ServerResponse pack;
		String text;
		String sender;
		BufferedImage image;
		Double command;

		if (obj instanceof ServerResponse) {
			pack = (ServerResponse) obj;
			sender = pack.getID();
			if (pack.getCommand() == null) {
				if (pack.getImage() != null) {
					image = pack.getImage();
					this.setReceivedPicture(image);
					GUI.showImage(getReceivedPicture());
				}
				if (!pack.getMessage().isEmpty()) {
					text = pack.getMessage();
					this.setReceivedMessage(sender + ": " + text);
					GUI.showOnChat(getReceivedMessage());
				}
				if (pack.getGroup() != null) {
					groupOfUsers = pack.getGroup();
				}
			}
			else if (pack.getCommand() != null) {
				handler.handleServerCommand(pack);
			}
		}else if(obj instanceof byte[]){
			BufferedImage newImage = byteToImage((byte[])obj);
			setReceivedPicture(newImage);
			GUI.confirmImage(newImage);
		}else if (obj instanceof String) {
			text = (String) obj;
			this.setReceivedMessage(text);
			GUI.showOnChat(getReceivedMessage());
		} else if (obj instanceof Double) {
			command = (Double) obj;
			performServerCommand(command);
			setServerCommand(command);
		}
	}
	/**
	 * This method checks the value of a command
	 * and performs an action base on that command.
	 * @param command
	 */
	public void performServerCommand(Double command){
		if(command == 1.0){
			logClientOut();
		}
		else if(command == 2.0){

		}
	}
	/**
	 * Logs events to the chat window of this client's GUI
	 */
	public void logClientOut(){
		ServerlogOut();
		GUI.showOnChat("Server: Connection has been terminated!");
	}
	/**
	 * This method logs out the client from the server
	 *
	 */

	public void ServerlogOut(){
		setConnected(false);
		closeConnection();
		if(GUI.getOnlineUsers()!=null)
			GUI.getOnlineUsers().getFrame().setVisible(false);
		GUI.getShowUsers().setText("Show private chat");
		GUI.runStatus();
		GUI.getLogIn().setText("Log in");
		GUI.getLogIn().setEnabled(true);
		GUI.modifyButtons(false);
	}
	/**
	 * Show all online user in the console
	 */
	public void seeWhoAndWhoIsntOnline(){
		for(int i = 0; i<onlineUsers.size(); i++){
			showEvent(onlineUsers.get(i) +" in in online list");
		}
		for(int r = 0; r<disconnectedUser.size(); r++){
			showEvent(disconnectedUser.get(r) +" in in offline list");
		}
	}
	/**
	 * Adds a new offline user to the offline users list.
	 * @param user
	 */
	public void addToOfflineList(String user) {
		showEvent("adding user offline list ");
		if(disconnectedUser.size()>0){
			for(int i = 0; i<disconnectedUser.size(); i++){
				if(disconnectedUser.get(i).contentEquals(user)){
					disconnectedUser.remove(i);
					showEvent("offline filled");
				}
			}
		}
		disconnectedUser.add(user);
		showEvent("user added: " + user);
	}

	public boolean loggedIn(){
		return connected;
	}
	public boolean justLoggedIn(){
		return justLoggedIn;
	}
	public void setLoggedIn(){
		this.justLoggedIn = true;
	}
	public String getServerMessage(){
		return messageFromServer;
	}
	public ObjectInputStream getInput() {
		return input;
	}
	public void setInput(ObjectInputStream input) {
		this.input = input;
	}
	public ObjectOutputStream getOutput() {
		return output;
	}
	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}
	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object){
		this.object = object;
	}
	public LinkedList<Object> getInbox() {
		return inbox;
	}
	public void setInbox(LinkedList<Object> inbox) {
		this.inbox = inbox;
	}
	public LinkedList<String> getGroupOfUsers() {
		return groupOfUsers;
	}
	public void setGroupOfUsers(LinkedList<String> groupOfUsers) {
		this.groupOfUsers = groupOfUsers;
	}
	public LinkedList<String> getOnlineUsers() {
		return onlineUsers;
	}
	public void setOnlineUsers(LinkedList<String> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}
	public LinkedList<String> getDisconnectedUser() {
		return disconnectedUser;
	}
	public void setDisconnectedUser(LinkedList<String> disconnectedUser) {
		this.disconnectedUser = disconnectedUser;
	}
	public String getIP_Address() {
		return IP_Address;
	}
	public void setFile(File file){
		this.file = file;
	}
	public File getFile(){
		return file;
	}
	public void setIP_Address(String IP_Address) {
		this.IP_Address = IP_Address;
	}
	public boolean isRun() {
		return run;
	}
	public void setRun(boolean run) {
		this.run = run;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getUserInformation() {
		return userInformation;
	}
	public void setUserInformation(String userInformation) {
		this.userInformation = userInformation;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ClientGUI getGUI() {
		return GUI;
	}
	public void setGUI(ClientGUI gUI) {
		this.GUI = gUI;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Double getServerCommand() {
		return serverCommand;
	}
	public void setServerCommand(Double serverCommand) {
		this.serverCommand = serverCommand;
	}
	public String getReceivedMessage() {
		return receivedMessage;
	}
	public void setReceivedMessage(String receivedMessage) {
		this.receivedMessage = receivedMessage;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public boolean isSending() {
		return sending;
	}
	public void setSending(boolean sending) {
		this.sending = sending;
	}
	public boolean isSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(boolean selectedUser) {
		this.selectedUser = selectedUser;
	}
	public boolean isAway() {
		return away;
	}
	public void setAway(boolean away) {
		this.away = away;
	}
	public BufferedImage getReceivedPicture() {
		return receivedPicture;
	}
	public void setReceivedPicture(BufferedImage receivedPicture) {
		this.receivedPicture = receivedPicture;
	}
	public BufferedImage getSendPicture() {
		return sendPicture;
	}
	public void setSendPicture(BufferedImage sendPicture) {
		this.sendPicture = sendPicture;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
