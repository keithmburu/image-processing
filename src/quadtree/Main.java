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
						width = Integer.parseInt(line.split("\\s+")[0]);
					} 
					preface.append(line).append("\n");
					wait++;
					continue;
				} else {
					String[] lineArr = line.split("\\s+");
					numbers.addAll(Arrays.asList(lineArr));
					// System.out.println(Arrays.asList(lineArr));
				}
			}
			// System.out.println(preface + "\n");
			br.close();
			String[] fileLine = new String[width * 3];
			int column = 0;
			for (String number : numbers) {
				if (column >= width * 3) {
					column = 0;
					fileLines.add(fileLine);
					// for (int i = 0; i < fileLine.length; i++) {
					// 	System.out.print(fileLine[i]+ " ");
					// }
					fileLine = new String[width * 3];
				}
				fileLine[column] = number;
				column++;
			}
		} 
		QuadTree QT = new QuadTree(fileLines);
		// if (outline) {
		// 	QT.outline(false).toPPM(String.format("../images/input%s.ppm", outlineArg), preface.toString());
		// } else {
		// 	QT.toPPM(String.format("../images/input%s.ppm", outlineArg), preface.toString());
		// }
		switch(task) {
			case "All":
				QT.compress(0.5, outline).toPPM(String.format("../images/%s-c%s.ppm", outputFilename, outlineArg), preface.toString());
				QT.edge_detect(outline).toPPM(String.format("../images/%s-e%s.ppm", outputFilename, outlineArg), preface.toString());
				QT.filter("Grayscale").toPPM(String.format("../images/%s-f-g%s.ppm", outputFilename, outlineArg), preface.toString());
				QT.filter("Negative").toPPM(String.format("../images/%s-f-n%s.ppm", outputFilename, outlineArg), preface.toString());
				QT.filter("Tint").toPPM(String.format("../images/%s-f-t%s.ppm", outputFilename, outlineArg), preface.toString());
				break;
			case "Compression":
				QT.compress(0.5, outline).toPPM(String.format("../images/%s-c%s.ppm", outputFilename, outlineArg), preface.toString());
				break;
			case "Edge Detection":
				QT.edge_detect(outline).toPPM(String.format("../images/%s-e%s.ppm", outputFilename, outlineArg), preface.toString());
				break;
			case "Filter":
				QT.filter("Grayscale").toPPM(String.format("../images/%s-f-g%s.ppm", outputFilename, outlineArg), preface.toString());
				QT.filter("Negative").toPPM(String.format("../images/%s-f-n%s.ppm", outputFilename, outlineArg), preface.toString());
				QT.filter("Tint").toPPM(String.format("../images/%s-f-t%s.ppm", outputFilename, outlineArg), preface.toString());
				break;
			default:
				System.out.println("Undefined task!");;
				System.exit(3);
		}
	}
}
