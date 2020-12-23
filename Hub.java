package SocketPainter;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Hub {
	static ArrayList<sPack> als = new ArrayList<>();
	static Message[] chat = new Message[4];
	static ArrayList<PaintingPrimitive> primitives = new ArrayList<>();

	public static void main(String[] args) throws IOException, EOFException {
		ServerSocket ss = new ServerSocket(7000);

		while (true) {
			Socket s = null;

			try {
				s = ss.accept();

				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

				sPack sp = new sPack(s, ois, oos);
				als.add(sp);

				Thread t = new Handler(sp);

				t.start();

			} catch (Exception e) {
				s.close();
				e.printStackTrace();
			}

		}
	}
	
	public static void clear() {
		primitives.clear();
		for (sPack sp : als) {
			try {
				sp.getOos().writeObject(new String("clear"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class sPack {

	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public sPack(Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
		this.s = s;
		this.ois = ois;
		this.setOos(oos);
	}

	public Socket getS() {
		return s;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
}

class Handler extends Thread {

	private sPack sp;

	public Handler(sPack sp) {
		this.sp = sp;
	}

	@Override
	public void run() {

		for (Message e : Hub.chat)
			try {
				sp.getOos().writeObject(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		for (PaintingPrimitive e : Hub.primitives)
			try {
				sp.getOos().writeObject(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		try {
			while (true) {
				
				Object x = (Object) sp.getOis().readObject();
				
				if (x instanceof PaintingPrimitive) {
					serverPrimitiveManger(sp.getS(), (PaintingPrimitive) x);
				}
				if (x instanceof Message) {
					serverChatManager((Message) x);
				}
				if (x instanceof String && x.equals("clear")) {
					Hub.clear();
				}
				if (x == null) {
					Hub.als.remove(sp);
					sp.getS().shutdownInput();
					sp.getS().shutdownOutput();
					sp.getOis().close();
					sp.getOos().close();
					sp.getS().close();
					break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

	}
	
	public void serverPrimitiveManger(Socket s, PaintingPrimitive p) {
		Hub.primitives.add(p);

		for (sPack sp : Hub.als) {
			try {
				sp.getOos().writeObject(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void serverChatManager(Message m) {
		for (int i = 3; i > 0; i--) {
			Hub.chat[i] = Hub.chat[i - 1];
		}

		Hub.chat[0] = m;

		for (sPack sp : Hub.als) {
			try {
				sp.getOos().writeObject(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
