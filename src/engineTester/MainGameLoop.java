package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
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
    
    RawModel model = OBJLoader.loadObjModel("stall", loader);
    //ModelTexture texture = new ModelTexture(loader.loadTexture("dragonTexture"));
    //TexturedModel texturedModel = new TexturedModel(model, texture);
    TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
    ModelTexture texture = staticModel.getTexture();
    texture.setShineDamper(10);
    texture.setReflectivity(0);
    
    Entity entity = new Entity(staticModel, new Vector3f(0, 0, -7), 0, 0, 0, 1);
    Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
    
    Camera camera = new Camera();
    
    MasterRenderer renderer = new MasterRenderer();
    //this is the MainGameLoop.
    while(!Display.isCloseRequested()) {
      //entity.increasePosition(0, 0, -0.1f);
      //entity.increaseRotation(0, 1, 0);
      camera.move();
      
      renderer.processEntity(entity); //need to called for each entity wich have to be rendered.
      
      renderer.render(light, camera);
      DisplayManager.updateDisplay();
      
    }
    //after the game has exit close the Display and CleanUp the loader.
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
    
  }

}
