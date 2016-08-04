package com.EudyContreras.Snake.MultiplayerClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import com.EudyContreras.Snake.DataPackage.ServerResponse;

public class PrivateChatHandler {
	private PrivateChatGUI PM;

	public PrivateChatHandler(PrivateChatGUI PM){
		this.PM = PM;
	}
	/**
	 * Populates the list of users that are online in the sidepanel.
	 * Puts a checkbox next to each of the users. When box/boxes is checked
	 * the user/users recieves message sent.
	 */
	public void populateList() {
			PM.clearPanel();
			PM.getOnlineUsers().clear();
			for(int i = 0; i<PM.getUserInterface().getClient().getOnlineUsers().size(); i++){
				PM.getOnlineUsers().add(new JCheckBox(PM.getUserInterface().getClient().getOnlineUsers().get(i)));
				JCheckBox userOnline = PM.getOnlineUsers().get(i);
				userOnline.setPreferredSize(new Dimension(20,40));
				userOnline.setHorizontalAlignment(SwingConstants.LEFT);
				userOnline.setBackground(Color.GREEN);
				for(int r = 0; r<PM.getUserInterface().getClient().getDisconnectedUser().size(); r++){
					if(userOnline.getText().contentEquals(PM.getUserInterface().getClient().getDisconnectedUser().get(r))){
						userOnline.setBackground(Color.RED);
						PM.repaint();
						PM.validate();
					}
				}
			}
			for(int i = 0; i<PM.getOnlineUsers().size(); i++){
				PM.getUserPanel().add(PM.getOnlineUsers().get(i));
			}
			PM.getUserPanel().revalidate();
			PM.validate();
	}
	/**
	 * This method will check to see who is disconnected and repaint the list accordingly.
	 * Red users are offline and green users are online
	 */
	public void checkWhoDisconnected(){
		PM.logEvent("check1");
			for(int r = 0; r<PM.getUserInterface().getClient().getDisconnectedUser().size(); r++){
				for(int i = 0; i<PM.getOnlineUsers().size(); i++){
					if(PM.getOnlineUsers().get(i).getText().contentEquals(PM.getUserInterface().getClient().getDisconnectedUser().get(r))){
						PM.getOnlineUsers().get(i).setBackground(Color.RED);
						PM.logEvent("check2");
						PM.repaint();
						PM.validate();
					}
					else{
						PM.getOnlineUsers().get(i).setBackground(Color.GREEN);
						PM.logEvent("check3");
						PM.repaint();
						PM.validate();
				}
			}
		}
	}

	/**
	 * This method sends a private chat confirmation to a user that is selected
	 */
	public void sendConfirmationToSelected(){
			for(int r = 0; r<PM.getOnlineUsers().size(); r++){
			if(PM.getOnlineUsers().get(r).isSelected()){
				ServerResponse pack = new ServerResponse(PM.getUserInterface().getUserName(),PM.getUserInterface().getClient().getOnlineUsers().get(r),PM.getUserInterface().getUserName() + ": Hi there", 3.0);
				PM.getUserInterface().getClient().sendPackage(pack);
			}
		}
	}
	/**
	 * This method sends a message to selected users
	 * @param message
	 */
	public void sendMessageToSelectedUsers(String message){
		for(int i = 0; i<PM.getUserInterface().getClient().getOnlineUsers().size(); i++){
			for(int r = 0; r<PM.getOnlineUsers().size(); r++){
			if(PM.getOnlineUsers().get(r).isSelected()){
				if(PM.getUserInterface().getClient().getOnlineUsers().get(i).contentEquals(PM.getOnlineUsers().get(r).getText())){
				ServerResponse pack = new ServerResponse(PM.getUserInterface().getUserName(),PM.getUserInterface().getClient().getOnlineUsers().get(i),PM.getUserInterface().getUserName() + ": "+message,2.0 );
				PM.getUserInterface().getClient().sendPackage(pack);
			}}
		}}
		PM.showOnGroupChat(PM.getUserInterface().getUserName() + ": "+message);
	}
	/**
	 * This method sends a message to selected users
	 * @param message
	 */
	public void sendTextToSelected(String message){
		for(int r = 0; r<PM.getOnlineUsers().size(); r++){
			if(PM.getOnlineUsers().get(r).isSelected()){
				ServerResponse pack = new ServerResponse(PM.getUserInterface().getUserName(),PM.getOnlineUsers().get(r).getText(),PM.getUserInterface().getUserName() + ": "+message,2.0 );
				PM.getUserInterface().getClient().sendPackage(pack);
			}
		}
		PM.showOnGroupChat(PM.getUserInterface().getUserName() + ": "+message);
	}

	/**
	 * This method sends an image to selected users
	 * @param image
	 */
	public void sendObjectToSelected(BufferedImage image)	{
		for(int r = 0; r<PM.getOnlineUsers().size(); r++){
			if(PM.getOnlineUsers().get(r).isSelected()){
				PM.getUserInterface().getClient().sendPrivateObject(PM.getUserInterface().getUserName(),PM.getUserInterface().getClient().getOnlineUsers().get(r),"have sent an image to you", 6.0,image);
			}
		}
		PM.showOnGroupChat(PM.getUserInterface().getUserName() + ": you have sent an image");
	}
	/**
	 * Allows or disallows sending of images and messages
	 * @param state
	 */
	public void allowSending(boolean state){
		PM.getSendImage().setEnabled(state);
		PM.getSendMessage().setEnabled(state);
	}

}
