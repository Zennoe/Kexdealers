package audio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import ecs.Transformable;

public class AudioTransformsHelper {

	public static Vector3f comp_to_AudioPos(Transformable entity, Matrix4f wv_matrix) {
		Vector3f pos = entity.getPosition();
		System.out.println("Entity pos: " + pos.toString());
		pos = wv_matrix.transformPosition(pos);
		
		return pos;
	}
}
