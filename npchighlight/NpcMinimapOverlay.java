/*    */ package net.runelite.client.plugins.npchighlight;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics2D;
/*    */ import javax.inject.Inject;
/*    */ import net.runelite.api.Client;
/*    */ import net.runelite.api.NPC;
/*    */ import net.runelite.api.NPCComposition;
/*    */ import net.runelite.api.Point;
/*    */ import net.runelite.client.ui.overlay.Overlay;
/*    */ import net.runelite.client.ui.overlay.OverlayLayer;
/*    */ import net.runelite.client.ui.overlay.OverlayPosition;
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
/*    */ 
/*    */ public class NpcMinimapOverlay
/*    */   extends Overlay
/*    */ {
/*    */   private final Client client;
/*    */   private final NpcIndicatorsConfig config;
/*    */   private final NpcIndicatorsPlugin plugin;
/*    */   
/*    */   @Inject
/*    */   NpcMinimapOverlay(Client client, NpcIndicatorsConfig config, NpcIndicatorsPlugin plugin) {
/* 49 */     this.client = client;
/* 50 */     this.config = config;
/* 51 */     this.plugin = plugin;
/* 52 */     setPosition(OverlayPosition.DYNAMIC);
/* 53 */     setLayer(OverlayLayer.ABOVE_WIDGETS);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Dimension render(Graphics2D graphics) {
/* 59 */     for (NPC npc : this.plugin.getHighlightedNpcs())
/*    */     {
/* 61 */       renderNpcOverlay(graphics, npc, npc.getName(), this.config.getHighlightColor());
/*    */     }
/*    */     
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private void renderNpcOverlay(Graphics2D graphics, NPC actor, String name, Color color) {
/* 69 */     NPCComposition npcComposition = actor.getTransformedComposition();
/* 70 */     if (npcComposition == null || !npcComposition.isInteractible() || (actor
/* 71 */       .isDead() && this.config.ignoreDeadNpcs())) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 76 */     Point minimapLocation = actor.getMinimapLocation();
/* 77 */     if (minimapLocation != null) {
/*    */       
/* 79 */       OverlayUtil.renderMinimapLocation(graphics, minimapLocation, color.darker());
/*    */       
/* 81 */       if (this.config.drawMinimapNames())
/*    */       {
/* 83 */         OverlayUtil.renderTextLocation(graphics, minimapLocation, name, color);
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npchighlight\NpcMinimapOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */