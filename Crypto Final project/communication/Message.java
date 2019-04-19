package communication;

import java.io.Serializable;

public class Message implements Serializable {

	private byte[] message;
	private byte[] key;
	private byte[] iv;

	public Message(byte[] message, byte[] key, byte[] iv) {
		this.message = message;
		this.key = key;
		this.iv = iv;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6759911464169257302L;

	public byte[] getKey() {
		return key;
	}
	public byte[] getMessage() {
		return message;
	}
	public byte[] getIv() {
		return iv;
	}

}
