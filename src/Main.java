public class Main {
	public static void main(String[] args) {
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/burma14.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco = new AntColonyOptimisation(problem.graph, 10000, 0.5, 100, 1, 2, 100, 1000);
		System.out.println(aco.getPathLength(aco.run(10000)));
	}
}