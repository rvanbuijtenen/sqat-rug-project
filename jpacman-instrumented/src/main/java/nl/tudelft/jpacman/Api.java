package nl.tudelft.jpacman;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Runtime;
import java.lang.Thread;

public class Api {
	public static PrintWriter pw;
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
	static {
	     try {
	    	 pw = new PrintWriter(new FileOutputStream(new File("apiOutput.csv")));
	    	 Runtime.getRuntime().addShutdownHook(new ShutdownThread(pw));
	     } catch (Exception e) {
	       // Handle exception.
	    	 e.printStackTrace();
	     }
	 }
	
	private static class ShutdownThread extends Thread {
		PrintWriter p = null;
		public ShutdownThread(PrintWriter p) {
			super();
			this.p = p;
		}
		
		public void run() {
			pw.close();
		}
	}
	
	static public void hit( String c, String m) {
		StringBuilder sb = new StringBuilder();
		sb.append(c);
		sb.append(",");
		sb.append(m);
		sb.append(",");
		sb.append("\n");
		Api.pw.append(sb.toString());
	
	}
	
	static public void hit(String c, String m, String line) {
		StringBuilder sb = new StringBuilder();
		sb.append(c);
		sb.append(",");
		sb.append(m);
		sb.append(",");
		sb.append(line);
		sb.append("\n");
		Api.pw.append(sb.toString());
	}
		

}
