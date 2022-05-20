package quadtree;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main { 

	public static void main(String[] args) throws IOException {

		String task = "";
		StringBuilder preface = new StringBuilder();
		String inputFilename = "";
		String outputFilename = "";
		boolean outline = false;
		String outlineArg = "";

		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-i")) {
				//<filename> indicates the input file
				inputFilename = args[i+1];
				outputFilename = inputFilename.substring(0, inputFilename.length() - 4);
			} 
			if(args[i].equals("-o")) {
				//<filename> indicates the root name of the output file that your program should
				//write to
				outputFilename = args[i+1];
			}
			if(args[i].equals("-a")) {
				//indicates that you should perform image compression and edge detection and filter
				task = "All";
			} else if(args[i].equals("-c")) {
			 	//indicates that you should perform image compression
				task = "Compression";
			} else if(args[i].equals("-e")) {
				//indicates that you should perform edge detection
				task = "Edge Detection";
			} else if(args[i].equals("-f")) {
				task = "Filter";
			}
			if (args[i].equals("-t")) {
				//indicates that output images should have the quadtree outlined
				outline = true;
			}
		}
		if (outline) {
			outlineArg = "-t";
		}
		if (inputFilename.equals("")) {
			System.out.println("Input filename not provided!");
			System.exit(1);
		}
		Pattern fileType = Pattern.compile(".ppm");
		Matcher file = fileType.matcher("../images/" + inputFilename);
		boolean match = file.find();
		ArrayList<String[]> fileLines = new ArrayList<>();
		int length = 0;
		int width = 0;
		if (match) {
			ArrayList<String> numbers = new ArrayList<String>();
			Path inputPath = Paths.get("../images/" + inputFilename);
			if (Files.notExists(inputPath)) {
				System.out.println("../images/" + inputFilename + " not found!");
				System.exit(2);
			}
			BufferedReader br = new BufferedReader(new FileReader(new File("../images/" + inputFilename)));
			String line;
			int wait = 0;
			while ((line = br.readLine()) != null) {
				if (line.length() != 0 && line.substring(0, 1).equals("#")) {
					preface.append(line).append("\n");
					continue;
				} else if (wait < 3) {
					if (wait == 1) {
						String[] dims = line.split("\\s+");
						length = Integer.parseInt(dims[0]) * 3;
						width = Integer.parseInt(dims[1]);
					} 
					preface.append(line).append("\n");
					wait++;
					continue;
				} else {
					String[] lineArr = line.split("\\s+");
					numbers.addAll(Arrays.asList(lineArr));
				}
			}
			br.close();
			String[] fileLine = new String[length];
			int column = 0;
			for (int i = 0; i < numbers.size(); i++) {
				if (column >= length || i == numbers.size()) {
					column = 0;
					fileLines.add(fileLine);
					fileLine = new String[length];
				}
				fileLine[column] = numbers.get(i);
				column++;
			}
		} 

		QuadTree QT = new QuadTree(fileLines);
		switch(task) {
			case "All":
				boolean edgeDetection = false;
				QT.compress(0.5, edgeDetection, outline).toPPM(String.format("../images/%s-c%s.ppm", outputFilename, outlineArg), preface.toString());
				QT.edge_detect().toPPM(String.format("../images/%s-e.ppm", outputFilename), preface.toString());
				QT.filter("Grayscale").toPPM(String.format("../images/%s-f-g.ppm", outputFilename), preface.toString());
				QT.filter("Negative").toPPM(String.format("../images/%s-f-n.ppm", outputFilename), preface.toString());
				QT.filter("BlueLight").toPPM(String.format("../images/%s-f-t.ppm", outputFilename), preface.toString());
				break;
			case "Compression":
				edgeDetection = false;
				QT.compress(0.5, edgeDetection, outline).toPPM(String.format("../images/%s-c%s.ppm", outputFilename, outlineArg), preface.toString());
				break;
			case "Edge Detection":
				QT.edge_detect().toPPM(String.format("../images/%s-e.ppm", outputFilename), preface.toString());
				break;
			case "Filter":
				QT.filter("Grayscale").toPPM(String.format("../images/%s-f-g.ppm", outputFilename), preface.toString());
				QT.filter("Negative").toPPM(String.format("../images/%s-f-n.ppm", outputFilename), preface.toString());
				QT.filter("BlueLight").toPPM(String.format("../images/%s-f-t.ppm", outputFilename), preface.toString());
				break;
			default:
				System.out.println("Undefined task!");;
				System.exit(3);
		}
	}
}
