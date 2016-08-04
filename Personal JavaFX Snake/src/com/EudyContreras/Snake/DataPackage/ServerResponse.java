package com.EudyContreras.Snake.DataPackage;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.LinkedList;
/**
 * This class serves as a package containing different types of information that
 * that can be send as packages. The infopack can contain a list containing a group
 * a receiver id, a message, an image, a sender ID, and byte array of data, and an amount.
 * @author Eudy Contreras
 *
 */

public class ServerResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private LinkedList<String> group;
	private String receiver;
	private String message;
	private Double command;
	private BufferedImage image;
	private String imageName = "";
	private String ID;
	private boolean isImage = false;
	private byte[] data;
	private int amount;

	public ServerResponse(String ID, String receiver, LinkedList<String> group, String message, Double command, BufferedImage image){
		this.ID = ID;
		this.receiver = receiver;
		this.group = group;
		this.command = command;
		this.message = message;
		this.image = image;
	}
	public ServerResponse(String ID, LinkedList<String> group, String message, Double command, BufferedImage image){
		this.ID = ID;
		this.group = group;
		this.command = command;
		this.message = message;
		this.image = image;
	}
	public ServerResponse(String ID, String receiver, LinkedList<String> group, String message, Double command){
		this.ID = ID;
		this.group = group;
		this.command = command;
		this.message = message;
		this.receiver = receiver;
	}
	public ServerResponse(String ID, String receiver, String message, Double command, BufferedImage image){
		this.ID = ID;
		this.receiver = receiver;
		this.message = message;
		this.image = image;
		this.command = command;
	}
	public ServerResponse(String ID, String receiver, LinkedList<String> group, Double command, byte[] data){
		this.ID = ID;
		this.receiver = receiver;
		this.group = group;
		this.command = command;
		this.data = data;
	}
	public ServerResponse(String ID, String receiver, String message, Double command,byte[] data){
		this.ID = ID;
		this.receiver = receiver;
		this.command = command;
		this.message = message;
		this.data = data;
	}
	public ServerResponse(String ID, String receiver, String message, Double command, int amount){
		this.ID = ID;
		this.command = command;
		this.receiver = receiver;
		this.message = message;
		this.amount = amount;
	}
	public ServerResponse(String ID, String receiver, String message, Double command){
		this.ID = ID;
		this.command = command;
		this.receiver = receiver;
		this.message = message;
	}
	public ServerResponse(String ID,  Double command, byte[] data, String imageName, boolean isImage){
		this.ID = ID;
		this.command = command;
		this.data = data;
		this.imageName = imageName;
		this.isImage = isImage;
	}
	public ServerResponse(String ID,  Double command, byte[] data){
		this.ID = ID;
		this.command = command;
		this.data = data;
	}
	public ServerResponse(String ID, String message, Double command){
		this.ID = ID;
		this.command = command;
		this.message = message;
	}
	public String getID(){
		return ID;
	}
	public String getMessage(){
		return message;
	}

	public boolean getImageStatus(){
		return isImage;
	}

	public String getImageName(){
		return imageName;
	}

	public BufferedImage getImage(){
		return image;
	}
	public String getReceiver(){
		return receiver;
	}
	public LinkedList<String> getGroup(){
		return group;
	}
	public Double getCommand(){
		return command;
	}
	public int getAmount(){
		return amount;
	}
	public byte[] getData(){
		return data;
	}
}