package com.EudyContreras.Snake.MultiplayerServer;



import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the server class which is in charge of starting a server
 * which is going to handle all clients.
 * @author Eudy Contreras
 *
 */
public class MultiplayerServer extends Thread {

	protected static int UNIQUE_ID = 1000;

	private Thread thread;
	private ServerGUI GUI;
	private Boolean connected;
	private ClientManager manager;
	private int port;

	public MultiplayerServer() {
	}
	/**
	 * Constructor which takes the server GUI, and the client manager as
	 * argument.
	 *
	 * @param GUI
	 * @param manager
	 */
	public MultiplayerServer(ServerGUI GUI, ClientManager manager) {
		this.manager = manager;
		this.connected = true;
		this.GUI = GUI;
	}
	/**
	 * this method starts the server thread.
	 * @param port: port number which the server will used for connection
	 */
	public void startThread(int port){
		if(thread==null){
			this.thread = new Thread(this);
			this.port = port;
			this.thread.start();
		}
	}
	/**
	 * This method stops the server thread.
	 */
	public void stopThread(){
		if(thread!=null){
			this.thread.interrupt();
		}
	}
	/**
	 * run method in which connections are accepted and
	 * clients are created
	 */
	public void run() {
		Socket clientSocket = null;
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			logToConsole("Server has been started");
			logToConsole("Local server address: " + InetAddress.getLocalHost().toString());

			while (getConnected()) {
				try {
					clientSocket = serverSocket.accept();
					UNIQUE_ID++;
					manager.addIncominglient(new MultiplayerClient(this, manager,clientSocket, UNIQUE_ID));
				} catch (IOException e) {
					logToConsole("Failed to establish a connection " + e.getMessage());
					e.printStackTrace();
					if (clientSocket != null) {
						clientSocket.close();
					}
					break;
				}
			}
			thread = null;
		} catch (IOException e) {
			logToConsole("Unable to initialize the server socket " + e.getMessage());
			logToConsole("Current port is already in use!");
			thread = null;
			setConnected(false);
		}

	}
	/**
	 * This method logs events to the console
	 * @param event
	 */
	public synchronized void logToConsole(String event){
		logToConsole("SERVER CONSOLE: " + event);
	}

	public synchronized Boolean getConnected() {
		return connected;
	}
	/**
	 * Set a the status of the server connection
	 * @param connected
	 */
	public synchronized void setConnected(Boolean connected) {
		this.connected = connected;
	}
	/**
	 * Returns the initialized instance of the client manager
	 * @return
	 */
	public synchronized ClientManager getManager() {
		return manager;
	}
	/**
	 * Returns the initialized instance of the GUI
	 * @return
	 */
	public ServerGUI getGUI() {
		return GUI;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}


}