import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Ant {
	int startCity;
	double fitness;
	List<Integer> path;


    public Ant(int startCity) {
        this.startCity = startCity;
    }

	public void generatePath(Graph graph, Function<Integer, Double> heuristic) {
		double[][] distanceMatrix = graph.getDistanceMatrix();
		path = new ArrayList<>();
		boolean[] visited = new boolean[distanceMatrix.length];
		int currentCity = startCity;

		visited[currentCity] = true;
		path.add(currentCity);

		while (path.size() < distanceMatrix.length) {
			double sum = 0.0;
			double[] probabilities = new double[distanceMatrix.length];

			for (int i = 0; i < distanceMatrix.length; i++) {
				if (!visited[i]) {
					double heuristicValue = heuristic.apply(i);
					double pheromoneValue = graph.getPheromoneMatrix()[currentCity][i];
					probabilities[i] = pheromoneValue * heuristicValue;
					sum += probabilities[i];
				}
			}

			for (int i = 0; i < probabilities.length; i++) {
				if(sum != 0){
					probabilities[i] /= sum;
				}else{
					System.out.println("ERROR");
				}
			}

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

			if (nextCity == -1) {
				throw new RuntimeException("No city selected for the next move.");
			}
			visited[nextCity] = true;
			path.add(nextCity);
			currentCity = nextCity;
		}
	}

	public List<Integer> hillClimbingLocalSearch(Graph graph, List<Integer> initialSolution) {
		List<Integer> currentSolution = new ArrayList<>(initialSolution);
		double currentSolutionLength = getPathLength(graph, currentSolution);

		while (true) {
			List<Integer> bestNeighbor = null;
			double bestNeighborLength = Double.MAX_VALUE;

			for (int i = 0; i < currentSolution.size() - 1; i++) {
				for (int j = i + 1; j < currentSolution.size(); j++) {
					List<Integer> neighbor = new ArrayList<>(currentSolution);
					Collections.swap(neighbor, i, j);
					double neighborLength = getPathLength(graph, neighbor);

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

	public List<Integer> tabuSearchLocalSearch(Graph graph, List<Integer> initialSolution, int tabuTenure, int maxIterationsWithoutImprovement) {
    List<Integer> currentSolution = new ArrayList<>(initialSolution);
    double currentSolutionLength = getPathLength(graph, currentSolution);
    List<List<Integer>> tabuList = new LinkedList<>();

    int iterationsWithoutImprovement = 0;
    while (iterationsWithoutImprovement < maxIterationsWithoutImprovement) {
        List<Integer> bestNeighbor = null;
        double bestNeighborLength = Double.MAX_VALUE;

        for (int i = 0; i < currentSolution.size() - 1; i++) {
            for (int j = i + 1; j < currentSolution.size(); j++) {
                List<Integer> neighbor = new ArrayList<>(currentSolution);
                Collections.swap(neighbor, i, j);
                double neighborLength = getPathLength(graph, neighbor);

                if ((neighborLength < bestNeighborLength) && (!tabuList.contains(neighbor) || iterationsWithoutImprovement >= maxIterationsWithoutImprovement)) {
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

	public double getPathLength(Graph graph, List<Integer> pathList) {
		double length = 0.0;
		for (int i = 0; i < pathList.size() - 1; i++) {
			int city1 = pathList.get(i);
			int city2 = pathList.get(i + 1);
			length += graph.getDistanceMatrix()[city1][city2];
		}
		return length;
	}

	public int getCurrentCity() {
		return path.get(path.size() - 1);
	}

	public List<Integer> getPath(){
		return path;
	}

	public double getPathLength(Graph graph) {
		double length = 0.0;
		for (int i = 0; i < path.size() - 1; i++) {
			int city1 = path.get(i);
			int city2 = path.get(i + 1);
			length += graph.getDistanceMatrix()[city1][city2];
		}
		return length;
	}
}
