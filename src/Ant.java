import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Class to represent an ant. Includes methods to generate the path
 */
public class Ant {
	// Stores the city the ant starts in
	int startCity;
	// Stores the fitness of the ant. In this case, the length of the path
	double fitness;
	// Stores the path the ant takes
	List<Integer> path;


	/**
	 * Constructor for the Ant class
	 * @param startCity The city the ant starts in
	 */
    public Ant(int startCity) {
        this.startCity = startCity;
    }

	/**
	 * Generates the path the ant takes
	 * @param graph The graph the ant is traversing
	 * @param heuristic The heuristic function to use
	 */
	public void generatePath(Graph graph, Function<Integer, Double> heuristic) {
		// Get the distance matrix from the graph
		double[][] distanceMatrix = graph.getDistanceMatrix();
		// Create a new list to store the path
		this.path = new ArrayList<>();
		// Create a new boolean array to store which cities have been visited
		boolean[] visited = new boolean[distanceMatrix.length];
		int currentCity = startCity;
		visited[currentCity] = true;
		path.add(currentCity);
		// Small constant to avoid zero pheromone value
		// This is needed to avoid a divide by zero error
		double epsilon = 1e-10;
		// While the path is not complete it will continue to add cities to the path
		while (path.size() < distanceMatrix.length) {
			double sum = 0.0;
			double[] probabilities = new double[distanceMatrix.length];
			// Calculate the probability of moving to each next city
			for (int i = 0; i < distanceMatrix.length; i++) {
				if (!visited[i]) {
					double heuristicValue = heuristic.apply(i);
					double pheromoneValue = graph.getPheromoneMatrix()[currentCity][i] + epsilon;
					probabilities[i] = pheromoneValue * heuristicValue;
					sum += probabilities[i];
				}
			}
			// Normalise the probabilities
			for (int i = 0; i < probabilities.length; i++) {
				if(sum != 0){
					probabilities[i] /= sum;
				}else{
					System.out.println("ERROR");
				}
			}

			// Select the next city to move tos
			double random = Math.random();
			int nextCity = -1;
			for (int i = 0; i < probabilities.length; i++) {
				if (!visited[i]) {
					random -= probabilities[i];
					if (random <= 0.0) {
						nextCity = i;
						break;
					}
				}
			}
			// If no city is selected, throw an error
			if (nextCity == -1) {
				throw new RuntimeException("No city selected for the next move.");
			}
    		// Mark the selected city as visited and add it to the path
			visited[nextCity] = true;
			path.add(nextCity);
			currentCity = nextCity;
		}
		path.add(startCity);
	}

	/**
	 * Gets the current city the ant is in
	 * @return The city the ant is currently in
	 */
	public int getCurrentCity() {
		return path.get(path.size() - 1);
	}

	/**
	 * Gets the path the ant has taken
	 * @return The path the ant has taken
	 */
	public List<Integer> getPath(){
		return path;
	}

	/**
	 * Gets the length of the path the ant has taken
	 * @param graph The graph the ant is traversing
	 * @return The length of the path the ant has taken
	 */
	public double getPathLength(Graph graph) {
		double length = 0.0;
		for (int i = 0; i < path.size() - 1; i++) {
			int city1 = path.get(i);
			int city2 = path.get(i + 1);
			length += graph.getDistanceMatrix()[city1][city2];
		}
		return length;
	}

	public void setPath(List<Integer> path) {
		this.path = path;
	}
}
