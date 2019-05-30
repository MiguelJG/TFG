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
 * This class implements the creation of an artificial graph structure with the given paramenters and computes the 
 * methodology with it
 */
public class ArtifficialTest {		
	ArrayList<ArrayList<Node>> data; // this matrix represents the different initial clusters(by row)	
	public Graph<String, DefaultEdge> G;	//this is the graph of the problem

	
	/** This method generates the artificial data from which the graph will be created
	 * @param numClusters  Number of clusters that the graph will have
	 * @param clusterSize Number of elements of each graph
	 * @param clusterConexion	percentage of nodes linked between graphs
	 * @param	numSolapCom number of solaping communities
	 */
	private void createData(int numClusters, int clusterSize,int numSolapCom , double clusterConexion) {
		for(int i = 0; i < numClusters; i++) {//initial data with separated clusters //TODO Las comunidades solapadas son elementos en varias comunidades a la vez
			ArrayList<Node> temp = new ArrayList<Node>();
			for(int j = 0; j < clusterSize; j++) { //nodes id added
				temp.add(new Node((i + "|" + j), new ArrayList<String>()));
				for(int k = 0; k < clusterSize; k++) { //links are added
					if(k != j) {
						temp.get(j).addLink((i + "|" + k));
					}
				}
			}
			this.data.add(temp);
		}
		for(int i = 0; i < numClusters; i++) { //links between clusters
			for(int k = 0; k < (clusterSize * (clusterConexion / 100)); k++) {
				for(int j = 0; j < clusterSize; j++) {	
					if(k != j) {
						data.get(i).get(k).addLink((i + "|" + j));
					}
				}
			}
		}
	}
	
	/** This method generates the initial graph in which we will compute the results
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
	 * @param numClusters total number of clusters
	 * @param clusterSize Size of the different clusters
	 * @param numSolapCom number of solaping communities 
	 * @param clusterConexion	percentage of links between clusters 
	 * @param initialNodes percentage of nodes that will be in the initial cluster
	 * @return
	 */
	public ArrayList<Double> compute(int numClusters, int clusterSize, int numSolapCom, double clusterConexion, double initialNodes) {
		ArrayList<Double> results = new ArrayList<Double>();
		this.createData(numClusters, clusterSize, numSolapCom, clusterConexion);
		this.generateGrpah(numClusters, clusterSize, initialNodes);
		//TODO generate results with the methodology method
		return results;
	}
	


}
