package SocketPainter;

import java.awt.Color;
import java.awt.Graphics;

public abstract class PaintingPrimitive implements java.io.Serializable {

	private Color color;

	public PaintingPrimitive() {
		
	}
	
	public PaintingPrimitive(Color color) {
		this.color = color;

	}

	public final void draw(Graphics g) {
		g.setColor(this.color);
		drawGeometry(g);
	}

	protected abstract void drawGeometry(Graphics g);

}
