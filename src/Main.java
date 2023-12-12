import java.util.ArrayList;
import java.util.List;

public class Main {

	// The path to the data file to use for grid search and
	// looking at the effect of modifying parameters.
	public static final String DATA_FILE = "../data/brazil58.xml";

	/**
	 * Main method for running the application
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java Main <filePath>");
			System.out.println("Usage: java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode>");
			System.out.println("Usage: java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode> <searchMode>");
			System.out.println("Usage: java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode> <searchMode> <tabuSize> <tabuIterations>");
			System.out.println("Usage: java Main calculateBestNumberOfAnts");
			System.out.println("Usage: java Main calculateBestEvaportationRate");
			System.out.println("Usage: java Main calculateBestHeuristicValue");
			System.out.println("Usage: java Main calculateBestPheromoneValue");
			System.out.println("Usage: java Main calculateBestACOApproach");
			System.out.println("Usage: java Main calculateBestSearchMode");
			System.out.println("Usage: java Main findBestParamaters");
			return;
		}
		if (args[0].equals("help")) {	
			System.out.println("Usage: java Main <filePath>");
			System.out.println("Usage: java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode>");
			System.out.println("Usage: java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode> <searchMode>");
			System.out.println("Usage: java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode> <searchMode> <tabuSize> <tabuIterations>");
			System.out.println("Usage: java Main calculateBestNumberOfAnts");
			System.out.println("Usage: java Main calculateBestEvaportationRate");
			System.out.println("Usage: java Main calculateBestHeuristicValue");
			System.out.println("Usage: java Main calculateBestPheromoneValue");
			System.out.println("Usage: java Main calculateBestACOApproach");
			System.out.println("Usage: java Main calculateBestSearchMode");
			System.out.println("Usage: java Main findBestParamaters");
			return;
		}
		if(args[0].equals("findBestParamaters")){
			gridSearch();
			return;
		}
		if (args[0].equals("calculateBestNumberOfAnts")) {
			calculateBestNumberOfAnts();
			return;
		}
		if (args[0].equals("calculateBestEvaportationRate")) {
			calculateBestEvaportationRate();
			return;
		}
		if (args[0].equals("calculateBestHeuristicValue")) {
			calculateBestHeuristicValue();
			return;
		}
		if (args[0].equals("calculateBestPheromoneValue")) {
			calculateBestPheromoneValue();
			return;
		}
		if (args[0].equals("calculateBestACOApproach")) {
			calculateBestACOApproach();
			return;
		}
		if (args[0].equals("calculateBestSearchMode")) {
			calculateBestSearchMode();
			return;
		}
		// Load the problem from the file
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(args[0]);
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		if (args.length == 1) {
			AntColonyOptimisation aco = new AntColonyOptimisation(problem.graph,
			 10, 0.5, 100, 1, 0,
			  2, 10, 1000);
			List<Integer> path = aco.run(10000);
			System.out.println("Path: " + path);
			System.out.println("Length: " + aco.getPathLength(path));
		}
		if (args.length == 6) {
			int ants = Integer.parseInt(args[1]);
			double rate = Double.parseDouble(args[2]);
			double q = Double.parseDouble(args[3]);
			int heuristic = Integer.parseInt(args[4]);
			int mode = Integer.parseInt(args[5]);
			AntColonyOptimisation aco = new AntColonyOptimisation(problem.graph, ants, rate, q,
			 heuristic, mode);
			List<Integer> path = aco.run(10000);
			System.out.println("Path: " + path);
			System.out.println("Length: " + aco.getPathLength(path));
		}
		if (args.length == 7) {
			int ants = Integer.parseInt(args[1]);
			double rate = Double.parseDouble(args[2]);
			double q = Double.parseDouble(args[3]);
			int heuristic = Integer.parseInt(args[4]);
			int mode = Integer.parseInt(args[5]);
			int searchMode = Integer.parseInt(args[6]);
			AntColonyOptimisation aco = new AntColonyOptimisation(problem.graph, ants, rate, q,
			 heuristic, mode, searchMode);
			List<Integer> path = aco.run(10000);
			System.out.println("Path: " + path);
			System.out.println("Length: " + aco.getPathLength(path));
		}
		if (args.length == 9) {
			int ants = Integer.parseInt(args[1]);
			double rate = Double.parseDouble(args[2]);
			double q = Double.parseDouble(args[3]);
			int heuristic = Integer.parseInt(args[4]);
			int mode = Integer.parseInt(args[5]);
			int searchMode = Integer.parseInt(args[6]);
			int tabuSize = Integer.parseInt(args[7]);
			int tabuIterations = Integer.parseInt(args[8]);
			AntColonyOptimisation aco = new AntColonyOptimisation(problem.graph, ants, rate, q,
			 heuristic, mode, searchMode, tabuSize, tabuIterations);
			List<Integer> path = aco.run(10000);
			System.out.println("Path: " + path);
			System.out.println("Length: " + aco.getPathLength(path));
		}
	}


	/**
	 * Calculates the best number of ants for the ACO algorithm
	 */
	public static void calculateBestNumberOfAnts(){
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(DATA_FILE);
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		int bestNumberOfAnts = 0;
		int[] amount = {10, 50, 100, 150, 200, 250, 300, 350, 400};
		for(int i = 0; i < amount.length; i++){
			aco = new AntColonyOptimisation(problem.graph, amount[i], 0.5, 100,
			 1, 0);
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
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(DATA_FILE);
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		double bestRate = 0;
		double[] rates = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
		for(int i = 0; i < rates.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, rates[i], 100,
			 1, 0);
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
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(DATA_FILE);
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		double bestValue = 0;
		int[] values = {1, 5, 10, 50, 100};
		for(int i = 0; i < values.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100,
			 values[i], 0);
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
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(DATA_FILE);
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		double bestWeight = 0;
		int[] values = {1, 5, 10, 50, 100};
		for(int i = 0; i < values.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, 0.5, values[i],
			 1, 0);
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
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(DATA_FILE);
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco;
		double bestLength = Double.MAX_VALUE;
		int[] modes = {0, 1, 2, 3};
		int bestMode = 0;
		for(int i = 0; i < modes.length; i++){
			aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100,
			 1, modes[i]);
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
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(DATA_FILE);
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
				aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100,
				 1, 0, modes[i], 10, 1000);
			}else {
				aco = new AntColonyOptimisation(problem.graph, 50, 0.5, 100,
				 1, 0, modes[i]);
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
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile(DATA_FILE);
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}

		// Define the range of values for each parameter
		int[] numberOfAntsValues = {10, 50, 100, 150, 300};
		double[] evaporationRateValues = {0.3, 0.5, 0.7};
		double[] Q = {10, 50, 100};
		int[] modes = {0, 1, 2, 3};
		int[] heuristicValues = {1, 10, 50, 100};
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
		for (int numberOfAnts : numberOfAntsValues) {
			for (double evaporationRate : evaporationRateValues) {
				for (double q : Q) {
					for (int mode : modes) {
						for (int heuristicValue : heuristicValues) {
							for (int searchMode : searchModes) {
								if (searchMode == 2) {
									aco = new AntColonyOptimisation(problem.graph, numberOfAnts, evaporationRate,
									 q, heuristicValue, mode, searchMode, 10, 10000);
								}else{
									aco = new AntColonyOptimisation(problem.graph, numberOfAnts, evaporationRate,
									 q, heuristicValue, mode, searchMode);
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