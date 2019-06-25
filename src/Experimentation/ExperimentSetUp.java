package Experimentation;

public class ExperimentSetUp {
	public ArtifficialTest ATest = new ArtifficialTest();
	
	public static void main(String[] argc) {
		ExperimentSetUp test = new ExperimentSetUp();
		System.out.println(test.ATest.compute(4, 10, 1, 20, 50));
	}
}
