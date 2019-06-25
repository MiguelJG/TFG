package lib;

import java.util.ArrayList;
import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class LED {
	ArrayList<ArrayList<Node>> solution;	//Generated clusters
	ArrayList<Node> independetnNodes;
	Graph<String, DefaultEdge> G;		//General graph of the problem
	
	
	/** Constructor from file
	 * @param Filename
	 */
	public LED(String Filename) {
		solution = new ArrayList<ArrayList<Node>>();
		GraphIO.loadGraph(G, Filename);
		independetnNodes = new ArrayList<>();
	}
	
	/** Contructor from JGraphT Graph
	 * @param G
	 */
	public LED(Graph<String, DefaultEdge> G) {
		solution = new ArrayList<ArrayList<Node>>();
		this.G= G;
		independetnNodes = new ArrayList<>();
	}	
	
	/** Getter of the solution
	 * @return
	 */
	public ArrayList<ArrayList<Node>> getSolution() {
		return solution;
	}

	/** Setter of the solution
	 * @param solution
	 */
	public void setSolution(ArrayList<ArrayList<Node>> solution) {
		this.solution = solution;
	}

	/** Computes the led algorithm and returns the different initial communities 
	 * @param alpha
	 */
	public void compute(Double alpha) {
		Graph<String, DefaultEdge> curG = this.G; // current graph
		ArrayList<DefaultEdge> edgeListi = new ArrayList<DefaultEdge>(); // Store the ith deleted edge
		//Step 1 and Step 2 calculating structural similarity and deleting edges less than alpha
		edgeDelete(edgeListi, curG, alpha);
		//Step 3 Identify overlapping vertices
		for(DefaultEdge edge : edgeListi) {
			overlapping(edge);
		}
		for(Node node : independetnNodes) { // extend initial communities adding isolated vertices
			addTohihgestStrSim(node);
		}
	}
	
	/** Delete all the edgest with a struct sim lesser than alpha and recursively calculates the subgraphs generated until nothing gets deleted or there is just 1 element
	 * @param edgeListi
	 * @param G
	 * @param alpha
	 */
	private void edgeDelete(ArrayList<DefaultEdge> edgeListi, Graph<String, DefaultEdge> G, Double alpha) {
		//Delete edges
		for(DefaultEdge edge : G.edgeSet()) {
			if(calcStructSim(edge, G) < alpha) {
				edgeListi.add(edge); // no se elimina directamente para poder calcular la similaridad estructural correctamente
			}
		}
		for(DefaultEdge edge : edgeListi) {
			G.removeEdge(edge);
		}
		//Check for different interconnected components 
		ConnectivityInspector connectivity = new ConnectivityInspector(G);
		List<Set<String>> interconnectedComponents = connectivity.connectedSets();		
		if(interconnectedComponents.size() == 1) { // si solo hay un componente, eso quiere decir que no se puede separar mas asi que se marca como comunidad
			ArrayList<Node> community = new ArrayList<Node>();
			for(String vertex : interconnectedComponents.get(0)) {
				ArrayList<String> edges = new ArrayList<String>();
				for(DefaultEdge str : G.edgesOf(vertex)) {
					edges.add(G.getEdgeSource(str).equals(vertex)?G.getEdgeTarget(str):G.getEdgeSource(str));
				}
				community.add(new Node(vertex, edges));
				if(community.size() == 1) {
					independetnNodes.add(community.get(0));
				} else { //si la componente tiene mas de un elemento se marca como comunidad inicial
					solution.add(community);
				}
			}
		} else {
			for(Set<String> commponent : interconnectedComponents) {
				Graph<String, DefaultEdge> subG = new DefaultDirectedGraph<>(DefaultEdge.class);
				for(String str : commponent) {
					subG.addVertex(str);
				}
				for(String str : commponent) {
					for(DefaultEdge edg : G.edgesOf(str)) {
						subG.addEdge(G.getEdgeSource(edg), G.getEdgeTarget(edg));
					}
				}
				edgeDelete(edgeListi, subG, alpha);
			}
		}
	}
	
	/**Checks if the edge is overlapping and adds the vertices to both communities
	 * @param edge
	 */
	void overlapping(DefaultEdge edge) {
		//TODO
	}
	
	/** Adds the given node to the community with highest structural similarity
	 * @param node
	 */
	void addTohihgestStrSim(Node node) {
		//TODO
	}
	
	/** Calculates the structural similarity of 2 vertices
	 * @param edge
	 * @param G
	 * @return
	 */
	private Double calcStructSim(DefaultEdge edge, Graph<String, DefaultEdge> G) {
		//TODO
		return 0.0;
	}
	
	/** Method that returns true if both vertices are in the same community
	 * @param v1
	 * @param v2
	 * @return
	 */
	private  boolean sameCommunity(String v1, String v2) {
		for(ArrayList<Node> community : solution) {
			for(Node node : community) {
				if(node.getId().equals(v1)) {
					for(Node node2 : community) {
						if(node2.getId().equals(v2)) {
							return true;
						}
					}
					return false;
				}
			}
		}
		return false;
	}
	
	
	/** This method returns index to the community in which a given vertex is
	 * @param vertex
	 * @return
	 */
	private int getCommunity(String vertex){
		for(ArrayList<Node> community : solution) {
			for(Node node : community) {
				if(node.getId().equals(vertex)) {
					return solution.indexOf(community);
				}
			}
		}
		return -1;
	}
}
