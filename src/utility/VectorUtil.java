package utility;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class VectorUtil {
	public static Vector3f modelToWorld(Vector3fc modelCoord, Vector3fc worldPos) {
		Vector3f retVec = new Vector3f(worldPos);
		retVec.add(modelCoord);
		
		return retVec;
	}
	
	public static Vector3f worldToModel(Vector3fc worldCoord, Vector3fc worldPos) {
		Vector3f retVec = new Vector3f(worldCoord);
		retVec.sub(worldPos);
		
		return retVec;
	}
}