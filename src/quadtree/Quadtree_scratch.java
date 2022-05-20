package quadtree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Quadtree_scratch {

    private ArrayList<String[]> fileLines;
    private int[] color = new int[3];
    private int pixels = 0;
    private boolean outline;
    private QuadTree node1;
    private QuadTree node2;
    private QuadTree node3;
    private QuadTree node4;

    public QuadTree(ArrayList<String[]> file_lines, boolean out_line) {
        if (file_lines == null) {
            System.out.println("Blank image");
        }
        else {
            fileLines = file_lines;
            outline = out_line;

            // System.out.println("fileLines.size() = " + fileLines.size());
//            System.out.println("fileLines.get(0).length = " + fileLines.get(0).length);

            if (outline) {
                String[] topBottom = new String[fileLines.get(0).length];
                Arrays.fill(topBottom, "0");
                fileLines.set(0, topBottom);
                fileLines.set(fileLines.size()-1, topBottom);
                for (int line = 0; line < fileLines.size(); line++) {
                    for (int j = 0; j < 3; j++) {
                        fileLines.get(line)[j] = "0";
                    }
                    for (int j = fileLines.get(line).length - 3; j < fileLines.get(line).length; j++) {
                        fileLines.get(line)[j] = "0";
                    }
                }
            }

            int Rtotal = 0;
            int Gtotal = 0;
            int Btotal = 0;
            for (int p = 0; p < fileLines.size(); p++) {
                for (int q = 0; q < fileLines.get(p).length - 2; q += 3) {
                    pixels++;
                    Rtotal += (fileLines.get(p)[q] != null) ? Integer.parseInt(fileLines.get(p)[q]) : 0;
                    Gtotal += (fileLines.get(p)[q + 1] != null) ? Integer.parseInt(fileLines.get(p)[q + 1]) : 0;
                    Btotal += (fileLines.get(p)[q + 2] != null) ? Integer.parseInt(fileLines.get(p)[q + 2]) : 0;
                }
            }
            int Raverage = (pixels == 0) ? 0 : Rtotal / pixels;
            int Gaverage = (pixels == 0) ? 0 : Gtotal / pixels;
            int Baverage = (pixels == 0) ? 0 : Btotal / pixels;

            color[0] = Raverage;
            color[1] = Gaverage;
            color[2] = Baverage;

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

                boolean node1turn = false;
                boolean node2turn = false;
                boolean node3turn = false;
                boolean node4turn = false;

                int half = (fileLines.size() % 2 == 0) ? (fileLines.size() / 2) - 1 : (fileLines.size() - 1) / 2;
                for (int i = 0; i < fileLines.size(); i++) {
                    for (int j = (outline ? 3 : 0); j < fileLines.get(0).length - (outline ? 3 : 0); j++) {
                        if (i <= half && j < node13length + (outline ? 3 : 0)) {
                            node1FileLine[pos1] = fileLines.get(i)[j];
                            pos1++;
                            node1turn = true;
                        } else if (i <= half && j >= node13length + (outline ? 3 : 0)) {
                            node2FileLine[pos2] = fileLines.get(i)[j];
                            pos2++;
                            node2turn = true;
                        } else if (i > half && j < node13length + (outline ? 3 : 0)) {
                            node3FileLine[pos3] = fileLines.get(i)[j];
                            pos3++;
                            node3turn = true;
                        } else if (i > half && j >= node13length + (outline ? 3 : 0)) {
                            node4FileLine[pos4] = fileLines.get(i)[j];
                            pos4++;
                            node4turn = true;
                        }
                    }
                    if (node1turn) {
                        node1FileLines.add(node1FileLine);
                        // for (String pxl : node1FileLine) {
                        //     System.out.print(pxl + " ");
                        // }
                        // System.exit(4);
                        node1turn = false;
                        pos1 = 0;
                        node1FileLine = new String[node13length];
                    }
                    if (node2turn) {
                        node2FileLines.add(node2FileLine);
                        node2turn = false;
                        pos2 = 0;
                        node2FileLine = new String[node24length];
                    }
                    if (node3turn) {
                        node3FileLines.add(node3FileLine);
                        node3turn = false;
                        pos3 = 0;
                        node3FileLine = new String[node13length];
                    }
                    if (node4turn) {
                        node4FileLines.add(node4FileLine);
                        node4turn = false;
                        pos4 = 0;
                        node4FileLine = new String[node24length];
                    }
                }
                if (node1FileLines.size() == 0 || node2FileLines.size() == 0 || node3FileLines.size() == 0 ||
                        node4FileLines.size() == 0) {
                    node1 = null;
                    node2 = null;
                    node3 = null;
                    node4 = null;
                } else {
                    node1 = new QuadTree(node1FileLines, outline);
                    node2 = new QuadTree(node2FileLines, outline);
                    node3 = new QuadTree(node3FileLines, outline);
                    node4 = new QuadTree(node4FileLines, outline);
                }
            }
        }
    }

    public QuadTree compress(double compressionLevel) {
        QuadTree compressedQT = new QuadTree(fileLines, outline);
        if (compressedQT.fileLines.size() - (outline? 2 : 0) == 1 && compressedQT.fileLines.get(0).length - (outline? 6 : 0) == 3) {
            return compressedQT;
        } else {
            double errthreshold = compressionLevel * 400000;

            int subimage1error = 0;
            int subimage2error = 0;
            int subimage3error = 0;
            int subimage4error = 0;

            QuadTree subimage1;
            if (node1 != null) {
                subimage1 = new QuadTree(node1.fileLines, node1.outline);
                for (int p = (outline ? 1 : 0); p < subimage1.fileLines.size() - (outline ? 1 : 0); p++) {
                    for (int q = (outline ? 3 : 0); q < subimage1.fileLines.get(p).length - (outline ? 6 : 2); q += 3) {
                        subimage1error += node1.error(Integer.parseInt(subimage1.fileLines.get(p)[q]),
                                Integer.parseInt(subimage1.fileLines.get(p)[q + 1]),
                                Integer.parseInt(subimage1.fileLines.get(p)[q + 2]));
                        subimage1.fileLines.get(p)[q] = String.valueOf(color[0]);
                        subimage1.fileLines.get(p)[q + 1] = String.valueOf(color[1]);
                        subimage1.fileLines.get(p)[q + 2] = String.valueOf(color[2]);
                    }
                }
            } else {
                subimage1 = null;
            }

            QuadTree subimage2;
            if (node2 != null) {
                subimage2 = new QuadTree(node2.fileLines, node2.outline);
                for (int p = (outline? 1 : 0); p < subimage2.fileLines.size() - (outline? 1 : 0); p++) {
                    for (int q = (outline? 3 : 0); q < subimage2.fileLines.get(p).length - (outline? 6 : 2); q += 3) {
                        subimage2error += node2.error(Integer.parseInt(subimage2.fileLines.get(p)[q]),
                                Integer.parseInt(subimage2.fileLines.get(p)[q + 1]),
                                Integer.parseInt(subimage2.fileLines.get(p)[q + 2]));
                        subimage2.fileLines.get(p)[q] = String.valueOf(color[0]);
                        subimage2.fileLines.get(p)[q + 1] = String.valueOf(color[1]);
                        subimage2.fileLines.get(p)[q + 2] = String.valueOf(color[2]);
                    }
                }
            } else {
                subimage2 = null;
            }

            QuadTree subimage3;
            if (node3 != null) {
                subimage3 = new QuadTree(node3.fileLines, node3.outline);
                for (int p = (outline? 1 : 0); p < subimage3.fileLines.size() - (outline? 1 : 0); p++) {
                    for (int q = (outline? 3 : 0); q < subimage3.fileLines.get(p).length - (outline? 6 : 2); q += 3) {
                        subimage3error += node3.error(Integer.parseInt(subimage3.fileLines.get(p)[q]),
                                Integer.parseInt(subimage3.fileLines.get(p)[q + 1]),
                                Integer.parseInt(subimage3.fileLines.get(p)[q + 2]));
                        subimage3.fileLines.get(p)[q] = String.valueOf(color[0]);
                        subimage3.fileLines.get(p)[q + 1] = String.valueOf(color[1]);
                        subimage3.fileLines.get(p)[q + 2] = String.valueOf(color[2]);
                    }
                }
            } else {
                subimage3 = null;
            }

            QuadTree subimage4;
            if (node4 != null) {
                subimage4 = new QuadTree(node4.fileLines, node4.outline);
                for (int p = (outline? 1 : 0); p < subimage4.fileLines.size() - (outline? 1 : 0); p++) {
                    for (int q = (outline? 3 : 0); q < subimage4.fileLines.get(p).length - (outline? 6 : 2); q += 3) {
                        subimage4error += node4.error(Integer.parseInt(subimage4.fileLines.get(p)[q]),
                                Integer.parseInt(subimage4.fileLines.get(p)[q + 1]),
                                Integer.parseInt(subimage4.fileLines.get(p)[q + 2]));
                        subimage4.fileLines.get(p)[q] = String.valueOf(color[0]);
                        subimage4.fileLines.get(p)[q + 1] = String.valueOf(color[1]);
                        subimage4.fileLines.get(p)[q + 2] = String.valueOf(color[2]);
                    }
                }
            } else {
                subimage4 = null;
            }

            int subimage1avgError = 0;
            int subimage2avgError = 0;
            int subimage3avgError = 0;
            int subimage4avgError = 0;


            if (subimage1 != null) {
                subimage1avgError = subimage1error / subimage1.pixels;
//                System.out.println("subimage1avgError = " + subimage1avgError);
            }
            if (subimage2 != null) {
                subimage2avgError = subimage2error / subimage2.pixels;
//                System.out.println("subimage2avgError = " + subimage2avgError);
            }
            if (subimage3 != null) {
                subimage3avgError = subimage3error / subimage3.pixels;
//                System.out.println("subimage3avgError = " + subimage3avgError);
            }
            if (subimage4 != null) {
                subimage4avgError = subimage4error / subimage4.pixels;
                // System.out.println("subimage4avgError = " + subimage4avgError + '\n');
            }

            compressedQT.node1 = (subimage1 == null)? null : (subimage1avgError > errthreshold) ? node1.compress(compressionLevel)
                    : subimage1;
            compressedQT.node2 = (subimage2 == null)? null : (subimage2avgError > errthreshold) ? node2.compress(compressionLevel)
                    : subimage2;
            compressedQT.node3 = (subimage3 == null)? null : (subimage3avgError > errthreshold) ? node3.compress(compressionLevel)
                    : subimage3;
            compressedQT.node4 = (subimage4 == null)? null : (subimage4avgError > errthreshold) ? node4.compress(compressionLevel)
                    : subimage4;
            return compressedQT;
        }
    }

    public double error(int r, int g, int b) {
        return Math.pow((r - (color[0] * r)), 2) + Math.pow((g - (color[1] * g)), 2) + Math.pow((b - (color[2] * b)), 2);
    }

    public QuadTree filter() {
        QuadTree filteredQT = new QuadTree(fileLines, outline);
        for (int p = (outline ? 1 : 0); p < filteredQT.fileLines.size() - (outline ? 1 : 0); p++) {
            for (int q = (outline ? 3 : 0); q < filteredQT.fileLines.get(p).length - (outline ? 6 : 2); q += 3) {
                filteredQT.fileLines.get(p)[q] = "0";
            }
        }
        return filteredQT;
    }

    public QuadTree edgeDetect() {
        QuadTree edDecQT = new QuadTree(fileLines, outline);
        return edDecQT;
    }

    public void toPPM(String filename, String preface, String outline_preface) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        if ((fileLines.size() == 127 + (outline? 2 : 0))) {
            writer.write(outline? outline_preface : preface);
        }
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
        boolean node1Null = (node1 == null);
        boolean node2Null = (node2 == null);
        boolean node3Null = (node3 == null);
        boolean node4Null = (node4 == null);
        if (node1Null && node2Null && node3Null && node4Null) {
            return fileLines.get(0)[0] + " " + fileLines.get(0)[1] + " " + fileLines.get(0)[2] + " ";
        }
        if (!node1Null) {
            quad1 = node1.toPPM_helper().split("\\s+");
        }
        if (!node2Null) {
            quad2 = node2.toPPM_helper().split("\\s+");
        }
        if (!node3Null) {
            quad3 = node3.toPPM_helper().split("\\s+");
        }
        if (!node4Null) {
            quad4 = node4.toPPM_helper().split("\\s+");
        }
        // for (String pxl : quad1) {
        //     System.out.print(pxl + " ");
        // }
        // System.out.println("\n");
        StringBuilder sb = new StringBuilder();
        for (int line = 0; line < fileLines.size()-1; line++) {
            System.out.printf("\n%d\n", line);

            String lineA = "";
            String lineB = "";

            if (!node1Null) {
                if (line < node1.fileLines.size()) {
                    String[] quad1Line = Arrays.copyOfRange(quad1, line * node1.fileLines.get(line).length, (line + 1) * node1.fileLines.get(line).length);  
                    // System.out.printf("%d %d %d %d", quad1.length, line * node1.fileLines.get(line).length, (line + 1) * node1.fileLines.get(line).length, quad1Line.length);
                    // for (String[] fileLine : node1.fileLines)
                    //     for (String pxl : fileLine) {
                    //         System.out.print(pxl + " ");
                    //     }
                    //     System.out.println();
                    // for (String pxl : quad1Line) {
                    //     System.out.print(pxl + " ");
                    // }
                    // System.out.println();
                    lineA = String.join(" ", quad1Line) + " ";
                }  
            } 
            if (lineA.equals("") && !node3Null) {
                if (line < node3.fileLines.size()) {
                    String[] quad3Line = Arrays.copyOfRange(quad3, line * node3.fileLines.get(line).length, (line + 1) * node3.fileLines.get(line).length);
                    lineA = String.join(" ", quad3Line) + " ";
                }
            }
            if (!node2Null) {
                if (line < node2.fileLines.size()) {
                    String[] quad2Line = Arrays.copyOfRange(quad2, line * node2.fileLines.get(line).length, (line + 1) * node2.fileLines.get(line).length);
                    lineB = String.join(" ", quad2Line) + " ";
                }
            }
            if (lineB.equals("") && !node4Null) {
                if (line < node4.fileLines.size()) {
                    String[] quad4Line = Arrays.copyOfRange(quad4, line * node4.fileLines.get(line).length, (line + 1) * node4.fileLines.get(line).length);
                    lineB = String.join(" ", quad4Line) + " ";
                }
            }
            // System.out.println(node1Null);
            // System.out.println(node2Null);
            // System.out.println(node3Null);
            // System.out.println(node4Null);
            System.out.println(lineA);
            System.out.println(lineB);
            sb.append(lineA).append(lineB).append('\n');
            System.out.println(sb.toString());
        }

        
        // if (node1 == null || node2 == null || node3 == null || node4 == null) {
        //     if(outline) {
        //         sb.append("0 0 0 0 0\n");
        //         sb.append("0 ");
        //     }
        //     for (int y = 0; y < fileLines.get(0).length; y++) {
        //         sb.append(fileLines.get(0)[y]);
        //         sb.append(" ");
        //     }
        //     if(outline) {
        //         sb.append("0\n");
        //         sb.append("0 0 0 0 0");
        //     }
        // } else {
        //     sb.append(node1.toPPM_helper()).append
        //                 (node2.toPPM_helper());
        //     sb.append('\n');
        //     sb.append(node3.toPPM_helper()).append
        //                 (node4.toPPM_helper());
        // }
        // System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("No. of lines: ").append(fileLines.size()).append("\n");
        if (fileLines.size() != 0) {
            sb.append("No. of strings per line: ").append(fileLines.get(0).length).append("\n");
        }
        sb.append("Color: (").append(color[0]).append(", ").append(color[1]).append(", ").append(color[2]).append(")\n");
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
