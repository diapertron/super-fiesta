/*    */ package net.runelite.client.plugins.tileindicators;
/*    */ 
/*    */ import com.google.inject.Provides;
/*    */ import javax.inject.Inject;
/*    */ import net.runelite.client.config.ConfigManager;
/*    */ import net.runelite.client.plugins.Plugin;
/*    */ import net.runelite.client.plugins.PluginDescriptor;
/*    */ import net.runelite.client.ui.overlay.OverlayManager;
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
/*    */ 
/*    */ 
/*    */ @PluginDescriptor(name = "Tile Indicators", description = "Highlight the tile you are currently moving to", tags = {"highlight", "overlay"}, enabledByDefault = false)
/*    */ public class TileIndicatorsPlugin
/*    */   extends Plugin
/*    */ {
/*    */   @Inject
/*    */   private OverlayManager overlayManager;
/*    */   @Inject
/*    */   private TileIndicatorsOverlay overlay;
/*    */   
/*    */   @Provides
/*    */   TileIndicatorsConfig provideConfig(ConfigManager configManager) {
/* 51 */     return (TileIndicatorsConfig)configManager.getConfig(TileIndicatorsConfig.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void startUp() throws Exception {
/* 57 */     this.overlayManager.add(this.overlay);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void shutDown() throws Exception {
/* 63 */     this.overlayManager.remove(this.overlay);
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\tileindicators\TileIndicatorsPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */