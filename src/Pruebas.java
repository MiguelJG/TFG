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
		/*Graph<Integer, DefaultEdge> graph =  new DefaultDirectedGraph<>(DefaultEdge.class);

        graph.addVertex(7);
        graph.addVertex(4);
        graph.addVertex(9);
        graph.addVertex(3);
        graph.addVertex(2);
        graph.addVertex(5);


        graph.addEdge(7, 4);
        graph.addEdge(7, 9);
        graph.addEdge(9, 3);
        graph.addEdge(3, 2);
        graph.addEdge(3, 5);

        GraphIterator<Integer, DefaultEdge> iterator = 
                new DepthFirstIterator<Integer, DefaultEdge>(graph);
        ArrayList<Integer> visited = new ArrayList<Integer>();
        while (iterator.hasNext()) {
        	Integer element = iterator.next();
            for(Integer inte: Graphs.neighborListOf(graph,element)){
            	if(!visited.contains(inte))
            		System.out.println(element + "->" + inte);
            }
            visited.add(element);
        }*/
		Graph<String, DefaultEdge> G = new DefaultDirectedGraph<>(DefaultEdge.class);
		GraphIO.loadGraph(G, "karate.txt");
		System.out.println(G);
		LED led = new LED(G);
		led.compute(0.35);
		for(ArrayList<Node> temp : led.solution) {
			System.out.println(temp);
		}
    }
}


