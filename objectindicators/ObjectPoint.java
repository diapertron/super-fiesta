/*    */ package net.runelite.client.plugins.objectindicators;
/*    */ 
/*    */ import java.awt.Color;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ObjectPoint
/*    */ {
/*    */   private int id;
/*    */   private String name;
/*    */   private int regionId;
/*    */   private int regionX;
/*    */   private int regionY;
/*    */   private int z;
/*    */   private Color color;
/*    */   
/*    */   public void setId(int id) {
/* 34 */     this.id = id; } public void setName(String name) { this.name = name; } public void setRegionId(int regionId) { this.regionId = regionId; } public void setRegionX(int regionX) { this.regionX = regionX; } public void setRegionY(int regionY) { this.regionY = regionY; } public void setZ(int z) { this.z = z; } public void setColor(Color color) { this.color = color; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ObjectPoint)) return false;  ObjectPoint other = (ObjectPoint)o; if (!other.canEqual(this)) return false;  if (getId() != other.getId()) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  if (getRegionId() != other.getRegionId()) return false;  if (getRegionX() != other.getRegionX()) return false;  if (getRegionY() != other.getRegionY()) return false;  if (getZ() != other.getZ()) return false;  Object this$color = getColor(), other$color = other.getColor(); return !((this$color == null) ? (other$color != null) : !this$color.equals(other$color)); } protected boolean canEqual(Object other) { return other instanceof ObjectPoint; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getId(); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); result = result * 59 + getRegionId(); result = result * 59 + getRegionX(); result = result * 59 + getRegionY(); result = result * 59 + getZ(); Object $color = getColor(); return result * 59 + (($color == null) ? 43 : $color.hashCode()); } public String toString() { return "ObjectPoint(id=" + getId() + ", name=" + getName() + ", regionId=" + getRegionId() + ", regionX=" + getRegionX() + ", regionY=" + getRegionY() + ", z=" + getZ() + ", color=" + getColor() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectPoint() {
/* 39 */     this.id = -1; } public ObjectPoint(int id, String name, int regionId, int regionX, int regionY, int z, Color color) { this.id = -1; this.id = id; this.name = name; this.regionId = regionId; this.regionX = regionX; this.regionY = regionY; this.z = z; this.color = color; } public int getId() { return this.id; }
/* 40 */   public String getName() { return this.name; }
/* 41 */   public int getRegionId() { return this.regionId; }
/* 42 */   public int getRegionX() { return this.regionX; }
/* 43 */   public int getRegionY() { return this.regionY; }
/* 44 */   public int getZ() { return this.z; } public Color getColor() {
/* 45 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\objectindicators\ObjectPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */