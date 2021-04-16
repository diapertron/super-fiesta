/*    */ package net.runelite.client.plugins.tileindicators;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.runelite.client.config.Alpha;
/*    */ import net.runelite.client.config.Config;
/*    */ import net.runelite.client.config.ConfigGroup;
/*    */ import net.runelite.client.config.ConfigItem;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ConfigGroup("tileindicators")
/*    */ public interface TileIndicatorsConfig
/*    */   extends Config
/*    */ {
/*    */   @Alpha
/*    */   @ConfigItem(keyName = "highlightDestinationColor", name = "Color of current destination highlighting", description = "Configures the highlight color of current destination")
/*    */   default Color highlightDestinationColor() {
/* 44 */     return Color.GRAY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ConfigItem(keyName = "highlightDestinationTile", name = "Highlight destination tile", description = "Highlights tile player is walking to")
/*    */   default boolean highlightDestinationTile() {
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Alpha
/*    */   @ConfigItem(keyName = "highlightHoveredColor", name = "Color of current hovered highlighting", description = "Configures the highlight color of hovered tile")
/*    */   default Color highlightHoveredColor() {
/* 65 */     return new Color(0, 0, 0, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ConfigItem(keyName = "highlightHoveredTile", name = "Highlight hovered tile", description = "Highlights tile player is hovering with mouse")
/*    */   default boolean highlightHoveredTile() {
/* 75 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Alpha
/*    */   @ConfigItem(keyName = "highlightCurrentColor", name = "Color of current true tile highlighting", description = "Configures the highlight color of current true tile")
/*    */   default Color highlightCurrentColor() {
/* 86 */     return Color.CYAN;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ConfigItem(keyName = "highlightCurrentTile", name = "Highlight current true tile", description = "Highlights true tile player is on as seen by server")
/*    */   default boolean highlightCurrentTile() {
/* 96 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\tileindicators\TileIndicatorsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */