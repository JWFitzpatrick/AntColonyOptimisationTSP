import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Class to store the information from the loaded file
 */
public class TravellingSalesmanProblem {
	// Stores the name of the problem
	String name;
	// Stores the source of the problem
	String source;
	// Stores the description of the problem
	String description;
	// Stores the double precision of the problem
	int doublePrecision;
	// Stores the number of ignored digits of the problem
	int ignoredDigits;
	// Stores the graph of the problem
	Graph graph;

	/**
	 * Constructor for the TravellingSalesmanProblem class
	 * @param name the name of the problem
	 * @param source the source of the problem
	 * @param description the description of the problem
	 * @param doublePrecision the double precision of the problem
	 * @param ignoredDigits the number of ignored digits of the problem
	 * @param graph the graph of the problem
	 */
	public TravellingSalesmanProblem(String name, String source, String description, int doublePrecision,
								   int ignoredDigits, Graph graph) {
		this.name = name;
		this.source = source;
		this.description = description;
		this.doublePrecision = doublePrecision;
		this.ignoredDigits = ignoredDigits;
		this.graph = graph;
	}

	/**
	 * Getter for the name of the problem
	 * @return the name of the problem
	 */
	public String getName() {
		return name;
	}
	/**
	 * Getter for the source of the problem
	 * @return the source of the problems
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Getter for the description of the problem
	 * @return the description of the problem
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter for the double precision of the problem
	 * @return the double precision of the problem
	 */
	public int getDoublePrecision() {
		return doublePrecision;
	}

	/**
	 * Getter for the number of ignored digits of the problem 
	 * @return the number of ignored digits of the problem
	 */
	public int getIgnoredDigits() {
		return ignoredDigits;
	}

	/**
	 * Getter for the graph of the problem
	 * @return the graph of the problem
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Prints the information of the problem to the console
	 */
	public String toString() {
		return name + " " + source + " " + description + " " + doublePrecision + " " + ignoredDigits;
	}

	/**
	 * Loads the file and stores the information in a TravellingSalesmanProblem object
	 * @param filename the name of the file to be loaded
	 * @return the TravellingSalesmanProblem object
	 */
	public static TravellingSalesmanProblem loadFile(String filename) {
		try {
			// Load the file
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			// Get the information from the file
			NodeList node = doc.getElementsByTagName("name");
			String name = node.item(0).getTextContent();
			node = doc.getElementsByTagName("source");
			String source = node.item(0).getTextContent();
			node = doc.getElementsByTagName("description");
			String description = node.item(0).getTextContent();
			node = doc.getElementsByTagName("doublePrecision");
			int doublePrecision = Integer.parseInt(node.item(0).getTextContent());
			node = doc.getElementsByTagName("ignoredDigits");
			int ignoredDigits = Integer.parseInt(node.item(0).getTextContent());

			// Get the distance matrix
			NodeList verticesList = doc.getElementsByTagName("vertex");
			double[][] distanceMatrix = new double[verticesList.getLength()][verticesList.getLength()];
			// Load the distance matrix with information from the file.
			for (int i = 0; i < verticesList.getLength(); i++) {
				Node vertexNode = verticesList.item(i);
				if (vertexNode.getNodeType() == Node.ELEMENT_NODE) {
					Element vertexElement = (Element) vertexNode;
					NodeList edgeList = vertexElement.getElementsByTagName("edge");
					for (int j = 0; j < edgeList.getLength(); j++) {
						Element edgeElement = (Element) edgeList.item(j);
						double cost = Double.parseDouble(edgeElement.getAttribute("cost"));
						int destination = Integer.parseInt(edgeElement.getTextContent());
						distanceMatrix[i][destination] = cost;
					}
				}
			}
			// Create a new graph object
			Graph graph = new Graph(distanceMatrix);
			// Create and return a new TravellingSalesmanProblem object
			return new TravellingSalesmanProblem(name, source, description, doublePrecision,
					ignoredDigits, graph);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
