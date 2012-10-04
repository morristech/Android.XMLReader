/*
 * How to Read an XML document on Android
 * 
 * Author: Natthapon Pinyo
 * http://www.iamnbty.com
 * October 4, 2012
 */
package com.iamnbty.android.reader.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLHandler extends DefaultHandler {

	private ArrayList<VerbalItem> data;
	private VerbalItem temp;
	private String currentValue;
	
	public MyXMLHandler() {
		data = new ArrayList<VerbalItem>();
		temp = null;
		currentValue = null;
	}
	
	public ArrayList<VerbalItem> getData() {
		return this.data;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch, start, length);
		currentValue = currentValue.trim();
		super.characters(ch, start, length);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ( localName.equalsIgnoreCase("verbal") ) {
			temp = new VerbalItem();
			temp.setId(Integer.parseInt(attributes.getValue("id")));
		}
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ( localName.equalsIgnoreCase("message") ) {
			temp.setMessage(currentValue);
		} else if ( localName.equalsIgnoreCase("author") ) {
			temp.setAuthor(currentValue);
		} else if ( localName.equalsIgnoreCase("verbal") ) {
			data.add(temp);
		}
		super.endElement(uri, localName, qName);
	}
	
}
