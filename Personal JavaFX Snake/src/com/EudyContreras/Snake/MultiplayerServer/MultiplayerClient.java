package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;

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
 *
 *  7/28/2016
 *Sequence: STARTING THE MULTIPLAYER MODE:
 *
 *Enter multi-player mode.
 *Connect to server if not already connected.
 *show connection achieved message.
 *look for online player in the same map for 60 seconds. store level id and number!!
 *if match found server sends confirmation and creates a session between the two players!
 *server starts the match and displays a picture of the 2 player, level, location etc!!
 *server starts the game and enters the player ready state
 *once both player are ready the match starts else wait!
 *if no match was found inform the player and go idle until the player re-attempts to look for a potential match again!
 *
 *
 *Sequence: TRANSLATING POSITIONS AND MOVEMENTS TO THE RIVAL PLAYER
 *received real-time direct input from the server
 *
 *
 */
public class MultiplayerClient extends Thread{

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ClientManager manager;
	private MultiplayerServer server;
	private SocketAddress address;
	private LinkedList<Object> inbox;
	private LinkedList<Object> queuedMessages;
	private String username = "unknown:";
	private String password = "password";
	private String response;
	private String location;
	private String userName;
	private Socket socket;
	private boolean connected;
	private int uniqueID;
	private int age;
	private int level;


	/**
	 * This constructors takes a socket and ID as
	 * an argument.
	 * @param socket: socket created thought this connection.
	 * @param ID: the unique id of this client.
	 */
	public MultiplayerClient(MultiplayerServer server, ClientManager manager, Socket socket, int uiqueID) {
		this.server = server;
		this.manager = manager;
		this.socket = socket;
		this.address = socket.getRemoteSocketAddress();
		this.inbox = new LinkedList<>();
		this.connected = true;
		this.uniqueID = uiqueID;
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			server.logToConsole("Unable to create output and input stream" + e.getMessage());
			e.printStackTrace();
		}
		try {

			Object object = input.readObject();
			inbox.add(object);
			if(inbox.size()>0){
			manager.receiveNewUser(ObjectPop(0), this);
			}
			String newUserConnected = username + " has connected to the server!";
			server.getGUI().showServerEvent(newUserConnected);
		} catch (ClassNotFoundException | IOException e) {
			server.logToConsole("Unable to fetch username " + e.getMessage() );
			e.printStackTrace();
		}
	}
	/**
	 * Adds objects to this client's inbox list
	 * @param obj
	 */
	public synchronized void populateInbox(Object obj) {
		inbox.addLast(obj);
		this.notifyAll();
	}
	/**
	 * removes and returns the first element in the inbox
	 * @return
	 */
	public synchronized Object sendIncoming() {
		while (inbox.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return inbox.removeFirst();
	}
	/**
	 * method which sends a generic package containing an object
	 * to the client
	 * @param object
	 */
	public void sendPackage(Object object) {
		try {
			if(connected){
			output.writeObject(object);
			output.flush();
			}
		} catch (IOException e) {
			server.logToConsole("Failed to write object to stream " + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * method which reads packages which are received through the input
	 * stream.
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object readPackage() throws ClassNotFoundException, IOException {
		if(isConnected()){
		Object object = input.readObject();
		return object;
		}
		return "No Connection!";
	}
	/**
	 * Method which closes all connection for this client
	 * and closes the input and output streams.
	 */
	public void closeConnection(){
		if(output!=null && input!=null && socket!=null){
		try {
			output.close();
			input.close();
			socket.close();
			server.getGUI().showServerEvent("Connection with client " +username+ " has been terminated");
		} catch (IOException e) {
			server.logToConsole("Failed to properly shut down connection");
		}
		}
	}
	/**
	 * Run method for this thread which receives objects and populates
	 * this inbox of this particular client.
	 */
	public void run() {
		while (connected && !socket.isClosed()) {
			try {
				Object object = input.readObject();
				populateInbox(object);
				if(inbox.size()>0){
				manager.handleObject(sendIncoming(), this);
				}
			} catch (ClassNotFoundException|IOException e) {
				if(connected){
					server.logToConsole("Failed to read package from client :" + username);
				e.printStackTrace();
				manager.disconnectClient2(this);
				closeConnection();
				break;
				}
				connected = false;
			}
		}
	}
	protected boolean isConnected(){
		return connected;
	}
	protected void setConnection(boolean status){
		this.connected = status;
	}
	protected Socket getSocket(){
		return socket;
	}
	protected SocketAddress getSocketAddress(){
		return address;
	}
	protected int getID(){
		return uniqueID;
	}
	protected void setResponse(String response){
		this.response = response;
	}
	protected String getResponse(){
		return response;
	}
	protected String getPassword(){
		return password;
	}
	protected void setPassword(String password){
		this.password = password;
	}
	protected void setUsername(String username) {
		this.username = username;
	}
	protected String getUsername(){
		return username;
	}
	protected LinkedList<Object> getInbox(){
		return inbox;
	}
	protected Object ObjectPop(int index){
		if(inbox.isEmpty())
			return null;
		return inbox.remove(index);
	}
	protected Object getObject(int index){
		return inbox.get(index);
	}

	public enum Status{
		PLAYING,
		IDLE,
		WAITING,
		SEARCHING,
		DISCONNECTED,
	}
}
