public class Graph {

	private double[][] distanceMatrix;
	private double[][] pheromoneMatrix;

	public Graph(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
		this.pheromoneMatrix = new double[distanceMatrix.length][distanceMatrix.length];
	}

	public double[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public double[][] getPheromoneMatrix() {
		return pheromoneMatrix;
	}

}
