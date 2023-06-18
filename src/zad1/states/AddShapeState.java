package zad1.states;

import zad1.gui.DocumentModel;
import zad1.renderers.Renderer;
import zad1.shapes.GraphicalObject;
import zad1.utils.Point;

public class AddShapeState implements State {
	
	private GraphicalObject prototype;
	private DocumentModel model;
	
	public AddShapeState(DocumentModel model, GraphicalObject prototype) {
		this.model = model;
		this.prototype = prototype;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// dupliciraj zapamćeni prototip, pomakni ga na poziciju miša i dodaj u model
		GraphicalObject newObject = prototype.duplicate();
		newObject.translate(mousePoint);
		model.addGraphicalObject(newObject);
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterDraw(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaving() {
		// TODO Auto-generated method stub
		
	}
	
	// ...
}
