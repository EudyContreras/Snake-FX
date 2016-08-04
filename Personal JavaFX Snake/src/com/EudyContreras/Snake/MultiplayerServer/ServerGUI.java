package com.EudyContreras.Snake.MultiplayerServer;

public class ServerGUI {

	private ClientManager manager;
	private InfoHandler infoHandler;
	private MultiplayerServer server;


	/**
	 * Constructor which initializes the interface of the GUI. It also
	 * creates a new client manager and a new chatserver object
	 */
	public ServerGUI() {
		this.manager = new ClientManager(this);
		this.server = new MultiplayerServer(this,manager);
		this.infoHandler = new InfoHandler(this,manager,server);
	}


}
