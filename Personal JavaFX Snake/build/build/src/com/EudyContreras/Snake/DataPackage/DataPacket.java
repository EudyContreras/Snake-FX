package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

import javafx.geometry.Point2D;

public class DataPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	Integer command;
	Point2D coordinates;
	String message;
	String playerID;
	String objectID;
	double x;
	double y;
	double r;
	byte[] data;

	public DataPacket(Integer command, String playerID, double x, double y, double r) {
		super();
		this.command = command;
		this.playerID = playerID;
		this.x = x;
		this.y = y;
		this.r = r;
	}

	public DataPacket(Integer command, String message, String playerID) {
		super();
		this.command = command;
		this.message = message;
		this.playerID = playerID;
	}

	public DataPacket(Integer command) {
		super();
		this.command = command;
	}

	public Integer getCommand() {
		return command;
	}

	public void setCommand(Integer command) {
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

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}