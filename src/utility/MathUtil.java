package utility;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathUtil {
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static int clampInt(int in, int floor, int ceil) {
		if (floor > ceil) {
			throw new RuntimeException("Floor is bigger than ceiling!");
		}
		if (in < floor) {
			return floor;
		} else if (in > ceil) {
			return ceil;
		} else {
			return in;
		}
	}
	
}
