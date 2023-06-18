package zad1;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import zad1.gui.GUI;
import zad1.shapes.GraphicalObject;
import zad1.shapes.concrete.LineSegment;
import zad1.shapes.concrete.Oval;

public class Main {
	
	public static void main(String[] args) {
		List<GraphicalObject> objects = new ArrayList<>();

		objects.add(new LineSegment());
		objects.add(new Oval());
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GUI gui = new GUI(objects);
				gui.setVisible(true);
			}
		});
	}
}
