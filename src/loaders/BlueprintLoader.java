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
		int idBeg = input.indexOf("eID=") + "eID=".length();
		int idEnd = input.indexOf("}");

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
		// an '/' inside '"' is not a separator.
		// if the '"' is prepended by a '\' it is considered escaped and part of
		// fragment.
		// if the fragment is not inside '"' the list of escapable chars has to be
		// considered.
		final char[] escapableChars = { '"', '\\', '/' };
		LinkedList<String> fragments = new LinkedList<>();
		boolean insideQuote = false;
		StringBuilder currFrag = new StringBuilder();
		for (int i = 0; i < inner.length(); i++) {
			switch (inner.charAt(i)) {
			case '/':
				// standard separator
				if (!insideQuote) {
					// end of frag reached
					fragments.add(currFrag.toString().trim());
					currFrag = new StringBuilder();
				} else {
					currFrag.append(inner.charAt(i));
				}
				break;
			case '"':
				// quoted fragment
				if (i <= 0 || inner.charAt(i - 1) != '\\') {
					insideQuote = !insideQuote;
					break;
				}
			case '\\':
				// escaped character. ('\' has to be escaped as well)
				if (i < inner.length() - 1) { // check if end of string
					if (insideQuote) {
						// inside quotations: only '"' needs to be escaped
						if (inner.charAt(i + 1) == '"') {
							currFrag.append('"');
							i++;
						}
					} else {
						// not inside quotations
						for (char c : escapableChars) {
							if (inner.charAt(i + 1) == c) {
								currFrag.append(c);
								i++;
							}
						}
					}
					break;
				}
			default:
				// normal char of frag
				currFrag.append(inner.charAt(i));
			}
		}

		// add last fragment
		fragments.add(currFrag.toString());

		return fragments.toArray(new String[fragments.size()]);
	}
}
