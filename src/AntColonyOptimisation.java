import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AntColonyOptimisation {
	private List<Ant> ants;
	private Graph graph;

	private double evaporationRate;
	private double Q;
	private int heuristicValue;
	private int searchMode;
	private int tabuTenure;
	private int tabuMaxIterations;

	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue) {
		this.ants = new ArrayList<>();
		for (int i = 0; i < numberOfAnts; i++) {
			this.ants.add(new Ant(i % graph.getDistanceMatrix().length));
		}
		this.graph = graph;
		this.evaporationRate = evaporationRate;
		this.Q = Q;
		this.heuristicValue = heuristicValue;
		this.searchMode = 0;
	}

	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue, int searchMode) {
		this.ants = new ArrayList<>();
		for (int i = 0; i < numberOfAnts; i++) {
			this.ants.add(new Ant(i % graph.getDistanceMatrix().length));
		}
		this.graph = graph;
		this.evaporationRate = evaporationRate;
		this.Q = Q;
		this.heuristicValue = heuristicValue;
		this.searchMode = searchMode;
		if (this.searchMode == 2) {
			System.out.println("You must enter the tabuTenure and max iterations without improvements to use tabuSearch.");
		}
	}

	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue, int searchMode, int tabuTenure, int tabuMaxIterations) {
		this.ants = new ArrayList<>();
		for (int i = 0; i < numberOfAnts; i++) {
			this.ants.add(new Ant(i % graph.getDistanceMatrix().length));
		}
		this.graph = graph;
		this.evaporationRate = evaporationRate;
		this.Q = Q;
		this.heuristicValue = heuristicValue;
		this.searchMode = searchMode;
		this.tabuTenure = tabuTenure;
		this.tabuMaxIterations = tabuMaxIterations;
	}

	public List<Integer> run(int maxIterations) {
		this.graph.initializePheromoneMatrix();
		List<Integer> bestSolution = null;
		double bestSolutionLength = Double.MAX_VALUE;

		for (int iteration = 0; iteration < maxIterations; iteration++) {
			for (Ant ant : ants) {
				ant.generatePath(graph, j -> heuristicValue / graph.getDistanceMatrix()[ant.getCurrentCity()][j]);
			}
			updatePheromones();
			evaporatePheromones();

			for (Ant ant : ants) {
				double pathLength = ant.getPathLength(graph);
				if (pathLength < bestSolutionLength) {
					bestSolution = new ArrayList<>(ant.getPath());
					bestSolutionLength = pathLength;
				}
			}
		}
		if (this.searchMode == 1) {
			bestSolution = hillClimbingSearch(bestSolution);	
		}
		if (this.searchMode == 2) {
			bestSolution = tabuSearch(bestSolution);
		}
		return bestSolution;
	}

	private void updatePheromones() {
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

	private void evaporatePheromones() {
		double[][] pheromoneMatrix = graph.getPheromoneMatrix();
		for (int i = 0; i < pheromoneMatrix.length; i++) {
			for (int j = 0; j < pheromoneMatrix[i].length; j++) {
				pheromoneMatrix[i][j] *= evaporationRate;
			}
		}
	}

		public List<Integer> hillClimbingSearch(List<Integer> initialSolution) {
		List<Integer> currentSolution = new ArrayList<>(initialSolution);
		double currentSolutionLength = getPathLength(currentSolution);

		while (true) {
			List<Integer> bestNeighbor = null;
			double bestNeighborLength = Double.MAX_VALUE;

			for (int i = 0; i < currentSolution.size() - 1; i++) {
				for (int j = i + 1; j < currentSolution.size(); j++) {
					List<Integer> neighbor = new ArrayList<>(currentSolution);
					Collections.swap(neighbor, i, j);
					double neighborLength = getPathLength(neighbor);

					if (neighborLength < bestNeighborLength) {
						bestNeighbor = neighbor;
						bestNeighborLength = neighborLength;
					}
				}
			}

			if (bestNeighborLength < currentSolutionLength) {
				currentSolution = bestNeighbor;
				currentSolutionLength = bestNeighborLength;
			} else {
				break;
			}
		}
		

		return currentSolution;
	}

	public List<Integer> tabuSearch(List<Integer> initialSolution) {
		List<Integer> currentSolution = new ArrayList<>(initialSolution);
		double currentSolutionLength = getPathLength(currentSolution);
		List<List<Integer>> tabuList = new LinkedList<>();

		int iterationsWithoutImprovement = 0;
		while (iterationsWithoutImprovement < tabuMaxIterations) {
			List<Integer> bestNeighbor = null;
			double bestNeighborLength = Double.MAX_VALUE;

			for (int i = 0; i < currentSolution.size() - 1; i++) {
				for (int j = i + 1; j < currentSolution.size(); j++) {
					List<Integer> neighbor = new ArrayList<>(currentSolution);
					Collections.swap(neighbor, i, j);
					double neighborLength = getPathLength(neighbor);

					if ((neighborLength < bestNeighborLength) && (!tabuList.contains(neighbor) || iterationsWithoutImprovement >= tabuMaxIterations)) {
						bestNeighbor = neighbor;
						bestNeighborLength = neighborLength;
					}
				}
			}

			if (bestNeighborLength < currentSolutionLength) {
				currentSolution = bestNeighbor;
				currentSolutionLength = bestNeighborLength;
				iterationsWithoutImprovement = 0;
			} else {
				iterationsWithoutImprovement++;
			}

			tabuList.add(currentSolution);
			if (tabuList.size() > tabuTenure) {
				tabuList.remove(0);
			}
		}

    	return currentSolution;
	}

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
