package graphcolouring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author Dell
 */
public class FilesIO {
    public static void saveGraphAsEL(String path, Graph g) {
        try {
            //File file = new File(path);
            FileWriter fw = new FileWriter(new File(path));
            fw.write(g.showEdgeList());
            fw.flush();
            fw.close();
            System.out.println("File saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Graph openGraphAsEL(String path) {
        Graph tempG = null;
        int tempV;
        int tempE = 0;
        StringTokenizer sTok;
        String tempLine;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            tempV = Integer.parseInt(br.readLine());
            tempG = new Graph(tempV);
            while((tempLine=br.readLine())!=null) {
                sTok = new StringTokenizer(tempLine);
                tempG.addEdge(Integer.parseInt(sTok.nextToken()), Integer.parseInt(sTok.nextToken()));
                tempE++;
                GraphColouring.workingStatus(tempE, "File opening - graph reading");
            }
            br.close();
            tempG.setNumE(tempE);
            System.out.println("File opening and parsing succeeded: "+tempV);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return tempG;
        }
        
    }
}
