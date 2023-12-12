import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class to represent an ant colony optimisation algorithm.
 * This is the main class to run the algorithm.
 */
public class AntColonyOptimisation {
	// Stores the ants
	private List<Ant> ants;
	// Stores the graph
	private Graph graph;
	// Stores the evaporation rate
	private double evaporationRate;
	// Stores the Q value which is used to calculate the pheromone to add
	private double Q;

	// Stores the heuristic value to use
	private int heuristicValue;
	// Stores the approach mode to use (0 = regular 1 = MMAS, 2 = Elitist, 3 = ASrank)
	// In this implementation MMAS is implemented as using the best iteration solution
	private int approachMode;
	// Stores the search mode to use (0 = none, 1 = hill climbing, 2 = tabu search)
	private int searchMode;
	// Stores the tabu tenure
	private int tabuTenure;
	// Stores the tabu max iterations
	private int tabuMaxIterations;

	/**
	 * Constructor for the AntColonyOptimisation class
	 * @param graph The graph to use
	 * @param numberOfAnts The number of ants to use
	 * @param evaporationRate The evaporation rate to use
	 * @param Q The Q value to use
	 * @param heuristicValue The heuristic value to use
	 * @param approachMode The approach mode to use
	 */
	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue, int approachMode) {
		this.ants = new ArrayList<>();
		if(numberOfAnts < 1){
			System.out.println("numberOfAnts must be greater than 0. Setting to 1.");
			numberOfAnts = 1;
		}
		// Create the ants
		for (int i = 0; i < numberOfAnts; i++) {
			this.ants.add(new Ant(i % graph.getDistanceMatrix().length));
		}
		this.graph = graph;
		this.evaporationRate = evaporationRate;
		this.Q = Q;
		this.heuristicValue = heuristicValue;
		this.approachMode = approachMode;
		this.searchMode = 0;
	}

	/**
	 * Constructor for the AntColonyOptimisation class with search mode
	 * @param graph The graph to use
	 * @param numberOfAnts The number of ants to use
	 * @param evaporationRate The evaporation rate to use
	 * @param Q The Q value to use
	 * @param heuristicValue The heuristic value to use
	 * @param apprachMode The approach mode to use
	 * @param searchMode The search mode to use
	 */
	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue, int apprachMode, int searchMode) {
		this.ants = new ArrayList<>();
		if(numberOfAnts < 1){
			System.out.println("numberOfAnts must be greater than 0. Setting to 1.");
			numberOfAnts = 1;
		}
		// Create the ants
		for (int i = 0; i < numberOfAnts; i++) {
			this.ants.add(new Ant(i % graph.getDistanceMatrix().length));
		}
		this.graph = graph;
		this.evaporationRate = evaporationRate;
		this.Q = Q;
		this.heuristicValue = heuristicValue;
		this.approachMode = apprachMode;
		this.searchMode = searchMode;
		if (this.searchMode == 2) {
			System.out.println("You must enter the tabu tenure and tabu max iterations to use tabuSearch.");
		}
	}

	/**
	 * Constructor for the AntColonyOptimisation class with search mode and tabu tenure and tabu max iterations
	 * @param graph The graph to use
	 * @param numberOfAnts The number of ants to use
	 * @param evaporationRate The evaporation rate to use
	 * @param Q The Q value to use
	 * @param heuristicValue The heuristic value to use
	 * @param approachMode The approach mode to use
	 * @param searchMode The search mode to use
	 * @param tabuTenure The tabu tenure to use
	 * @param tabuMaxIterations The tabu max iterations to use
	 */
	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue,int approachMode, int searchMode, int tabuTenure, int tabuMaxIterations) {
		this.ants = new ArrayList<>();
		if(numberOfAnts < 1){
			System.out.println("numberOfAnts must be greater than 0. Setting to 1.");
			numberOfAnts = 1;
		}
		for (int i = 0; i < numberOfAnts; i++) {
			this.ants.add(new Ant(i % graph.getDistanceMatrix().length));
		}
		this.graph = graph;
		this.evaporationRate = evaporationRate;
		this.Q = Q;
		this.heuristicValue = heuristicValue;
		this.approachMode = approachMode;
		this.searchMode = searchMode;
		this.tabuTenure = tabuTenure;
		this.tabuMaxIterations = tabuMaxIterations;
	}

	/**
	 * Runs the ant colony optimisation algorithm
	 * @param maxEvaluations The maximum number of ant evalutations to run
	 * @return The best solution found
	 */
	public List<Integer> run(int maxEvaluations) {
		// If the approach mode is MMAS then initialise the pheromone matrix
		// with 1 for all values instead of random values.
		if(approachMode == 1){
			this.graph.initializePheromoneMatrixMMAS();
		}else{
			this.graph.initializePheromoneMatrix();
		}
		// Stores the best solution found
		List<Integer> bestSolution = null;
		double bestSolutionLength = Double.MAX_VALUE;
		// Stores the best solution found in the current iteration (used for MMAS)
		List<Integer> bestIterationSolution = null;
		double bestIterationPathLength = Double.MAX_VALUE;
		// Stores the number of ants to use for ASrank approach
		int numberOfTopAnts = (int) Math.round(ants.size() * 0.1);
		if(numberOfTopAnts < 1){
			numberOfTopAnts = 1;
		}
		// Stores the top ants for ASrank approach
		PriorityQueue<Ant> topAnts = new PriorityQueue<>(numberOfTopAnts, Comparator.comparing(a -> a.getPathLength(graph)));
		// Run the algorithm until a specified amount of fitness 
		// evaluations has occured
		int iterations = 0;
		while(iterations < maxEvaluations) {
			//Reset the best iteration solution
			bestIterationSolution = null;
			bestIterationPathLength = Double.MAX_VALUE;
			// Clears the top ants for ASrank approach
			topAnts.clear();
			// For each ant generate the path
			for (Ant ant : ants) {
				ant.generatePath(graph, j -> heuristicValue / graph.getDistanceMatrix()[ant.getCurrentCity()][j]);
			}
			// If the approach mode is not MMAS or ASrank then update the pheromones
			if (approachMode != 1 || approachMode != 3) {
				updatePheromones();
				evaporatePheromones();
			}
			// Calculate the best solution found so far. 
			// If the approach is MMAS then calculate the best solution found in the current iteration.
			// If the approach is ASrank then get the top ants.
			for (Ant ant : ants) {
				// If the iterations is greater than the
				// maximum number of evaluations then break.
				if (iterations >= maxEvaluations) {
					break;
				}
				// Gets the best solution found so far.
				double pathLength = ant.getPathLength(graph);
				if (pathLength < bestSolutionLength) {
					bestSolution = new ArrayList<>(ant.getPath());
					bestSolutionLength = pathLength;
				}
				// Gets the best solution found in the current iteration.
				if(approachMode == 1){
					if (pathLength < bestIterationPathLength) {
						bestIterationSolution = new ArrayList<>(ant.getPath());
						bestIterationPathLength = pathLength;
					}
				}
				// Gets the top ants for ASrank approach.
				if (approachMode == 3){
					topAnts.add(ant);
					if (topAnts.size() > numberOfTopAnts) {
						topAnts.poll(); 
					}
				}
				iterations++;
			}
			// If the approach mode is MMAS then update the pheromones with the best iteration solution.
			if (approachMode == 1) {
				double pheromoneToAdd = Q / bestIterationPathLength;
				for (int i = 0; i < bestIterationSolution.size() - 1; i++) {
					int city1 = bestIterationSolution.get(i);
					int city2 = bestIterationSolution.get(i + 1);
					graph.getPheromoneMatrix()[city1][city2] += pheromoneToAdd;
					graph.getPheromoneMatrix()[city2][city1] += pheromoneToAdd;
				}
				evaporatePheromones();
				// If the approach mode is MMAS then ensure the pheromones are within the limits [0-1].
				graph.ensurePheromoneLimit();
			}
			// If the approach mode is Elitist then update the pheromones with the best solution found so far.
			if (approachMode == 2) {
				double pheromoneToAdd = Q / bestSolutionLength;
				for (int i = 0; i < bestSolution.size() - 1; i++) {
					int city1 = bestSolution.get(i);
					int city2 = bestSolution.get(i + 1);
					graph.getPheromoneMatrix()[city1][city2] += pheromoneToAdd;
					graph.getPheromoneMatrix()[city2][city1] += pheromoneToAdd;
				}
				evaporatePheromones();
			}
			// If the approach mode is ASrank then update the pheromones with the top ants.
			if (approachMode == 3) {
				for (Ant ant : topAnts) {
					List<Integer> path = ant.getPath();
					double pheromoneToAdd = Q / ant.getPathLength(graph);

					for (int i = 0; i < path.size() - 1; i++) {
						int city1 = path.get(i);
						int city2 = path.get(i + 1);
						graph.getPheromoneMatrix()[city1][city2] += pheromoneToAdd;
						graph.getPheromoneMatrix()[city2][city1] += pheromoneToAdd;
					}
				}
				evaporatePheromones();
			}
		}
		// If the program is running hill climbing or tabu search then run the search
		if (this.searchMode == 1) {
			bestSolution = hillClimbingSearch(bestSolution);	
		}
		if (this.searchMode == 2) {
			bestSolution = tabuSearch(bestSolution);
		}
		// Return the best solution found
		return bestSolution;
	}

	/**
	 * Updates the pheromones
	 */
	private void updatePheromones() {
		// For each ant drop the pheromones.
		for (Ant ant : ants) {
			List<Integer> path = ant.getPath();
			double pheromoneToAdd = Q / ant.getPathLength(graph);
			for (int i = 0; i < path.size() - 1; i++) {
				int city1 = path.get(i);
				int city2 = path.get(i + 1);
				graph.getPheromoneMatrix()[city1][city2] += pheromoneToAdd;
				graph.getPheromoneMatrix()[city2][city1] += pheromoneToAdd;
			}
		}
	}

	/**
	 * Evaporates the pheromones
	 */
	private void evaporatePheromones() {
		double[][] pheromoneMatrix = graph.getPheromoneMatrix();
		for (int i = 0; i < pheromoneMatrix.length; i++) {
			for (int j = 0; j < pheromoneMatrix[i].length; j++) {
				//Evaporates the pheromones by multiplying by the evaporation rate.
				pheromoneMatrix[i][j] *= evaporationRate;
			}
		}
	}

	/**
	 * Runs the hill climbing search
	 * @param initialSolution The initial solution to use
	 * @return The best solution found
	 */
	public List<Integer> hillClimbingSearch(List<Integer> initialSolution) {
		if (initialSolution == null) {
			return new ArrayList<>();
		}
		// Copy the initial solution to start the search
		List<Integer> currentSolution = new ArrayList<>(initialSolution);
		// Calculate the length of the current solution
		double currentSolutionLength = getPathLength(currentSolution);

		// Continue until no better solution is found
		while (true) {
			// Initialize the best neighbor and its length
			List<Integer> bestNeighbor = null;
			double bestNeighborLength = Double.MAX_VALUE;

			// Iterate over all pairs of cities in the current solution
			for (int i = 0; i < currentSolution.size() - 1; i++) {
				for (int j = i + 1; j < currentSolution.size(); j++) {
					// Create a neighbor solution by swapping two cities
					List<Integer> neighbor = new ArrayList<>(currentSolution);
					Collections.swap(neighbor, i, j);
					// Calculate the length of the neighbor solution
					double neighborLength = getPathLength(neighbor);

					// If the neighbor solution is better, update the best neighbor
					if (neighborLength < bestNeighborLength) {
						bestNeighbor = neighbor;
						bestNeighborLength = neighborLength;
					}
				}
			}

			// If the best neighbor is better than the current solution, move to the best neighbor
			if (bestNeighborLength < currentSolutionLength) {
				currentSolution = bestNeighbor;
				currentSolutionLength = bestNeighborLength;
			} else {
				// If no better solution was found, stop the search
				break;
			}
		}
		// Return the best solution found
		return currentSolution;
	}
	/**
	 * Runs the tabu search
	 * @param initialSolution the initial solution to start the search from
	 * @return the best solution found
	 */
	public List<Integer> tabuSearch(List<Integer> initialSolution) {
		// Copy the initial solution to start the search
		List<Integer> currentSolution = new ArrayList<>(initialSolution);
		// Calculate the length of the current solution
		double currentSolutionLength = getPathLength(currentSolution);
		// Initialize the tabu list
		List<List<Integer>> tabuList = new LinkedList<>();

		// Initialize the counter for iterations without improvement
		int iterationsWithoutImprovement = 0;

		// Continue until the maximum number of iterations without improvement is reached
		while (iterationsWithoutImprovement < tabuMaxIterations) {
			// Initialize the best neighbor and its length
			List<Integer> bestNeighbor = null;
			double bestNeighborLength = Double.MAX_VALUE;

			// Iterate over all pairs of cities in the current solution
			for (int i = 0; i < currentSolution.size() - 1; i++) {
				for (int j = i + 1; j < currentSolution.size(); j++) {
					// Create a neighbor solution by swapping two cities
					List<Integer> neighbor = new ArrayList<>(currentSolution);
					Collections.swap(neighbor, i, j);
					// Calculate the length of the neighbor solution
					double neighborLength = getPathLength(neighbor);

					// If the neighbor solution is better and not in the tabu list (or the maximum number of iterations without improvement has been reached), update the best neighbor
					if ((neighborLength < bestNeighborLength) && (!tabuList.contains(neighbor) || iterationsWithoutImprovement >= tabuMaxIterations)) {
						bestNeighbor = neighbor;
						bestNeighborLength = neighborLength;
					}
				}
			}

			// If the best neighbor is better than the current solution, move to the best neighbor and reset the counter for iterations without improvement
			if (bestNeighborLength < currentSolutionLength) {
				currentSolution = bestNeighbor;
				currentSolutionLength = bestNeighborLength;
				iterationsWithoutImprovement = 0;
			} else {
				// If no better solution was found, increment the counter for iterations without improvement
				iterationsWithoutImprovement++;
			}

			// Add the current solution to the tabu list
			tabuList.add(currentSolution);
			// If the tabu list is too large, remove the oldest solution
			if (tabuList.size() > tabuTenure) {
				tabuList.remove(0);
			}
		}

		// Return the best solution found
		return currentSolution;
	}

	/**
	 * Gets the length of a path
	 * @param pathList The path to get the length of
	 * @return The length of the path
	 */
	public double getPathLength(List<Integer> pathList) {
		double length = 0.0;
		for (int i = 0; i < pathList.size() - 1; i++) {
			int city1 = pathList.get(i);
			int city2 = pathList.get(i + 1);
			length += graph.getDistanceMatrix()[city1][city2];
		}
		return length;
	}
}
