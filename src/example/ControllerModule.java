package example;

import example.Display;
import render.LatchOnCamera;

public class ControllerModule {
	
	private double oldMouseScroll;
	private double oldMouseX;
	private double oldMouseY;
	
	private double mouseScroll;
	private double mouseX;
	private double mouseY;
	
	private int controlledEntity;
	
	public ControllerModule(int eID){
		controlledEntity = eID;
		
		oldMouseScroll = Display.mouseScroll;
		oldMouseX = Display.mouseX;
		oldMouseY = Display.mouseY;
	}
	
	public void run(LatchOnCamera camera, double timeDelta){
		mouseScroll = Display.mouseScroll - oldMouseScroll;
		mouseX = Display.mouseX - oldMouseX;
		mouseY = Display.mouseY - oldMouseY;
		
		oldMouseScroll = Display.mouseScroll;
		oldMouseX = Display.mouseX;
		oldMouseY = Display.mouseY;
		
		camera.reactToInput(mouseScroll, mouseY);
		camera.lookAt(controlledEntity);
	}
	
	public int getControlledEntity(){
		return controlledEntity;
	}
	
}
