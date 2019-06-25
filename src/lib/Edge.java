package lib;

public class Edge {
	String firstElement;
	String secondElement;
	
	public Edge(String firstElement, String secondElement) {
		this.firstElement = firstElement;
		this.secondElement = secondElement;
	}

	public String getFirstElement() {
		return firstElement;
	}

	public void setFirstElement(String firstElement) {
		this.firstElement = firstElement;
	}

	public String getSecondElement() {
		return secondElement;
	}

	public void setSecondElement(String secondElement) {
		this.secondElement = secondElement;
	}
}
