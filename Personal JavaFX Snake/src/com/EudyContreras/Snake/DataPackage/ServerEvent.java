package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

import com.EudyContreras.Snake.Commands.ServerCommand;

public class ServerEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private ServerCommand command;
	private String message = "";

	public ServerEvent(ServerCommand command, String message) {
		super();
		this.command = command;
		this.message = message;
	}
	public ServerEvent(ServerCommand command) {
		super();
		this.command = command;
	}
	public ServerCommand getCommand() {
		return command;
	}
	public String getMessage(){
		return message;
	}

	public void setCommand(ServerCommand command) {
		this.command = command;
	}

}