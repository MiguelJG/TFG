package Experimentation;
import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import lib.Node;
/**
 * @author Miguel Jiménez Gomis
 * @email jimenezgomismiguel@gmail.com
 * 
 * This class implements the creation of an artifficial graph structure with the given paramenters and computes the 
 * methodology with it
 */
public class ArtifficialTest {		
	ArrayList<ArrayList<Node>> data; // this matrix represents the different initial clusters(by row)	
	public Graph<String, DefaultEdge> G;	//this is the graph of the problem

	
	/** This method generates the artificial data from which the graph will be created
	 * @param numClusters  Number of clusters that the graph will have
	 * @param clusterSize Number of elements of each graph
	 * @param clusterConexion	percentage of nodes linked between graphs
	 */
	private void createData(int numClusters, int clusterSize, double clusterConexion) {
		for(int i = 0; i < numClusters; i++) {//initial data with separated clusters
			ArrayList<Node> temp = new ArrayList<Node>();
			for(int j = 0; j < clusterSize; j++) { //nodes id added
				temp.add(new Node((i + "" + j), new ArrayList<String>()));
				for(int k = 0; k < clusterSize; k++) { //links are added
					if(k != j) {
						temp.get(j).addLink((i + "" + k));
					}
				}
			}
			this.data.add(temp);
		}
		for(int i = 0; i < numClusters; i++) { //links between clusters
			for(int k = 0; k < (clusterSize * (clusterConexion / 100)); k++) {
				for(int j = 0; j < clusterSize; j++) {		
						data.get(i).get(j).addLink((i + "" + j));//TODO Repensar aquí se tienen que generar el numero de comunidades solapadas adecuadas
				}
			}
		}
	}
	
	/** This method generates the initial graph in wich we will compute the results
	 * @param initialNodes Percentage of nodes that will be in the initial graph
	 */
	private void generateGrpah(int numClusters,int clusterSize,  Double initialNodes) {		
		for(int i = 0; i  < (clusterSize * (initialNodes / 100)); i++){//nodes added (vertex)
			for(int j = 0; j < numClusters; j++) {
				this.G.addVertex(this.data.get(j).get(i).getId()); //the proportionate number of elements per row
			}
		}
		for(int j = 0; j < numClusters; j++) {//links added (edges)
			for(int i = 0; i  < (clusterSize * (initialNodes / 100)); i++){ 	
				for (String link : this.data.get(j).get(i).getLinks()) {
					if(G.containsVertex(link) && G.containsVertex(this.data.get(j).get(i).getId())) {
						this.G.addEdge(this.data.get(j).get(i).getId(), link); // add all the links in the graph
					}
				}
			}
		}
	}
	
	/** Default constructor
	 * 
	 */ 
	public ArtifficialTest() {
		this.data = new ArrayList<ArrayList<Node>>();
		this.G= new DefaultDirectedGraph<>(DefaultEdge.class);
	}
	
	/** This method generates and computes the methodology for the given parameters
	 * @param numClusters
	 * @param clusterSize
	 * @param clusterConexio
	 * @return
	 */
	public ArrayList<Double> compute(int numClusters, int clusterSize, double clusterConexio, double initialNodes) {
		ArrayList<Double> results = new ArrayList<Double>();
		this.createData(numClusters, clusterSize, clusterConexio);
		this.generateGrpah(numClusters, clusterSize, initialNodes);
		//TODO generate results with the methodology method
		return results;
	}
	


}
