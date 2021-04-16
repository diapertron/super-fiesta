/*    */ package net.runelite.client.plugins.playerindicators;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Polygon;
/*    */ import javax.inject.Inject;
/*    */ import net.runelite.api.Player;
/*    */ import net.runelite.client.ui.overlay.Overlay;
/*    */ import net.runelite.client.ui.overlay.OverlayLayer;
/*    */ import net.runelite.client.ui.overlay.OverlayPosition;
/*    */ import net.runelite.client.ui.overlay.OverlayPriority;
/*    */ import net.runelite.client.ui.overlay.OverlayUtil;
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
/*    */ public class PlayerIndicatorsTileOverlay
/*    */   extends Overlay
/*    */ {
/*    */   private final PlayerIndicatorsService playerIndicatorsService;
/*    */   private final PlayerIndicatorsConfig config;
/*    */   
/*    */   @Inject
/*    */   private PlayerIndicatorsTileOverlay(PlayerIndicatorsConfig config, PlayerIndicatorsService playerIndicatorsService) {
/* 46 */     this.config = config;
/* 47 */     this.playerIndicatorsService = playerIndicatorsService;
/* 48 */     setLayer(OverlayLayer.ABOVE_SCENE);
/* 49 */     setPosition(OverlayPosition.DYNAMIC);
/* 50 */     setPriority(OverlayPriority.MED);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Dimension render(Graphics2D graphics) {
/* 56 */     if (!this.config.drawTiles())
/*    */     {
/* 58 */       return null;
/*    */     }
/*    */     
/* 61 */     this.playerIndicatorsService.forEachPlayer((player, color) -> {
/*    */           Polygon poly = player.getCanvasTilePoly();
/*    */ 
/*    */           
/*    */           if (poly != null) {
/*    */             OverlayUtil.renderPolygon(graphics, poly, color);
/*    */           }
/*    */         });
/*    */ 
/*    */     
/* 71 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicators\PlayerIndicatorsTileOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */