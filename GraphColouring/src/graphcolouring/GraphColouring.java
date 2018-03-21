package graphcolouring;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class GraphColouring {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int choice = -1;
        Graph g1 = new Graph();
        Scanner sc = new Scanner(System.in);
        int numOfEdges, numOfVertices, i, nC; //graph atributes, etc.
        int[] vertexColoringOrder;
        
        System.out.println("Welcome to the graph coloring program\n");
        while (choice!=0) {
            System.out.println();
            System.out.println("Choose one of the options:");
            System.out.println("\t[0] - quit program");
            System.out.println("\t----------------GRAPH GENERATING-------------------------------------------");
            System.out.println("\t[1] - generate random graph with declared number of edges");
            System.out.println("\t[2] - generate random graph with declared number of vertices");
            System.out.println("\t----------------GRAPH PRESENTING-------------------------------------------");
            System.out.println("\t[3] - show generated graph as the 'adjacency list'");
            System.out.println("\t[4] - show generated graph as the 'edge list'");
            System.out.println("\t----------------GRAPH SAVING/OPENING---------------------------------------");
            System.out.println("\t[5] - save generated graph as the 'edge list'");
            System.out.println("\t[6] - open generated graph (edge list)");
            System.out.println("\t----------------ALGORITHMS-------------------------------------------------");
            System.out.println("\t[7] - use greedy algorithm to find chromatic number");
            System.out.println("\t[8] - use brute force algorithm to find chromatic number");
            System.out.println("\t[9] - generate to first difference");
            
            choice = sc.nextInt(); //read input from user
            String filePath;
            
            switch (choice) {
                case 0:
                    System.out.println("Closing app ...");
                    break;
                case 1:
                    System.out.println("Give number of edges:");
                    numOfEdges = sc.nextInt();
                    g1 = GraphColouring.graphGenerator(0, numOfEdges);
                    
                    break;
                case 2:
                    System.out.println("Give number of vertices:");
                    numOfVertices = sc.nextInt();
                    g1 = GraphColouring.graphGenerator(numOfVertices, 0);
                    
                    break;
                case 3:
                    System.out.println(g1.showAdjacencyList());
                    
                    break;
                case 4:
                    System.out.println(g1.showEdgeList());
                    
                    break;
                    
                case 5:
                    if (g1.getNumV() != 0) {
                        System.out.println("Give a name of the file: ");
                        sc.nextLine();
                        filePath = sc.nextLine();
                        FilesIO.saveGraphAsEL(filePath, g1);
                    } else {
                        System.out.println("There is no graph to save!");
                    }
                    
                    break;
                case 6:
                    System.out.println("Give a name of the file: ");
                    sc.nextLine();
                    filePath = sc.nextLine();
                    g1 = FilesIO.openGraphAsEL(filePath);
                    
                    break;
                    
                    /*
                        Greedy algorithm to find first possible graph coloring
                    */

                case 7:
                    nC = g1.colorGreedy();
                    System.out.println("Greedy algorith: The graph has been colored with <<<"+nC+">>>");
                    System.out.println("Do you want to show colors of vertices? [1 - yes; 0 - no]");
                    sc.nextLine();
                    if ( (choice = sc.nextInt()) == 1) {
                        for (i = 0; i<g1.getNumV(); i++) {
                            System.out.println((i+1)+"# vertex has color: " + g1.getArrayOfColorsGreedy()[i]);
                        }
                    }
                    choice = -1;
                    
                    break;
                    
                    /*
                        Brute force algorithm to find all graph coloring. It uses greedy algorith, but it colors
                        vertices in all possible orders. Algorith sets chromatic number as the smallest possible coloring
                        of graph.
                    */
                    
                case 8:
                    vertexColoringOrder = new int[g1.getNumV()];
                    
                    //initialize standard vertex coloring order 1,2...num of vertex
                    for (i = 0; i<vertexColoringOrder.length; i++) {
                        vertexColoringOrder[i] = i+1;
                    }
                    
                    g1.permuteAndColor(vertexColoringOrder, vertexColoringOrder.length);
                    nC = g1.getChromaticNumberFoundBF2();
                    
                    System.out.println("Brute force algorith: The graph has been colored with <<<"+nC+">>>");
                    System.out.println("Do you want to show colors of vertices? [1 - yes; 0 - no]");
                    sc.nextLine();
                    if ( (choice = sc.nextInt()) == 1) {
                        for (i = 0; i<g1.getNumV(); i++) {
                            System.out.println((i+1)+"# vertex has color: " + g1.getArrayOfColorsBF2()[i]);
                        }
                    }
                    choice = -1;
                    

                    /*nC = g1.colorBF();
                    System.out.println("Brute force algorith: The graph has been colored with <<<"+nC+">>>");
                    System.out.println("Do you want to show colors of vertices? [1 - yes; 0 - no]");
                    sc.nextLine();
                    if ( (choice = sc.nextInt()) == 1) {
                        for (i = 0; i<g1.getNumV(); i++) {
                            System.out.println((i+1)+"# vertex has color: " + g1.getArrayOfColorsBF()[i]);
                        }
                    }
                    choice = -1;*/
                    
                    break;
                    
                case 9:
                    System.out.println("Give number of vertices");
                    numOfVertices = sc.nextInt();
                    vertexColoringOrder = new int[numOfVertices];
                    
                    do {
                        g1 = GraphColouring.graphGenerator(numOfVertices, 0);

                        //initialize standard vertex coloring order 1,2...num of vertex
                        for (i = 0; i<vertexColoringOrder.length; i++) {
                            vertexColoringOrder[i] = i+1;
                        }

                        g1.permuteAndColor(vertexColoringOrder, vertexColoringOrder.length);
                        System.out.println("Brute force: "+g1.getChromaticNumberFoundBF2());
                        nC = g1.colorGreedy();
                        System.out.println("Greedy: "+nC);
                    } while (g1.getChromaticNumberFoundBF2() == nC);
                    System.out.println("Graph with differences has been found!");
                    
                    choice = -1;
 
                    break;
                default:
                    System.out.println("["+choice+"] - invalid option\n");
                    break;
            }
                        
        }
        sc.close();
    }
    
    //GRAPH GENERATING METHOD
    
    private static Graph graphGenerator(int v, int e) {
        Graph tempG = new Graph();
        int numOfEdges, numOfVertices, maxOfVertices, minOfVertices, maxOfEdges, minOfEdges, i, v1, v2, edgesLeft; //graph atributes, etc. 
        Random rand;
        
        if (v==0) {
            numOfEdges = e;
            maxOfVertices = numOfEdges + 1;
            minOfVertices = (int) Math.ceil((1 + Math.sqrt(1 + 8 * numOfEdges)) / 2);

            rand = new Random();
            numOfVertices = rand.nextInt(maxOfVertices - minOfVertices) + minOfVertices;
            
            //System.out.println("Maximum number of vertices:" + maxOfVertices);
            //System.out.println("Minimum number of vertices:" + minOfVertices);
            //System.out.println("Graph consists of: [" + numOfVertices +"] vartices and: ["+numOfEdges+"] edges");
            
            tempG = new Graph(numOfVertices, numOfEdges);
            
            //ensure that all vertex has connection to the graph
            for (i = 1; i <= numOfVertices; i++ ) {
                while ( ((v1 = rand.nextInt(numOfVertices) + 1) == i) || (tempG.getListOfEdges(i).contains(v1)));                     
                tempG.addEdge(i, v1);
                GraphColouring.workingStatus(i, "Graph generating");
            }

            //generate the rest connections
            edgesLeft = numOfEdges - numOfVertices;
            for (i = 1; i<=edgesLeft; i++) {
                v1 = rand.nextInt(numOfVertices) + 1;
                while ( ((v2 = rand.nextInt(numOfVertices) + 1) == v1) || (tempG.getListOfEdges(v1).contains(v2)));
                tempG.addEdge(v1, v2);
                GraphColouring.workingStatus(i, "Graph generating");
            }
            
        } else if (e==0) {
            numOfVertices = v;
            minOfEdges = numOfVertices - 1;
            maxOfEdges = (int) Math.ceil((numOfVertices*(numOfVertices-1)) / 2);

            rand = new Random();
            numOfEdges = rand.nextInt(maxOfEdges - minOfEdges) + minOfEdges;

            //System.out.println("Maximum number of edges:" + maxOfEdges);
            //System.out.println("Minimum number of edges:" + minOfEdges);
            
            tempG = new Graph(numOfVertices, numOfEdges);
                    
            //ensure that all vertex has connection to the graph
            for (i = 1; i <= numOfVertices; i++ ) {
                while ( ((v1 = rand.nextInt(numOfVertices) + 1) == i) || (tempG.getListOfEdges(i).contains(v1)));                     
                tempG.addEdge(i, v1);
                GraphColouring.workingStatus(i, "Graph generating");
            }

            edgesLeft = numOfEdges - numOfVertices;

            //generate the rest connections
            for (i = 1; i<=edgesLeft; i++) {
                v1 = rand.nextInt(numOfVertices) + 1;
                while ( ((v2 = rand.nextInt(numOfVertices) + 1) == v1) || (tempG.getListOfEdges(v1).contains(v2)));
                tempG.addEdge(v1, v2);
                GraphColouring.workingStatus(i, "Graph generating");
            }
        }
        
        
        //sort list of vertices
        tempG.sortVertices();
        return tempG;
    }
    
    //UTILITY METHODS
    
    public static void workingStatus(int i, String description) {
        if ((i%3)==0) {
            System.out.println(description+" ..");
        } else if ((i%3)==1) {
            System.out.println(description+" ....");
        } else {
            System.out.println(description+" ......");
        }
    }
    
}
