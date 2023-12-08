public class Main {
	public static void main(String[] args) {
		TravellingSalesmanProblem problem = TravellingSalesmanProblem.loadFile("data/burma14.xml");
		if (problem == null) {
			System.out.println("Error loading file");
			return;
		}
		AntColonyOptimisation aco = new AntColonyOptimisation(problem.graph, 20000, 0.5);
		System.out.println(aco.run(10000000, 1000));
	}
}