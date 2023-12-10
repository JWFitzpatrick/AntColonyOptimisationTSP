import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AntColonyOptimisation {
	private List<Ant> ants;
	private Graph graph;

	private double evaporationRate;
	private double Q;
	private int heuristicValue;
	private int approachMode;
	private int searchMode;
	private int tabuTenure;
	private int tabuMaxIterations;

	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue, int approachMode) {
		this.ants = new ArrayList<>();
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

	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue, int apprachMode, int searchMode) {
		this.ants = new ArrayList<>();
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

	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate, double Q, int heuristicValue,int approachMode, int searchMode, int tabuTenure, int tabuMaxIterations) {
		this.ants = new ArrayList<>();
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

	public List<Integer> run(int maxIterations) {
		if(approachMode == 1){
			this.graph.initializePheromoneMatrixMMAS();
		}else{
			this.graph.initializePheromoneMatrix();
		}
		List<Integer> bestSolution = null;
		double bestSolutionLength = Double.MAX_VALUE;
		List<Integer> bestIterationSolution = null;
		double bestIterationPathLength = Double.MAX_VALUE;
		// int numberOfTopAnts = (int) Math.round(ants.size() * 0.1);
		int numberOfTopAnts = 2;
		PriorityQueue<Ant> topAnts = new PriorityQueue<>(numberOfTopAnts, Comparator.comparing(a -> a.getPathLength(graph)));
		for (int iteration = 0; iteration < maxIterations; iteration++) {
			topAnts.clear();
			for (Ant ant : ants) {
				ant.generatePath(graph, j -> heuristicValue / graph.getDistanceMatrix()[ant.getCurrentCity()][j]);
			}
			if (approachMode != 1 || approachMode != 3) {
				updatePheromones();
				evaporatePheromones();
			}

			for (Ant ant : ants) {
				double pathLength = ant.getPathLength(graph);
				if (pathLength < bestSolutionLength) {
					bestSolution = new ArrayList<>(ant.getPath());
					bestSolutionLength = pathLength;
				}
				if(approachMode == 1){
					bestIterationSolution = null;
					bestIterationPathLength = Double.MAX_VALUE;
					if (pathLength < bestIterationPathLength) {
						bestIterationSolution = new ArrayList<>(ant.getPath());
						bestIterationPathLength = pathLength;
					}
				}
				if (approachMode == 3){
					topAnts.add(ant);
					if (topAnts.size() > numberOfTopAnts) {
						topAnts.poll(); 
					}
				}
			}
			if (approachMode == 1) {
				double pheromoneToAdd = Q / bestIterationPathLength;
				for (int i = 0; i < bestIterationSolution.size() - 1; i++) {
					int city1 = bestIterationSolution.get(i);
					int city2 = bestIterationSolution.get(i + 1);
					graph.getPheromoneMatrix()[city1][city2] += pheromoneToAdd;
					graph.getPheromoneMatrix()[city2][city1] += pheromoneToAdd;
				}
				evaporatePheromones();
				graph.ensurePheromoneLimit();
			}
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
