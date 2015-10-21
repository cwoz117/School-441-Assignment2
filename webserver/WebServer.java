package webserver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WebServer implements IWeb{
	private ServerSocket sock;
	private Map<String, File> webDB;
	
	public WebServer(int port){
		try {
			sock = new ServerSocket(port);
		
			webDB = new HashMap<String, File>();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		try {
			while(true){
				System.out.println("Server started");
				Socket s = sock.accept();
				RequestHandler rh = new RequestHandler(s, webDB);

				rh.run();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
