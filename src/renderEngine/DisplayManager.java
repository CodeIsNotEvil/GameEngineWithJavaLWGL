package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Manages the Display and his properties
 * @author CodeIsNotEvil
 */
public class DisplayManager {
  
  private static final int WIDTH = 1280;
  private static final int HEIGHT = 720;
  private static final int FPS_CAP = 120;
  private static final String GAME_TITLE = "My First OpenGL Game";
  
  /**
   * Creates the Display with it's properties. 
   */
  public static void createDisplay() {
    
    ContextAttribs attribs =  new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
    
    try {
      Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
      Display.create(new PixelFormat(), attribs);
      Display.setTitle(GAME_TITLE);
    } catch (LWJGLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //Render the Game at the hole Display
    GL11.glViewport(0, 0, WIDTH, HEIGHT);
    
  }
  
  /**
   * Updates the Display to reach the FPS_CAP.
   */
  public static void updateDisplay() {
    
    Display.sync(FPS_CAP);
    Display.update();
    
  }
  
  /**
   * Closes the Display.
   */
  public static void closeDisplay() {
    
    Display.destroy();
    
  }

}
