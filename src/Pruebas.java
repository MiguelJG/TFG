import Experimentation.ArtifficialTest;

public class Pruebas {
	public static void main(String[] args) {
		ArtifficialTest test = new ArtifficialTest();
		test.compute(2, 10, 1, 10, 50);
		System.out.println(test.G.toString());
	}
}
