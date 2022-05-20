package quadtree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class QuadTree {

    private ArrayList<String[]> fileLines = null;
    private int[] meanColor = new int[]{0, 0, 0};
    private int pixels = 0;
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
    private String[] nodeNames = new String[]{"node1", "node2", "node3", "node4"};


    public QuadTree(ArrayList<String[]> fileLines) {
        if (fileLines == null || fileLines.size() == 0) {
            System.out.println("Blank image!");
            System.exit(9);
        }
        else {
            this.fileLines = fileLines;

            // System.out.println("fileLines.size() = " + fileLines.size());
            // System.out.println("fileLines.get(0).length = " + fileLines.get(0).length);


            int Rtotal = 0;
            int Gtotal = 0;
            int Btotal = 0;
            for (int line = 0; line < fileLines.size(); line++) {
                for (int col = 0; col < fileLines.get(line).length - 2; col += 3) {
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

            if (fileLines.size() == 1 && fileLines.get(0).length == 3) {
                node1 = null;
                node2 = null;
                node3 = null;
                node4 = null;
            } else {
                int node13length = (fileLines.get(0).length == 3) ? 3
                        : (((fileLines.get(0).length % 2 == 0) ? (fileLines.get(0).length / 2)
                        : ((fileLines.get(0).length - 1) / 2) + 2));
                int node24length = fileLines.get(0).length - node13length;

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

                int half = (fileLines.size() % 2 == 0) ? (fileLines.size() / 2) - 1 : (fileLines.size() - 1) / 2;
                for (int i = 0; i < fileLines.size(); i++) {
                    for (int col = 0; col < fileLines.get(0).length; col++) {
                        if (i <= half) {
                            if (col < node13length) {
                                node1FileLine[pos1] = fileLines.get(i)[col];
                                pos1++;
                            } else {
                                node2FileLine[pos2] = fileLines.get(i)[col];
                                pos2++;
                            }
                        } else {
                            if (col < node13length) {
                                node3FileLine[pos3] = fileLines.get(i)[col];
                                pos3++;
                            } else {
                                node4FileLine[pos4] = fileLines.get(i)[col];
                                pos4++;
                            }
                        }
                    }
                    if (i <= half) {
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

    public QuadTree(ArrayList<QuadTree> nodes, String constructor2) {
        this.nodes = nodes;
        node1 = nodes.get(0);
        node2 = nodes.get(1);
        node3 = nodes.get(2);
        node4 = nodes.get(3);
        node1Null = (nodes.get(0) == null);
        node2Null = (nodes.get(1) == null);
        node3Null = (nodes.get(2) == null);
        node4Null = (nodes.get(3) == null);
    }


    public QuadTree outline(boolean useConstructor2) {
        // QuadTree outlined;
        // if (useConstructor2) {
        //     outlined = new QuadTree(nodes, "constructor2");
        // } else {
        //     outlined = new QuadTree(fileLines);
        // }
        // outlined = outlined.outline_helper();
        return outline_helper();
    }


    public QuadTree outline_helper() {
        if (fileLines != null) {
            if (fileLines.size() == 1 && fileLines.get(0).length == 3) {
                fileLines.set(0, new String[]{"0", "0", "0"});
                return this;
            }
        }
        if (node1Null && node2Null && node3Null && node4Null) {
            if (fileLines.size() >= 2) {
                String[] topBottom = new String[fileLines.get(0).length];
                Arrays.fill(topBottom, "0");
                fileLines.set(0, topBottom);
                if (fileLines.size() >= 3) {
                    fileLines.set(fileLines.size()-1, topBottom);
                }
            }
            if (fileLines.get(0).length >= 6) {
                for (int line = 0; line < fileLines.size(); line++) {
                    for (int col = 0; col < 3; col++) {
                        fileLines.get(line)[col] = "0";
                    }
                    if (fileLines.get(line).length >= 9) {
                        for (int col = fileLines.get(line).length - 3; col < fileLines.get(line).length; col++) {
                            fileLines.get(line)[col] = "0";
                        }
                    }   
                }
            }
            return this;
        } else {
            if (!node1Null) {
                node1 = node1.outline_helper();
            }
            if (!node2Null) {
                node2 = node2.outline_helper();
            }
            if (!node3Null) {
                node3 = node3.outline_helper();
            }
            if (!node4Null) {
                node4 = node4.outline_helper();
            }
            return this;
        }
    }


    public QuadTree compress(double compressionLevel, boolean outline) {
        QuadTree compressedQT = compress_helper(compressionLevel);
        if (outline) {
            return compressedQT.outline(true);
        } else {
            return compressedQT;
        }
    }


    public QuadTree compress_helper(double compressionLevel) {
        QuadTree compressedQT = new QuadTree(fileLines);
        if (compressedQT.fileLines.size() == 1 && compressedQT.fileLines.get(0).length == 3) {
            return compressedQT;
        } 

        // if (Math.abs(1/pixels - compressionLevel) <=) {}
        double errthreshold = 1000;
        int squaredError = 0;
        
        for (int line = 0; line < compressedQT.fileLines.size(); line++) {
            for (int col= 0; col< compressedQT.fileLines.get(line).length - 2; col+= 3) {
                int r = (!compressedQT.fileLines.get(line)[col].equals("")) ? Integer.parseInt(compressedQT.fileLines.get(line) [col]) : 0;
                int g = (!compressedQT.fileLines.get(line)[col+1].equals("")) ? Integer.parseInt(compressedQT.fileLines.get(line)[col+ 1]) : 0;
                int b = (!compressedQT.fileLines.get(line)[col+2].equals("")) ? Integer.parseInt(compressedQT.fileLines.get(line)[col+ 2]) : 0;
                squaredError += squared_error(r, g, b);
            }
        }
        double meanSquaredError = squaredError / compressedQT.pixels;
        // System.out.println(meanSquaredError);

        if (meanSquaredError > errthreshold) {
            if (!compressedQT.node1Null) {
                compressedQT.node1 = compressedQT.node1.compress_helper(compressionLevel);
            }
            if (!compressedQT.node2Null) {
                compressedQT.node2 = compressedQT.node2.compress_helper(compressionLevel);
            }
            if (!compressedQT.node3Null) {
                compressedQT.node3 = compressedQT.node3.compress_helper(compressionLevel);
            }
            if (!compressedQT.node4Null) {
                compressedQT.node4 = compressedQT.node4.compress_helper(compressionLevel);
            }
        } else {
            for (int line = 0; line < compressedQT.fileLines.size(); line++) {
                for (int col = 0; col < compressedQT.fileLines.get(line).length - 2; col += 3) {
                    compressedQT.fileLines.get(line)[col] = String.valueOf(meanColor[0]);
                    compressedQT.fileLines.get(line)[col + 1] = String.valueOf(meanColor[1]);
                    compressedQT.fileLines.get(line)[col + 2] = String.valueOf(meanColor[2]);
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


    public QuadTree edge_detect(boolean outline) {
        QuadTree edgeDetectQT = compress(0.5, outline);
        edgeDetectQT.edge_detect_helper();
        return edgeDetectQT;
    }

    public void edge_detect_helper() {
        // nodes with higher compression to be set to black
        int threshold = 10;
        if (pixels > threshold) {
            for (int line = 0; line < fileLines.size(); line++) {
                for (int col = 0; col < fileLines.get(line).length; col++) {
                    fileLines.get(line)[col] = "0";
                }
            }
        } else if (pixels <= threshold) {
            for (int line = 0; line < fileLines.size(); line++) {
                for (int col = 0; col < fileLines.get(line).length-2; col+=3) {
                    int r = (!fileLines.get(line)[col].equals("")) ? Integer.parseInt(fileLines.get(line)[col]) : 0;
                    int g = (!fileLines.get(line)[col+1].equals("")) ? Integer.parseInt(fileLines.get(line)[col+1]) : 0;
                    int b = (!fileLines.get(line)[col+2].equals("")) ? Integer.parseInt(fileLines.get(line)[col+2]) : 0;
                    int rDiff = Math.abs(r - meanColor[0]);
                    int gDiff = Math.abs(g - meanColor[1]);
                    int bDiff = Math.abs(b - meanColor[2]);
                    int meanDiff = (rDiff + gDiff + bDiff) / 3;
                    fileLines.get(line)[col] = String.valueOf(meanDiff);
                    fileLines.get(line)[col+1] = String.valueOf(meanDiff);
                    fileLines.get(line)[col+2] = String.valueOf(meanDiff);
                    // fileLines.get(line)[col] = "255";
                }
            }
        } 
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


    public QuadTree filter(String type) {
        ArrayList<String[]> fileLinesFilter = new ArrayList<String[]>();
        for (int line = 0; line < fileLines.size(); line++) {
            String[] fileLineFilter = new String[fileLines.get(line).length];
            for (int col = 0; col < fileLines.get(line).length - 2; col += 3) {
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
        // for (int line = 0; line < node1.fileLines.size(); line++) {
        //     for (int col = 0; col < node1.fileLines.get(line).length - 2; col += 3) {
        //         System.out.println(Arrays.asList(node1.fileLines.get(line)));
        //     }
        // }
        String[] quad1 = {""};
        String[] quad2 = {""};
        String[] quad3 = {""};
        String[] quad4 = {""};
        if (node1Null && node2Null && node3Null && node4Null) {
            // System.out.println(Arrays.asList(fileLines.get(0)));
            StringBuilder f = new StringBuilder();
            for (int line = 0; line < fileLines.size(); line++) {
                for (int col = 0; col < fileLines.get(line).length; col++) {   
                    f.append(fileLines.get(line)[col] + " ");
                }
                f.append("\n");
            }
            // System.out.println(f.toString() + "\n");
            return f.toString();
        }
        int lines = 0;
        if (!node1Null) {
            lines += node1.fileLines.size();
            quad1 = node1.toPPM_helper().split("\n");
            // System.out.println(Arrays.asList(quad1));
        } else {
            // System.out.println("node1 is null");
        }
        if (!node2Null) {
            quad2 = node2.toPPM_helper().split("\n");
        } else {
            // System.out.println("node2 is null");
        }
        if (!node3Null) {
            lines += node3.fileLines.size();
            quad3 = node3.toPPM_helper().split("\n");
        } else {
            // System.out.println("node3 is null");
        } 
        if (!node4Null) {
            quad4 = node4.toPPM_helper().split("\n");
        } else {
            // System.out.println("node4 is null");
        } 
        // for (String pxl : quad1) {
        //     System.out.print(pxl + " ");
        // }
        // System.out.println("\n");
        StringBuilder sb = new StringBuilder();
        // int half = (fileLines.size() % 2 == 0) ? (fileLines.size() / 2) - 1 : (fileLines.size() - 1) / 2;

        for (int line = 0; line < lines; line++) {
            String lineA = "";
            String lineB = "";
            
            if (line < quad1.length) {
                if (!quad1[0].equals("")) {
                    // System.out.println(quad1.length + " " + line + " " + node1.fileLines.get(line).length);
                    String[] quad1Line = quad1[line].split(" ");
                    // if (quad1Line.length >= 9) {
                    //     quad1Line = Arrays.copyOfRange(quad1Line, 0, quad1Line.length-3);
                    // }
                    lineA = String.join(" ", quad1Line) + " ";
                } else {
                    // System.out.println("quad1");
                }

                if (!quad2[0].equals("")) {
                    String[] quad2Line = quad2[line].split(" ");
                    // if (quad2Line.length >= 9) {
                    //     quad2Line = Arrays.copyOfRange(quad2Line, 3, quad2Line.length);
                    // }
                    lineB = String.join(" ", quad2Line) + " ";
                }  else {
                    // System.out.println("quad2");
                }
            } else {
                int line34 = line - (quad1.length);
                if (!quad3[0].equals("")) {
                    String[] quad3Line = quad3[line34].split(" ");
                    // if (quad3Line.length >= 9) {
                    //     quad3Line = Arrays.copyOfRange(quad3Line, 0, quad3Line.length-3);
                    // }
                    lineA = String.join(" ", quad3Line) + " ";
                }  else {
                    // System.out.println("quad3");
                }

                if (!quad4[0].equals("")) {
                    String[] quad4Line = quad4[line34].split(" ");
                    // if (quad4Line.length >= 9) {
                    //     quad4Line = Arrays.copyOfRange(quad4Line, 3, quad4Line.length);
                    // }
                    lineB = String.join(" ", quad4Line) + " ";
                }  else {
                    // System.out.println("quad4");
                }
            }  

            sb.append(lineA).append(lineB).append('\n');

            // System.out.println(lineA);
            // System.out.println(lineB);
            // System.out.println(sb.toString());
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("No. of lines: ").append(fileLines.size()).append("\n");
        if (fileLines.size() != 0) {
            sb.append("No. of strings per line: ").append(fileLines.get(0).length).append("\n");
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