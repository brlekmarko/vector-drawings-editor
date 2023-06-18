package zad1.renderers;

import zad1.utils.Point;

public interface Renderer {
	void drawLine(Point s, Point e);
	void fillPolygon(Point[] points);
}
