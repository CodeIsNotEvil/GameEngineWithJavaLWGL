package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import models.RawModel;

/**
 * Loading 3DModels into memory, by storing positioning data about it in a VBO. 
 * @author CodeIsNotEvil
 */
public class Loader {
  
  private List<Integer> vaos = new ArrayList<Integer>();
  private List<Integer> vbos = new ArrayList<Integer>();
  private List<Integer> textures = new ArrayList<Integer>();
  
  /**
   * Loads the Data into a VAO.
   * @param positions Vertices Positions.
   * @param indices List of witch Vertices to pick in witch Order.
   * @return RawModel 
   */
  public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
    int vaoID = createVAO();
    bindIndicesBuffer(indices);
    storeDataInAttributeList(0, 3, positions);
    storeDataInAttributeList(1, 2, textureCoords);
    storeDataInAttributeList(2, 3, normals);
    unbindVAO();
    return new RawModel(vaoID, indices.length);
  }
  
  /**
   * Loads the texture into OpenGL.
   * @param fileName Name of the texture file.
   * @return Returns the ID of the Texture.
   */
  public int loadTexture(String fileName) {
    Texture texture = null;
    try {
      texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
      GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
      GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int textureID = texture.getTextureID();
    textures.add(textureID);
    return textureID;
  }
  
  /**
   * Deletes all VBOs and VAOs 
   */
  public void cleanUp() {
    for(int vao:vaos) {
      GL30.glDeleteVertexArrays(vao);
    }
    for(int vbo:vbos) {
      GL15.glDeleteBuffers(vbo);
    }
    for(int texture:textures) {
      GL11.glDeleteTextures(texture);
    }
  }
  
  /**
   * Creates a VAO bind it and return it's ID.
   * @return vaoID The id of the created VAO.
   */
  private int createVAO() {
    int vaoID = GL30.glGenVertexArrays();
    vaos.add(vaoID);
    GL30.glBindVertexArray(vaoID);
    return vaoID;
  }
  
  /**
   * Stores the data into the VAO at the attribute Number. 
   * @param attributeNumber The Number of the Attribute of the List.
   * @param data The Data witch will Stored in the List.
   */
  private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
    int vboID = GL15.glGenBuffers();
    vbos.add(vboID);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
    //GL15.glBindBuffer(vboID, GL15.GL_ARRAY_BUFFER);
    FloatBuffer buffer = storeDataInFloatBuffer(data);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }
  
  /**
   * Unbind's the VAO.
   */
  private void unbindVAO() {
    GL30.glBindVertexArray(0);
  }
  
  /**
   * Loads the Indices Buffer and Bind it to the VBO.
   * @param indices
   */
  private void bindIndicesBuffer(int[] indices) {
    int vboID = GL15.glGenBuffers();
    vbos.add(vboID);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
    IntBuffer buffer = storeDataInIntBuffer(indices);
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
  }
  
  /**
   * Stores the Data into an IntBuffer and return it.
   * @param data The data witch will be stored in the IntBuffer.
   * @return The IntBuffer.
   */
  private IntBuffer storeDataInIntBuffer(int[] data) {
    IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }
  
  /**
   * Converts the Float Array data into a FloatBuffer.
   * @param data The Float Array witch will be converted.
   * @return buffer The Buffer with the Data.
   */
  private FloatBuffer storeDataInFloatBuffer(float[] data) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

}
