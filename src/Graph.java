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

	public void printDistanceMatrix() {
		System.out.println("Distance Matrix:");
		System.out.print("City 0   ");
		for (int i = 1; i < distanceMatrix.length; i++) {
			System.out.print(i + "     ");
		}
		System.out.println();
		for (int i = 0; i < distanceMatrix.length; i++) {
			System.out.print("City " + i + " ");
			for (int j = 0; j < distanceMatrix.length; j++) {
				System.out.print(distanceMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void initializePheromoneMatrix() {
		int size = this.getDistanceMatrix().length;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				pheromoneMatrix[i][j] = Math.random();
			}
		}
	}

	public void initializePheromoneMatrixMMAS() {
		int size = this.getDistanceMatrix().length;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				pheromoneMatrix[i][j] = 1;
			}
		}
	}

	/**
	 * This method goes through the pheromone matrix and ensures that no pheromone value is above 1.
	 */
	public void ensurePheromoneLimit() {
		int size = this.getDistanceMatrix().length;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pheromoneMatrix[i][j] > 1) {
					pheromoneMatrix[i][j] = 1;
				}
			}
		}
	}
}
