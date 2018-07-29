package loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.sun.xml.internal.txw2.IllegalAnnotationException;

public class BlueprintLoader {

	public static ArrayList<String> loadFromFile(String filePath) {
		ArrayList<String> processedLines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			for (String line; (line = br.readLine()) != null;) {
				if (line.startsWith(">c")) {
					continue;
				} else {
					processedLines.add(line.trim());
				}
			}
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
		return processedLines;
	}

	public static ArrayList<String> splitIntoLines(String blueprint) {
		ArrayList<String> processedLines = new ArrayList<>();
		processedLines.addAll(Arrays.asList(blueprint.split("\\n")));
		return processedLines;

	}

	public static int extractEID(String input) {
		int idBeg = input.indexOf("eID=") + "eID=".length();
		int idEnd = input.indexOf("}");

		if (idBeg > 0) {
			String subStr;
			if (idEnd > idBeg) {
				subStr = input.substring(idBeg, idEnd);
			} else {
				subStr = input.substring(idBeg);
			}
			return Integer.valueOf(subStr);
		} else {
			return -1;
		}
	}

	public static String getLineWith(String exp, ArrayList<String> lines) {
		for (String line : lines) {
			if (line.startsWith(exp)) {
				return line;
			}
		}
		return "";
	}

	public static ArrayList<String> getAllLinesWith(String exp, ArrayList<String> lines) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).startsWith(exp)) {
				result.add(lines.get(i));
			}
		}
		return result;
	}

	public static String[] getDataFragments(String line) throws IllegalArgumentException {
		// -1 to cut away the trailing "}"
		String inner = line.substring(line.lastIndexOf("{") + 1, line.length() - 1);

		if (inner == null || line.charAt(line.length() - 1) != '}') {
			throw new IllegalArgumentException("Syntax error in line with contents: " + line);
		}

		// split inner at "/
		String[] split = inner.split("/");
		LinkedList<String> ret = new LinkedList<>();
		ret.add(split[0]);
		for (int i = 1; i < split.length; i++) {
			if (split[i - 1].charAt(split[i - 1].length() - 1) == '\\') {
				ret.set(ret.size() - 1, ret.getLast() + split[i]);
			} else {
				ret.add(split[i]);
			}
		}

		return ret.toArray(split);
	}
}
