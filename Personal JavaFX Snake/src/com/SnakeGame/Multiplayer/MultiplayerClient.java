package com.SnakeGame.Multiplayer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Observable;

import com.SnakeGame.Interface.MenuMultiplayer;

public class MultiplayerClient extends Observable {

	/**
	 * allow the player to open a multiplayer menu, in the menu he will see
	 * users currently online and also be able to see if the users are currently
	 * playing or not and if not then the player can send a play request. the
	 * play request reaches the second client while the requesting client awaits
	 * for a response inside a waiting window. if a response is not received in
	 * within 1 minute then the request sender will be taken out of the await
	 * response window and a message saying "the request has timed out" will
	 * appear. if the client receiving the request accepts the request he will
	 * then enter a window which will allow client to press a button confirming
	 * they are ready to start and if the other player presses the ready to
	 * start button the game will then start
	 *
	 *
	 * all actions performed by each client will be send to the server which
	 * will then use this commands in order to control the rival snake on the
	 * screen of each client. the commands will constantly flow through the
	 * buffer. all the actions and events will be send as commands through the
	 * server.
	 *
	 * the server will also take care of things that are randomly generated
	 * allow this items to be generated symmetrically on both sides and at
	 * parallel times.
	 *
	 */

	private LinkedList<Object> inbox;
	private LinkedList<String> groupOfUsers;
	private LinkedList<String> onlineUsers;
	private LinkedList<String> disconnectedUser;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ByteArrayOutputStream byteOutput;
	private BufferedImage receivedPicture;
	private BufferedImage sendPicture;
	private MenuMultiplayer GUI;
	private String IP_Address;
	private Socket clientSocket;
	private Thread connect;
	private Object object;
	private String userName;
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

	public MultiplayerClient(String IP_Address, int port, String userName) {

	}

	public MultiplayerClient(String IP_Address, int port, String userName, MenuMultiplayer GUI) {
//		this.inbox = new LinkedList<>();
//		this.groupOfUsers = new LinkedList<>();
//		this.onlineUsers = new LinkedList<>();
//		this.disconnectedUser = new LinkedList<>();
//		this.byteOutput = new ByteArrayOutputStream();
//		this.IP_Address = IP_Address;
//		this.userName = userName;
//		this.GUI = GUI;
//		this.port = port;
//		this.IP_Address = IP_Address;
	}
	// public void allowToRun(){
	// if(run)
	// return;
	// run = true;
	// }
	// public void stopRunning(){
	// if(!run)
	// return;
	// run = false;
	// }
	// public void connect() throws IOException {
	// try{
	// clientSocket = new Socket(IP_Address, port);
	// setConnected(true);
	// startThread();
	// }catch(SocketException e){
	// showEvent("Unable to connect to a server!");
	// GUI.getServerMessageLabel().setText("Server is currently offline!");
	// }
	//
	// }
	// public void startThread(){
	// if(connect==null){
	// this.connect = new Thread(new ClientConnect());
	// this.connect.start();
	// }
	// }
	// public void stopThread(){
	// if(connect!=null){
	// this.connect.interrupt();
	// }
	// }
	// public Object getObject() {
	// return object;
	// }
	// public void setObject(Object object){
	// this.object = object;
	// }
	// public void SendPackage(Object obj){
	// try {
	// if(isConnected() && clientSocket.isConnected()){
	// output.writeObject(obj);
	// output.flush();
	// }
	// } catch (IOException e) {
	// showEvent("Failed to write package stream " + e.getMessage());
	// //e.printStackTrace();
	// }
	// }
	// public byte[] converToByte(Object obj){
	// ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
	// ObjectOutput objectOutput = null;
	// byte[] newByteArray = null;
	// try {
	// objectOutput = new ObjectOutputStream(byteOutput);
	// objectOutput.writeObject(obj);
	// newByteArray = byteOutput.toByteArray();
	// } catch (IOException e) {
	// showEvent("unable to write object to byte array" +e.getMessage());
	// e.printStackTrace();
	// } finally {
	// try {
	// if (objectOutput != null) {
	// objectOutput.close();
	// }
	// } catch (IOException ex) {}
	// try {
	// byteOutput.close();
	// } catch (IOException ex) {}
	// }
	// return newByteArray;
	// }
	// public Object converToObject(byte[] bytes){
	// ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
	// ObjectInput objectInput = null;
	// Object newObject = null;
	// try {
	// objectInput = new ObjectInputStream(byteInput);
	// newObject = objectInput.readObject();
	// } catch (ClassNotFoundException | IOException e) {
	// showEvent("Unable to convert byte array to an object" + e.getMessage());
	// e.printStackTrace();
	// } finally {
	// try {
	// byteInput.close();
	// } catch (IOException ex) {}
	// try {
	// if (objectInput != null) {
	// objectInput.close();
	// }
	// } catch (IOException ex) {}
	// }
	// return newObject;
	// }
	// public void closeConnection() {
	// try {
	// if(output!=null && input!=null && clientSocket!=null){
	// output.close();
	// input.close();
	// clientSocket.close();
	// //GUI.setConnection(false);
	// showEvent("Connection has been successfully terminated ");
	// }
	// } catch (IOException e) {
	// showEvent("Something went wrong while attempting to close the connection
	// " +e.getMessage());
	// e.printStackTrace();
	// }
	// finally{
	// output = null;
	// input = null;
	// clientSocket = null;
	// connect = null;
	// }
	// }
	// public void closeOutuputStream() {
	// try {
	// if (output != null) {
	// output.close();
	// }
	// } catch (IOException e) {
	// showEvent("Something went wrong while attempting to close the
	// outputStream " + e.getMessage());
	// e.printStackTrace();
	// }
	// }
	// public BufferedImage loadImage(String path){
	// BufferedImage image = null;
	// try {
	// image = ImageIO.read(new File(path));
	// image.flush();
	// } catch (IOException e) {
	// showEvent("Unable to read buffered image" + e.getMessage());
	// }try{
	// return image;
	// }
	// finally{
	// image.flush();
	// }
	// }
	// public void showEvent(String event){
	// System.out.println("CLIENT :" + event);
	// }
	//
	// public void handleObject(Object obj) {
	// DataPacket pack = (DataPacket) obj;
	// Integer command = pack.getCommand();
	// String message = pack.getMessage();
	// String playerID = pack.getPlayerID();
	// String objectID = pack.getObjectID();
	// double x = pack.getX();
	// double y = pack.getY();
	// double r = pack.getR();
	// if (pack.getCommand() == null) {
	// if (pack.getCoordinates()!= null) {
	//
	// }
	// if (!pack.getMessage().isEmpty()) {
	//
	// }
	// if (pack.getData()!= null) {
	//
	// }
	//
	// } else if (pack.getCommand() != null) {
	// handleServerCommand(pack);
	// }
	//
	// }
	// public void performServerCommand(Double command){
	// if(command == 1.0){
	// logClientOut();
	// }
	// else if(command == 2.0){
	//
	// }
	// }
	// public void logClientOut(){
	// GUI.ServerlogOut();
	// GUI.showOnChat("Server: Connection has been terminated!");
	// }
	// public void handleServerCommand(DataPacket pack){
	// if(pack.getCommand() == 1.0){
	// onlineUsers = pack.getGroup();
	// for(int i = 0; i<onlineUsers.size(); i++){
	// if(onlineUsers.get(i).contentEquals(userName)){
	// onlineUsers.remove(i);
	// }
	// }
	// setChanged();
	// notifyObservers(pack.getCommand());
	// }
	// else if(pack.getCommand() == 2.0){
	// GUI.showOnChat(pack.getReceiver()+" has left the chat!");
	// disconnectedUser = pack.getGroup();
	// setChanged();
	// notifyObservers(pack.getCommand());
	// }
	// else if(pack.getCommand() == 3.0){
	// onlineUsers = pack.getGroup();
	// }
	// else if(pack.getCommand() == 4.0){
	// GUI.privateChatNotification(pack);
	// GUI.getOnlineUsers().showOnGroupChat(pack.getMessage());
	// }
	// else if(pack.getCommand() == 5.0){
	// String text = pack.getMessage();
	// String sender = pack.getID();
	// BufferedImage image;
	// if (pack.getImage() != null) {
	// image = pack.getImage();
	// this.setReceivedPicture(image);
	// GUI.showPrivateChat();
	// GUI.getOnlineUsers().showOnGroupChat(pack.getMessage());
	// }
	// if (!pack.getMessage().isEmpty()) {
	// text = pack.getMessage();
	// this.setReceivedMessage(sender + ": " + text);
	// GUI.showNotificationMessage(pack);
	// GUI.getOnlineUsers().showOnGroupChat(text);
	// }
	// if (pack.getGroup() != null) {
	// groupOfUsers = pack.getGroup();
	// }
	// }
	// else if(pack.getCommand() == 6.0){
	// GUI.getServerMessageLabel().setText(pack.getMessage());
	// }
	// else if(pack.getCommand() == 7.0){
	// GUI.setInboxSize(pack.getAmount());
	// GUI.closeLogInWindow(pack.getID(),pack.getReceiver());
	// GUI.showOnChat(pack.getMessage());
	// }
	// else if(pack.getCommand() == 8.0){
	// setReceivedPicture(byteToImage(pack.getData()));
	// GUI.comfirmImage(byteToImage(pack.getData()));
	// if(!pack.getID().contentEquals(userName))
	// GUI.showOnChat(pack.getID() + " sent an image!");
	// }
	// else if(pack.getCommand() == 9.0){
	// setReceivedPicture(byteToImage(pack.getData()));
	// GUI.getOnlineUsers().newImageDialog(pack.getID(),
	// byteToImage(pack.getData()));
	// if(!pack.getID().contentEquals(userName))
	// GUI.getOnlineUsers().showOnGroupChat(pack.getID() + " sent an image!");
	// }
	// else if(pack.getCommand() == 10.0){
	// disconnectedUser = pack.getGroup();
	// setChanged();
	// notifyObservers(pack.getCommand());
	// }
	// else if(pack.getCommand() == 11.0){
	// String text = pack.getMessage();
	// if (!pack.getMessage().isEmpty()) {
	// text = pack.getMessage();
	// GUI.getOnlineUsers().showOnGroupChat(text);
	// }
	// }
	//
	// }
	// public void seeWhoAndWhoIsntOnline(){
	// for(int i = 0; i<onlineUsers.size(); i++){
	// showEvent(onlineUsers.get(i) +" in in online list");
	// }
	// for(int r = 0; r<disconnectedUser.size(); r++){
	// showEvent(disconnectedUser.get(r) +" in in offline list");
	// }
	// }
	// public void addToOfflineList(String user) {
	// showEvent("adding user offline list ");
	// if(disconnectedUser.size()>0){
	// for(int i = 0; i<disconnectedUser.size(); i++){
	// if(disconnectedUser.get(i).contentEquals(user)){
	// disconnectedUser.remove(i);
	// showEvent("offline filled");
	// }
	// }
	// }
	// disconnectedUser.add(user);
	// showEvent("user addeded: " + user);
	// }
	//
	// private class ClientConnect extends Thread{
	//
	// public ClientConnect() {
	// try {
	// output = new ObjectOutputStream(clientSocket.getOutputStream());
	// output.flush();
	// input = new ObjectInputStream(clientSocket.getInputStream());
	//
	// } catch (IOException io) {
	// showEvent("Failed to create output or input stream " + io.getMessage());
	// }
	// }
	// public void run() {
	// while (isConnected() && !clientSocket.isClosed()) {
	//
	// Object object;
	// try {
	// object = input.readObject();
	// populateInbox(object);
	// handleObject(sendIncoming());
	// } catch ( ClassNotFoundException | IOException e) {
	// //TODO: Handle
	// break;
	// }
	// }
	// }
	// public synchronized void populateInbox(Object obj) {
	// inbox.addLast(obj);
	// this.notifyAll();
	// }
	// public synchronized Object sendIncoming(){
	// while (inbox.isEmpty()) {
	// try {
	// this.wait();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// return inbox.removeFirst();
	// }
	// }
	// public boolean loggedIn(){
	// return connected;
	// }
	// public boolean justLoggedIn(){
	// return justLoggedIn;
	// }
	// public void setLoggedIn(){
	// this.justLoggedIn = true;
	// }
	// public String getServerMessage(){
	// return messageFromServer;
	// }
	// public ObjectInputStream getInput() {
	// return input;
	// }
	// public void setInput(ObjectInputStream input) {
	// this.input = input;
	// }
	// public ObjectOutputStream getOutput() {
	// return output;
	// }
	// public void setOutput(ObjectOutputStream output) {
	// this.output = output;
	// }
	// public Socket getClientSocket() {
	// return clientSocket;
	// }
	// public void setClientSocket(Socket clientSocket) {
	// this.clientSocket = clientSocket;
	// }
	// public LinkedList<Object> getInbox() {
	// return inbox;
	// }
	// public void setInbox(LinkedList<Object> inbox) {
	// this.inbox = inbox;
	// }
	// public LinkedList<String> getGroupOfUsers() {
	// return groupOfUsers;
	// }
	// public void setGroupOfUsers(LinkedList<String> groupOfUsers) {
	// this.groupOfUsers = groupOfUsers;
	// }
	// public LinkedList<String> getOnlineUsers() {
	// return onlineUsers;
	// }
	// public void setOnlineUsers(LinkedList<String> onlineUsers) {
	// this.onlineUsers = onlineUsers;
	// }
	// public LinkedList<String> getDisconnectedUser() {
	// return disconnectedUser;
	// }
	// public void setDisconnectedUser(LinkedList<String> disconnectedUser) {
	// this.disconnectedUser = disconnectedUser;
	// }
	// public String getIP_Address() {
	// return IP_Address;
	// }
	// public void setFile(File file){
	// this.file = file;
	// }
	// public File getFile(){
	// return file;
	// }
	// public void setIP_Address(String IP_Address) {
	// this.IP_Address = IP_Address;
	// }
	// public boolean isRun() {
	// return run;
	// }
	// public void setRun(boolean run) {
	// this.run = run;
	// }
	// public String getResponse() {
	// return response;
	// }
	// public void setResponse(String response) {
	// this.response = response;
	// }
	// public String getUserInformation() {
	// return userInformation;
	// }
	// public void setUserInformation(String userInformation) {
	// this.userInformation = userInformation;
	// }
	// public String getUserName() {
	// return userName;
	// }
	// public void setUserName(String userName) {
	// this.userName = userName;
	// }
	// public MenuMultiplayer getGUI() {
	// return GUI;
	// }
	// public void setGUI(MenuMultiplayer gUI) {
	// this.GUI = gUI;
	// }
	// public String getName() {
	// return name;
	// }
	// public void setName(String name) {
	// this.name = name;
	// }
	// public String getLastname() {
	// return lastname;
	// }
	// public void setLastname(String lastname) {
	// this.lastname = lastname;
	// }
	// public Double getServerCommand() {
	// return serverCommand;
	// }
	// public void setServerCommand(Double serverCommand) {
	// this.serverCommand = serverCommand;
	// }
	// public String getReceivedMessage() {
	// return receivedMessage;
	// }
	// public void setReceivedMessage(String receivedMessage) {
	// this.receivedMessage = receivedMessage;
	// }
	// public String getSendMessage() {
	// return sendMessage;
	// }
	// public void setSendMessage(String sendMessage) {
	// this.sendMessage = sendMessage;
	// }
	// public boolean isConnected() {
	// return connected;
	// }
	// public void setConnected(boolean connected) {
	// this.connected = connected;
	// }
	// public boolean isSending() {
	// return sending;
	// }
	// public void setSending(boolean sending) {
	// this.sending = sending;
	// }
	// public boolean isSelectedUser() {
	// return selectedUser;
	// }
	// public void setSelectedUser(boolean selectedUser) {
	// this.selectedUser = selectedUser;
	// }
	// public boolean isAway() {
	// return away;
	// }
	// public void setAway(boolean away) {
	// this.away = away;
	// }
	// public BufferedImage getReceivedPicture() {
	// return receivedPicture;
	// }
	// public void setReceivedPicture(BufferedImage receivedPicture) {
	// this.receivedPicture = receivedPicture;
	// }
	// public BufferedImage getSendPicture() {
	// return sendPicture;
	// }
	// public void setSendPicture(BufferedImage sendPicture) {
	// this.sendPicture = sendPicture;
	// }
	// public int getPort() {
	// return port;
	// }
	// public void setPort(int port) {
	// this.port = port;
	// }

}
