package Experimentation;
import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import lib.Methodology;
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
	ArrayList<ArrayList<Node>> initialData;
	ArrayList<Node> inputData;
	public Graph<String, DefaultEdge> G;	//this is the graph of the problem
	private Methodology M;
	
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
				for(int k = 0; k < numClusters; k++) {//Se añaden los links a los otros cluster
					if(k != j) {
						for(double h = 0; h < (clusterSize * (clusterConexion / 100)); h++) {
							temp.get(j).addLink((k + "|" + (int)h));
						}
					}
				}
			}
			this.data.add(temp);
		}
	}
	
	/** This method generates the initial graph in which we will compute the results
	 * @param initialNodes Percentage of nodes that will be in the initial graph
	 */
	private void generateGrpah(int numClusters,int clusterSize,  Double initialNodes) {
		for(int j = 0; j < numClusters; j++) {	
			ArrayList<Node> dummy = new ArrayList<Node>();
			for(int i = 0; i < clusterSize; i++){//nodes added (vertex)		
				if(i  < (clusterSize * (initialNodes / 100))) {
					this.G.addVertex(this.data.get(j).get(i).getId()); //the proportionate number of elements per row
					dummy.add(this.data.get(j).get(i));
				} else {
					inputData.add(this.data.get(j).get(i));
				}
			}
			initialData.add(dummy);
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
		this.initialData = new ArrayList<ArrayList<Node>>();
		this.G= new DefaultDirectedGraph<>(DefaultEdge.class);
		this.inputData = new ArrayList<Node>();
	}
	
	/** This method generates and computes the methodology for the given parameters
	 * @param numClusters total number of clusters
	 * @param clusterSize Size of the different clusters
	 * @param numSolapCom number of solaping communities 
	 * @param clusterConexion	percentage of links between clusters 
	 * @param initialNodes percentage of nodes that will be in the initial cluster
	 * @return
	 */
	public ArrayList<String> compute(int numClusters, int clusterSize, int numSolapCom, double clusterConexion, double initialNodes) {

		this.createData(numClusters, clusterSize, numSolapCom, clusterConexion);
		this.generateGrpah(numClusters, clusterSize, initialNodes);
		System.out.println(this.G);
		this.M = new Methodology(this.G, this.initialData, inputData);		
		return this.M.compute(1, 90);
	}
	


}
