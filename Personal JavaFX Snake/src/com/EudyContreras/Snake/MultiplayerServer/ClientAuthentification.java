package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;

import com.EudyContreras.Snake.DataPackage.ServerResponse;
/**
 * Class used to check if the user has previously logged into the system. this class
 * will determined the appropriate action for upcoming clients.
 * @author Eudy Contreras
 *
 */
public class ClientAuthentification {
	private ClientManager manager;

	public ClientAuthentification(ClientManager manager){
		this.manager = manager;
	}
	/**
	 * Method that checks what to do with the connecting client
	 * according to size of different lists
	 * @param client
	 */
	public void performInstanceCheck(MultiplayerClient client) {
		if (manager.getOffline_clients().size()<= 0 && manager.getOnline_clients().size()<=0){
			manager.getOnline_clients().put(client.getUsername(),client);
			manager.getOnline_user_info().put(client.getPassword(),client.getUsername());
			client.start();
			client.setResponse("Server: Welcome to the chat "+client.getUsername());//
			initializeClient(client,"new","Welcome to communication! Please remember your details for next login!");
			manager.addToDatabase(client);
		}
		else {
			checkUpcoming(client);
			}
	}
	/**
	 * Method that checks whether the upcoming client is a new user or a returning user
	 * this method also assures that every client is unique by making sure that no client
	 * has the same user name. This method processes all incoming clients besides the first one
	 * and it registers the client. if the registration does not succeed the client will then
	 * be disconnected an the client will be told why.
	 * @param client
	 */
	public void checkUpcoming(MultiplayerClient client){
		if (manager.getOnline_clients().size() > 0 && manager.getOffline_clients().size() <= 0) {
			if (manager.getOnline_user_info().containsValue(client.getUsername())) {
				client.setResponse("Sorry! That username is taken!!");
				disconnectClient(client);
			} else {
				manager.getOnline_clients().put(client.getUsername(),client);
				manager.getOnline_user_info().put(client.getPassword(),client.getUsername());
				client.start();
				client.setResponse("Server: Welcome to the chat "+client.getUsername());//
				initializeClient(client,"new","Welcome to communication! Please remember your details for next login!");
				manager.addToDatabase(client);
			}
		}
		else if(manager.getOnline_clients().size()>0 && manager.getOffline_clients().size()>0){
			if (manager.getOnline_user_info().containsValue(client.getUsername()) && !manager.getOffline_user_info().containsValue(client.getUsername())) {
				client.setResponse("Sorry! That username is taken!!");
				disconnectClient(client);
			}
			else if (!manager.getOnline_user_info().containsValue(client.getUsername()) && manager.getOffline_user_info().containsValue(client.getUsername())) {
				if(manager.getOffline_clients().get(client.getUsername()).getPassword().contentEquals(client.getPassword())){
					manager.getOnline_clients().put(client.getUsername(),client);
					manager.getOnline_user_info().put(client.getPassword(),client.getUsername());
					client.start();
					client.setResponse("Server: Welcome back to the chat "+client.getUsername());//
					processReturningClient(client);
					initializeClient(client,"old","Welcome back to the chat! You have :  queued messages!");
				}
				else{
					client.setResponse("Sorry! Wrong password!!");
					disconnectClient(client);
				}
			}
		}
		else if(manager.getOnline_clients().size()<=0 && manager.getOffline_clients().size()>0){
			if (!manager.getOffline_user_info().containsValue(client.getUsername())) {
				manager.getOnline_clients().put(client.getUsername(),client);
				manager.getOnline_user_info().put(client.getPassword(),client.getUsername());
				client.start();
				client.setResponse("Server: Welcome to the chat "+client.getUsername());//
				initializeClient(client,"new","Welcome to communication! Please remember your details for next login!");
				manager.addToDatabase(client);
			}
			else if (manager.getOffline_user_info().containsValue(client.getUsername())) {
				if (manager.getOffline_clients().get(client.getUsername()).getPassword().contentEquals(client.getPassword())){
					manager.getOnline_clients().put(client.getUsername(),client);
					manager.getOnline_user_info().put(client.getPassword(),client.getUsername());
					client.start();
					client.setResponse("Server: Welcome back to the chat "+client.getUsername());//
					processReturningClient(client);
					initializeClient(client,"old","Welcome back to the chat! You have queued messages!");
				}
				else{
					client.setResponse("Sorry! Wrong password!!");
					disconnectClient(client);
				}
			}
		}
	}
	/**
	 * Method used for processing client have previously been connected
	 * to the server.
	 * @param client
	 */
	public void processReturningClient(MultiplayerClient client){
		manager.getOffline_user_info().remove(client.getPassword());

		manager.getOffline_clients().remove(client.getUsername());

	}
	/**
	 * This method initializes a client and sends that client
	 * information containing who is connected to the server and who isn't.
	 *
	 * @param client
	 * @param status
	 * @param scenario
	 */
	public void initializeClient(MultiplayerClient client, String status, String scenario){
		client.sendPackage(new ServerResponse(status, scenario ,client.getResponse(), 7.0));
		client.sendPackage(new ServerResponse(client.getUsername(),"All users",manager.getAllUsers(),client.getUsername(),1.0));
		client.sendPackage(new ServerResponse("Server: ", "All users", manager.getDisconnectedUsers(),"",10.0));
		try {
			manager.sendWelcomeMessage(client,"Server: " + client.getUsername() + " has joined the chat");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		manager.confirmOnlineUsers(client.getUsername());
		try {
			manager.sendPackage(new ServerResponse(client.getUsername(),"All users",manager.getAllUsers(),client.getUsername(),1.0));
			manager.sendPackage(new ServerResponse("Server: ", "All users", manager.getDisconnectedUsers(),"",10.0));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * This method disconnects a specific client from the server.
	 * this method is called when the registration fails.
	 * @param client
	 */
	public void disconnectClient(MultiplayerClient client){
		manager.getOnline_clients().remove(client);
		client.sendPackage(new ServerResponse("Server", client.getUsername(),client.getResponse(), 6.0));
		Double closingCommand = 1.0;
		client.sendPackage(closingCommand);
		client.setConnection(false);
		client.closeConnection();
		client = null;
	}

}
