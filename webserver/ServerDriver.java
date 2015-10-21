package webserver;

import java.util.Scanner;

public class ServerDriver {
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		WebServer serv = new WebServer(64483);
		String answer = "0";
		
		serv.start();
		
		System.out.println("The server is now Running, press ENTER to quit");
		if (in.hasNextLine()){
			serv.stop();
			System.out.println("Goodbye");
		}
	}
}
