package loaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import com.mokiat.data.front.parser.IMTLParser;
import com.mokiat.data.front.parser.IOBJParser;
import com.mokiat.data.front.parser.MTLColor;
import com.mokiat.data.front.parser.MTLLibrary;
import com.mokiat.data.front.parser.MTLMaterial;
import com.mokiat.data.front.parser.MTLParser;
import com.mokiat.data.front.parser.OBJModel;
import com.mokiat.data.front.parser.OBJParser;

/**
 * New OBJLoader using the java-data-front library https://github.com/mokiat/java-data-front
 */
public class DataFrontLoader {
	
	public void obj() {
		
		try (InputStream is = new FileInputStream("example.obj")) {
			// Create an OBJParser and parse the resource
			final IOBJParser parser = new OBJParser();
			final OBJModel model =  parser.parse(is);
			
			// Use the model representation to get some basic info
			System.out.println(MessageFormat.format(
		          "OBJ model has {0} vertices, {1} normals, {2} texture coordinates, and {3} objects.",
		          model.getVertices().size(),
		          model.getNormals().size(),
		          model.getTexCoords().size(),
		          model.getObjects().size())); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MTLLibrary library = null;
		MTLMaterial mat = library.getMaterials().get(0);
		MTLColor color = mat.getAmbientColor();
		
	}
	
	public void mtl() {
		try (InputStream in = new FileInputStream("example.mtl")) {
			  final IMTLParser parser = new MTLParser();
			  final MTLLibrary library = parser.parse(in);
			  for (MTLMaterial material : library.getMaterials()) {
			  	System.out.println(MessageFormat.format("Material with name `{0}`.", material.getName()));
			  }  
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
}
