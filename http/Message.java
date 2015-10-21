package http;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class Message implements Serializable{

	private static final long serialVersionUID = -8712240486510113552L;
	public static final int TEXT = 0;
	public static final int PDF = 1;
	public static final int JPEG = 2;
	public static final int GIF = 3;

	private byte[] entity;
	private String[] messageInfo = new String[3];
	private List<ArrayList<String>> headerLines;
	
	/**
	 * Creates a message in the appropriate format for an HTTP message
	 * over a TCP/IP network connection.
	 * 
	 * @param messgeType : String (GET, POST, etc..)
	 * @param contentLocation : String (/index.html, /data/file.pdf etc..)
	 * @param contentType : String (text/http, application/pdf etc..)
	 */
	public Message(String messgeType, String contentLocation, String contentType){
		this.messageInfo[0] = messgeType; 	// Request Type
		this.messageInfo[1] = contentLocation;	// Content URL
		this.messageInfo[2] = contentType;	// Content Type

		headerLines = new ArrayList<ArrayList<String>>();
		entity = new byte[0];
	}
	
	/**
	 * This constructor is for receiving http responses from the server.
	 * When creating messages the other constructor should be used.
	 * 
	 * @param unparsedData : String (from a TCP/IP network connection)
	 */
	public Message(String unparsedData){
		headerLines = new ArrayList<ArrayList<String>>();
		String[] headFromEntity = unparsedData.split("\n\n", 2);
		String[] msgInfo = headFromEntity[0].split("\n");
		String[] status = msgInfo[0].split(" ");
		
		this.messageInfo[0]= status[0];					// Version
		this.messageInfo[1] = status[1];				// status code
		this.messageInfo[2] = msgInfo[0].substring(2);	// phrase
		
		for (int i = 1; i < msgInfo.length; i++){
			status = msgInfo[i].split(" ");
			headerLines.add(new ArrayList<String>());
			headerLines.get(headerLines.size()-1).add(status[0]);
			headerLines.get(headerLines.size()-1).add(msgInfo[i].substring(status[0].length()));
		}
		
		entity = headFromEntity[1].getBytes();
	}

	// Standard Getters
	public byte[] getEntity(){return entity;}
	public String getTypeOfMessage(){return messageInfo[0];}
	public String getContentStatus(){return messageInfo[1];}
	public String getPhraseType(){return messageInfo[2];}
	
	public void setEntity(byte[] webPage){
		this.entity = new byte[webPage.length];
		for (int i = 0; i < webPage.length; i++){
			entity[i] = webPage[i];
		}
	}
	/**
	 * Entity type allows us to identify what kind of content
	 * is in the entity field of a server response. If its 
	 * HTML/Image/GIF/PDF. More entity fields exist, and 
	 * should be included but this fits the assignment req's
	 * 
	 * @return returns the integer equivalent as defined by 
	 * 		   static variables. or -1 if content-type is not found.
	 */
	
	public int getEntityType(){
		Iterator<ArrayList<String>> i = headerLines.iterator();
		while(i.hasNext()){
			ArrayList<String> headerLines = i.next();
			String headerName = headerLines.get(0);
			if (headerName.matches("Content-Type:")){
				String headerData = headerLines.get(1);
				if(headerData.contains("text")){
					return TEXT;
				} else if (headerData.contains("pdf")){
					return PDF;
				} else if (headerData.contains("gif")){
					return GIF;
				} else if (headerData.contains("jpeg")){
					return JPEG;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Returns the long value of the date of the "last modified" header line
	 * in an http server response. As per course assignment req's
	 * 
	 * @return last modified date : long
	 */
	public long getLastModified(){
		Date lastMod;
		Iterator<ArrayList<String>> i = headerLines.iterator();
		while(i.hasNext()){
			ArrayList<String> headerLines = i.next();
			String headerName = headerLines.get(0);
			if (headerName.matches("Last-Modified:")){
				String headerData = headerLines.get(1);
				SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
				try {
					lastMod = dateFormat.parse(headerData.trim());
					return lastMod.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return (long) -1;
	}
	
	/**
	 * Overloaded getLastModified will return a string of the last-modified
	 * date. As the course requires a long output for the assignment the
	 * best situation was to overload it.
	 * 
	 * The parameter "superflousOverloadRequirement" is just that. Superflous.
	 * the function makes no use of the value, it is there to allow for 
	 * overriding the function.
	 * 
	 * @param superflousOverloadRequirement : boolean
	 * @return last-modified: date, in proper formatting : String
	 */
	public String getLastModified(boolean superflousOverloadRequirement){
		Date lastMod;
		SimpleDateFormat dateFormat;
		Iterator<ArrayList<String>> i = headerLines.iterator();
		while(i.hasNext()){
			ArrayList<String> headerLines = i.next();
			String headerName = headerLines.get(0);
			if (headerName.matches("Last-Modified:")){
				String headerData = headerLines.get(1);
				dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
				try {
					lastMod = dateFormat.parse(headerData.trim());
					return dateFormat.format(lastMod);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * used to add header lines to an http message.
	 * 
	 * @param headerFieldName : String
	 * @param value : String
	 */
	public void addHeader(String headerFieldName, String value){
		headerLines.add(new ArrayList<String>());
		headerLines.get(headerLines.size()-1).add(headerFieldName);
		headerLines.get(headerLines.size()-1).add(value);
	}
	
	/**
	 * Will print out the http messages, encluding entity data. Used for
	 * debugging.
	 */
	public String toString(){
		String msg = "";
		msg += messageInfo[0] + " " + messageInfo[1] + " " + messageInfo[2] + "\n";
		if (!headerLines.isEmpty()){
			for (int i = 0; i < headerLines.size(); i++){
				msg += headerLines.get(i).get(0) + " " + headerLines.get(i).get(1) + "\n";
			}
		}
		msg += "\n";
		if (entity.length > 0){
				msg += entity;
		}
		return msg;
	}
}