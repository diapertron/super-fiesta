/*    */ package net.runelite.client.plugins.npchighlight;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.runelite.api.NPC;
/*    */ import net.runelite.api.NPCComposition;
/*    */ import net.runelite.api.coords.WorldPoint;
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
/*    */ class MemorizedNpc
/*    */ {
/*    */   private int npcIndex;
/*    */   private String npcName;
/*    */   private int npcSize;
/*    */   private int diedOnTick;
/*    */   private int respawnTime;
/*    */   private List<WorldPoint> possibleRespawnLocations;
/*    */   
/*    */   public int getNpcIndex() {
/* 38 */     return this.npcIndex;
/*    */   }
/*    */   public String getNpcName() {
/* 41 */     return this.npcName;
/*    */   }
/*    */   public int getNpcSize() {
/* 44 */     return this.npcSize;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDiedOnTick() {
/* 50 */     return this.diedOnTick; } public void setDiedOnTick(int diedOnTick) {
/* 51 */     this.diedOnTick = diedOnTick;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRespawnTime() {
/* 57 */     return this.respawnTime; } public void setRespawnTime(int respawnTime) {
/* 58 */     this.respawnTime = respawnTime;
/*    */   }
/*    */   
/* 61 */   public List<WorldPoint> getPossibleRespawnLocations() { return this.possibleRespawnLocations; } public void setPossibleRespawnLocations(List<WorldPoint> possibleRespawnLocations) {
/* 62 */     this.possibleRespawnLocations = possibleRespawnLocations;
/*    */   }
/*    */ 
/*    */   
/*    */   MemorizedNpc(NPC npc) {
/* 67 */     this.npcName = npc.getName();
/* 68 */     this.npcIndex = npc.getIndex();
/* 69 */     this.possibleRespawnLocations = new ArrayList<>();
/* 70 */     this.respawnTime = -1;
/* 71 */     this.diedOnTick = -1;
/*    */     
/* 73 */     NPCComposition composition = npc.getTransformedComposition();
/*    */     
/* 75 */     if (composition != null)
/*    */     {
/* 77 */       this.npcSize = composition.getSize();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npchighlight\MemorizedNpc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */