/*    */ package net.runelite.client.plugins.npcunaggroarea;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics2D;
/*    */ import net.runelite.client.ui.overlay.OverlayPanel;
/*    */ import net.runelite.client.ui.overlay.OverlayPosition;
/*    */ import net.runelite.client.ui.overlay.OverlayPriority;
/*    */ import net.runelite.client.ui.overlay.components.LineComponent;
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
/*    */ class NpcAggroAreaNotWorkingOverlay
/*    */   extends OverlayPanel
/*    */ {
/*    */   private final NpcAggroAreaPlugin plugin;
/*    */   
/*    */   @Inject
/*    */   private NpcAggroAreaNotWorkingOverlay(NpcAggroAreaPlugin plugin) {
/* 42 */     this.plugin = plugin;
/*    */     
/* 44 */     this.panelComponent.getChildren().add(LineComponent.builder()
/* 45 */         .left("Unaggressive NPC timers will start working when you teleport far away or enter a dungeon.")
/* 46 */         .build());
/*    */     
/* 48 */     setPriority(OverlayPriority.LOW);
/* 49 */     setPosition(OverlayPosition.TOP_LEFT);
/* 50 */     setClearChildren(false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Dimension render(Graphics2D graphics) {
/* 56 */     if (!this.plugin.isActive() || this.plugin.getSafeCenters()[1] != null)
/*    */     {
/* 58 */       return null;
/*    */     }
/*    */     
/* 61 */     return super.render(graphics);
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npcunaggroarea\NpcAggroAreaNotWorkingOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */