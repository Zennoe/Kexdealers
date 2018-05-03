package ecs;

import org.joml.Vector3f;

public class PointLightComponent extends Component{

	private int eID;
	
	private Vector3f position = new Vector3f();
	private Vector3f ambient = new Vector3f();
	private Vector3f diffuse = new Vector3f();
	private Vector3f specular = new Vector3f();
	private float radius = 0.0f;
	private float cutoff = 0.005f;
	
	public PointLightComponent(int eID){
		this.eID = eID;
	}
	
	@Override
	public int getEID(){
		return eID;
	}
	
	@Override
	public void setEID(int eID) {
		this.eID = eID;
	}

	@Override
	public PointLightComponent clone() {
		PointLightComponent deepCopy = new PointLightComponent(eID)
				.setPosition(new Vector3f(this.position))
				.setAmbient(new Vector3f(this.ambient))
				.setDiffuse(new Vector3f(this.diffuse))
				.setSpecular(new Vector3f(this.specular))
				.setRadius(this.radius)
				.setCutoff(this.cutoff);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("PointLightComponent<").append(eID).append(">");
		s.append("(");
		s.append(" T: ").append(position.x).append("/").append(position.y).append("/").append(position.z);
		s.append(" A: ").append(ambient.x).append("/").append(ambient.y).append("/").append(ambient.z);
		s.append(" D: ").append(diffuse.x).append("/").append(diffuse.y).append("/").append(diffuse.z);
		s.append(" S: ").append(specular.x).append("/").append(specular.y).append("/").append(specular.z);
		s.append(" R: ").append(radius);
		s.append(" C: ").append(cutoff);
		s.append(" )");
		return s.toString();
	}
	
	public Vector3f getPosition(){
		return position;
	}

	public PointLightComponent setPosition(Vector3f position){
		this.position = position;
		return this;
	}

	public Vector3f getAmbient(){
		return ambient;
	}

	public PointLightComponent setAmbient(Vector3f ambient){
		this.ambient = ambient;
		return this;
	}

	public Vector3f getDiffuse(){
		return diffuse;
	}

	public PointLightComponent setDiffuse(Vector3f diffuse){
		this.diffuse = diffuse;
		return this;
	}

	public Vector3f getSpecular(){
		return specular;
	}

	public PointLightComponent setSpecular(Vector3f specular){
		this.specular = specular;
		return this;
	}

	public float getRadius() {
		return radius;
	}

	public PointLightComponent setRadius(float radius) {
		this.radius = radius;
		return this;
	}

	public float getCutoff() {
		return cutoff;
	}

	public PointLightComponent setCutoff(float cutoff) {
		this.cutoff = cutoff;
		return this;
	}
	
}

