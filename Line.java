package SocketPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Line extends PaintingPrimitive implements java.io.Serializable {

	private Point startPoint;
	private Point endPoint;

	public Line() {
		super();
	}
	
	public Line(Color color, Point startPoint, Point endPoint) {
		super(color);

		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	protected void drawGeometry(Graphics g) {
		g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

	}

}
