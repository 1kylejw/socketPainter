package SocketPainter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Painter extends JFrame implements ActionListener, MouseListener, MouseMotionListener, WindowListener {

	private static String name;
	private static Socket s;
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	private String primitive = "line";
	private Color color = Color.BLACK;
	private Point start;
	private Point end;
	private static PaintingPanel cPanel;
	private JTextField field;
	private static JLabel chat[];
	static boolean done = false;

	public Painter() {
		this.setTitle(name);

		setSize(512, 512);

		Frame f = new Frame();
		f.setLayout(new BorderLayout());

		addWindowListener(this);

//		North Panel

		JPanel nPanel = new JPanel();
		nPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		nPanel.setLayout(new GridLayout(0, 4, 5, 5));

		nPanel.setBackground(Color.WHITE);

		JButton lineButton = new JButton("Line");
		JButton circleButton = new JButton("Circle");
		JButton rectButton = new JButton("Rectangle");
		JButton clearButton = new JButton("Clear Screen");

		lineButton.setFocusPainted(false);
		circleButton.setFocusPainted(false);
		rectButton.setFocusPainted(false);
		clearButton.setFocusPainted(false);

		lineButton.addActionListener(this);
		circleButton.addActionListener(this);
		rectButton.addActionListener(this);
		clearButton.addActionListener(this);

		lineButton.setActionCommand("line");
		circleButton.setActionCommand("circle");
		rectButton.setActionCommand("rect");
		clearButton.setActionCommand("clear");

		nPanel.add(lineButton);
		nPanel.add(circleButton);
		nPanel.add(rectButton);
		nPanel.add(clearButton);

		add(nPanel, BorderLayout.NORTH);

//		West Panel

		JPanel wPanel = new JPanel();
		wPanel.setLayout(new GridLayout(4, 0));

		JButton blackPaint = new JButton("          ");
		blackPaint.setBackground(Color.BLACK);
		blackPaint.setOpaque(true);
		blackPaint.setBorderPainted(false);

		JButton redPaint = new JButton("          ");
		redPaint.setBackground(Color.RED);
		redPaint.setOpaque(true);
		redPaint.setBorderPainted(false);

		JButton bluePaint = new JButton("          ");
		bluePaint.setBackground(Color.BLUE);
		bluePaint.setOpaque(true);
		bluePaint.setBorderPainted(false);

		JButton greenPaint = new JButton("          ");
		greenPaint.setBackground(Color.GREEN);
		greenPaint.setOpaque(true);
		greenPaint.setBorderPainted(false);

		blackPaint.setFocusPainted(false);
		redPaint.setFocusPainted(false);
		bluePaint.setFocusPainted(false);
		greenPaint.setFocusPainted(false);

		blackPaint.addActionListener(this);
		redPaint.addActionListener(this);
		bluePaint.addActionListener(this);
		greenPaint.addActionListener(this);

		blackPaint.setActionCommand("black");
		redPaint.setActionCommand("red");
		bluePaint.setActionCommand("blue");
		greenPaint.setActionCommand("green");

		wPanel.add(blackPaint);
		wPanel.add(redPaint);
		wPanel.add(bluePaint);
		wPanel.add(greenPaint);

		add(wPanel, BorderLayout.WEST);

//		Center Panel

		cPanel = new PaintingPanel();

		cPanel.addMouseListener(this);
		cPanel.addMouseMotionListener(this);

		add(cPanel, BorderLayout.CENTER);

//		South Panel

		JPanel sPanel = new JPanel();
		sPanel.setLayout(new BorderLayout());

		JPanel write = new JPanel();
		write.setLayout(new GridLayout(0, 2));

		field = new JTextField("", 200);
		JButton send = new JButton("Send");

		send.setFocusPainted(false);

		send.addActionListener(this);

		send.setActionCommand("send");

		write.add(field);
		write.add(send);

		JPanel read = new JPanel();
		read.setLayout(new GridLayout(4, 0));

		chat = new JLabel[4];
		for (int i = 0; i < 4; i++)
			chat[i] = new JLabel(" ");

		for (JLabel n : chat)
			read.add(n);

		sPanel.add(write, BorderLayout.NORTH);
		sPanel.add(read, BorderLayout.SOUTH);

		add(sPanel, BorderLayout.SOUTH);

		setVisible(true);

	}

	public synchronized static void main(String args[]) throws Exception {
		name = JOptionPane.showInputDialog("Enter your name");

		new Painter();

		try {
			s = new Socket("localhost", 7000);

			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

			try {
				while (true) {
					Object x = (Object) ois.readObject();
					if (x instanceof PaintingPrimitive) {
						clientPrimitiveManager((PaintingPrimitive) x);
					}
					if (x instanceof Message) {
						clientChatManager((Message) x);
					}
					if (x instanceof String) {
						clearFromServer();
					}
					
					if (done == true) {
						s.shutdownInput();
						s.shutdownOutput();
						s.close();
						ois.close();
						oos.close();
						break;
					}
				}
			} catch (Exception e) {
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized static void clientPrimitiveManager(PaintingPrimitive p) {
		cPanel.addPrimitive(p);

		cPanel.repaint();
	}

	public synchronized static void clientChatManager(Message m) {
		for (int i = 3; i > 0; i--) {
			if (chat[i].equals(" "))
				chat[i].setText(m.getName() + ": " + chat[i - 1].getText());
			else
				chat[i].setText(chat[i - 1].getText());
		}

		chat[0].setText(m.getName() + ": " + m.getS());
	}

	public synchronized static void clearFromServer() {
		cPanel.clear();

		cPanel.addTemp(new Line(Color.WHITE, new Point(0, 0), new Point(0, 0)));

		cPanel.repaint();
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "line" || e.getActionCommand() == "circle" || e.getActionCommand() == "rect") {
			primitive = e.getActionCommand();

		} else if (e.getActionCommand() == "clear") {
			cPanel.clear();

			try {
				oos.writeObject("clear");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			cPanel.addTemp(new Line(Color.WHITE, start, end));

			cPanel.repaint();

		} else if (e.getActionCommand() == "red") {
			color = Color.RED;

		} else if (e.getActionCommand() == "blue") {
			color = Color.BLUE;

		} else if (e.getActionCommand() == "green") {
			color = Color.GREEN;

		} else if (e.getActionCommand() == "black") {
			color = Color.BLACK;

		} else if (e.getActionCommand() == "send") {
			try {
				oos.writeObject(new Message(name, field.getText()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			field.setText("");

		}

	}

	@Override
	public synchronized void mousePressed(MouseEvent e) {
		start = e.getPoint();

	}

	@Override
	public synchronized void mouseReleased(MouseEvent e) {
		end = e.getPoint();
		PaintingPrimitive p = null;

		if (primitive == "line") {
			p = new Line(color, start, end);
			cPanel.addPrimitive(p);
		} else if (primitive == "circle") {
			p = new Circle(color, start, end);
			cPanel.addPrimitive(p);
		} else if (primitive == "rect") {
			p = new Rect(color, start, end);
			cPanel.addPrimitive(p);
		}

		try {
			oos.writeObject(p);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		cPanel.repaint();

	}

	@Override
	public synchronized void mouseDragged(MouseEvent e) {
		end = e.getPoint();

		if (primitive == "line") {
			cPanel.addTemp(new Line(color, start, end));
		} else if (primitive == "circle") {
			cPanel.addTemp(new Circle(color, start, end));
		} else if (primitive == "rect") {
			cPanel.addTemp(new Rect(color, start, end));
		}

		cPanel.repaint();

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {

			oos.writeObject(null);
			
			done = true;
			
			System.exit(0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
