package lib;

import java.util.ArrayList;

/**
 * @author Miguel Jiménez Gomis
 * @email jimenezgomismiguel@gmail.com
 * 
 * This class is the abstract representation of a node element of the graph. It requires to be independent because 
 * this will imitate the incoming data
 */
public class Node {
	String Id;						//Identifier of the node
	ArrayList<String> links;		//list of the IDs wich this node has a link to
	
	public Node(String ID, ArrayList<String> links) {
		setId(ID);
		setLinks(links);
	}
	
	/** This method checks if the given element is in it
	 * @param ID
	 * @return Boolean
	 */
	public Boolean contains(String ID) {
		for (String node : links) {
			if(node == ID) {
				return true;
			}
		}
		return false;
	}
	
	/** This method adds a new link to the node
	 * @param ID
	 */
	public void addLink(String ID) {
		this.links.add(ID);
	}
	
	/** This method removes a given link from a node
	 * @param ID
	 */
	public void removeLink(String ID) {
		int i = 0;
		while(i < this.links.size()) {
			if(ID == this.links.get(i)) {
				this.links.remove(i);
				i = this.links.size();
			}
			i++;
		}
	}

	/** Getter of the id of the node
	 * @return ID string identifier 
	 */
	public String getId() {
		return this.Id;
	}

	/** Setter of the Id of the node
	 * @param id
	 */
	public void setId(String id) {
		this.Id = id;
	}

	/** Getter of the list of links of the node
	 * @return arrayList<string> links
	 */
	public ArrayList<String> getLinks() {
		return this.links;
	}

	/** Setter of the list of links of the node
	 * @param links
	 */
	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}
	
	
}
