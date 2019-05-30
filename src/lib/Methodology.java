package lib;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;


/**
 * @author Miguel Jiménez Gomis
 * @email jimenezgomismiguel@gmail.com
 * 
 * This class is the implementation od the proposed methodology and all the sub-functions that it requires to work
 *
 */
public class Methodology {
	ArrayList<Node> data;
	ArrayList<ArrayList<Node>> solution;
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
	
	
	public ArrayList<ArrayList<Node>> compute(int metricOption,double belongPercentage, double errorPercentage){
		//TODO compute the data
		return this.getSolution();
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
	
	
	
}
