package utility;

public class Circle {
	
	private float x, y;
	private float radius;
	
	public Circle(){
		x = 0;
		y = 0;
		radius = 1;
	}
	
	public Circle(float x, float y, float radius){
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public float getX(){
		return x;
	}

	public void setX(float x){
		this.x = x;
	}

	public float getY(){
		return y;
	}

	public void setY(float y){
		this.y = y;
	}

	public float getRadius(){
		return radius;
	}

	public void setRadius(float radius){
		this.radius = radius;
	}
	
	public boolean intersects(Rectangle rect){
		float deltaX = x - Math.max(rect.getX(), Math.min(x, rect.getX() + rect.getWidth()));
		float deltaY = y - Math.max(rect.getY(), Math.min(y, rect.getY() + rect.getHeight()));
		return (deltaX * deltaX + deltaY * deltaY) < (radius * radius);
	}
}
