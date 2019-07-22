package Experimentation;
import java.io.*;
public class ExperimentSetUp {	
	/** Calculates the 
	 * @param path to the desire output folder
	 * @throws IOException 
	 */
	void experiment(String path) throws IOException {
		//TODO experimentos con artificial data comunidades iniciales perfectas
		double clusterconexion = 25; // fixed value
		double ledAlpha = 0.5;
		//number of sollaping nodes: number of nodes per community / 4
		int numMetricas = 2;
		FileWriter writer = new FileWriter(path + "resultadosArtifTest.txt");
		BufferedWriter bw = new BufferedWriter(writer);
		for(int nNodos = 5; nNodos < 100; nNodos += 5) {
			System.out.println(nNodos + " < 100");
			for(int nClusters = 2; nClusters < 10; nClusters++) {
				int sNodes = (nNodos / nClusters) / 4;
				for(double inNode = 10; inNode <= 80; inNode += 10) {
					for(int i = 1; i <= numMetricas; i++) {
						ArtifficialTest instance = new ArtifficialTest();
						try{
								bw.write(nClusters +"-"+ nNodos+"-"+ sNodes+"-"+ clusterconexion+"-"+ inNode+"-"+ i+"-"+ 90 + ",");
								bw.write(instance.compute(nClusters, nNodos, sNodes, clusterconexion, inNode, numMetricas, 90).toString());
								bw.newLine();
								//bw.flush();								
						} catch (IOException e) {
								System.err.format("IOException: %s%n", e);
						}
					}
				}
			}			
		}
		bw.close();
		//TODO experimentos con artificial data comunidades iniciales LED
		//TODO experimentos con real data and LED
	}
	/*
	 try (FileWriter writer = new FileWriter("app.log");
		 BufferedWriter bw = new BufferedWriter(writer)) {

		bw.write(content);

		} catch (IOException e) {
		System.err.format("IOException: %s%n", e);
		} 
	 */
	
	public static void main(String[] argc) throws IOException {
		ExperimentSetUp test = new ExperimentSetUp();
		test.experiment("result/");
	}	
}
