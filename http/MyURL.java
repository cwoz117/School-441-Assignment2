package http;

import java.io.Serializable;

/**
 * The data for a URL is parsed here. Since we often just need either the
 * hostname, port number, or content its easier to split them up here.
 * 
 * The toString() function will put it back together again exactly as it
 * was received, with or without the port number.
 * 
 * @author Chris Wozniak
 *
 */
public class MyURL implements Serializable{


	private static final long serialVersionUID = -744815455035889858L;
	private String host;
	private int port;
	private String content;
	private String contentType;
	private boolean wasPortSpecified;
	
	/**
	 * Takes a website URL and parses it into the appropriate variables.
	 * @param unparsedURL
	 */
	public MyURL(String unparsedURL){
	// Separate content from server
		String hostContent[] = unparsedURL.split("/", 2);
		if (hostContent.length == 1){
			this.content = "/index.html";
		} else {
			this.content = "/" + hostContent[1];
		}
		
	// Separate Port from DNS
		String[] hostPort = hostContent[0].split(":", 2);
		this.host = hostPort[0];
		if (hostPort.length == 1){
			this.port = 80;
			wasPortSpecified = false;
		} else {
			this.wasPortSpecified = true;
			this.port = Integer.parseInt(hostPort[1].trim());
		}
	
	// Determine content type for HTTP request version
		if (content.contains(".gif")){
			setContentType("image/gif");
		}else if(content.contains(".pdf")){
			setContentType("application/pdf");
		}else{
			setContentType("text/html");
		}
	}
	
	// Standard getters
	public String getHost() {return host;}
	public String getContent() {return content;}
	public String getContentType() {return contentType;}
	public int getPort() {return port;}

	// Standard setters
	public void setHost(String host) {this.host = host;}
	public void setPort(int port) {this.port = port;}
	public void setContent(String content) {this.content = content;}
	public void setContentType(String contentType) {this.contentType = contentType;}
	
	public boolean equals(Object obj){
		if (!(obj instanceof MyURL)){
			return false;
		}
		MyURL url = (MyURL) obj;
		if (this.toString().equals(url.toString())){
			return true;
		} else{
			return false;
		}
	}
	public String toString(){
		String url = "";
		if (wasPortSpecified){
			url = host + ":"+ Integer.toString(port) + content;
		}else{
			url = host + content;
		}
		return url;
	}


	
}
