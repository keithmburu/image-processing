package quadtree;
 
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class QuadTree {

    private ArrayList<String[]> fileLines = null;
    private int[] meanColor = new int[]{0, 0, 0};
    private int pixels = 0;
    private int length = 0;
    private int width = 0;
    private boolean outline = false;
    private QuadTree node1 = null;
    private QuadTree node2 = null;
    private QuadTree node3 = null;
    private QuadTree node4 = null;
    private boolean node1Null = true;
    private boolean node2Null = true;
    private boolean node3Null = true;
    private boolean node4Null = true;
    private ArrayList<QuadTree> nodes = null;


    public QuadTree(ArrayList<String[]> fileLines) {
        if (fileLines == null) {
            System.out.println("Blank image!");
            System.exit(9);
        }
        else {
            this.fileLines = fileLines;
            length = (fileLines != null) ? fileLines.get(0).length : 0;
            width = (fileLines != null) ? fileLines.size() : 0;

            int Rtotal = 0;
            int Gtotal = 0;
            int Btotal = 0;
            for (int line = 0; line < width; line++) {
                for (int col = 0; col < length-2; col += 3) {
                    pixels++;
                    Rtotal += (!fileLines.get(line)[col].equals("")) ? Integer.parseInt(fileLines.get(line)[col]) : 0;
                    Gtotal += (!fileLines.get(line)[col + 1].equals("")) ? Integer.parseInt(fileLines.get(line)[col + 1]) : 0;
                    Btotal += (!fileLines.get(line)[col + 2].equals("")) ? Integer.parseInt(fileLines.get(line)[col + 2]) : 0;
                }
            }
            int Raverage = (pixels == 0) ? 0 : Rtotal / pixels;
            int Gaverage = (pixels == 0) ? 0 : Gtotal / pixels;
            int Baverage = (pixels == 0) ? 0 : Btotal / pixels;

            meanColor[0] = Raverage;
            meanColor[1] = Gaverage;
            meanColor[2] = Baverage;

            if (width == 1 && length == 3) {
                node1 = null;
                node2 = null;
                node3 = null;
                node4 = null;
            } else {
                int node13length = (length == 3) ? 3
                        : (((length % 2 == 0) ? (length / 2)
                        : ((length - 1) / 2) + 2));
                int node24length = length - node13length;

                ArrayList<String[]> node1FileLines = new ArrayList<String[]>();
                String[] node1FileLine = new String[node13length];
                ArrayList<String[]> node2FileLines = new ArrayList<String[]>();
                String[] node2FileLine = new String[node24length];
                ArrayList<String[]> node3FileLines = new ArrayList<String[]>();
                String[] node3FileLine = new String[node13length];
                ArrayList<String[]> node4FileLines = new ArrayList<String[]>();
                String[] node4FileLine = new String[node24length];
                int pos1 = 0;
                int pos2 = 0;
                int pos3 = 0;
                int pos4 = 0;

                int half = (width % 2 == 0) ? (width / 2) - 1 : (width - 1) / 2;
                for (int line = 0; line < width; line++) {
                    for (int col = 0; col < length; col++) {
                        if (line <= half) {
                            if (col < node13length) {
                                node1FileLine[pos1] = fileLines.get(line)[col];
                                pos1++;
                            } else {
                                node2FileLine[pos2] = fileLines.get(line)[col];
                                pos2++;
                            }
                        } else {
                            if (col < node13length) {
                                node3FileLine[pos3] = fileLines.get(line)[col];
                                pos3++;
                            } else {
                                node4FileLine[pos4] = fileLines.get(line)[col];
                                pos4++;
                            }
                        }
                    }
                    if (line <= half) {
                        node1FileLines.add(node1FileLine);
                        if (node2FileLine.length != 0) {
                            node2FileLines.add(node2FileLine);
                        }
                        pos1 = 0;
                        pos2 = 0;
                        node1FileLine = new String[node13length];
                        node2FileLine = new String[node24length];
                    } else {
                        if (node3FileLine.length != 0) {
                            node3FileLines.add(node3FileLine);
                        }
                        if (node4FileLine.length != 0) {
                            node4FileLines.add(node4FileLine);
                        }
                        pos3 = 0;
                        pos4 = 0;
                        node3FileLine = new String[node13length];
                        node4FileLine = new String[node24length];
                    }
                }
                if (node1FileLines.size() == 0) {
                    node1 = null;
                } else {
                    node1 = new QuadTree(node1FileLines);
                }

                if (node2FileLines.size() == 0) {
                    node2 = null;
                } else {
                    if (node2FileLines.size() == 0) {
                    }
                    node2 = new QuadTree(node2FileLines);
                }

                if (node3FileLines.size() == 0) {
                    node3 = null;
                } else {
                    node3 = new QuadTree(node3FileLines);
                }

                if (node4FileLines.size() == 0) {
                    node4 = null;
                } else {
                    node4 = new QuadTree(node4FileLines);
                }
            }
            nodes = new ArrayList<QuadTree>(Arrays.asList(node1, node2, node3, node4));
            node1Null = (node1 == null);
            node2Null = (node2 == null);
            node3Null = (node3 == null);
            node4Null = (node4 == null);
        }
    }


    public QuadTree outline() {
        if (fileLines != null) {
            if (width == 1 && length == 3) {
                fileLines.set(0, new String[]{"0", "0", "0"});
                return this;
            }
        }
        if (node1Null && node2Null && node3Null && node4Null) {
            if (width >= 2) {
                String[] topBottom = new String[length];
                Arrays.fill(topBottom, "0");
                fileLines.set(0, topBottom);
                if (width >= 3) {
                    fileLines.set(width-1, topBottom);
                }
            }
            if (length >= 6) {
                for (int line = 0; line < width; line++) {
                    for (int col = 0; col < 3; col++) {
                        fileLines.get(line)[col] = "0";
                    }
                    if (length >= 9) {
                        for (int col = length - 3; col < length; col++) {
                            fileLines.get(line)[col] = "0";
                        }
                    }   
                }
            }
            return this;
        } else {
            if (!node1Null) {
                node1 = node1.outline();
            }
            if (!node2Null) {
                node2 = node2.outline();
            }
            if (!node3Null) {
                node3 = node3.outline();
            }
            if (!node4Null) {
                node4 = node4.outline();
            }
            return this;
        }
    }


    public QuadTree compress(double compressionLevel, boolean edgeDetection,    boolean outline) {
        QuadTree compressedQT = compress_helper(compressionLevel, edgeDetection);
        if (outline) {
            return compressedQT.outline();
        } else {
            return compressedQT;
        }
    }

    public QuadTree compress_helper(double compressionLevel, boolean edgeDetection) {
        QuadTree compressedQT = new QuadTree(fileLines);
        if (compressedQT.width == 1 && compressedQT.length == 3) {
            return compressedQT;
        } 

        // if (Math.abs(1/pixels - compressionLevel) <=) {}
        double errthreshold = 100;
        int squaredError = 0;
        
        for (int line = 0; line < compressedQT.width; line++) {
            for (int col= 0; col < compressedQT.length-2; col+= 3) {
                int r = (!compressedQT.fileLines.get(line)[col].equals("")) ? Integer.parseInt(compressedQT.fileLines.get(line) [col]) : 0;
                int g = (!compressedQT.fileLines.get(line)[col+1].equals("")) ? Integer.parseInt(compressedQT.fileLines.get(line)[col+1]) : 0;
                int b = (!compressedQT.fileLines.get(line)[col+2].equals("")) ? Integer.parseInt(compressedQT.fileLines.get(line)[col+2]) : 0;
                squaredError += squared_error(r, g, b);
            }
        }
        double meanSquaredError = squaredError / compressedQT.pixels;

        if (meanSquaredError > errthreshold) {
            if (!compressedQT.node1Null) {
                compressedQT.node1 = compressedQT.node1.compress_helper(compressionLevel, edgeDetection);
            }
            if (!compressedQT.node2Null) {
                compressedQT.node2 = compressedQT.node2.compress_helper(compressionLevel, edgeDetection);
            }
            if (!compressedQT.node3Null) {
                compressedQT.node3 = compressedQT.node3.compress_helper(compressionLevel, edgeDetection);
            }
            if (!compressedQT.node4Null) {
                compressedQT.node4 = compressedQT.node4.compress_helper(compressionLevel, edgeDetection);
            }
        } else {
            if (!edgeDetection) {
                for (int line = 0; line < compressedQT.width; line++) {
                    for (int col = 0; col < compressedQT.length-2; col += 3) {
                        compressedQT.fileLines.get(line)[col] = String.valueOf(meanColor[0]);
                        compressedQT.fileLines.get(line)[col+1] = String.valueOf(meanColor[1]);
                        compressedQT.fileLines.get(line)[col+2] = String.valueOf(meanColor[2]);
                    }
                }
            }
            compressedQT.node1 = null;
            compressedQT.node1Null = true;
            compressedQT.node2 = null;
            compressedQT.node2Null = true;
            compressedQT.node3 = null;
            compressedQT.node3Null = true;
            compressedQT.node4 = null;
            compressedQT.node4Null = true;
        }
        return compressedQT;
    }


    public double squared_error(int r, int g, int b) {
        return Math.pow(r - meanColor[0], 2) + Math.pow(g - meanColor[1], 2) + Math.pow(b - meanColor[2], 2);
    }


    public QuadTree edge_detect() {
        boolean edgeDetection = true;
        boolean outline = false;
        QuadTree edgeDetectQT = compress(0.5, edgeDetection, outline);
        edgeDetectQT.edge_detect_helper();
        return edgeDetectQT;
    }

    public void edge_detect_helper() {
        int threshold = 1;
        if (node1Null && node2Null && node3Null && node4Null) {
            if (pixels > threshold) {
                for (int line = 0; line < width; line++) {
                    for (int col = 0; col < length; col++) {
                        fileLines.get(line)[col] = "0";
                    }
                }
            } else if (pixels <= threshold) {
                for (int line = 0; line < width; line++) {
                    for (int col = 0; col < length-2; col+=3) {
                        int r = (!fileLines.get(line)[col].equals("")) ? Integer.parseInt(fileLines.get(line)[col]) : 0;
                        int g = (!fileLines.get(line)[col+1].equals("")) ? Integer.parseInt(fileLines.get(line)[col+1]) : 0;
                        int b = (!fileLines.get(line)[col+2].equals("")) ? Integer.parseInt(fileLines.get(line)[col+2]) : 0;
                        int rDiff = Math.abs(r - meanColor[0]);
                        int gDiff = Math.abs(g - meanColor[1]);
                        int bDiff = Math.abs(b - meanColor[2]);
                        int meanDiff = (rDiff + gDiff + bDiff) / 3;
                        int newVal = ((int) Math.pow(meanDiff, -2) + 100) % 255;
                        fileLines.get(line)[col] = String.valueOf(newVal);
                        fileLines.get(line)[col+1] = String.valueOf(newVal);
                        fileLines.get(line)[col+2] = String.valueOf(newVal);
                    }
                }
            } 
        } else {
            if (!node1Null) {
                node1.edge_detect_helper();
            }
            if (!node2Null) {
                node2.edge_detect_helper();
            }
            if (!node3Null) {
                node3.edge_detect_helper();
            }
            if (!node4Null) {
                node4.edge_detect_helper();
            }
        }
    }


    public QuadTree filter(String type) {
        ArrayList<String[]> fileLinesFilter = new ArrayList<String[]>();
        for (int line = 0; line < width; line++) {
            String[] fileLineFilter = new String[length];
            for (int col = 0; col < length-2; col += 3) {
                int r = (!fileLines.get(line)[col].equals("")) ? Integer.parseInt(fileLines.get(line)[col]) : 0;
                int g = (!fileLines.get(line)[col+1].equals("")) ? Integer.parseInt(fileLines.get(line)[col+1]) : 0;
                int b = (!fileLines.get(line)[col+2].equals("")) ? Integer.parseInt(fileLines.get(line)[col+2]) : 0;
                if (type.equals("Grayscale")) {
                    int val = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                    r = val;
                    g = val;
                    b = val;
                } else if (type.equals("Negative")) {
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                } else if (type.equals("Tint")) {
                    r = (int) (((double) r/255) * 225);
                    g = (int) (((double) g/255) * 6);
                    b = (int) (((double) b/255) * 0);
                }
                fileLineFilter[col] = String.valueOf(r);
                fileLineFilter[col+1] = String.valueOf(g);
                fileLineFilter[col+2] = String.valueOf(b);
            }
            fileLinesFilter.add(fileLineFilter);
        }
        QuadTree filteredQT = new QuadTree(fileLinesFilter);
        return filteredQT;
    }


    public void toPPM(String filename, String preface) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(preface);
        try {
            writer.write(toPPM_helper());
            System.out.println("Written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.close();
    }


    public String toPPM_helper() {
        String[] quad1 = {""};
        String[] quad2 = {""};
        String[] quad3 = {""};
        String[] quad4 = {""};
        if (node1Null && node2Null && node3Null && node4Null) {
            StringBuilder f = new StringBuilder();
            for (int line = 0; line < width; line++) {
                for (int col = 0; col < length; col++) {   
                    f.append(fileLines.get(line)[col] + " ");
                }
                f.append("\n");
            }
            return f.toString();
        }
        int lines = 0;
        if (!node1Null) {
            lines += node1.width;
            quad1 = node1.toPPM_helper().split("\n");
        } 
        if (!node2Null) {
            quad2 = node2.toPPM_helper().split("\n");
        }
        if (!node3Null) {
            lines += node3.width;
            quad3 = node3.toPPM_helper().split("\n");
        } 
        if (!node4Null) {
            quad4 = node4.toPPM_helper().split("\n");
        } 
        StringBuilder sb = new StringBuilder();

        for (int line = 0; line < lines; line++) {
            String lineA = "";
            String lineB = "";
            
            if (line < quad1.length) {
                if (!quad1[0].equals("")) {
                    String[] quad1Line = quad1[line].split(" ");
                    lineA = String.join(" ", quad1Line) + " ";
                }

                if (!quad2[0].equals("")) {
                    String[] quad2Line = quad2[line].split(" ");
                    lineB = String.join(" ", quad2Line) + " ";
                }
            } else {
                int line34 = line - (quad1.length);
                if (!quad3[0].equals("")) {
                    String[] quad3Line = quad3[line34].split(" ");
                    lineA = String.join(" ", quad3Line) + " ";
                } 

                if (!quad4[0].equals("")) {
                    String[] quad4Line = quad4[line34].split(" ");
                    lineB = String.join(" ", quad4Line) + " ";
                }  
            }  
            sb.append(lineA).append(lineB).append('\n');
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("No. of lines: ").append(width).append("\n");
        if (width != 0) {
            sb.append("No. of strings per line: ").append(length).append("\n");
        }
        sb.append("meanColor: (").append(meanColor[0]).append(", ").append(meanColor[1]).append(", ").append(meanColor[2]).append(")\n");
        sb.append("Pixels: ").append(pixels).append("\n");
        sb.append("Outline? ").append(outline).append("\n");
        sb.append("Nodes: ");
        if (node1 != null) {
            sb.append(" node1 ");
        }
        if (node2 != null) {
            sb.append(" node2 ");
        }
        if (node3 != null) {
            sb.append(" node3 ");
        }
        if (node4 != null) {
            sb.append(" node4 ");
        }
        return sb.toString();
    }
}
