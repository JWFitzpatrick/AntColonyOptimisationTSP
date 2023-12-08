public class TravellingSalesmanProblem {
	String name;
	String source;
	String description;

	int doublePrecision;

	int ignoredDigits;

	public TravellingSalesmanProblem(String name, String source, String description, int doublePresision,
								   int ignoredDigits) {
		this.name = name;
		this.source = source;
		this.description = description;
		this.doublePrecision = doublePresision;
		this.ignoredDigits = ignoredDigits;
	}

	public String getName() {
		return name;
	}

	public String getSource() {
		return source;
	}

	public String getDescription() {
		return description;
	}

	public int getDoublePrecision() {
		return doublePrecision;
	}

	public int getIgnoredDigits() {
		return ignoredDigits;
	}

	public String toString() {
		return name + " " + source + " " + description + " " + doublePrecision + " " + ignoredDigits;
	}

	public static void loadFile(String filename) {

	}
}
