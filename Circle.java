package SocketPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Circle extends PaintingPrimitive implements java.io.Serializable {

	private Point centerPoint;
	private Point radiusPoint;

	public Circle() {
		super();
	}
	
	public Circle(Color color, Point centerPoint, Point radiusPoint) {
		super(color);

		this.centerPoint = centerPoint;
		this.radiusPoint = radiusPoint;
	}

	@Override
	public void drawGeometry(Graphics g) {
		int radius = (int) Math.abs(centerPoint.distance(radiusPoint));
		g.drawOval(centerPoint.x - radius, centerPoint.y - radius, radius * 2, radius * 2);
	}

}
