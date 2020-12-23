package SocketPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Rect extends PaintingPrimitive implements java.io.Serializable {

	private Point startPoint;
	private Point endPoint;

	public Rect() {
		super();
	}
	
	public Rect(Color color, Point startPoint, Point endPoint) {
		super(color);

		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	protected void drawGeometry(Graphics g) {
		g.drawRect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
	}

}
