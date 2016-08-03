package com.EudyContreras.Snake.MultiplayerClient;

import java.awt.image.BufferedImage;

import com.EudyContreras.Snake.DataPackage.InfoPack;

/**
 * This class is in charge of handling the commands received through the infopacks.
 *
 * @author Eudy
 *
 */
public class CommandHandler{
	private ChatClient client;
	private ClientGUI GUI;

	/**
	 * CommandHandler constructor
	 * @param client
	 * @param GUI
	 */
	public CommandHandler(ChatClient client, ClientGUI GUI){
		this.client = client;
		this.GUI = GUI;
	}

	/**
	 * Handles commands fetched through an infopack.
	 * this method will read the value of a command and
	 * it will then perform an action according to that command.
	 * @param pack
	 */
	public void handleServerCommand(InfoPack pack){
		if(pack.getCommand() == 1.0){
			client.setOnlineUsers(pack.getGroup());
			for(int i = 0; i<client.getOnlineUsers().size(); i++){
				if(client.getOnlineUsers().get(i).contentEquals(client.getUserName())){
					client.getOnlineUsers().remove(i);
				}
			}
			client.changeNotification();
			client.notifyObservers(pack.getCommand());
		}
		else if(pack.getCommand() == 2.0){
			client.getGUI().showOnChat(pack.getReceiver()+" has left the chat!");
			client.setDisconnectedUser(pack.getGroup());
			client.changeNotification();
			client.notifyObservers(pack.getCommand());
		}
		else if(pack.getCommand() == 3.0){
			client.setOnlineUsers(pack.getGroup());
		}
		else if(pack.getCommand() == 4.0){
			GUI.privateChatNotification(pack);
			GUI.getOnlineUsers().showOnGroupChat(pack.getMessage());
		}
		else if(pack.getCommand() == 5.0){
			String text = pack.getMessage();
			String sender = pack.getID();
			BufferedImage image;
			if (pack.getImage() != null) {
				image = pack.getImage();
				client.setReceivedPicture(image);
				GUI.showPrivateChat();
				GUI.getOnlineUsers().showOnGroupChat(pack.getMessage());
			}
			if (!pack.getMessage().isEmpty()) {
				text = pack.getMessage();
				client.setReceivedMessage(sender + ": " + text);
				GUI.showNotificationMessage(pack);
				GUI.getOnlineUsers().showOnGroupChat(text);
			}
			if (pack.getGroup() != null) {
				client.setGroupOfUsers(pack.getGroup());
			}
		}
		else if(pack.getCommand() == 6.0){
			GUI.getServerMessageLabel().setText(pack.getMessage());
		}
		else if(pack.getCommand() == 7.0){
			GUI.setInboxSize(pack.getAmount());
			GUI.closeLogInWindow(pack.getID(),pack.getReceiver());
			GUI.showOnChat(pack.getMessage());
		}
		else if(pack.getCommand() == 8.0){
			client.setReceivedPicture(client.byteToImage(pack.getData()));
			GUI.confirmImage(client.byteToImage(pack.getData()));
			if(!pack.getID().contentEquals(client.getUserName()))
			GUI.showOnChat(pack.getID() + " sent an image!");
		}
		else if(pack.getCommand() == 9.0){
			client.setReceivedPicture(client.byteToImage(pack.getData()));
			GUI.getOnlineUsers().newImageDialog(pack.getID(), client.byteToImage(pack.getData()));
			if(!pack.getID().contentEquals(client.getUserName()))
			GUI.getOnlineUsers().showOnGroupChat(pack.getID() + " sent an image!");
		}
		else if(pack.getCommand() == 10.0){
			client.setDisconnectedUser(pack.getGroup());
			client.changeNotification();
			client.notifyObservers(pack.getCommand());
		}
		else if(pack.getCommand() == 11.0){
			String text = pack.getMessage();
			if (!pack.getMessage().isEmpty()) {
				text = pack.getMessage();
				GUI.getOnlineUsers().showOnGroupChat(text);
			}
		}
	}
}
