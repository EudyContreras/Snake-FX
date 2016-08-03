package com.EudyContreras.Snake.MultiplayerClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class containing the connection made by this client.
 * @author Eudy Contreras
 *
 */
	public class ConnectionHandler extends Thread{
	private ChatClient client;
	/**
	 * constructor which initializes the output and input streams of this client.
	 */
	public ConnectionHandler(ChatClient client) {
		this.client = client;
		try {
			client.setOutput(new ObjectOutputStream(client.getClientSocket().getOutputStream()));
			client.getOutput().flush();
			client.setInput(new ObjectInputStream(client.getClientSocket().getInputStream()));

		} catch (IOException io) {
			client.showEvent("Failed to create output or input stream " + io.getMessage());
		}
	}
	/**
	 * Run method of this thread which is in charged of receiving objects and placing them in
	 * and inbox for further processing.
	 */
	public void run() {
		while (client.isConnected() && !client.getClientSocket().isClosed()) {

			Object object;
			try {
				object = client.getInput().readObject();
				populateInbox(object);
				client.handleObject(sendIncoming());
			} catch ( ClassNotFoundException | IOException e) {
				break;
			}
		}
	}
	/**
	 * Buffer used to populate the inbox of this client
	 * @param obj
	 */
	public synchronized void populateInbox(Object obj) {
		client.getInbox().addLast(obj);
		this.notifyAll();
	}
	/**
	 * Buffer used for returning and removing the content
	 * of this client's inbox
	 * @return
	 */
	public synchronized Object sendIncoming(){
		while (client.getInbox().isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return client.getInbox().removeFirst();
	}
}