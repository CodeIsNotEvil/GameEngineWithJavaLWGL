package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

/**
 * Contains the Main Game Loop.
 * @author CodeIsNotEvil
 */
public class MainGameLoop {

  /**
   * The main is called on GameStart.
   * @param args
   */
  public static void main(String[] args) {
    //creating the Display.
    DisplayManager.createDisplay();
    Loader loader = new Loader();
    StaticShader shader = new StaticShader();
    Renderer renderer = new Renderer(shader);
    
    
    RawModel model = OBJLoader.loadObjModel("stall", loader);
    ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
    TexturedModel texturedModel = new TexturedModel(model, texture);
    
    Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
    
    Camera camera = new Camera();
    
    //this is the MainGameLoop.
    while(!Display.isCloseRequested()) {
      //entity.increasePosition(0, 0, -0.1f);
      entity.increaseRotation(0, 1, 0);
      camera.move();
      renderer.prepare();
      shader.start();
      shader.loadViewMatrix(camera);
      renderer.render(entity, shader);
      shader.stop();
      DisplayManager.updateDisplay();
      
    }
    //after the game has exit close the Display and CleanUp the loader.
    shader.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
    
  }

}
