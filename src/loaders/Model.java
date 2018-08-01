package loaders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Model {
	
	public class Part {
		
		private final Mesh mesh;
		private final Material[] materials; // 1 - n elements
		
		public Part(Mesh mesh, Material[] materials) {
			this.mesh = mesh;
			this.materials = materials;
		}
		
		public Mesh getMesh() {
			return mesh;
		}
		
		public Material[] getMaterials() {
			return materials;
		}
		
	}
	
	private int referenceCounter = 0;
	private final HashMap<String, Part> objects = new HashMap<>();
	
	public void addObject(String objectName, Mesh mesh, Material[] materials) {
		objects.put(objectName, new Part(mesh, materials));
	}
	
	public Set<Part> getAllObjects() {
		return new HashSet<Part>(objects.values());
	}
	
	public void refCountUp() {
		referenceCounter++;
	}
	
	/**
	 * Decrease the reference counter towards this model instance. Will automatically destroy it's internal data
	 * when a reference count of zero is reached.
	 * @return
	 * Returns whether this instance is still alive after the method call.
	 */
	public boolean refCountDown() {
		referenceCounter--;
		if(referenceCounter <= 0) {
			for(Part part : objects.values()) {
				part.getMesh().delete();
				for(Material material : part.getMaterials()) {
					material.destroyMaterial();
				}
			}
			objects.clear();
			return false;
		}
		return true;
	}
	
}
