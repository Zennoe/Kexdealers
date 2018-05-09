package loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BlueprintLoader {
	
	public static ArrayList<String> loadFromFile(String filePath){
		ArrayList<String> processedLines = new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
			for(String line; (line = br.readLine()) != null;){
				if(line.startsWith(">c")){
					continue;
				}else{
					processedLines.add(line);
				}
			}
		}catch (FileNotFoundException x){
			x.printStackTrace();
		}catch (IOException x){
			x.printStackTrace();
		}
		return processedLines;
	}
	
	public static ArrayList<String> splitIntoLines(String blueprint) {
		ArrayList<String> processedLines = new ArrayList<>();
		processedLines.addAll(Arrays.asList(blueprint.split("\\n")));
		return processedLines;
		
	}
	
	public static int extractEID(String input){
		String substring = input.substring(
				input.indexOf("eID=") + 4, 
				input.indexOf("}"));
		
		return Integer.valueOf(substring);
	}
	
	public static String getLineWith(String exp, ArrayList<String> lines){
		for(String line : lines){
			if(line.startsWith(exp)){
				return line;
			}
		}
		return "";
	}
	
	public static ArrayList<String> getAllLinesWith(String exp, ArrayList<String> lines){
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < lines.size(); i++){
			if(lines.get(i).startsWith(exp)){
				result.add(lines.get(i));
			}
		}
		return result;
	}
	
	public static String[] getDataFragments(String line){
		// -1 to cut away the trailing "}"
		String inner = line.substring(line.lastIndexOf("{") + 1, line.length() - 1);
		return inner.split("/");
	}
}
