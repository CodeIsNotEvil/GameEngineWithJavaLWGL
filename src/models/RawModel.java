package models;

/**
 * Represents a Raw Model witch is Stored in Memory.
 * @author CodeIsNotEvil
 */
public class RawModel {
  
  private int vaoID;
  private int vertexCount;
  
  public RawModel(int vaoID, int vertexCount) {
    this.vaoID = vaoID;
    this.vertexCount = vertexCount;
  }

  public int getVaoID() {
    return vaoID;
  }

  public int getVertexCount() {
    return vertexCount;
  }
  
}
