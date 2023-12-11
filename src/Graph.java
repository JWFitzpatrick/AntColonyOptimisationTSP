/**
 * This class represents the graph of cities. It contains the distance matrix and the pheromone matrix.
 */
public class Graph {
	// The distance matrix contains the distances between each city
	private double[][] distanceMatrix;
	// The pheromone matrix contains the pheromone values between each city
	private double[][] pheromoneMatrix;

	/**
	 * Constructor for the graph class.
	 * @param distanceMatrix The distance matrix
	 */
	public Graph(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
		this.pheromoneMatrix = new double[distanceMatrix.length][distanceMatrix.length];
	}

	/**
	 * Getter for the distance matrix
	 * @return The distance matrix
	 */
	public double[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	/**
	 * Getter for the pheromone matrix
	 * @return The pheromone matrix
	 */
	public double[][] getPheromoneMatrix() {
		return pheromoneMatrix;
	}

	/**
	 * Prints the distance matrix to the console
	 * This is used for debugging purposes
	 */
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

	/**
	 * Initializes the pheromone matrix with random values between 0 and 1
	 */
	public void initializePheromoneMatrix() {
		int size = this.getDistanceMatrix().length;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				pheromoneMatrix[i][j] = Math.random();
			}
		}
	}

	/**
	 * Initializes the pheromone matrix with values of 1
	 * This is used for the MMAS algorithm
	 */
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
