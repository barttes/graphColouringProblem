package graphcolouring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dell
 */
public class Graph {
    
    private Map<Integer, List<Integer>> adjList; //adjacency list
    private int[] arrayOfColorsGreedy;
    private int chromaticNumberFoundGreedy;
    private int[] arrayOfColorsGreedyLFBF;
    private int chromaticNumberFoundGreedyLFBF = 0;
    private int numV;
    private int numE;
    private int[][] vertexDegree; 
    
    //=============CONSTRUCTORS================
    
    public Graph() {
    }
    
    public Graph(int v) {
        this.numV = v;
        vertexDegree = new int[this.getNumV()][2];
        adjList = new HashMap<>();
        arrayOfColorsGreedy = new int[this.numV];
        arrayOfColorsGreedyLFBF = new int[this.numV];
        
        for(int i = 1; i <= v; i++) {
            adjList.put(i, new LinkedList<Integer>());
        }
    }
    
    public Graph(int v, int e) {
        this.numV = v;
        this.numE = e;
        vertexDegree = new int[this.getNumV()][2];
        adjList = new HashMap<>();
        arrayOfColorsGreedy = new int[this.numV];
        arrayOfColorsGreedyLFBF = new int[this.numV];
        
        for(int i = 1; i <= v; i++) {
            adjList.put(i, new LinkedList<Integer>());
        }
    }
    
    //================GETTERS AND SETTERS================
    
    /**
     * @return the arrayOfColors
     */
    public int[] getArrayOfColorsGreedy() {
        return arrayOfColorsGreedy;
    }

    /**
     * @return the numV
     */
    public int getNumV() {
        return numV;
    }
    
    /**
     * @param numE the numE to set
     */
    public void setNumE(int numE) {
        this.numE = numE;
    }
       
    
    public List<Integer> getListOfEdges(int v) {
        return getAdjList().get(v);
    }
    
    /**
     * @return the adjList
     */
    public Map<Integer, List<Integer>> getAdjList() {
        return adjList;
    }
    
    /**
     * @return the chromaticNumberFoundBF2
     */
    public int getChromaticNumberFoundGreedyLFBF() {
        return chromaticNumberFoundGreedyLFBF;
    }
    
    /**
     * @return the arrayOfColorsBF2
     */
    public int[] getArrayOfColorsGreedyLFBF() {
        return arrayOfColorsGreedyLFBF;
    }
    
    /**
     * @return the vertexDegree
     */
    public int[][] getVertexDegree() {
        return vertexDegree;
    }
    
    /**
     * @return the numE
     */
    public int getNumE() {
        return numE;
    }
    
    //================GRAPH MODIFICATION METHODS================
    
    public void addEdge(int v1, int v2) {
        //generated graph is indirected so add adnotation about edge in two vertexes
        getAdjList().get(v1).add(v2);
        getAdjList().get(v2).add(v1);
    }

    public void sortVertices(){
        for (int i = 1 ; i <= getNumV() ; i++  ) {
            Collections.sort(this.getListOfEdges(i));
        }
    }
    
    public void initializeVerticesDegrees(){
        //initialize array of vertices degree
        for (int i = 0; i<this.getVertexDegree().length; i++) {
            vertexDegree[i][0] = (i+1);
            vertexDegree[i][1] = this.getListOfEdges(i+1).size();
        }
        
        //sort vertices by degrees
        Arrays.sort(this.getVertexDegree(), (a, b) -> Integer.compare(a[1], b[1]));
    }
    
    //================UTILITY METHODS================
    
    @Override
    public String toString(){
        String output = "";
        
        //graph density for undirected graph is: 2E / V * (V-1)
        float density = (float)(2*numE)/(numV*(numV-1));
        
        int maxDeegree, minDeegree;
        if ( (maxDeegree = this.getVertexDegree()[this.getNumV()-1][1]) == 0) {
            this.initializeVerticesDegrees();
            maxDeegree = this.getVertexDegree()[this.getNumV()-1][1];
            minDeegree = this.getVertexDegree()[0][1];
        } else {
            maxDeegree = this.getVertexDegree()[this.getNumV()-1][1];
            minDeegree = this.getVertexDegree()[0][1];
        }
        
        output = output + "Number of vertices: " + this.getNumV() + "\n";
        output = output + "Number of edges: " + this.getNumE() + "\n";
        output = output + "Graph density: " + Float.toString(density) + "\n";
        output = output + "Max vertex deegree: " + maxDeegree + "\n";
        output = output + "Min vertex deegree: " + minDeegree + "\n";
        
        return output;
    }
    
    public String showAdjacencyList() {
        String output = "Number of vertices: " + Integer.toString(getNumV()) + "\n";
        output = output + "Number of edges: " + Integer.toString(getNumE()) + "\n";
        for (int i = 1; i<=getNumV(); i++) {
            output = output + Integer.toString(i) + " " + this.getListOfEdges(i).toString() + "\n";
        }
        
        return output;
    }
    
