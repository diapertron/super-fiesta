/*    */ package net.runelite.client.plugins.playerindicatorscombatlevel;
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
/*    */ public enum PlayerNameLocationn
/*    */ {
/* 33 */   DISABLED("Disabled"),
/* 34 */   ABOVE_HEAD("Above head"),
/* 35 */   MODEL_CENTER("Center of model"),
/* 36 */   MODEL_RIGHT("Right of model");
/*    */   
/*    */   PlayerNameLocationn(String name) {
/*    */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */   private final String name;
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicatorscombatlevel\PlayerNameLocationn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */