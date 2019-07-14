package lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/** This class includes a few methods to import and export graphs from a to GML file format
 * @author Miguel Jiménez Gomis
 *
 */
public class GraphIO {
	
	/** Load graph from a list of integer pairs graph file into a JgraphT graph
	 * @param G
	 * @param Filename
	 */
	public static void loadGraph(Graph<String, DefaultEdge> G, String Filename) {
		 try {
			 File f = new File(Filename);
	         BufferedReader b = new BufferedReader(new FileReader(f));
	         String readLine = "";
	         b.readLine(); // quitamos el nombre e info extra
	         readLine = b.readLine(); // extraemos el numero de elementos del grafo
	         for(int i = 0; i < new Integer(readLine.replaceAll("//s+", "")); i++) {
	        	 G.addVertex(new Integer(i + 1).toString());// se añaden los vertices
	         }
	         while ((readLine = b.readLine()) != null) { // se añaden los enlaces
	        	 for(String str : readLine.split(",")) {
	        		 if(!str.equals("")) {
		                str = str.replaceAll("\\[|\\]", "");
		                G.addEdge(str.split(" ")[0], str.split(" ")[1]);
	        		 }
	               }
	         }
	         b.close();
		 } catch (IOException e) {
			 e.printStackTrace();
	     }
	}
	
	/**Exports the graph as list of integer pairs
	 * @param G
	 * @param Filename
	 */
	public static void exportGraph(Graph<String, DefaultEdge> G, String Filename) {
		try {
			 File f = new File(Filename);
	         BufferedWriter b = new BufferedWriter(new FileWriter(f));	         
	         b.write("Generated graph");//Se añade el nombre
	         b.write(G.vertexSet().size()); // se añade el numero de vertices
	         for(String str : G.vertexSet()) { // se añaden los enlaces (pueden existir duplicados)
	        	 String writeLine = "";
	        	 for(DefaultEdge edge : G.edgesOf(str)) {
	        		 writeLine += "[" + G.getEdgeSource(edge) + " " + G.getEdgeTarget(edge) + "]";
	        	 }
	        	 b.write(writeLine);
	         }
	         b.flush();
	         b.close();
		 } catch (IOException e) {
			 e.printStackTrace();
	     }
	}
}
