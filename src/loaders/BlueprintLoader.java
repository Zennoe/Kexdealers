package loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

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
		// expected syntax is {eID=2}
		// indexOf("eID=") has to return at least 1 if correct syntax
		int idBeg = input.indexOf("eID=") + "eID=".length();
		int idEnd = input.indexOf("}");

		// check if eID was found and if eID is encapsuled in {}
		// idBeg is bigger than the length of the prefix by at least 1 for correct
		// syntax
		// idEnd is of course bigger than idBeg if an eID and a } is present
		if (idBeg > "eID=".length() && idEnd > idBeg) {
			String subStr = input.substring(idBeg, idEnd);
			return Integer.valueOf(subStr);
		} else {
			System.err.println("Syntax Error while parsing eID.");
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
		// extract last bracket pair substring
		String inner = line.substring(line.lastIndexOf('{') + 1, line.lastIndexOf('}'));

		// split inner into fragments separated by '/'.
		// if the '/' is escaped. it will not be treated as a separator
		final char[] escapableChars = { '\\', '/' };
		LinkedList<String> fragments = new LinkedList<>();
		boolean insideQuote = false;
		StringBuilder currFrag = new StringBuilder();
		for (int i = 0; i < inner.length(); i++) {
			switch (inner.charAt(i)) {
			case '/':
				// separator reached
				fragments.add(currFrag.toString().trim());
				currFrag = new StringBuilder();
				break;
			case '\\':
				// escaped character reached.
				if (i < inner.length() - 1) { // check if end of string
					for (char c : escapableChars) {
						if (inner.charAt(i + 1) == c) {
							// if the following char is an escapable char, it is added
							currFrag.append(c);
							i++;
							break;
						}
					}
				}
			default:
				// normal char of frag
				currFrag.append(inner.charAt(i));
			}
		}
		// add last fragment
		fragments.add(currFrag.toString().trim());

		return fragments.toArray(new String[fragments.size()]);
	}
}
