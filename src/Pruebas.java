import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import lib.*;

import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

public class Pruebas {
	public static void main(String[] args) {
		
		Graph<String, DefaultEdge> G = new DefaultDirectedGraph<>(DefaultEdge.class);
		GraphIO.loadGraph(G, "karate.txt");
		System.out.println(G);
		LED led = new LED("karate.txt");
		led.compute(0.35);
		for(ArrayList<Node> temp : led.solution) {
			System.out.println(temp);
		}
    }
}


