package zad1.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import zad1.renderers.SVGRendererImpl;
import zad1.shapes.CompositeShape;
import zad1.shapes.GraphicalObject;
import zad1.shapes.concrete.LineSegment;
import zad1.shapes.concrete.Oval;
import zad1.states.AddShapeState;
import zad1.states.EraserState;
import zad1.states.IdleState;
import zad1.states.SelectShapeState;
import zad1.states.State;

public class GUI extends JFrame{


	private static final long serialVersionUID = 1L;
	
	private List<GraphicalObject> objects;
	private DocumentModel model;
	private Platno platno;
	private State currentState;

	public GUI(List<GraphicalObject> objects) {
		this.objects = objects;
		this.model = new DocumentModel();
		this.currentState = new IdleState();
		setTitle("Lab4");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
		initGUI();
	}
	
	
	private void initGUI() {
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		generateToolbarItems(toolBar);
		pane.add(toolBar, BorderLayout.NORTH);
		
		this.platno = new Platno(model, currentState);
		pane.add(platno, BorderLayout.CENTER);
	}
	
	private void generateToolbarItems(JToolBar toolBar){
		
		toolBar.add(new AbstractAction("Učitaj") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// https://stackoverflow.com/questions/3548140/how-to-open-and-save-using-java
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save image");
				if(jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "Odaberite datoteku", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				Path path = jfc.getSelectedFile().toPath();
				if(!Files.isReadable(path)) {
					JOptionPane.showMessageDialog(null, "Datoteka nije citljiva", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					List<String> lines = Files.readAllLines(path);
					Stack<GraphicalObject> stog = new Stack<>();
					Map<String, GraphicalObject> mapa = new HashMap<>();
					GraphicalObject go = new LineSegment();
					mapa.put(go.getShapeID(), go);
					go = new Oval();
					mapa.put(go.getShapeID(), go);
					go = new CompositeShape();
					mapa.put(go.getShapeID(), go);

					for(String line : lines){
						int index = line.indexOf(" ");
						String id = line.substring(0, index);
						go = mapa.get(id);
						if(go == null) {
							JOptionPane.showMessageDialog(null, "Nepoznata naredba", "Warning", JOptionPane.WARNING_MESSAGE);
							return;
						}
						go.load(stog, line.substring(index+1));
					}
					model.clear();
					// potrebno je obrnuti stog
					Stack <GraphicalObject> temp = new Stack<>();
					while(!stog.isEmpty()) {
						temp.push(stog.pop());
					}
					
					// dodajemo objekte u model
					while(!temp.isEmpty()) {
						model.addGraphicalObject(temp.pop());
					}
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Greska prilikom citanja datoteke", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});
		
		toolBar.add(new AbstractAction("Pohrani") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// https://stackoverflow.com/questions/3548140/how-to-open-and-save-using-java
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save image");
				if(jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "Odaberite datoteku", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String fileName = jfc.getSelectedFile().getAbsolutePath();
				if(!fileName.endsWith(".txt")) {
					fileName += ".txt";
				}
				List<String> rows = new ArrayList<>();
				for(GraphicalObject go : model.list()) {
					go.save(rows);
				}

				File file = new File(fileName);
				try{
					file.createNewFile();
					FileWriter fw = new FileWriter(file);
					for(String line : rows) {
						fw.write(line + '\n');
					}
					fw.close();
				}catch(IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		toolBar.add(new AbstractAction("SVG export") {
				
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// https://stackoverflow.com/questions/3548140/how-to-open-and-save-using-java
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save SVG file");
				if(jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "Odaberite datoteku", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String fileName = jfc.getSelectedFile().getAbsolutePath();
				if(!fileName.endsWith(".svg")) {
					fileName += ".svg";
				}
				SVGRendererImpl svg = new SVGRendererImpl(fileName);
				for(GraphicalObject go : model.list()) {
					go.render(svg);
				}
				try {
					svg.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		for(GraphicalObject go : objects) {
			toolBar.add(new AbstractAction(go.getShapeName()) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					currentState.onLeaving();
					currentState = new AddShapeState(model, go);
					platno.setState(currentState);
					platno.requestFocus();
				}
			});
		}
		toolBar.add(new AbstractAction("Selektiraj") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentState.onLeaving();
				currentState = new SelectShapeState(model);
				platno.setState(currentState);
				platno.requestFocus();
			}
		});
		toolBar.add(new AbstractAction("Brisalo") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentState.onLeaving();
				currentState = new EraserState(model);
				platno.setState(currentState);
				platno.requestFocus();
			}
		});
	}

}
