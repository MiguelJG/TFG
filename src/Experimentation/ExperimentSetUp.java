package Experimentation;

public class ExperimentSetUp {
	public ArtifficialTest ATest = new ArtifficialTest();
	
	/** Calculates the 
	 * @param path to the desire output folder
	 */
	void experiment(String path) {
		//TODO experimentos con artificial data comunidades iniciales perfectas
		//TODO experimentos con artificial data comunidades iniciales LED
		//TODO experimentos con real data and LED
	}
	
	
	public static void main(String[] argc) {
		ExperimentSetUp test = new ExperimentSetUp();
		System.out.println(test.ATest.compute(4, 10, 1, 20, 50,1,90));
	}	
}
