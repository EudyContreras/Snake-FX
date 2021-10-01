package com.EudyContreras.Snake.MultiplayerClient;

import java.io.IOException;

import com.EudyContreras.Snake.DataPackage.ServerResponse;

public class ClientGUIHandler {
	private ClientGUI GUI;
	private ChatClient client;


	public ClientGUIHandler(ClientGUI GUI, ChatClient client){
		this.GUI = GUI;
		this.client = client;
	}

	/**
	 * This method is for logging in. It begins with checking if the portfield has a value.
	 * If it does it will check so that the user enters a valid username and password. If not
	 * an error message will be displayed. If the user enters a correct username and password
	 * he or she will be connected to the server.
	 */
	public void tryConnection(){
		if (!GUI.getPortField().getText().isEmpty()) {
			if (GUI.getUserNameField().getText().length() < 2 || GUI.getUserNameField().getText().length() > 20) {
				GUI.getServerMessage().setText("please enter another username!");
			} else if (GUI.getUserPasswordField().getPassword().length < 2 || GUI.getUserPasswordField().getPassword().length > 20) {
				GUI.getServerMessage().setText("please enter another password!");
			} else {
				GUI.setUserName(GUI.getUserNameField().getText());
				GUI.setPassWord(GUI.charsToString(GUI.getUserPasswordField().getPassword()));
			//	GUI.userInformation = userName + "," + GUI.passWord;
				client.setPort(Integer.valueOf(GUI.getPortField().getText()));
				client.setUserInformation(GUI.userInformation);
				client.setUserName(GUI.getUserName());
				try {
					client.connect();
					client.sendPackage(new ServerResponse(GUI.getUserName(), GUI.getPassWord(), "User information", 4.0));
				} catch (IOException e) {
					GUI.getServerMessageLabel().setText("Server offline !");
					client.showEvent(
							"Unable to achieve client to server connection, Please try again at a later time!");
				}

				GUI.initializePrivateChat();
			}
		}
	}

	/**
	 * This method formats a path to a compatible one
	 * and returns it as a string
	 * @param path
	 * @return
	 */
	public static String pathFormatter(String path){
		String compatiblePath = path;
		char[] characters = new char[path.length()];
		for(int i = 0; i<characters.length; i++){
			characters[i] = path.charAt(i);
		}
		for(int r = 0; r<characters.length; r++){
			if(characters[r] == '\\'){
				characters[r] = '/';
			}
		}
		compatiblePath = String.valueOf(characters);
		return 	compatiblePath ;
	}

	public String setImageName(String path) {
		String imageName = "";
		for(int i = 0; i<path.length(); i++){
			if(path.charAt(i) == '\\'){
				imageName = "";
			} else {
				imageName += path.charAt(i);
			}
		}
		return imageName;
	}
}
