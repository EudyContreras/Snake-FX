package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;

public class InfoHandler{
	private ClientManager manager;
	private MultiplayerServer server;
	private ServerGUI GUI;

	public InfoHandler(ServerGUI GUI, ClientManager manager, MultiplayerServer server){
		this.GUI = GUI;
		this.manager = manager;
		this.server = server;
	}
	public void startUp(){
		if(!GUI.getPortField().getText().isEmpty()){
			try{
				GUI.setPort(Integer.valueOf(GUI.getPortField().getText()));
				server.startThread(Integer.valueOf(GUI.getPortField().getText()));
			}
			catch(NumberFormatException e){
				server.logToConsole("Port number is invalid!");
			}
		}
		else{
			server.logToConsole("Port field is empty!");
		}
	}
	/**
	 * This method shows which online users that are in the servers database on the log
	 */
	public void showOnlineUsers(){
		GUI.addSpace();
		GUI.showOnlog("All users currently online in the servers database " + manager.getOnline_clients().size());
		for(MultiplayerClient client : manager.getOnline_clients().values()){
			GUI.showOnlog("Username: "+client.getUsername() + ",  Password: " + client.getPassword() + ",  Connected: "+ client.isConnected() + ",  Inbox: " + client.getInbox().size() + ",  Queued Messages");
		}
	}
	/**
	 * This method shows which offline users that are in the servers database on the log
	 */
	public void showOfflineUsers(){
		GUI.addSpace();
		GUI.showOnlog("All users currently offline in the servers database " + manager.getOffline_clients().size());
	}
	/**
	 * This method shows all users that are in the servers database on the log
	 */
	public void showAllUsers(){
		GUI.addSpace();
		GUI.showOnlog("All users currently logged in the servers database " + manager.getAllUsers().size());
		for(int i = 0; i<manager.getAllUsers().size(); i++){
			GUI.showOnlog(manager.getAllUsers().get(i));
		}
	}
	/**
	 * Shows the information about users in the log
	 */
	public void authentificationList(){
		GUI.addSpace();
		GUI.showOnlog("Information inside online ID list: ");
		for(String values: manager.getOnline_user_info().values()){
			GUI.showOnlog("Stored ID: " + values);
		}
		GUI.addSpace();
		GUI.showOnlog("Information inside offline ID list: ");
		for(String values: manager.getOffline_user_info().values()){
			GUI.showOnlog("Stored ID: " + values);
		}
	}

	/**
	 * Method to shut down the server
	 */
	public void exit(){
		String[] options = { "Yes, please", "No, thanks" };
		int answer = DialogUtility.showConfirmationDialog(GUI.getWindow(), "Are you sure that you want to shutdown the server?", "Attention!", options);
		if (answer == DialogUtility.YES_OPTION) {
			try {
				manager.sendPackage("Server: Server is now offline, try again at a later time!");
				manager.disconnectAll();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
}
