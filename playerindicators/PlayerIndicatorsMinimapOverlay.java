/*    */ package net.runelite.client.plugins.playerindicators;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics2D;
/*    */ import javax.inject.Inject;
/*    */ import javax.inject.Singleton;
/*    */ import net.runelite.api.Player;
/*    */ import net.runelite.api.Point;
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
/*    */ @Singleton
/*    */ public class PlayerIndicatorsMinimapOverlay
/*    */   extends Overlay
/*    */ {
/*    */   private final PlayerIndicatorsService playerIndicatorsService;
/*    */   private final PlayerIndicatorsConfig config;
/*    */   
/*    */   @Inject
/*    */   private PlayerIndicatorsMinimapOverlay(PlayerIndicatorsConfig config, PlayerIndicatorsService playerIndicatorsService) {
/* 48 */     this.config = config;
/* 49 */     this.playerIndicatorsService = playerIndicatorsService;
/* 50 */     setLayer(OverlayLayer.ABOVE_WIDGETS);
/* 51 */     setPosition(OverlayPosition.DYNAMIC);
/* 52 */     setPriority(OverlayPriority.HIGH);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Dimension render(Graphics2D graphics) {
/* 58 */     this.playerIndicatorsService.forEachPlayer((player, color) -> renderPlayerOverlay(graphics, player, color));
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color) {
/* 64 */     String name = actor.getName().replace(' ', ' ');
/*    */     
/* 66 */     if (this.config.drawMinimapNames()) {
/*    */       
/* 68 */       Point minimapLocation = actor.getMinimapLocation();
/*    */       
/* 70 */       if (minimapLocation != null)
/*    */       {
/* 72 */         OverlayUtil.renderTextLocation(graphics, minimapLocation, name, color);
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicators\PlayerIndicatorsMinimapOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */