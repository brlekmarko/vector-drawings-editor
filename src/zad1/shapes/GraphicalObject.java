package zad1.shapes;

import java.util.List;
import java.util.Stack;

import zad1.renderers.Renderer;
import zad1.utils.Point;
import zad1.utils.Rectangle;

public interface GraphicalObject {
	
	// Podrška za uređivanje objekta
		boolean isSelected();
		void setSelected(boolean selected);
		int getNumberOfHotPoints();
		Point getHotPoint(int index);
		void setHotPoint(int index, Point point);
		boolean isHotPointSelected(int index);
		void setHotPointSelected(int index, boolean selected);
		double getHotPointDistance(int index, Point mousePoint);

		// Geometrijska operacija nad oblikom
		void translate(Point delta);
		Rectangle getBoundingBox();
		double selectionDistance(Point mousePoint);

		// Podrška za crtanje (dio mosta)
		void render(Renderer r);
		
		// Observer za dojavu promjena modelu
		public void addGraphicalObjectListener(GraphicalObjectListener l);
		public void removeGraphicalObjectListener(GraphicalObjectListener l);

		// Podrška za prototip (alatna traka, stvaranje objekata u crtežu, ...)
		String getShapeName();
		GraphicalObject duplicate();
		
		// Podrška za snimanje i učitavanje
		public String getShapeID();
		public void load(Stack<GraphicalObject> stack, String data);
		public void save(List<String> rows);
}
