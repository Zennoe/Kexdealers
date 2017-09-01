package wrapper;

public class Mouse {
	
	private double mouseScroll = 0;
	private double mouseX = 0;
	private double mouseY = 0;
	
	public Mouse(double mouseScroll, double mouseX, double mouseY) {
		this.mouseScroll = mouseScroll;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	public double getMouseScroll() {
		return mouseScroll;
	}

	public double getMouseX() {
		return mouseX;
	}

	public double getMouseY() {
		return mouseY;
	}
	
}
