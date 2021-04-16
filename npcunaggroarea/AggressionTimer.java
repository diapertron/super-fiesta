/*    */ package net.runelite.client.plugins.npcunaggroarea;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import java.time.temporal.ChronoUnit;
/*    */ import net.runelite.client.plugins.Plugin;
/*    */ import net.runelite.client.ui.overlay.infobox.Timer;
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
/*    */ class AggressionTimer
/*    */   extends Timer
/*    */ {
/*    */   private boolean visible;
/*    */   
/*    */   public boolean isVisible() {
/* 39 */     return this.visible; } public void setVisible(boolean visible) {
/* 40 */     this.visible = visible;
/*    */   }
/*    */ 
/*    */   
/*    */   AggressionTimer(Duration duration, BufferedImage image, Plugin plugin, boolean visible) {
/* 45 */     super(duration.toMillis(), ChronoUnit.MILLIS, image, plugin);
/* 46 */     setTooltip("Time until NPCs become unaggressive");
/* 47 */     this.visible = visible;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Color getTextColor() {
/* 53 */     Duration timeLeft = Duration.between(Instant.now(), getEndTime());
/*    */     
/* 55 */     if (timeLeft.getSeconds() < 60L)
/*    */     {
/* 57 */       return Color.RED.brighter();
/*    */     }
/*    */     
/* 60 */     return Color.WHITE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean render() {
/* 66 */     return (this.visible && super.render());
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npcunaggroarea\AggressionTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */