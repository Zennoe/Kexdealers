package water;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import render.FrameBufferObject;

public class WaterFrameBuffers {
	
	private int reflectionFrameBuffer;
    private int reflectionTexture;
    private int reflectionDepthBuffer;
     
    private int refractionFrameBuffer;
    private int refractionTexture;
    private int refractionDepthTexture;
	
	public WaterFrameBuffers(int reflWidth, int reflHeight, int refrWidth, int refrHeight){
		initialiseReflectionFrameBuffer(reflWidth, reflHeight);
		initialiseRefractionFrameBuffer(refrWidth, refrHeight);
	}
	
	public void cleanUp(){
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
        GL11.glDeleteTextures(reflectionTexture);
        GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
        GL30.glDeleteFramebuffers(refractionFrameBuffer);
        GL11.glDeleteTextures(refractionTexture);
        GL11.glDeleteTextures(refractionDepthTexture);
	}
	
	private void initialiseReflectionFrameBuffer(int reflWidth, int reflHeight) {
        reflectionFrameBuffer = FrameBufferObject.createFrameBufferObject();
        reflectionTexture = FrameBufferObject.createTextureAttachment(reflWidth,reflHeight);
        reflectionDepthBuffer = FrameBufferObject.createDepthBufferAttachment(reflWidth,reflHeight);
        FrameBufferObject.unbindFrameBufferObject();
    }
     
    private void initialiseRefractionFrameBuffer(int refrWidth, int refrHeight) {
        refractionFrameBuffer = FrameBufferObject.createFrameBufferObject();
        refractionTexture = FrameBufferObject.createTextureAttachment(refrWidth,refrHeight);
        refractionDepthTexture = FrameBufferObject.createDepthTextureAttachment(refrWidth,refrHeight);
        FrameBufferObject.unbindFrameBufferObject();
    }
}
