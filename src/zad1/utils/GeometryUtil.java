package zad1.utils;

public class GeometryUtil {

	public static double distanceFromPoint(Point point1, Point point2) {
		// sqrt((x1 - x2)^2 + (y1 - y2)^2)
		int distanceX = point1.getX() - point2.getX();
		int distanceY = point1.getY() - point2.getY();
		return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}
	
	public static double distanceFromLineSegment(Point s, Point e, Point p) {
		// Izračunaj koliko je točka P udaljena od linijskog segmenta određenog
		// početnom točkom S i završnom točkom E. Uočite: ako je točka P iznad/ispod
		// tog segmenta, ova udaljenost je udaljenost okomice spuštene iz P na S-E.
		// Ako je točka P "prije" točke S ili "iza" točke E, udaljenost odgovara
		// udaljenosti od P do početne/konačne točke segmenta.

		// ako je linijski segment samo točka
		if (s.getX() == e.getX() && s.getY() == e.getY()) {
			return distanceFromPoint(s, p);
		}

		// ako je pocetna tocka S desno od zavrsne tocke E, prebaci ih
		int lijeviX = s.getX() < e.getX() ? s.getX() : e.getX();
		int lijeviY = s.getX() < e.getX() ? s.getY() : e.getY();
		int desniX = s.getX() > e.getX() ? s.getX() : e.getX();
		int desniY = s.getX() > e.getX() ? s.getY() : e.getY();

		Point lijevaTocka = new Point(lijeviX, lijeviY);
		Point desnaTocka = new Point(desniX, desniY);

		// ako je tocka P prije pocetne tocke
		if(p.getX() < lijevaTocka.getX()) {
			return distanceFromPoint(lijevaTocka, p);
		}
		
		// ako je tocka P poslije zavrsne tocke
		if(p.getX() > desnaTocka.getX()) {
			return distanceFromPoint(desnaTocka, p);
		}

		// inace, tocka P je izmedu pocetne i zavrsne tocke
		// potrebno je izracunati udaljenost okomice spustene iz P na S-E

		// nadjemo jednadzbu pravca S-E
		// uvrstimo P.getX() u jednadzbu pravca, dobijemo Y koordinatu
		// abs(P.getY() - Y) = duljina okomice
		float koeficjentPravca = (float) (desnaTocka.getY() - lijevaTocka.getY())
				/ (float) (desnaTocka.getX() - lijevaTocka.getX());
		if(koeficjentPravca == 0) {
			return Math.abs(lijevaTocka.getY() - p.getY());
		}
		float y_y1 = (float) koeficjentPravca * (p.getX() - lijevaTocka.getX());
		float y = y_y1 + lijevaTocka.getY();
		
		return Math.abs(p.getY() - y);
	}
}
