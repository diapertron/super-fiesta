/*    */ package net.runelite.client.plugins.playerindicatorscombatlevel;
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
/*    */ @Singleton
/*    */ public class PlayerIndicatorsMinimapOverlayy
/*    */   extends Overlay
/*    */ {
/*    */   private final PlayerIndicatorsServicee playerIndicatorsServicee;
/*    */   private final PlayerIndicatorsConfigg config;
/*    */   
/*    */   @Inject
/*    */   private PlayerIndicatorsMinimapOverlayy(PlayerIndicatorsConfigg config, PlayerIndicatorsServicee playerIndicatorsServicee) {
/* 43 */     this.config = config;
/* 44 */     this.playerIndicatorsServicee = playerIndicatorsServicee;
/* 45 */     setLayer(OverlayLayer.ABOVE_WIDGETS);
/* 46 */     setPosition(OverlayPosition.DYNAMIC);
/* 47 */     setPriority(OverlayPriority.HIGH);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Dimension render(Graphics2D graphics) {
/* 53 */     this.playerIndicatorsServicee.forEachPlayer((player, color) -> renderPlayerOverlay(graphics, player, color));
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color) {
/* 59 */     String name = actor.getName().replace(' ', ' ');
/*    */     
/* 61 */     if (this.config.drawMinimapNames()) {
/*    */       
/* 63 */       Point minimapLocation = actor.getMinimapLocation();
/*    */       
/* 65 */       if (minimapLocation != null)
/*    */       {
/* 67 */         OverlayUtil.renderTextLocation(graphics, minimapLocation, name, color);
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicatorscombatlevel\PlayerIndicatorsMinimapOverlayy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */