package exceptions;

public class SoldOutException extends Exception {

	public SoldOutException() {
	}

	public SoldOutException(String message) {
		super(message);
	}
}
