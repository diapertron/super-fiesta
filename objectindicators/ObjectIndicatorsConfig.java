/*     */ package net.runelite.client.plugins.objectindicators;
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
/*     */ 
/*     */ 
/*     */ @ConfigGroup("objectindicators")
/*     */ public interface ObjectIndicatorsConfig
/*     */   extends Config
/*     */ {
/*     */   @Alpha
/*     */   @ConfigItem(keyName = "markerColor", name = "Marker color", description = "Configures the color of object marker", position = 0)
/*     */   default Color markerColor() {
/*  47 */     return Color.YELLOW;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "rememberObjectColors", name = "Remember color per object", description = "Color objects using the color from time of marking", position = 1)
/*     */   default boolean rememberObjectColors() {
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "fillSolidColor", name = "Fill solid color", description = "Configures if fill solid color", position = 2)
/*     */   default boolean fillSolidColor() {
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "checkHash", name = "checkHash", description = "checkHash", position = 3)
/*     */   default boolean checkHash() {
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "solidSquare", name = "solidSquare", description = "solidSquare", position = 4)
/*     */   default int solidSquare() {
/*  91 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(keyName = "checkAnimation", name = "checkAnimation", description = "checkAnimation", position = 5)
/*     */   default boolean checkAnimation() {
/* 102 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\objectindicators\ObjectIndicatorsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */