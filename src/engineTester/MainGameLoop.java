package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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
    
    //**************Terrain Texture STUFF*********************
    TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
    TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
    TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
    TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
    
    TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
    TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
    //********************************************************
    
    
    //**************Loading Models and Textures***************
    TexturedModel stallTexturedModel = new TexturedModel(OBJLoader.loadObjModel("stall", loader), new ModelTexture(loader.loadTexture("stallTexture")));
    TexturedModel fernTexturedModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
    TexturedModel grassTexturedModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
    TexturedModel lowPolyTree = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
    TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
    //*********************************************************
    
    
    //***************Shine and Fake Lighting*******************
    fernTexturedModel.getTexture().setHasTransparency(true);
    grassTexturedModel.getTexture().setUseFakeLighting(true);
    grassTexturedModel.getTexture().setHasTransparency(true);
    //*********************************************************
    
    
    //*******************Spawn Entities************************
    Entity stall = new Entity(stallTexturedModel, new Vector3f(-5, 0, -25), 0, 0, 0, 1);
    Entity fern = new Entity(fernTexturedModel, new Vector3f(10, 0, -25), 0, 0, 0, 1);
    Entity grass = new Entity(grassTexturedModel, new Vector3f(15, 0, -25), 0, 0, 0, 1);
    
    List<Entity> entities = new ArrayList<Entity>();
    Random random = new Random(676452);
    for(int i = 0; i < 400; i++) {
      if(i % 7 == 0) {
        entities.add(new Entity(grassTexturedModel, new Vector3f(random.nextFloat() * 400 - 200, 0,
            random.nextFloat() * - 400), 0, 0, 0, 1.8f));
      }
      if(i % 3 == 0) {
        entities.add(new Entity(fernTexturedModel, new Vector3f(random.nextFloat() * 400 - 200, 0,
            random.nextFloat() * - 400), 0, random.nextFloat() * 360, 0, 0.9f));
        entities.add(new Entity(lowPolyTree, new Vector3f(random.nextFloat() * 800 - 400, 0,
            random.nextFloat() * - 600), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
        entities.add(new Entity(tree, new Vector3f(random.nextFloat() * 800 - 400, 0,
            random.nextFloat() * - 600), 0, 0, 0, random.nextFloat() * 1 + 4));
      }
    }
    //*********************************************************
    
    
    //Terrain, Camera and Light setup
    Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
    Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);
    
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
      for (Entity entity:entities) {
        renderer.processEntity(entity);
      }
      renderer.processEntity(fern);
      renderer.processEntity(stall);
      renderer.processEntity(grass);
      
      
      renderer.render(light, camera);
      DisplayManager.updateDisplay();
      
    }
    //after the game has exit close the Display and CleanUp the loader.
    entities.clear();
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
    
  }

}
