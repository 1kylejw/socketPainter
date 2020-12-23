package SocketPainter;

public class Message implements java.io.Serializable {

	private String name;
	private String s;

	public Message() {

	}

	public Message(String name, String s) {
		this.name = name;
		this.s = s;
	}

	public String getName() {
		return name;
	}

	public String getS() {
		return s;
	}

}
