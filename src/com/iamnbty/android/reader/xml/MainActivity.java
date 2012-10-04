/*
 * How to Read an XML document on Android
 * 
 * Author: Natthapon Pinyo
 * http://www.iamnbty.com
 * October 4, 2012
 */
package com.iamnbty.android.reader.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Context context;
	private ListView listview;
	private MyListViewAdapter myListViewAdapter;
	
	private Thread thread;
	private Handler handler;
	
	public static final int XML_RESULT_OK = 1;
	public static final int XML_RESULT_ERROR = 2;
	
	private ArrayList<VerbalItem> data;
	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Loading...");

		data = new ArrayList<VerbalItem>();
		listview = new ListView(context);
		myListViewAdapter = new MyListViewAdapter();
		listview.setAdapter(myListViewAdapter);
		
		setContentView(listview);
		
		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch ( msg.what ) {
				case XML_RESULT_OK:
					progressDialog.dismiss();
					data = (ArrayList<VerbalItem>) msg.obj;
					Toast.makeText(context, "Loaded "+data.size()+" items.", Toast.LENGTH_SHORT).show();
					myListViewAdapter.notifyDataSetChanged();
					break;
					
				case XML_RESULT_ERROR:
					progressDialog.dismiss();
					Toast.makeText(context, "Failed to read an XML document.", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		
		thread = new Thread() {
			@Override
			public void run() {
				// Define message to be sent with handler
				Message msg = new Message();
				msg.what = XML_RESULT_ERROR;
				
				// Create Sax Parser Factory to get XML Reader
				// Reader XML Document from specific Address
				SAXParserFactory spf = SAXParserFactory.newInstance();
				try {
					SAXParser sp = spf.newSAXParser();
					XMLReader xmlReader = sp.getXMLReader();
					
					MyXMLHandler myXMLHandler = new MyXMLHandler();
					
					xmlReader.setContentHandler(myXMLHandler);
					xmlReader.parse(new InputSource(new URL("http://www.iamnbty.com/android/verbal.xml").openStream()));
					
					msg.what = XML_RESULT_OK;
					msg.obj = myXMLHandler.getData();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}	
				
				handler.sendMessage(msg);
			}
		};
		progressDialog.show();
		thread.start();
	}
	
	private class MyListViewAdapter extends BaseAdapter {

		private MyListViewHolder myListViewHolder;
		
		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// Create view
			if ( convertView == null ) {
				convertView = LayoutInflater.from(context).inflate(R.layout.layout_listview_item, null);
				myListViewHolder = new MyListViewHolder();
				myListViewHolder.title = (TextView) convertView.findViewById(R.id.listview_item_title);
				myListViewHolder.description = (TextView) convertView.findViewById(R.id.listview_item_description);
				convertView.setTag(myListViewHolder);
			} else {
				myListViewHolder = (MyListViewHolder) convertView.getTag();
			}
			
			// Assign view
			myListViewHolder.title.setText(data.get(position).getMessage());
			myListViewHolder.description.setText("by "+data.get(position).getAuthor());
			
			return convertView;
		}
		
		private class MyListViewHolder {
			
			public TextView title, description;
			
		}
		
	}

}
