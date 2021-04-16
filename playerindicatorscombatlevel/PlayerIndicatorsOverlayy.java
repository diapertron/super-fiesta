/*     */ package net.runelite.client.plugins.playerindicatorscombatlevel;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.inject.Inject;
/*     */ import javax.inject.Singleton;
/*     */ import net.runelite.api.FriendsChatRank;
/*     */ import net.runelite.api.Player;
/*     */ import net.runelite.api.Point;
/*     */ import net.runelite.client.game.FriendChatManager;
/*     */ import net.runelite.client.ui.overlay.Overlay;
/*     */ import net.runelite.client.ui.overlay.OverlayPosition;
/*     */ import net.runelite.client.ui.overlay.OverlayPriority;
/*     */ import net.runelite.client.ui.overlay.OverlayUtil;
/*     */ import net.runelite.client.util.Text;
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
/*     */ @Singleton
/*     */ public class PlayerIndicatorsOverlayy
/*     */   extends Overlay
/*     */ {
/*     */   private static final int ACTOR_OVERHEAD_TEXT_MARGIN = 40;
/*     */   private static final int ACTOR_HORIZONTAL_TEXT_MARGIN = 10;
/*     */   private final PlayerIndicatorsServicee playerIndicatorsServicee;
/*     */   private final PlayerIndicatorsConfigg config;
/*     */   private final FriendChatManager clanManager;
/*     */   
/*     */   @Inject
/*     */   private PlayerIndicatorsOverlayy(PlayerIndicatorsConfigg config, PlayerIndicatorsServicee playerIndicatorsServicee, FriendChatManager clanManager) {
/*  57 */     this.config = config;
/*  58 */     this.playerIndicatorsServicee = playerIndicatorsServicee;
/*  59 */     this.clanManager = clanManager;
/*  60 */     setPosition(OverlayPosition.DYNAMIC);
/*  61 */     setPriority(OverlayPriority.MED);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension render(Graphics2D graphics) {
/*  67 */     this.playerIndicatorsServicee.forEachPlayer((player, color) -> renderPlayerOverlay(graphics, player, color));
/*  68 */     return null;
/*     */   }
/*     */   
/*     */   private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color) {
/*     */     int zOffset;
/*  73 */     PlayerNameLocationn drawPlayerNamesConfig = this.config.playerNamePosition();
/*  74 */     if (drawPlayerNamesConfig == PlayerNameLocationn.DISABLED) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  80 */     switch (drawPlayerNamesConfig) {
/*     */       
/*     */       case MODEL_CENTER:
/*     */       case MODEL_RIGHT:
/*  84 */         zOffset = actor.getLogicalHeight() / 2;
/*     */         break;
/*     */       default:
/*  87 */         zOffset = actor.getLogicalHeight() + 40;
/*     */         break;
/*     */     } 
/*  90 */     String name = Text.sanitize(actor.getName());
/*  91 */     Point textLocation = actor.getCanvasTextLocation(graphics, name, zOffset);
/*     */     
/*  93 */     if (drawPlayerNamesConfig == PlayerNameLocationn.MODEL_RIGHT) {
/*     */       
/*  95 */       textLocation = actor.getCanvasTextLocation(graphics, "", zOffset);
/*     */       
/*  97 */       if (textLocation == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 102 */       textLocation = new Point(textLocation.getX() + 10, textLocation.getY());
/*     */     } 
/*     */     
/* 105 */     if (textLocation == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 110 */     if (this.config.showClanRanks() && actor.isFriendsChatMember()) {
/*     */       
/* 112 */       FriendsChatRank rank = this.clanManager.getRank(name);
/*     */       
/* 114 */       if (rank != FriendsChatRank.UNRANKED) {
/*     */         
/* 116 */         BufferedImage clanchatImage = this.clanManager.getRankImage(rank);
/*     */         
/* 118 */         if (clanchatImage != null) {
/*     */           
/* 120 */           int clanImageTextMargin, clanImageNegativeMargin, clanImageWidth = clanchatImage.getWidth();
/*     */ 
/*     */ 
/*     */           
/* 124 */           if (drawPlayerNamesConfig == PlayerNameLocationn.MODEL_RIGHT) {
/*     */             
/* 126 */             clanImageTextMargin = clanImageWidth;
/* 127 */             clanImageNegativeMargin = 0;
/*     */           }
/*     */           else {
/*     */             
/* 131 */             clanImageTextMargin = clanImageWidth / 2;
/* 132 */             clanImageNegativeMargin = clanImageWidth / 2;
/*     */           } 
/*     */           
/* 135 */           int textHeight = graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getMaxDescent();
/* 136 */           Point imageLocation = new Point(textLocation.getX() - clanImageNegativeMargin - 1, textLocation.getY() - textHeight / 2 - clanchatImage.getHeight() / 2);
/* 137 */           OverlayUtil.renderImageLocation(graphics, imageLocation, clanchatImage);
/*     */ 
/*     */           
/* 140 */           textLocation = new Point(textLocation.getX() + clanImageTextMargin, textLocation.getY());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicatorscombatlevel\PlayerIndicatorsOverlayy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */