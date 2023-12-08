public class City {
	private double x;
	private double y;

	public City(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double distanceTo(City city) {
		double xDistance = Math.abs(getX() - city.getX());
		double yDistance = Math.abs(getY() - city.getY());
		return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));

	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public String toString() {
		return getX() + ", " + getY();
	}
}
