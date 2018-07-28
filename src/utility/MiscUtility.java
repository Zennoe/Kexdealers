package utility;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class MiscUtility {

	public static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static <T> T arrayConcatenation(T a, T b) {
	    if (!a.getClass().isArray() || !b.getClass().isArray()) {
	        throw new IllegalArgumentException();
	    }

	    Class<?> resCompType;
	    Class<?> aCompType = a.getClass().getComponentType();
	    Class<?> bCompType = b.getClass().getComponentType();

	    if (aCompType.isAssignableFrom(bCompType)) {
	        resCompType = aCompType;
	    } else if (bCompType.isAssignableFrom(aCompType)) {
	        resCompType = bCompType;
	    } else {
	        throw new IllegalArgumentException();
	    }

	    int aLen = Array.getLength(a);
	    int bLen = Array.getLength(b);

	    @SuppressWarnings("unchecked")
	    T result = (T) Array.newInstance(resCompType, aLen + bLen);
	    System.arraycopy(a, 0, result, 0, aLen);
	    System.arraycopy(b, 0, result, aLen, bLen);        

	    return result;
	}
}
