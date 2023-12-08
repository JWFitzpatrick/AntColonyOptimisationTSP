import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TravellingSalesmanProblem {
	String name;
	String source;
	String description;

	int doublePrecision;

	int ignoredDigits;

	Graph graph;

	public TravellingSalesmanProblem(String name, String source, String description, int doublePrecision,
								   int ignoredDigits, Graph graph) {
		this.name = name;
		this.source = source;
		this.description = description;
		this.doublePrecision = doublePrecision;
		this.ignoredDigits = ignoredDigits;
		this.graph = graph;
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

	public Graph getGraph() {
		return graph;
	}

	public String toString() {
		return name + " " + source + " " + description + " " + doublePrecision + " " + ignoredDigits;
	}

	public static TravellingSalesmanProblem loadFile(String filename) {
		try {
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
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

			NodeList verticesList = doc.getElementsByTagName("vertex");
			double[][] distanceMatrix = new double[verticesList.getLength()][verticesList.getLength()];

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
			Graph graph = new Graph(distanceMatrix);
			return new TravellingSalesmanProblem(name, source, description, doublePrecision,
					ignoredDigits, graph);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
