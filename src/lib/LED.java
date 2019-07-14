package lib;

import java.util.ArrayList;
import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.common.collect.Lists;

public class LED {
	public ArrayList<ArrayList<Node>> solution;	//Generated clusters
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
		for(DefaultEdge edge : Lists.reverse(edgeListi)) { 
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
			Double temp = calcStructSim(edge, G);
			if(temp < alpha) {
				edgeListi.add(edge); // no se elimina directamente para poder calcular la similaridad estructural correctamente
			}
		}
		for(DefaultEdge edge : edgeListi) {
			G.removeEdge(edge);
		}
		//Check for different interconnected components 
		ConnectivityInspector<String, DefaultEdge> connectivity = new ConnectivityInspector<String, DefaultEdge>(G);
		List<Set<String>> interconnectedComponents = connectivity.connectedSets();		
		if(interconnectedComponents.size() == 1) { // si solo hay un componente, eso quiere decir que no se puede separar mas asi que se marca como comunidad
			ArrayList<Node> community = new ArrayList<Node>();
			for(String vertex : interconnectedComponents.get(0)) {
				ArrayList<String> edges = new ArrayList<String>();
				for(DefaultEdge str : G.edgesOf(vertex)) {
					edges.add(G.getEdgeSource(str).equals(vertex)?G.getEdgeTarget(str):G.getEdgeSource(str));
				}
				community.add(new Node(vertex, edges));				
			}
			if(community.size() == 1) {
				independetnNodes.add(community.get(0));
			} else { //si la componente tiene mas de un elemento se marca como comunidad inicial
				this.solution.add(community);
				System.out.println("Comunidad inicial detectada");
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
		G.getEdgeSource(edge);
		int C1 = getCommunity(G.getEdgeSource(edge));
		int C2 = getCommunity(G.getEdgeTarget(edge));
		if(C1 != C2) {
			if(C1 == -1) { // para vertices aislados
				
			} else if(C2 == -1) { // para vertices aislados
				
			} else {
				Double adC1 = averageDegree(C1, G, solution);
				Double adC2 = averageDegree(C2, G, solution);
				ArrayList<Node> temp = solution.get(C1);
				ArrayList<Node> dummy = solution.get(C1);
				dummy.add(getNode(G.getEdgeTarget(edge)));
				solution.set(C1, dummy);
				Double adC1p = averageDegree(C1, G, solution);
				if(adC1p < adC1) { // si no existe solapamiento en la primera comunidad y se prueba la segunda
					solution.set(C1, temp); //se vuelve al estado inicial
					temp = solution.get(C2);
					dummy = solution.get(C2);
					dummy.add(getNode(G.getEdgeSource(edge)));
					solution.set(C2, dummy);
					Double adC2p = averageDegree(C2, G, solution);
					if(adC2p < adC2) { // si no existe solapamiento en la segunda comunidad, se vuelve al estado inicial
						solution.set(C2, temp);
					}
				}
			}
		}
	}
	
	/** Adds the given node to the community with highest structural similarity
	 * @param node
	 */
	void addTohihgestStrSim(Node node) {
		//TODO revisar ----------------------------------------------------
		int community = -1;
		Double structSim = 0.0;
		for(String vertice : node.getLinks()) {
			String id = node.getId();
			DefaultEdge temp = G.getEdge(id, vertice);
			if(temp == null) {
				temp = G.getEdge(vertice,id);
				Double dummy = calcStructSim(temp,this.G);
				if(structSim < dummy) {
					structSim = dummy;
					community = getCommunity(id);
				}
			}else {
				Double dummy = calcStructSim(temp,this.G);
				if(structSim < dummy) {
					structSim = dummy;
					community = getCommunity(vertice);
				}
			}
		}
		if(community != -1) {
			ArrayList<Node> temp = this.solution.get(community);
			temp.add(node);
			this.solution.set(community, temp);
		}else {
			ArrayList<Node> temp = new ArrayList<Node>();
			temp.add(node);
			this.solution.add(temp);
		}
	}
	
	/** Calculates the structural similarity of 2 vertices
	 * @param edge
	 * @param G
	 * @return
	 */
	private Double calcStructSim(DefaultEdge edge, Graph<String, DefaultEdge> G) {
		Set<String> u = Graphs.neighborSetOf(G, G.getEdgeSource(edge));
		Set<String> v = Graphs.neighborSetOf(G, G.getEdgeTarget(edge));
		int uSize = u.size();
		int vSize = v.size();
		u.retainAll(v);
		int uIntervSize = u.size();
		return (uIntervSize)/(Math.sqrt(uSize * vSize));
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
	
	private Node getNode(String vertex) {
		for(ArrayList<Node> community : solution) {
			for(Node node : community) {
				if(node.getId().equals(vertex)) {
					return node;
				}
			}
		}
		throw new Error("El nodo que se intenta extraer no existe");
	}
	
	/** Calculates the Average degree of a given community
	 * Option 3
	 * @return
	 */
	public double averageDegree(int community, Graph<String, DefaultEdge> G, ArrayList<ArrayList<Node>> solution) {
		ArrayList<String> e = new ArrayList<String>(); //number of links with both vertex in the same community
		ArrayList<String> c = new ArrayList<String>(); //total number of links of the community
		for(Node vertex : solution.get(community)) {
			for(String neighbor : Graphs.neighborListOf(G, vertex.getId())) {
				for(Node elem : solution.get(community)) {
					if(neighbor == elem.getId()) {
						e.add(neighbor);
					}
				}
				c.add(neighbor); // se añade esté o no en la comunidad (todos los links)
			}
		} 
		return new Double(2 * (e.size() / c.size()));
	}
}
