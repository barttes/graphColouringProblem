package graphcolouring;

import java.util.Arrays;
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
            System.out.println("\t[8] - use greedy LF algorithm to find chromatic number");
            System.out.println("\t[9] - use brute force algorithm to find chromatic number");
            System.out.println("\t----------------UTILITY FUNCTIONS------------------------------------------");
            System.out.println("\t[10] - generate to first difference");
            System.out.println("\t[11] - info about graph");
            
            choice = sc.nextInt(); //read input from user
            String filePath = "graphsToColour/";
            
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
                        filePath += sc.nextLine();
                        FilesIO.saveGraphAsEL(filePath, g1);
                    } else {
                        System.out.println("There is no graph to save!");
                    }
                    
                    break;
                case 6:
                    System.out.println("Give a name of the file: ");
                    sc.nextLine();
                    filePath += sc.nextLine();
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
                        g1.showGreedyColouring();
                    }
                    choice = -1;
                    
                    break;
                    
                    /*
                        Improved greedy algorithm. It sorts vertices by degree of each vertex descendig, 
                        or ascending then with that order colour it using greedy algorithm.
                    */
                case 8:
                    vertexColoringOrder = new int[g1.getNumV()];
                    //check if vertexDegree array has been declared, if not initialize it
                    if ( g1.getVertexDegree()[g1.getNumV()-1][1] == 0) {
                        g1.initializeVerticesDegrees();
                    }
                    
                    for (i=(g1.getNumV()-1); i>=0; i--) {
                        vertexColoringOrder[g1.getNumV()-i-1] = g1.getVertexDegree()[i][0];
                    }
                       
                    g1.colorGreedy(vertexColoringOrder);
                    nC = g1.getChromaticNumberFoundGreedyLFBF();
                    
                    System.out.println("Greedy LF algorith: The graph has been colored with <<<"+nC+">>>");
                    System.out.println("Do you want to show colors of vertices? [1 - yes; 0 - no]");
                    sc.nextLine();
                    if ( (choice = sc.nextInt()) == 1) {
                        g1.showGreedyLFColouring();
                    }
                    
                    choice = -1;
                    
                    break;
                    
                    /*
                        Brute force algorithm to find all graph coloring. It uses greedy algorith, but it colors
                        vertices in all possible orders. Algorith sets chromatic number as the smallest possible coloring
                        of graph.
                    */
                case 9:
                    vertexColoringOrder = new int[g1.getNumV()];
                    
                    //initialize standard vertex coloring order 1,2...num of vertex
                    for (i = 0; i<vertexColoringOrder.length; i++) {
                        vertexColoringOrder[i] = i+1;
                    }
                    
                    g1.permuteAndColor(vertexColoringOrder, vertexColoringOrder.length);
                    nC = g1.getChromaticNumberFoundGreedyLFBF();
                    
                    System.out.println("Brute force algorith: The graph has been colored with <<<"+nC+">>>");
                    System.out.println("Do you want to show colors of vertices? [1 - yes; 0 - no]");
                    sc.nextLine();
                    if ( (choice = sc.nextInt()) == 1) {
                        for (i = 0; i<g1.getNumV(); i++) {
                            System.out.println((i+1)+"# vertex has color: " + g1.getArrayOfColorsGreedyLFBF()[i]);
                        }
                    }
                    choice = -1;
                                        
                    break;
                    
                    /*
                        Generate instance and check if it has differences in greedy and brute force colouring
                    */
                    
                case 10:
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
                        System.out.println("Brute force: "+g1.getChromaticNumberFoundGreedyLFBF());
                        nC = g1.colorGreedy();
                        System.out.println("Greedy: "+nC);
                    } while (g1.getChromaticNumberFoundGreedyLFBF() == nC);
                    System.out.println("Graph with differences has been found!");
                    
                    choice = -1;
 
                    break;
                    
                case 11:
                    System.out.println("Graph information:");
                    System.out.println(g1.toString());
                    
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
        Random rand = new Random();
        
        if (v==0) {
            numOfEdges = e;
            maxOfVertices = numOfEdges + 1;
            minOfVertices = (int) Math.ceil((1 + Math.sqrt(1 + 8 * numOfEdges)) / 2);
            
            System.out.println("Maximum number of vertices:" + maxOfVertices);
            System.out.println("Minimum number of vertices:" + minOfVertices);

            numOfVertices = rand.nextInt(maxOfVertices - minOfVertices) + minOfVertices;
        } else {
            numOfVertices = v;
            minOfEdges = v - 1;
            maxOfEdges = (int) Math.ceil((v*(v-1)) / 2);
            
            System.out.println("Maximum number of edges:" + maxOfEdges);
            System.out.println("Minimum number of edges:" + minOfEdges);

            numOfEdges = rand.nextInt(maxOfEdges - minOfEdges) + minOfEdges;
        }
        
        System.out.println("Graph consists of: [" + numOfVertices +"] vartices and: ["+numOfEdges+"] edges");

        tempG = new Graph(numOfVertices, numOfEdges);

        //ensure that all vertex has connection to the graph - conect all vertices together
        for (i = 1; i < numOfVertices && i <= numOfEdges; i++ ) {                      
            tempG.addEdge(i, i+1);
            //System.out.println("Adding edge:"+i+"->"+(i+1));
            //GraphColouring.workingStatus(i, "Graph generating");
        }

        edgesLeft = numOfEdges - numOfVertices;

        //generate the rest, random connections
        for (i = 0; i<=edgesLeft; i++) {
            //ensure that vertex won't connect to itself and there won't be double vertex connection
            while ( ( (v1 = rand.nextInt(numOfVertices) + 1) == (v2 = rand.nextInt(numOfVertices) + 1) ) || 
                    (tempG.getListOfEdges(v1).contains(v2)) );
            tempG.addEdge(v1, v2);
            //System.out.println("Adding edge:"+v1+"->"+v2);
            //GraphColouring.workingStatus(i, "Graph generating");
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
