package scraping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReadData {

	public File selectFile() {
	/*	JFileChooser browser = new JFileChooser();
		int value = browser.showOpenDialog(null);
		File file = null;
		if (value == JFileChooser.APPROVE_OPTION) {
			file = browser.getSelectedFile();
		}*/
		File file = new File("src/googlePlayStore.csv");
		return file;
	}

	public ArrayList<String> readCSV() {
		ArrayList<String> urls = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(selectFile()));
			String line = null;
			Scanner scanner = null;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				scanner = new Scanner(line);
				scanner.useDelimiter(";");
				String data = scanner.next().substring(1);
				urls.add(data);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urls;
	}

	public LinkedHashMap<Integer, ArrayList<Object>> retrieveData() throws IOException {
		
		LinkedHashMap<Integer, ArrayList<Object>> data_map = new LinkedHashMap<Integer, ArrayList<Object>>();
		Integer id=1;
		for(String url:readCSV()){
			ArrayList<Object> contents = new ArrayList<Object>();
			Document document = Jsoup.connect(url).get();
			String name = document.select(".id-app-title").text();
			String description = document.select("div.show-more-content.text-body").text();
			String rate = document.select(".score").text();
			String reviews_number = document.select(".reviews-num").text();	
		
			contents.add(name);
			contents.add(description);
			contents.add(rate);
			contents.add(reviews_number);
			
			Elements rate_information = document.select(".bar-number");
			for(Element element : rate_information){
				String rate_value = element.text();				
				contents.add(rate_value);
			}
			
			String whats_new = document.select(".recent-change").text();
			contents.add(whats_new);
			Elements additional_information = document.select(".meta-info");
			String date = additional_information.first().text();
			contents.add(date);
			String size = additional_information.get(1).text();
			contents.add(size);
			String installs = additional_information.get(2).text();
			contents.add(installs);
			String android = additional_information.get(4).text();
			contents.add(android);

			data_map.put(id, contents);
			id++;
		}
		return data_map;
	}

}
