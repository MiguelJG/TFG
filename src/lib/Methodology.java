package lib;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.traverse.*;


/**
 * @author Miguel Jiménez Gomis
 * @email jimenezgomismiguel@gmail.com
 * 
 * This class is the implementation of the proposed methodology and all the sub-functions that it requires to work
 *
 */
public class Methodology {
	ArrayList<Node> data;
	ArrayList<ArrayList<Node>> solution; //initial clusters
	Graph<String, DefaultEdge> G;		//General graph of the problem
	
	/** Constructor of the methodology object
	 * @param G		Initial graph
	 * @param solution Initial set of clusters
	 * @param data List of data to be inserted in the different clusters
	 */
	public Methodology(Graph<String, DefaultEdge> G,ArrayList<ArrayList<Node>> solution, ArrayList<Node>data) {
		this.data = data;
		this.G = G;
		this.solution = solution;
	}
	
	
	/**
	 * @param metricOption
	 * @param belongPercentage se considera miembro de una comunidad cuya metrica sea mejor que este tanto porciento * el mayor valor posible
	 * @return
	 */
	public ArrayList<String> compute(int metricOption,double belongPercentage){
		ArrayList<String> results = new ArrayList<String>();
		if(metricOption == 1) {// with modularity	
			//System.out.println("Init:" + modularity());//----------------debug
			results.add("Init:" + modularity()  + "|");
			//System.out.println("Data Size:" + data.size());
			for(int i = 0; i < data.size(); i++) {				
				Double hMod = new Double(0); //higest modularity
				ArrayList<Integer> community = new ArrayList<Integer>(); // listado de comunidades en las que puede entrar
				ArrayList<Double> Modcommunity = new ArrayList<Double>(); // listado de modularidades de las comunidades en que puede entrar
				Graph<String, DefaultEdge> prevG = G; // grafo inicial, solución inicial para no perderlo
				ArrayList<ArrayList<Node>> prevSolution = solution;
				//Se inserta el dato en el grafo
				G.addVertex(data.get(i).getId());
				for(String str : data.get(i).getLinks()) {
					if(G.containsVertex(str)) {
						G.addEdge(data.get(i).getId(), str);
					}
				}
				//----
				for(int j = 0; j < solution.size(); j++) {// itera por todas las comunidades
					solution.get(j).add(data.get(i)); // se añade la posible solucion
					Double temp = this.modularity();
					if( temp > hMod) {
						community.add(j);
						Modcommunity.add(temp);
						hMod = temp;						
					} else if (temp > ((belongPercentage / 100) * hMod)) {
						community.add(j);
						Modcommunity.add(temp);
					}
					G = prevG;// se establece el grafo inicial
					solution = prevSolution;
				}
				for(int j = 0; j < Modcommunity.size();j++) {
					if(Modcommunity.get(j) < ((belongPercentage / 100) * hMod)) {
						community.set(j, -1);
					}
				}
				for(Integer index: community) {
					if(index != -1) {
						solution.get(index).add(data.get(i)); // se añade en las comunidades adecuadas
					}
				}
				//System.out.println(i + ":" + modularity());//----------------debug
				results.add(i + ":" + modularity() + "|");
			}

		}
		if(metricOption == 2) {//Similaridad Estructural promedio
			//System.out.println("Init:" + modularity());//----------------debug
			results.add("Init:" + modularity()  + "|");
			//System.out.println("Data Size:" + data.size());
			for(int i = 0; i < data.size(); i++) {				
				Double hAStructS = new Double(0); //higest average Structural Similari
				ArrayList<Integer> community = new ArrayList<Integer>(); // listado de comunidades en las que puede entrar
				ArrayList<Double> StructSimcommunity = new ArrayList<Double>(); // listado de modularidades de las comunidades en que puede entrar
				Graph<String, DefaultEdge> prevG = G; // grafo inicial, solución inicial para no perderlo
				ArrayList<ArrayList<Node>> prevSolution = solution;
				//Se inserta el dato en el grafo
				G.addVertex(data.get(i).getId());
				for(String str : data.get(i).getLinks()) {
					if(G.containsVertex(str)) {
						G.addEdge(data.get(i).getId(), str);
					}					
				}
				//----
				for(int j = 0; j < solution.size(); j++) {// itera por todas las comunidades
					//-------------------------------------------------- solution.j comunidad data.i elemento a insertar
					Double sum = 0.0;
					int n = 0;
					for(Node node : solution.get(j)) {
						DefaultEdge edge = G.getEdge(node.getId(), data.get(i).getId());
						if(edge == null) {
							edge = G.getEdge(data.get(i).getId(),node.getId());
						}
						if(edge != null) {
							sum += calcStructSim(edge);							
						}
						n++;
					}
					Double temp = sum / n;
					//--------------------------------------------
					if( temp > hAStructS) {
						community.add(j);
						StructSimcommunity.add(temp);
						hAStructS = temp;						
					} else if (temp > ((belongPercentage / 100) * hAStructS)) {
						community.add(j);
						StructSimcommunity.add(temp);
					}
					G = prevG;// se establece el grafo inicial
					solution = prevSolution;
				}
				for(int j = 0; j < StructSimcommunity.size();j++) {
					if(StructSimcommunity.get(j) < ((belongPercentage / 100) * hAStructS)) {
						community.set(j, -1);
					}
				}
				for(Integer index: community) {
					if(index != -1) {
						solution.get(index).add(data.get(i)); // se añade en las comunidades adecuadas
					}
				}
				//System.out.println(i + ":" + modularity());//----------------debug
				results.add(i + ":" + modularity() + "|");
			}		
		}
		return results;
	}
	


