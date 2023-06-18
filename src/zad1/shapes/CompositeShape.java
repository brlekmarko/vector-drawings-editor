package zad1.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import zad1.renderers.Renderer;
import zad1.utils.Point;
import zad1.utils.Rectangle;

public class CompositeShape extends AbstractGraphicalObject{

    private List<GraphicalObject> children = new ArrayList<>();

    public CompositeShape() {
        super(new Point[0]);
        this.children = new ArrayList<>();
    }

    public CompositeShape(List<GraphicalObject> children) {
        super(new Point[0]);
        this.children = children;
    }

    @Override
    public String getShapeName() {
        return "Composite";
    }

    @Override
    public GraphicalObject duplicate() {
        return null;
    }

    @Override
    public void render(Renderer r) {
        for(GraphicalObject child : children) {
            child.render(r);
        }
    }

    @Override
    public void translate(Point delta) {
        for(GraphicalObject child : children) {
            child.translate(delta);
        }
        notifyListenersObjectChanged();
    }

    @Override
    public Rectangle getBoundingBox() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for(GraphicalObject child : children) {
            Rectangle childBoundingBox = child.getBoundingBox();
            if(childBoundingBox.getX() < minX) {
                minX = childBoundingBox.getX();
            }
            if(childBoundingBox.getY() < minY) {
                minY = childBoundingBox.getY();
            }
            if(childBoundingBox.getX() + childBoundingBox.getWidth() > maxX) {
                maxX = childBoundingBox.getX() + childBoundingBox.getWidth();
            }
            if(childBoundingBox.getY() + childBoundingBox.getHeight() > maxY) {
                maxY = childBoundingBox.getY() + childBoundingBox.getHeight();
            }
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        int minDistance = Integer.MAX_VALUE;
        for(GraphicalObject child : children) {
            double distance = child.selectionDistance(mousePoint);
            if(distance < minDistance) {
                minDistance = (int) distance;
            }
        }
        return minDistance;
    }

    public List<GraphicalObject> getChildren() {
        return children;
    }

    public void addChild(GraphicalObject child) {
        children.add(child);
        notifyListenersObjectChanged();
    }

    public void removeChild(GraphicalObject child) {
        children.remove(child);
        notifyListenersObjectChanged();
    }
    
    public void reset() {
        children.clear();
        notifyListenersObjectChanged();
    }
    
    @Override
	public String getShapeID() {
		return "@COMP";
	}

    @Override
	public void save(List<String> rows) {
		for(GraphicalObject child : children) {
            child.save(rows);
        }
        rows.add(this.getShapeID() + " " + children.size());
	}

    @Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split(" ");
        int numberOfChildren = Integer.parseInt(parts[0]);
        List<GraphicalObject> children = new ArrayList<>();
        // potrebno obrnuti stog
        Stack<GraphicalObject> temp = new Stack<>();
        for(int i = 0; i < numberOfChildren; i++) {
            temp.add(stack.pop());
        }
        for(int i = 0; i < numberOfChildren; i++) {
            children.add(temp.pop());
        }
        stack.push(new CompositeShape(children));
	}
}