    public String showEdgeList() {
        String output = Integer.toString(getNumV()) + "\n";
        for (int i = 1; i<=getNumV(); i++) {
            for (int vertexConnected : this.getAdjList().get(i)) {
                if (vertexConnected > i) {
                    output = output + Integer.toString(i) + " " + Integer.toString(vertexConnected) + "\n";               
                }

            }
        }
        
        return output;
    }
    
    public void showGreedyColouring() {
        for (int i = 0; i<this.getNumV(); i++) {
            System.out.println((i+1)+"# vertex has color: " + this.getArrayOfColorsGreedy()[i]);
        }
    }
    
    public void showGreedyLFColouring() {
        for (int i = 0; i<this.getNumV(); i++) {
            System.out.println((i+1)+"# vertex has color: " + this.getArrayOfColorsGreedyLFBF()[i]);
        }
    }
    
    //================GRAPH COLORING METHODS================
    
    /*
        Greedy algorithm to find first possible graph coloring
    */

    public int colorGreedy(){
        int i, j, numOfColors = 0;
        int[] tempColorArray = new int[this.getNumV()];
        Arrays.fill(tempColorArray, -1);
        boolean[] arrayOfTakenColors = new boolean[this.getNumV()];
        
        //first vertex has 0-color
        tempColorArray[0] = 0;
        
        //start from second vertex
        for (i=2; i<=this.getNumV(); i++) {
            Arrays.fill(arrayOfTakenColors, true);
            for (int vertexConnected : this.getListOfEdges(i) ) {
                if (tempColorArray[vertexConnected-1] != -1) {
                    arrayOfTakenColors[tempColorArray[vertexConnected-1]] = false;
                }
            }
            
            //search for the first possible color
            j = 0;
            while (arrayOfTakenColors[j]!=true) {
                j++;
            }
            
            //set vertex color and number of used colors if the color is bigger           
            if ( (tempColorArray[i-1] = j) > numOfColors ) { numOfColors=j;}
        }
        
        this.chromaticNumberFoundGreedy = numOfColors+1;
        this.arrayOfColorsGreedy = tempColorArray;
        return numOfColors+1;
    }
    
    /*
        Brute force algorithm to find all graph coloring. It uses greedy algorith, but it colors
        vertices in all possible orders. Algorith sets chromatic number as the smallest possible coloring
        of graph.
    */
    
    //algorithm is used to generate all permutations of list, that will be used to set order of vertex coloring 
    public void permuteAndColor(int[] vertexColoringOrder, int k) {
        if (k==1) {
            //start vertices coloring with specific order
            this.colorGreedy(vertexColoringOrder);
        } else {
            for (int i=0; i<k; i++) {
                
                //swap elements
                int temp = vertexColoringOrder[k-1];
                vertexColoringOrder[k-1] = vertexColoringOrder[i];
                vertexColoringOrder[i] = temp;
                
                this.permuteAndColor(vertexColoringOrder, k-1);
                
                //swap elements                
                temp = vertexColoringOrder[k-1];
                vertexColoringOrder[k-1] = vertexColoringOrder[i];
                vertexColoringOrder[i] = temp;
            }
        }
    }
    
    public void colorGreedy(int[] vertexColoringOrder){
        int i, j, numOfColors = 0;
        int[] tempColorArray = new int[this.getNumV()];
        Arrays.fill(tempColorArray, -1);
        boolean[] arrayOfTakenColors = new boolean[this.getNumV()];
        
        //first vertex, that is pointed by vertexColoringOrder has 0-color
        tempColorArray[vertexColoringOrder[0]-1] = 0;
        
        //iterate all vertices and their connected vertices and check for the color availability
        for (i=1; i<vertexColoringOrder.length; i++) {
            Arrays.fill(arrayOfTakenColors, true);
            for (int vertexConnected : this.getListOfEdges(vertexColoringOrder[i])) {
                if(tempColorArray[vertexConnected-1] != -1) {
                    arrayOfTakenColors[tempColorArray[vertexConnected-1]] = false;
                }
            }
            
            //search for the first available color
            j = 0;
            while (arrayOfTakenColors[j] != true) {
                j++;
            }
            
            //set vertex color and number of used colors if the color is bigger
            if ( (tempColorArray[vertexColoringOrder[i]-1] = j) > numOfColors ) { numOfColors=j;}
        }
        
        //set new values to chromaticNumber and arrayOfColorsGreedy if generated 
        if (this.chromaticNumberFoundGreedyLFBF == 0) {
            this.arrayOfColorsGreedyLFBF = tempColorArray;
            this.chromaticNumberFoundGreedyLFBF = numOfColors+1;
        }
        if (numOfColors+1 < this.getChromaticNumberFoundGreedyLFBF()) {
            this.arrayOfColorsGreedyLFBF = tempColorArray;
            this.chromaticNumberFoundGreedyLFBF = numOfColors+1;
        }
    }

}