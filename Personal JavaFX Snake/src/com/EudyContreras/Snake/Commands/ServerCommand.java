package com.EudyContreras.Snake.Commands;

import java.io.Serializable;

public enum ServerCommand implements Serializable {

	DISCONNECT,
	RESEND,
	REQUEST_FAILED,
	REQUEST_SUCESS,
	REQUEST_DECLINED,
	REQUEST_ACCEPTED

}