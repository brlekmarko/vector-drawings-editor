package zad1.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zad1.shapes.GraphicalObject;
import zad1.shapes.GraphicalObjectListener;
import zad1.utils.Point;

public class DocumentModel {

	public final static double SELECTION_PROXIMITY = 10;

	// Kolekcija svih grafičkih objekata:
	private List<GraphicalObject> objects = new ArrayList<>();
	// Read-Only proxy oko kolekcije grafičkih objekata:
	private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
	// Kolekcija prijavljenih promatrača:
	private List<DocumentModelListener> listeners = new ArrayList<>();
	// Kolekcija selektiranih objekata:
	private List<GraphicalObject> selectedObjects = new ArrayList<>();
	// Read-Only proxy oko kolekcije selektiranih objekata:
	private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

	// Promatrač koji će biti registriran nad svim objektima crteža...
	private final GraphicalObjectListener goListener = new GraphicalObjectListener() {

		@Override
		public void graphicalObjectChanged(GraphicalObject go) {
			notifyListeners();
		}

		@Override
		public void graphicalObjectSelectionChanged(GraphicalObject go) {
			if(go.isSelected()) {
				if(!selectedObjects.contains(go)) {
					selectedObjects.add(go);
				}
			}
			else {
				if(selectedObjects.contains(go)) {
					selectedObjects.remove(go);
				}
			}
			notifyListeners();
		}
		
	};
	
	// Konstruktor...
	public DocumentModel() {
		// nista
	}

	// Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
	// i potom obavijeste svi promatrači modela
	public void clear() {
		for(GraphicalObject go : objects) {
			go.removeGraphicalObjectListener(goListener);
		}
		selectedObjects.clear();
		objects.clear();
		notifyListeners();
	}

	// Dodavanje objekta u dokument (pazite je li već selektiran; registrirajte model kao promatrača)
	public void addGraphicalObject(GraphicalObject obj) {
		objects.add(obj);
		obj.addGraphicalObjectListener(goListener);
		if(obj.isSelected()) {
			selectedObjects.add(obj);
		}
		notifyListeners();
	}
	
	// Uklanjanje objekta iz dokumenta (pazite je li već selektiran; odregistrirajte model kao promatrača)
	public void removeGraphicalObject(GraphicalObject obj) {
		if(selectedObjects.contains(obj)) {
			selectedObjects.remove(obj);
		}
		obj.removeGraphicalObjectListener(goListener);
		objects.remove(obj);
		notifyListeners();
	}

	// Vrati nepromjenjivu listu postojećih objekata (izmjene smiju ići samo kroz metode modela)
	public List<GraphicalObject> list() {
		return roObjects;
	}

	// Prijava...
	public void addDocumentModelListener(DocumentModelListener l) {
		this.listeners.add(l);
	}
	
	// Odjava...
	public void removeDocumentModelListener(DocumentModelListener l) {
		this.listeners.remove(l);
	}

	// Obavještavanje...
	public void notifyListeners() {
		for(DocumentModelListener l : listeners) {
			l.documentChange();
		}
	}
	
	// Vrati nepromjenjivu listu selektiranih objekata
	public List<GraphicalObject> getSelectedObjects() {
		return roSelectedObjects;
	}

	// Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
	// Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
	public void increaseZ(GraphicalObject go) {
		int Z = objects.indexOf(go);
		if(Z == objects.size()-1) {
			return; // vec je zadnji
		}
		objects.remove(Z);
		objects.add(Z+1, go);
		notifyListeners();
	}
	
	// Pomakni predani objekt u listi objekata na jedno mjesto ranije...
	public void decreaseZ(GraphicalObject go) {
		int Z = objects.indexOf(go);
		if(Z == 0) {
			return; // vec je prvi
		}
		objects.remove(Z);
		objects.add(Z-1, go);
		notifyListeners();
	}
	
	// Pronađi postoji li u modelu neki objekt koji klik na točku koja je
	// predana kao argument selektira i vrati ga ili vrati null. Točka selektira
	// objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
	// SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
	public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
		GraphicalObject closest = null;
		double minUdaljenost = SELECTION_PROXIMITY; // mora biti manje od ovoga
		for(GraphicalObject go : objects) {
			double udaljenost = go.selectionDistance(mousePoint);
			if(udaljenost < minUdaljenost) {
				minUdaljenost = udaljenost;
				closest = go;
			}
		}
		return closest;
	}

	// Pronađi da li u predanom objektu predana točka miša selektira neki hot-point.
	// Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet da ta
	// udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa 
	// kojeg bi predana točka selektirala ili -1 ako takve nema. Status selekcije 
	// se pri tome NE dira.
	public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
		int closest = -1;
		double minUdaljenost = SELECTION_PROXIMITY; // mora biti manje od ovoga
		// idemo po svim hotpointovima i gledamo koji je najblizi
		for(int i = 0; i<object.getNumberOfHotPoints(); i++) {
			double udaljenost = object.getHotPointDistance(i, mousePoint);
			if(udaljenost < minUdaljenost) {
				minUdaljenost = udaljenost;
				closest = i;
			}
		}
		return closest;
	}

	public void deselectAll() {
		for(GraphicalObject go : objects) {
			go.setSelected(false);
		}
		notifyListeners();
	}

}