package zad1.renderers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zad1.utils.Point;

public class SVGRendererImpl implements Renderer {

	private List<String> lines = new ArrayList<>();
	private String fileName;
	
	public SVGRendererImpl(String fileName) {
		// zapamti fileName; u lines dodaj zaglavlje SVG dokumenta:
		// <svg xmlns=... >
		// ...
		this.fileName = fileName;
		lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\">");
	}

	public void close() throws IOException {
		// u lines još dodaj završni tag SVG dokumenta: </svg>
		// sve retke u listi lines zapiši na disk u datoteku
		// ...
		lines.add("</svg>");
		File file = new File(fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		for(String line : lines) {
			fw.write(line);
		}
		fw.close();
	}
	
	@Override
	public void drawLine(Point s, Point e) {
		// Dodaj u lines redak koji definira linijski segment:
		// <line ... />
		lines.add("<line x1=\"" + s.getX() + "\" y1=\"" + s.getY() + 
					"\" x2=\"" + e.getX() + "\" y2=\"" + e.getY() + 
					"\" style=\"stroke:rgb(0,0,255);stroke-width:1\" />");
	}

	@Override
	public void fillPolygon(Point[] points) {
		// Dodaj u lines redak koji definira popunjeni poligon:
		// <polygon points="..." style="stroke: ...; fill: ...;" />
		lines.add("<polygon points=\"");
		for(Point p : points) {
			lines.add(p.getX() + "," + p.getY() + " ");
		}
		lines.add("\" style=\"fill:rgb(0,0,255);stroke:rgb(255,0,0);stroke-width:1\" />");
	}

}
