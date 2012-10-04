/*
 * How to Read an XML document on Android
 * 
 * Author: Natthapon Pinyo
 * http://www.iamnbty.com
 * October 4, 2012
 */
package com.iamnbty.android.reader.xml;

public class VerbalItem {

	private int id;
	private String message;
	private String author;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
}