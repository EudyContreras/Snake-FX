package com.EudyContreras.Snake.PlayRoomHub;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MailItem {

	private boolean selected;
	private String sender;
	private String header;
	private String message;
	private Object attachment;
	private LocalDateTime date;
	private DateTimeFormatter format;

	public MailItem() {
		super();
		format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	}

	public MailItem(String sender, String header) {
		super();
		this.format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.sender = sender;
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	public String getDate() {
		return format.format(date);
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
		this.date.format(format);
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	public void selected(boolean state) {
		this.selected = state;
	}

	public boolean isSelected() {
		return selected;
	}

}
