package zad1.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;

import zad1.renderers.G2DRendererImpl;
import zad1.renderers.Renderer;
import zad1.shapes.GraphicalObject;
import zad1.states.IdleState;
import zad1.states.State;
import zad1.utils.Point;

public class Platno extends JComponent{

	private static final long serialVersionUID = 1L;
	private DocumentModel model;
	private State currentState;
	
	public Platno(DocumentModel model, State currentState) {
		this.model = model;
		model.addDocumentModelListener(new DocumentModelListener() {
			
			@Override
			public void documentChange() {
				repaint();
			}
		});
		
		this.currentState = currentState;
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
					getState().onLeaving();
					setState(new IdleState());
				}
				else {
					getState().keyPressed(arg0.getKeyCode());
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				getState().mouseDragged(new Point((int) arg0.getPoint().getX(), (int) arg0.getPoint().getY()));
			}
		});
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent arg0) {
				getState().mouseDown(new Point((int) arg0.getPoint().getX(), (int) arg0.getPoint().getY()), arg0.isShiftDown(), arg0.isControlDown());
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				getState().mouseUp(new Point((int) arg0.getPoint().getX(), (int) arg0.getPoint().getY()), arg0.isShiftDown(), arg0.isControlDown());
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		Renderer r = new G2DRendererImpl(g2d);
		for(GraphicalObject go : model.list()) {
			go.render(r);
			getState().afterDraw(r, go);
		}
		getState().afterDraw(r);
	}

	public void setState(State state) {
		this.currentState = state;
	}
	
	public State getState() {
		return currentState;
	}

}
