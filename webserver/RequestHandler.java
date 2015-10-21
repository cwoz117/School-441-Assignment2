package webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import http.Message;

public class RequestHandler implements Runnable{
	private Map<String, File> webDB;
	private Socket soc;
	
	public RequestHandler(Socket soc, Map<String, File> m){
		this.soc = soc;
		this.webDB = m;
	}

	public void run() {
		System.out.println("I'm here");
		if (soc.isConnected()){
			try {
				soc.setSoTimeout(9000);
				// Open Input/Output streams
				BufferedReader reader = 
						new BufferedReader(
								new InputStreamReader(soc.getInputStream()));
				BufferedWriter writer = 
						new BufferedWriter(
								new OutputStreamWriter(soc.getOutputStream()));
				
				// Read in the reply
				String tmp, unparsedReply = "";
				while ((tmp = reader.readLine()) != null){
					unparsedReply += tmp + "\n";
				}
				Message received = new Message(unparsedReply);
				Message response;
				String version, status, phrase;
				// locate message, if valid
				if(received.getTypeOfMessage().equals("GET")){
					if (received.getContentStatus().equals("index.html")){
						version = "HTTP/1.1";
						status = "200";
						phrase = "OK";
						response = new Message(version, status, phrase);
						response.addHeader("Connection", "close");
						response.setEntity("this is a byteArray".getBytes());
						writer.write(response.toString());
						soc.shutdownInput();
						soc.shutdownOutput();
						soc.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
