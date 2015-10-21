package http;

public class MyUrlException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7809304156034537397L;

	public MyUrlException(){
		super("The provided URL is invalid");
	}
	public MyUrlException(String msg){
		super(msg);
	}
}
