package http;

public class HTTPMessageParseException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4026789131599121093L;
	public HTTPMessageParseException() {
		super("Message could not parse this bytestream");
	}
	public HTTPMessageParseException(String msg){
		super(msg);
	}
}
