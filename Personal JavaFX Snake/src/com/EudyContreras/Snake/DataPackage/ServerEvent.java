package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

import com.EudyContreras.Snake.Commands.ServerCommand;

import javafx.geometry.Point2D;

public class ServerEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private ServerCommand command;
	private Point2D coordinates;
	private String message;
	private String playerID;
	private String objectID;
	byte[] data;

	public ServerEvent(ServerCommand command, String playerID) {
		super();
		this.command = command;
		this.playerID = playerID;
	}

	public ServerEvent(ServerCommand command, String message, String playerID) {
		super();
		this.command = command;
		this.message = message;
		this.playerID = playerID;
	}

	public ServerEvent(ServerCommand command) {
		super();
		this.command = command;
	}

	public ServerCommand getCommand() {
		return command;
	}

	public void setCommand(ServerCommand command) {
		this.command = command;
	}

	public Point2D getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point2D coordinates) {
		this.coordinates = coordinates;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setID(String iD) {
		objectID = iD;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}