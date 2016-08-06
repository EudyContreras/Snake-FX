package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;

import com.EudyContreras.Snake.DataPackage.MatchRequest;

/**
 * allow the player to open a multiplayer menu, in the menu he will see users
 * currently online and also be able to see if the users are currently playing
 * or not and if not then the player can send a play request. the play request
 * reaches the second client while the requesting client awaits for a response
 * inside a waiting window. if a response is not received in within 1 minute
 * then the request sender will be taken out of the await response window and a
 * message saying "the request has timed out" will appear. if the client
 * receiving the request accepts the request he will then enter a window which
 * will allow client to press a button confirming they are ready to start and if
 * the other player presses the ready to start button the game will then start
 *
 *
 * all actions performed by each client will be send to the server which will
 * then use this commands in order to control the rival snake on the screen of
 * each client. the commands will constantly flow through the buffer. all the
 * actions and events will be send as commands through the server.
 *
 * the server will also take care of things that are randomly generated allow
 * this items to be generated symmetrically on both sides and at parallel times.
 *
 *
 * 7/28/2016 Sequence: STARTING THE MULTIPLAYER MODE:
 *
 * Enter multi-player mode. Connect to server if not already connected. show
 * connection achieved message. look for online player in the same map for 60
 * seconds. store level id and number!! if match found server sends confirmation
 * and creates a session between the two players! server starts the match and
 * displays a picture of the 2 player, level, location etc!! server starts the
 * game and enters the player ready state once both player are ready the match
 * starts else wait! if no match was found inform the player and go idle until
 * the player re-attempts to look for a potential match again!
 *
 *
 * Sequence: TRANSLATING POSITIONS AND MOVEMENTS TO THE RIVAL PLAYER received
 * real-time direct input from the server
 *
 *
 */
public class MultiplayerClient extends Thread {

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ClientManager clientManager;
	private PackageManager packageManager;
	private MultiplayerServer server;
	private SocketAddress address;
	private LinkedList<Object> chatInbox;
	private LinkedList<MatchRequest> requestInbox;
	private String name;
	private String username = "unknown";
	private String password = "password";
	private String response;
	private String location;
	private Socket socket;
	private byte[] profilePic;
	private boolean connected;
	private int uniqueID;
	private int level;
	private int age;

	/**
	 * This constructors takes a socket and ID as an argument.
	 *
	 * @param socket: socket created thought this connection.
	 * @param ID: the unique id of this client.
	 */
	public MultiplayerClient(MultiplayerServer server, ClientManager manager, Socket socket, int uiqueID) {
		this.server = server;
		this.clientManager = manager;
		this.socket = socket;
		this.connected = true;
		this.uniqueID = uiqueID;
		this.address = socket.getRemoteSocketAddress();
		this.chatInbox = new LinkedList<>();
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			server.logToConsole("Unable to create output and input stream" + e.getMessage());
			e.printStackTrace();
		}
		try {

			chatInbox.add(input.readObject());
			if (chatInbox.size() > 0) {
				manager.receiveNewUser(ObjectPop(0), this);
			}
			String newUserConnected = username + " has connected to the server!";
			server.logToConsole(newUserConnected);

		} catch (ClassNotFoundException | IOException e) {
			server.logToConsole("Unable to fetch username " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Adds objects to this client's in-box list
	 *
	 * @param obj
	 */
	public synchronized void populateInbox(Object obj) {
		chatInbox.addLast(obj);
		this.notifyAll();
	}

	/**
	 * removes and returns the first element in the inbox
	 *
	 * @return
	 */
	public synchronized Object sendIncoming() {
		while (chatInbox.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return chatInbox.removeFirst();
	}

	/**
	 * method which sends a generic package containing an object to the client
	 *
	 * @param object
	 */
	public void sendPackage(Object object) {
		try {
			if (connected) {
				output.writeObject(object);
				output.flush();
			}
		} catch (IOException e) {
			server.logToConsole("Failed to write object to stream " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * method which reads packages which are received through the input stream.
	 *
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object readPackage() throws ClassNotFoundException, IOException {
		if (isConnected()) {
			Object object = input.readObject();
			return object;
		}
		return "No Connection!";
	}

	/**
	 * Method which closes all connection for this client and closes the input
	 * and output streams.
	 */
	public void closeConnection() {
		if (output != null && input != null && socket != null) {
			try {
				output.close();
				input.close();
				socket.close();
				server.logToConsole("Connection with client " + username + " has been terminated");
			} catch (IOException e) {
				server.logToConsole("Failed to properly shut down connection");
			}
		}
	}

	public void run() {
		while (connected && !socket.isClosed()) {
			try {
				Object object = input.readObject();
				populateInbox(object);
				if (chatInbox.size() > 0) {
					packageManager.handleObject(sendIncoming(), this);
				}
			} catch (ClassNotFoundException | IOException e) {
				if (connected) {
					server.logToConsole("Failed to read package from client :" + username);
					e.printStackTrace();
					clientManager.disconnectClient2(this);
					closeConnection();
					break;
				}
				connected = false;
			}
		}
	}
	public void setProfilePic(byte[] profilePicture) {
		this.profilePic = profilePicture;

	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnection(boolean status) {
		this.connected = status;
	}

	public Socket getSocket() {
		return socket;
	}

	public SocketAddress getSocketAddress() {
		return address;
	}

	public int getID() {
		return uniqueID;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponse() {
		return response;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPlayerName(String name){
		this.name = name;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public void setLevel(int level){
		this.level = level;
	}

	public void setAge(int age){
		this.age = age;
	}

	public final String getPlayerName() {
		return name;
	}

	public final String getLocation() {
		return location;
	}

	public final byte[] getProfilePic() {
		return profilePic;
	}

	public final int getLevel() {
		return level;
	}

	public final int getAge() {
		return age;
	}

	public String getUsername() {
		return username;
	}

	public LinkedList<Object> getInbox() {
		return chatInbox;
	}

	public Object ObjectPop(int index) {
		if (chatInbox.isEmpty())
			return null;
		return chatInbox.remove(index);
	}

	public Object getObject(int index) {
		return chatInbox.get(index);
	}

	public enum Status {
		PLAYING, IDLE, WAITING, SEARCHING, DISCONNECTED,
	}


}
