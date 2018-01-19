package nl.tudelft.jpacman;
import java.io.FileWriter;
import java.io.IOException;


public class Testapi {
	
	/*
	 * - Write a little Java class that contains an API for collecting coverage information
  		and writing it to a file. NB: if you write out CSV, it will be easy to read into Rascal
  		for further processing and analysis (see here: lang::csv::IO)

		- Write two transformations:
  		1. to obtain method coverage statistics
     		(at the beginning of each method M in class C, insert statement `hit("C", "M")`
  		2. to obtain line-coverage statistics
     		(insert hit("C", "M", "<line>"); after every statement.)

	 */
	
	static public void hit( String c, String m) {
		try {
			FileWriter writer = new FileWriter("test.csv");
			writer.append("c");
			writer.append(";");
			writer.append("m");
			writer.append(";");
			writer.append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static public void hit(String c, String m, String line) {
	
		try {
			FileWriter writer = new FileWriter("test.csv");
			writer.append("c");
			writer.append(";");
			writer.append("m");
			writer.append(";");
			writer.append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		

}
