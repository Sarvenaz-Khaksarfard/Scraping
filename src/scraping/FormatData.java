package scraping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FormatData {
	ReadData rd = new ReadData();
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder;

	public void createXmlFile() {
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.newDocument();
			Element rootElement = document.createElementNS("https://play.google.com/store?hl=en", "Applications");
			document.appendChild(rootElement);

			for (Map.Entry<Integer, ArrayList<Object>> entry : rd.retrieveData().entrySet()) {
				int i = 0;

				Element application = document.createElement("Application");
				application.setAttribute("Id", entry.getKey().toString());

				application.appendChild(createNodes(document, "Name", entry.getValue().get(i++).toString()));
				application.appendChild(createNodes(document, "Description", entry.getValue().get(i++).toString()));
				application.appendChild(createNodes(document, "Rate", entry.getValue().get(i++).toString()));
				application.appendChild(createNodes(document, "Reviews", entry.getValue().get(i++).toString()));
				Element Rate_scores = document.createElement("Rate_Scores");
				Rate_scores.appendChild(createNodes(document, "Five", entry.getValue().get(i++).toString()));
				Rate_scores.appendChild(createNodes(document, "Four", entry.getValue().get(i++).toString()));
				Rate_scores.appendChild(createNodes(document, "Three", entry.getValue().get(i++).toString()));
				Rate_scores.appendChild(createNodes(document, "Two", entry.getValue().get(i++).toString()));
				Rate_scores.appendChild(createNodes(document, "One", entry.getValue().get(i++).toString()));
				application.appendChild(Rate_scores);
				application.appendChild(createNodes(document, "Changes", entry.getValue().get(i++).toString()));
				application.appendChild(createNodes(document, "Updated_data", entry.getValue().get(i++).toString()));
				application.appendChild(createNodes(document, "Size", entry.getValue().get(i++).toString()));
				application.appendChild(createNodes(document, "Downloads", entry.getValue().get(i++).toString()));
				application.appendChild(createNodes(document, "Requires_android", entry.getValue().get(i++).toString()));

				rootElement.appendChild(application);
				// }
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);

			StreamResult result = new StreamResult(new File("src/scraping.xml"));
			transformer.transform(source, result);

			System.out.println("Done");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Node createNodes(Document document, String name, String value) {
		Element node = document.createElement(name);
		node.appendChild(document.createTextNode(value));
		return node;
	}
}