	public ArrayList<Node> getData() {
		return data;
	}

	public void setData(ArrayList<Node> data) {
		this.data = data;
	}

	public ArrayList<ArrayList<Node>> getSolution() {
		return solution;
	}

	public void setSolution(ArrayList<ArrayList<Node>> solution) {
		this.solution = solution;
	}

	public Graph<String, DefaultEdge> getG() {
		return G;
	}

	public void setG(Graph<String, DefaultEdge> g) {
		G = g;
	}
	
	/** Calculates the general modularity of the graph
	 * Option 1
	 * @return
	 */
	public double modularity() {
		Integer eii = 0; //fraction of links within comunnities
		Integer ai = 0; //fraction of edges with at leas one end in comunity i
		ArrayList<String> visited = new ArrayList<String>();
		//GraphIterator<String, DefaultEdge> iterator = new DepthFirstIterator<String, DefaultEdge>(this.getG());
        for(int a = 0; a < solution.size(); a++) {
        	for(int b  = 0; b < solution.get(a).size(); b++) {
        	String element = solution.get(a).get(b).getId();
            for(String link : solution.get(a).get(b).getLinks()) {//se revisan sus enlaces    
            	boolean found = false;
            	for(int i = 0; i < solution.get(a).size(); i++) {
            		if(solution.get(a).get(i).getId().equals(link) && !visited.contains(element + "|" + link)) {
            			eii++;
            			found = true;
            		}
            	}
            	if(!found && G.vertexSet().contains(link)) {
            		ai++;
            	}
            }
        }
	}
        //System.out.println("eii:" + eii + " ai:" + ai);
		return new Double(eii)/G.edgeSet().size() - new Double(Math.pow((ai/(2*G.edgeSet().size())), 2));
	}
	
	/** Calculates the conductance for a given community
	 * Option 2
	 * @param community
	 * @return
	 */
	public double conductance(int community) {
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
		return new Double((e.size() / c.size()));
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
	
	public double avgStrucSim(int community, String element) {
		double structSim = 0;
		//TODO
		return structSim;
	}
	
	/** Calculates the structural similarity of 2 vertices
	 * @param edge
	 * @param G
	 * @return
	 */
	private Double calcStructSim(DefaultEdge edge) {
		Set<String> u = Graphs.neighborSetOf(G, G.getEdgeSource(edge));
		Set<String> v = Graphs.neighborSetOf(G, G.getEdgeTarget(edge));
		int uSize = u.size();
		int vSize = v.size();
		u.retainAll(v);
		int uIntervSize = u.size();
		return (uIntervSize)/(Math.sqrt(uSize * vSize));
	}
	
}
