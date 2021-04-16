/*    */ package net.runelite.client.plugins.objectindicators;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.runelite.api.TileObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ColorTileObject
/*    */ {
/*    */   private final TileObject tileObject;
/*    */   private final Color color;
/*    */   
/*    */   public boolean equals(Object o) {
/* 36 */     if (o == this) return true;  if (!(o instanceof ColorTileObject)) return false;  ColorTileObject other = (ColorTileObject)o; Object this$tileObject = getTileObject(), other$tileObject = other.getTileObject(); if ((this$tileObject == null) ? (other$tileObject != null) : !this$tileObject.equals(other$tileObject)) return false;  Object this$color = getColor(), other$color = other.getColor(); return !((this$color == null) ? (other$color != null) : !this$color.equals(other$color)); } public int hashCode() { int PRIME = 59; result = 1; Object $tileObject = getTileObject(); result = result * 59 + (($tileObject == null) ? 43 : $tileObject.hashCode()); Object $color = getColor(); return result * 59 + (($color == null) ? 43 : $color.hashCode()); } public String toString() { return "ColorTileObject(tileObject=" + getTileObject() + ", color=" + getColor() + ")"; } public ColorTileObject(TileObject tileObject, Color color) {
/* 37 */     this.tileObject = tileObject; this.color = color;
/*    */   }
/*    */   
/* 40 */   public TileObject getTileObject() { return this.tileObject; } public Color getColor() {
/* 41 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\objectindicators\ColorTileObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */