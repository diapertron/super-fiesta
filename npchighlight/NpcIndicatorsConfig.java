/*     */ package net.runelite.client.plugins.npchighlight;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.runelite.client.config.Alpha;
/*     */ import net.runelite.client.config.Config;
/*     */ import net.runelite.client.config.ConfigGroup;
/*     */ import net.runelite.client.config.ConfigItem;
/*     */ import net.runelite.client.config.ConfigSection;
/*     */ import net.runelite.client.plugins.SquareOverlay;
/*     */ import net.runelite.client.ui.overlay.OverlayLayer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigGroup("npcindicators")
/*     */ public interface NpcIndicatorsConfig
/*     */   extends Config
/*     */ {
/*     */   @ConfigSection(name = "Render style", description = "The render style of NPC highlighting", position = 0)
/*     */   public static final String renderStyleSection = "renderStyleSection";
/*     */   @ConfigSection(name = "Solid square", description = "The render style of NPC highlighting", position = 0)
/*     */   public static final String solidSquare = "solidSquare";
/*     */   
/*     */   @ConfigItem(position = 0, keyName = "highlightHull", name = "Highlight hull", description = "Configures whether or not NPC should be highlighted by hull", section = "renderStyleSection")
/*     */   default boolean highlightHull() {
/*  52 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 1, keyName = "highlightTile", name = "Highlight tile", description = "Configures whether or not NPC should be highlighted by tile", section = "renderStyleSection")
/*     */   default boolean highlightTile() {
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 2, keyName = "highlightSouthWestTile", name = "Highlight south west tile", description = "Configures whether or not NPC should be highlighted by south western tile", section = "renderStyleSection")
/*     */   default boolean highlightSouthWestTile() {
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 3, keyName = "npcToHighlight", name = "NPCs to Highlight", description = "List of NPC names to highlight")
/*     */   default String getNpcToHighlight() {
/*  87 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "npcToHighlight", name = "", description = "")
/*     */   void setNpcToHighlight(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 4, keyName = "npcColor", name = "Highlight Color", description = "Color of the NPC highlight")
/*     */   @Alpha
/*     */   default Color getHighlightColor() {
/* 106 */     return Color.CYAN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 5, keyName = "drawNames", name = "Draw names above NPC", description = "Configures whether or not NPC names should be drawn above the NPC")
/*     */   default boolean drawNames() {
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 6, keyName = "drawMinimapNames", name = "Draw names on minimap", description = "Configures whether or not NPC names should be drawn on the minimap")
/*     */   default boolean drawMinimapNames() {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 7, keyName = "highlightMenuNames", name = "Highlight menu names", description = "Highlight NPC names in right click menu")
/*     */   default boolean highlightMenuNames() {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 8, keyName = "ignoreDeadNpcs", name = "Ignore dead NPCs", description = "Prevents highlighting NPCs after they are dead")
/*     */   default boolean ignoreDeadNpcs() {
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 9, keyName = "deadNpcMenuColor", name = "Dead NPC menu color", description = "Color of the NPC menus for dead NPCs")
/*     */   Color deadNpcMenuColor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 10, keyName = "showRespawnTimer", name = "Show respawn timer", description = "Show respawn timer of tagged NPCs")
/*     */   default boolean showRespawnTimer() {
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "solidSquare", name = "solidSquare", description = "solidSquare", section = "solidSquare")
/*     */   default int solidSquare() {
/* 188 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "checkCombat", name = "Disable on npcs when in combat", description = "Disable when in combat", section = "solidSquare")
/*     */   default boolean checkCombat() {
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "interacting", name = "Show on current target", description = "Enable on entity you are interacting with", section = "solidSquare")
/*     */   default boolean interacting() {
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "interactingDelay", name = "Current target fade delay", description = "interactingDelay", section = "solidSquare")
/*     */   default int interactingDelay() {
/* 221 */     return 2000;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "layer", name = "Layer", description = "The layer of the overlay", section = "solidSquare")
/*     */   default OverlayLayer overlayLayer() {
/* 232 */     return SquareOverlay.OVERLAY_LAYER2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npchighlight\NpcIndicatorsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */