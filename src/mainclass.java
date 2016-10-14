
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

import libFileExtensions.files.TFile;
import libFileExtensions.files.TIniFile;
import libFileExtensions.files.TTextFile;
import libFileExtensions.folders.TFolder;

@SuppressWarnings("unused")

public class mainclass {

	public static void main(String[] args) {
		try {
			TTextFile file = new TTextFile("src/testing/test.txt", true);
			
			System.out.println("FILE::FUNCTIONS");
			
			System.out.println(file.getAbsolutePath());
			System.out.println(file.getExtension());
			System.out.println(file.getName());
			System.out.println(file.getSize());
			
			
			System.out.println("FILE::WRITING");
			file.mode(true);

			file.write("TESTING\nTESTING\nSOM\nTESTING\nTESTING", true);
			file.write("Test", false);
			System.out.println(file.getSize());
			
			System.out.println("FILE::READING");
			file.mode(false);
			System.out.println(file.read());
			System.out.println(file.read());
			file.close();
			System.out.println(file.find("TEST"));
			
			System.out.println(file.getParent() + "test2.txt");
			System.out.println(file = (TTextFile) file.rename(file.getParent() + "test2.txt"));
			System.out.println(file.getName());
			System.out.println(file.getParent());
			System.out.println(file.copy(file.getParent(), "copied_test.txt"));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		try {
			System.out.println("FOLDER::TESTING");
			TFolder folder = new TFolder("src/testing/", false);
			System.out.println(folder.getPath());
			System.out.println(folder.exists());
			System.out.println(folder.count(true));
			
			folder.information(true);
			Map<String, Map<String, String>> found = folder.search("test", true);
			System.out.println("SUBFILES/FOLDERS: " + found.size());
						
			for (String key : found.keySet())
			{
				System.out.println("KEY: " + key);
				System.out.println("VALUE: " + found.get(key));
				System.out.println("AMOUNT: " + found.get(key).size());
			}
			
			
			System.out.println(folder.getFiles().size());
			
			
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			TIniFile ini = new TIniFile("src/testing/Nadeo.ini", false);
			
			ini.add("TmForever", "Something", "Hello");
			ini.add("TmForever", "Working", "True");
			ini.set("TmForever", "Language", "fi");
			ini.mode(true);
			ini.write();
			ini.information();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		try {
			File file = new File("src/testing/conversion.txt");
			TFile file2 = new TFile(file.getPath(), false);
			
			System.out.println(UUID.randomUUID().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
