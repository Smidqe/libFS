


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import files.*;
import filesystem.*;
import folders.*;

@SuppressWarnings("unused")

public class mainclass {

	public static void main(String[] args) {
		try {
			TFile file = new TFile("src/testing/test.txt", true);
			
			System.out.println("FILE::FUNCTIONS");
			
			System.out.println(file.getAbsolutePath());
			System.out.println(file.getExtension());
			System.out.println(file.getName());
			System.out.println(file.input());
			System.out.println(file.output());
			System.out.println(file.getSize());
			
			
			System.out.println("FILE::WRITING");
			file.set(true);
			
			System.out.println(file.input());
			System.out.println(file.output());
			file.write("TESTING\nTESTING\nSOM\nTESTING\nTESTING", true);
			System.out.println(file.getSize());
			
			System.out.println("FILE::READING");
			file.set(false);
			System.out.println(file.read());
			System.out.println(file.read());
			System.out.println(file.find("SOM"));
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		try {
			System.out.println("FOLDER::TESTING");
			TFolder folder = new TFolder("src/testing/");
			System.out.println(folder.getPath());
			System.out.println(folder.exists());
			System.out.println(folder.count(true));
			
			Map<String, Map<String, String>> found = folder.search("test", true);
			System.out.println("SUBFILES/FOLDERS: " + found.size());
						
			for (String key : found.keySet())
			{
				System.out.println("KEY: " + key);
				System.out.println("\tVALUE: " + found.get(key));
				System.out.println("\tAMOUNT: " + found.get(key).size());
			}
			
			
			System.out.println(folder.getFiles().size());
			
			
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
