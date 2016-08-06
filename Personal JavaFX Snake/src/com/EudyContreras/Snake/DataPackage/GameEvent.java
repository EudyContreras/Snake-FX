package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

import com.EudyContreras.Snake.FrameWork.PlayerMovement;

import javafx.geometry.Point2D;

public class GameEvent implements Serializable {

	private static final long serialVersionUID = 1L;


	private PlayerMovement direction;
	private Integer command;
	private Point2D coordinates;
	private String message;
	private String playerID;
	private String objectID;
	private double x;
	private double y;
	private double r;
	private byte[] data;

	public GameEvent(Integer command, String playerID, double x, double y, double r) {
		super();
		this.command = command;
		this.playerID = playerID;
		this.x = x;
		this.y = y;
		this.r = r;
	}

	public GameEvent(Integer command, String message, String playerID) {
		super();
		this.command = command;
		this.message = message;
		this.playerID = playerID;
	}

	public GameEvent(Integer command) {
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