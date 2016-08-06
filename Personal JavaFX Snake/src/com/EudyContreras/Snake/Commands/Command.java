package com.EudyContreras.Snake.Commands;

import java.io.Serializable;

public class Command implements Serializable {

	private static final long serialVersionUID = 1L;


	public enum CommandID{

		startMatch,
		updateCount,
		disconnect,

	}
}