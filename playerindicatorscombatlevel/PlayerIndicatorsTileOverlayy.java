/*    */ package net.runelite.client.plugins.playerindicatorscombatlevel;
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
/*    */ public class PlayerIndicatorsTileOverlayy
/*    */   extends Overlay
/*    */ {
/*    */   private final PlayerIndicatorsServicee playerIndicatorsServicee;
/*    */   private final PlayerIndicatorsConfigg config;
/*    */   
/*    */   @Inject
/*    */   private PlayerIndicatorsTileOverlayy(PlayerIndicatorsConfigg config, PlayerIndicatorsServicee playerIndicatorsServicee) {
/* 41 */     this.config = config;
/* 42 */     this.playerIndicatorsServicee = playerIndicatorsServicee;
/* 43 */     setLayer(OverlayLayer.ABOVE_SCENE);
/* 44 */     setPosition(OverlayPosition.DYNAMIC);
/* 45 */     setPriority(OverlayPriority.MED);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Dimension render(Graphics2D graphics) {
/* 51 */     if (!this.config.drawTiles())
/*    */     {
/* 53 */       return null;
/*    */     }
/*    */     
/* 56 */     this.playerIndicatorsServicee.forEachPlayer((player, color) -> {
/*    */           Polygon poly = player.getCanvasTilePoly();
/*    */ 
/*    */           
/*    */           if (poly != null) {
/*    */             OverlayUtil.renderPolygon(graphics, poly, color);
/*    */           }
/*    */         });
/*    */ 
/*    */     
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicatorscombatlevel\PlayerIndicatorsTileOverlayy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */