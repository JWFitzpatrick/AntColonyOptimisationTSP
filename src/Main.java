public class Main {
	public static void main(String[] args) {
		gridSearch();
		// if(args[0].equals("findBestParamaters")){
		// 	gridSearch();
		// 	return;
		// }
		// if (args[0].equals("calculateBestNumberOfAnts")) {
		// 	calculateBestNumberOfAnts();
		// 	return;
		// }
		// if (args[0].equals("calculateBestEvaportationRate")) {
		// 	calculateBestEvaportationRate();
		// 	return;
		// }
		// if (args[0].equals("calculateBestHeuristicValue")) {
		// 	calculateBestHeuristicValue();
		// 	return;
		// }
		// if (args[0].equals("calculateBestPheromoneValue")) {
		// 	calculateBestPheromoneValue();
		// 	return;
		// }
		// if (args[0].equals("calculateBestACOApproach")) {
		// 	calculateBestACOApproach();
		// 	return;
		// }
		// if (args[0].equals("calculateBestSearchMode")) {
		// 	calculateBestSearchMode();
		// 	return;
		// }

// 		Best length: 19921.0
// Best number of ants: 100
// Best evaporation rate: 0.3
// Best q value: 10.0
// Best mode: 2
// Best heuristic value: 50.0
// Best search mode: 2
	}


	/**
	 * Calculates the best number of ants for the ACO algorithm
	 */
	public static void calculateBestNumberOfAnts(){
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/brazil58.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		int bestNumberOfAnts = 0;
		int[] amount = {10, 50, 100, 150, 200, 250, 300, 350, 400};
		for(int i = 0; i < amount.length; i++){
			aco = new AntColonyOptimisation(problem.graph, amount[i], 0.5, 100, 1, 0);
			double length = aco.getPathLength(aco.run(10000));
			if(length < bestLength){
				bestLength = length;
				bestNumberOfAnts = amount[i];
			}
			System.out.println("Number of ants: " + amount[i] + " Length: " + length);
		}
		System.out.println("Best number of ants: " + bestNumberOfAnts);
		System.out.println("Best length: " + bestLength);
	}

	/**
	 * Calculates the best evaporation rate for the ACO algorith
	 */
	public static void calculateBestEvaportationRate() {
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/brazil58.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		double bestRate = 0;
		double[] rates = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
		for(int i = 0; i < rates.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, rates[i], 100, 1, 0);
			double length = aco.getPathLength(aco.run(10000));
			if(length < bestLength){
				bestLength = length;
				bestRate = rates[i];
			}
			System.out.println("Rate: " + rates[i] + " Length: " + length);
		}
		System.out.println("Best alpha: " + bestRate);
		System.out.println("Best length: " + bestLength);
	}
	/**
	 * Calculates the best heuristic value for the ACO algorithm
	 */
	public static void calculateBestHeuristicValue() {
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/brazil58.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		double bestValue = 0;
		int[] values = {1, 5, 10, 50, 100};
		for(int i = 0; i < values.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100, values[i], 0);
			double length = aco.getPathLength(aco.run(10000));
			if(length < bestLength){
				bestLength = length;
				bestValue = values[i];
			}
			System.out.println("Weight: " + values[i] + " Length: " + length);
		}
		System.out.println("Best heuristic weight: " + bestValue);
		System.out.println("Best length: " + bestLength);
	}

	/**
	 * Calculates the best amount of pheromone to drop for the ACO algorithm
	 */
	public static void calculateBestPheromoneValue() {
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/brazil58.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		double bestWeight = 0;
		int[] values = {1, 5, 10, 50, 100};
		for(int i = 0; i < values.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, 0.5, values[i], 1, 0);
			double length = aco.getPathLength(aco.run(10000));
			if(length < bestLength){
				bestLength = length;
				bestWeight = values[i];
			}
			System.out.println("Value: " + values[i] + " Length: " + length);
		}
		System.out.println("Best pheromone value: " + bestWeight);
		System.out.println("Best length: " + bestLength);
	}

	/**
	 * Calculates the best ACO approach mode for the ACO algorithm
	 * 0 - Normal
	 * 1 - MMAS 
	 * 2 - Elitist
	 * 3 - ASrank
	 */
	public static void calculateBestACOApproach(){
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/brazil58.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		int[] modes = {0, 1, 2, 3};
		int bestMode = 0;
		for(int i = 0; i < modes.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100, 1, modes[i]);
			double length = aco.getPathLength(aco.run(10000));
			if(length < bestLength){
				bestLength = length;
				bestMode = i;
			}
			System.out.println("Mode: " + modes[i] + " Length: " + length);
		}
		System.out.println("Best length: " + bestLength);
		System.out.println("Best mode: " + bestMode);
	}

	/**
	 * Calculates the best search mode for the ACO algorithm
	 */
	public static void calculateBestSearchMode(){
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/brazil58.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		int[] modes = {0, 1, 2};
		int bestMode = 0;
		for(int i = 0; i < modes.length; i++){
			if(i == 2){
				aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100, 1, 0, modes[i], 10, 1000);
			}else {
				aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100, 1, 0, modes[i]);
			}
			double length = aco.getPathLength(aco.run(10000));
			if(length < bestLength){
				bestLength = length;
				bestMode = i;
			}
			System.out.println("SearchMode: " + modes[i] + " Length: " + length);
		}
		System.out.println("Best length: " + bestLength);
		System.out.println("Best search mode: " + bestMode);
	}

	public static void gridSearch() {
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/brazil58.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}

		// Define the range of values for each parameter
		int[] numberOfAntsValues = {50, 100, 150};
		double[] evaporationRateValues = {0.3, 0.5, 0.7};
		double[] Q = {10, 50, 100};
		int[] modes = {0, 1, 2, 3};
		int[] heuristicValues = {10, 50, 100};
		int[] searchModes = {0, 1, 2};

		double bestLength = Double.MAX_VALUE;
		int bestNumberOfAnts = 0;
		double bestEvaporationRate = 0;
		double bestQ = 0;
		int bestMode = 0;
		double bestHeuristicValue = 0;
		int bestSearchMode = 0;

		// Try every combination of parameter values
		AntColonyOptimisation aco;
		int i = 0;
		for (int numberOfAnts : numberOfAntsValues) {
			for (double evaporationRate : evaporationRateValues) {
				for (double q : Q) {
					for (int mode : modes) {
						for (int heuristicValue : heuristicValues) {
							for (int searchMode : searchModes) {
								if (searchMode == 2) {
									aco = new AntColonyOptimisation(problem.graph, numberOfAnts, evaporationRate, q, heuristicValue, mode, searchMode, 10, 1000);
								}else{
									aco = new AntColonyOptimisation(problem.graph, numberOfAnts, evaporationRate, q, heuristicValue, mode, searchMode);
								}
								double length = aco.getPathLength(aco.run(1000));
								if (length < bestLength) {
									bestLength = length;
									bestNumberOfAnts = numberOfAnts;
									bestEvaporationRate = evaporationRate;
									bestQ = q;
									bestMode = mode;
									bestHeuristicValue = heuristicValue;
									bestSearchMode = searchMode;
								}
								System.out.println(i);
								i++;
							}
						}
					}
				}
			}
		}

		System.out.println("Best length: " + bestLength);
		System.out.println("Best number of ants: " + bestNumberOfAnts);
		System.out.println("Best evaporation rate: " + bestEvaporationRate);
		System.out.println("Best q value: " + bestQ);
		System.out.println("Best mode: " + bestMode);
		System.out.println("Best heuristic value: " + bestHeuristicValue);
		System.out.println("Best search mode: " + bestSearchMode);
	}
}