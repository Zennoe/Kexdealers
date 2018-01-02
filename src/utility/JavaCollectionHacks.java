package utility;

import java.util.List;

public class JavaCollectionHacks {
	
	public static float[] listToFloatArray(List<Float> list) {
		float[] array = new float[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = (float) list.get(i);
		}
		return array;
	}
	
	public static int[] listToIntArray(List<Integer> list) {
		int[] array = new int[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = (int) list.get(i);
		}
		return array;
	}
	
	
}
