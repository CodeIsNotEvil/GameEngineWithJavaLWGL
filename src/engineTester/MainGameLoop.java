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
import terrains.Terrain;
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
    
    //Loading Models and Textures
    
    // STALL
    RawModel stallModel = OBJLoader.loadObjModel("stall", loader); 
    TexturedModel stallTexturedModel = new TexturedModel(stallModel, new ModelTexture(loader.loadTexture("stallTexture")));
    //ModelTexture stallTexture = stallTexturedModel.getTexture();
    //stallTexture.setShineDamper(2); // adding some shine
    //stallTexture.setReflectivity(0); // adding reflectivity
    Entity stall = new Entity(stallTexturedModel, new Vector3f(-5, 0, -25), 0, 0, 0, 1);
    
    // FERN
    RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
    TexturedModel fernTexturedModel = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("fern")));
    fernTexturedModel.getTexture().setHasTransparency(true);
    Entity fern = new Entity(fernTexturedModel, new Vector3f(10, 0, -25), 0, 0, 0, 1);
    
    // GRASS
    RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
    TexturedModel grassTexturedModel = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
    grassTexturedModel.getTexture().setUseFakeLighting(true);
    Entity grass = new Entity(grassTexturedModel, new Vector3f(15, 0, -25), 0, 0, 0, 1);
    
    //Terrain, Camera and Light setup
    Terrain terrain = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
    Terrain terrain2 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
    
    Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
    Camera camera = new Camera();
    
    MasterRenderer renderer = new MasterRenderer();
    //this is the MainGameLoop.
    while(!Display.isCloseRequested()) {
      //entity.increasePosition(0, 0, -0.1f);
      stall.increaseRotation(0, 1, 0);
      camera.move();
      
      renderer.processTerrain(terrain);
      renderer.processTerrain(terrain2);
      renderer.processEntity(fern);
      renderer.processEntity(stall);
      renderer.processEntity(grass);
      
      
      renderer.render(light, camera);
      DisplayManager.updateDisplay();
      
    }
    //after the game has exit close the Display and CleanUp the loader.
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
    
  }

}
