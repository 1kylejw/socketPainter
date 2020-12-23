package SocketPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PaintingPanel extends JPanel {

	ArrayList<PaintingPrimitive> primitives = new ArrayList<PaintingPrimitive>();
	PaintingPrimitive temp;
	
	public PaintingPanel() {
		this.setBackground(Color.WHITE);
	}
	
	public void addPrimitive(PaintingPrimitive obj) {
        this.primitives.add(obj);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (temp != null) temp.draw(g);
		for(PaintingPrimitive obj : primitives) {
	    	obj.draw(g);
	    }        
	}

	public void addTemp(PaintingPrimitive obj) {
		temp = obj;
	}

	public void clear() {
		primitives.clear();
	}

}
