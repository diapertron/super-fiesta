/*     */ package net.runelite.client.plugins.playerindicators;
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
/*     */ 
/*     */ @Singleton
/*     */ public class PlayerIndicatorsOverlay
/*     */   extends Overlay
/*     */ {
/*     */   private static final int ACTOR_OVERHEAD_TEXT_MARGIN = 40;
/*     */   private static final int ACTOR_HORIZONTAL_TEXT_MARGIN = 10;
/*     */   private final PlayerIndicatorsService playerIndicatorsService;
/*     */   private final PlayerIndicatorsConfig config;
/*     */   private final FriendChatManager friendChatManager;
/*     */   
/*     */   @Inject
/*     */   private PlayerIndicatorsOverlay(PlayerIndicatorsConfig config, PlayerIndicatorsService playerIndicatorsService, FriendChatManager friendChatManager) {
/*  58 */     this.config = config;
/*  59 */     this.playerIndicatorsService = playerIndicatorsService;
/*  60 */     this.friendChatManager = friendChatManager;
/*  61 */     setPosition(OverlayPosition.DYNAMIC);
/*  62 */     setPriority(OverlayPriority.MED);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension render(Graphics2D graphics) {
/*  68 */     this.playerIndicatorsService.forEachPlayer((player, color) -> renderPlayerOverlay(graphics, player, color));
/*  69 */     return null;
/*     */   }
/*     */   
/*     */   private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color) {
/*     */     int zOffset;
/*  74 */     PlayerNameLocation drawPlayerNamesConfig = this.config.playerNamePosition();
/*  75 */     if (drawPlayerNamesConfig == PlayerNameLocation.DISABLED) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  81 */     switch (drawPlayerNamesConfig) {
/*     */       
/*     */       case MODEL_CENTER:
/*     */       case MODEL_RIGHT:
/*  85 */         zOffset = actor.getLogicalHeight() / 2;
/*     */         break;
/*     */       default:
/*  88 */         zOffset = actor.getLogicalHeight() + 40;
/*     */         break;
/*     */     } 
/*  91 */     String name = Text.sanitize(actor.getName());
/*  92 */     Point textLocation = actor.getCanvasTextLocation(graphics, name, zOffset);
/*     */     
/*  94 */     if (drawPlayerNamesConfig == PlayerNameLocation.MODEL_RIGHT) {
/*     */       
/*  96 */       textLocation = actor.getCanvasTextLocation(graphics, "", zOffset);
/*     */       
/*  98 */       if (textLocation == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 103 */       textLocation = new Point(textLocation.getX() + 10, textLocation.getY());
/*     */     } 
/*     */     
/* 106 */     if (textLocation == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 111 */     if (this.config.showFriendsChatRanks() && actor.isFriendsChatMember()) {
/*     */       
/* 113 */       FriendsChatRank rank = this.friendChatManager.getRank(name);
/*     */       
/* 115 */       if (rank != FriendsChatRank.UNRANKED) {
/*     */         
/* 117 */         BufferedImage rankImage = this.friendChatManager.getRankImage(rank);
/*     */         
/* 119 */         if (rankImage != null) {
/*     */           
/* 121 */           int imageTextMargin, imageNegativeMargin, imageWidth = rankImage.getWidth();
/*     */ 
/*     */ 
/*     */           
/* 125 */           if (drawPlayerNamesConfig == PlayerNameLocation.MODEL_RIGHT) {
/*     */             
/* 127 */             imageTextMargin = imageWidth;
/* 128 */             imageNegativeMargin = 0;
/*     */           }
/*     */           else {
/*     */             
/* 132 */             imageTextMargin = imageWidth / 2;
/* 133 */             imageNegativeMargin = imageWidth / 2;
/*     */           } 
/*     */           
/* 136 */           int textHeight = graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getMaxDescent();
/* 137 */           Point imageLocation = new Point(textLocation.getX() - imageNegativeMargin - 1, textLocation.getY() - textHeight / 2 - rankImage.getHeight() / 2);
/* 138 */           OverlayUtil.renderImageLocation(graphics, imageLocation, rankImage);
/*     */ 
/*     */           
/* 141 */           textLocation = new Point(textLocation.getX() + imageTextMargin, textLocation.getY());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicators\PlayerIndicatorsOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */