
import java.io.FileNotFoundException;
import java.io.IOException;

import files.*;
import filesystem.*;
import folders.*;

public class mainclass {

	public static void main(String[] args) {
		try {
			TFile file = new TFile("Test.txt", true);
			
			System.out.println("FILE::FUNCTIONS");
			
			System.out.println(file.getAbsolutePath());
			System.out.println(file.getExtension());
			System.out.println(file.getName());
			System.out.println(file.input());
			System.out.println(file.output());
			System.out.println(file.getSize());
			
			
			System.out.println("FILE::WRITING");
			file.set_method(true);
			
			System.out.println(file.input());
			System.out.println(file.output());
			file.write("TESTING\nTESTING\nSOM\nTESTING\nTESTING", true);
			System.out.println(file.getSize());
			
			System.out.println("FILE::READING");
			file.set_method(false);
			System.out.println(file.read());
			System.out.println(file.read());
			System.out.println(file.find("SOM"));
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
