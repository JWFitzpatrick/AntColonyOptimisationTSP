import java.util.ArrayList;
import java.util.List;

public class AntColonyOptimisation {
	List<Ant> ants;
	Graph graph;

	double evaporationRate;

	public AntColonyOptimisation(Graph graph, int numberOfAnts, double evaporationRate) {
		this.ants = new ArrayList<>();
		for (int i = 0; i < numberOfAnts; i++) {
			this.ants.add(new Ant(i % graph.getDistanceMatrix().length));
		}
		this.graph = graph;
		this.evaporationRate = evaporationRate;
	}

	public List<Integer> run(int maxIterations, int maxNoImprovementIterations) {
		graph.initializePheromoneMatrix();
		List<Integer> bestSolution = null;
		double bestSolutionLength = Double.MAX_VALUE;
		int noImprovementIterations = 0;

		for (int iteration = 0; iteration < maxIterations; iteration++) {
			for (Ant ant : ants) {
				ant.generatePath(graph, j -> 1.0 / graph.getDistanceMatrix()[ant.getCurrentCity()][j]);
			}
			updatePheromones();
			evaporatePheromones();

			for (Ant ant : ants) {
				double pathLength = ant.getPathLength(graph);
				if (pathLength < bestSolutionLength) {
					bestSolution = new ArrayList<>(ant.getPath());
					bestSolutionLength = pathLength;
					noImprovementIterations = 0;
				}
			}

			if (++noImprovementIterations >= maxNoImprovementIterations) {
				break;
			}
		}

		return bestSolution;
	}

	private void updatePheromones() {
		double Q = 100.0; // This is a constant. You can adjust this value based on your problem.

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
}
