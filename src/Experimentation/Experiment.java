package Experimentation;

public class Experiment {
	public ArtifficialTest ATest = new ArtifficialTest();
	
	public static void main(String[] argc) {
		Experiment test = new Experiment();
		System.out.println(test.ATest.compute(2, 10, 1, 20, 40));
	}
}
