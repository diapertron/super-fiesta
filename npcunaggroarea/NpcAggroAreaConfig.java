/*     */ package net.runelite.client.plugins.npcunaggroarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.runelite.client.config.Alpha;
/*     */ import net.runelite.client.config.Config;
/*     */ import net.runelite.client.config.ConfigGroup;
/*     */ import net.runelite.client.config.ConfigItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigGroup("npcUnaggroArea")
/*     */ public interface NpcAggroAreaConfig
/*     */   extends Config
/*     */ {
/*     */   public static final String CONFIG_GROUP = "npcUnaggroArea";
/*     */   public static final String CONFIG_CENTER1 = "center1";
/*     */   public static final String CONFIG_CENTER2 = "center2";
/*     */   public static final String CONFIG_LOCATION = "location";
/*     */   public static final String CONFIG_DURATION = "duration";
/*     */   
/*     */   @ConfigItem(keyName = "npcUnaggroAlwaysActive", name = "Always active", description = "Always show this plugins overlays<br>Otherwise, they will only be shown when any NPC name matches the list", position = 1)
/*     */   default boolean alwaysActive() {
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "npcUnaggroNames", name = "NPC names", description = "Enter names of NPCs where you wish to use this plugin", position = 2)
/*     */   default String npcNamePatterns() {
/*  61 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "npcUnaggroShowTimer", name = "Show timer", description = "Display a timer until NPCs become unaggressive", position = 3)
/*     */   default boolean showTimer() {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "npcUnaggroShowAreaLines", name = "Show area lines", description = "Display lines, when walked past, the unaggressive timer resets", position = 4)
/*     */   default boolean showAreaLines() {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "npcAggroAreaColor", name = "Aggressive colour", description = "Choose colour to use for marking NPC unaggressive area when NPCs are aggressive", position = 5)
/*     */   @Alpha
/*     */   default Color aggroAreaColor() {
/*  95 */     return new Color(1694498560, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "npcUnaggroAreaColor", name = "Unaggressive colour", description = "Choose colour to use for marking NPC unaggressive area after NPCs have lost aggression", position = 6)
/*     */   @Alpha
/*     */   default Color unaggroAreaColor() {
/* 107 */     return new Color(16776960);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "notifyExpire", name = "Notify Expiration", description = "Send a notifcation when the unaggressive timer expires", position = 7)
/*     */   default boolean notifyExpire() {
/* 118 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npcunaggroarea\NpcAggroAreaConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */