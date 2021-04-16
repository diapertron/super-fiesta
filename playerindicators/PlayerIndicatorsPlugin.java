/*     */ package net.runelite.client.plugins.playerindicators;
/*     */ 
/*     */ import com.google.inject.Provides;
/*     */ import java.awt.Color;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.FriendsChatRank;
/*     */ import net.runelite.api.MenuAction;
/*     */ import net.runelite.api.MenuEntry;
/*     */ import net.runelite.api.Player;
/*     */ import net.runelite.api.events.ClientTick;
/*     */ import net.runelite.client.config.ConfigManager;
/*     */ import net.runelite.client.eventbus.Subscribe;
/*     */ import net.runelite.client.game.FriendChatManager;
/*     */ import net.runelite.client.plugins.Plugin;
/*     */ import net.runelite.client.plugins.PluginDescriptor;
/*     */ import net.runelite.client.ui.overlay.OverlayManager;
/*     */ import net.runelite.client.util.ColorUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PluginDescriptor(name = "Player Indicators", description = "Highlight players on-screen and/or on the minimap", tags = {"highlight", "minimap", "overlay", "players"})
/*     */ public class PlayerIndicatorsPlugin
/*     */   extends Plugin
/*     */ {
/*     */   @Inject
/*     */   private OverlayManager overlayManager;
/*     */   @Inject
/*     */   private PlayerIndicatorsConfig config;
/*     */   @Inject
/*     */   private PlayerIndicatorsOverlay playerIndicatorsOverlay;
/*     */   @Inject
/*     */   private PlayerIndicatorsTileOverlay playerIndicatorsTileOverlay;
/*     */   @Inject
/*     */   private PlayerIndicatorsMinimapOverlay playerIndicatorsMinimapOverlay;
/*     */   @Inject
/*     */   private Client client;
/*     */   @Inject
/*     */   private FriendChatManager friendChatManager;
/*     */   
/*     */   @Provides
/*     */   PlayerIndicatorsConfig provideConfig(ConfigManager configManager) {
/*  77 */     return (PlayerIndicatorsConfig)configManager.getConfig(PlayerIndicatorsConfig.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startUp() throws Exception {
/*  83 */     this.overlayManager.add(this.playerIndicatorsOverlay);
/*  84 */     this.overlayManager.add(this.playerIndicatorsTileOverlay);
/*  85 */     this.overlayManager.add(this.playerIndicatorsMinimapOverlay);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shutDown() throws Exception {
/*  91 */     this.overlayManager.remove(this.playerIndicatorsOverlay);
/*  92 */     this.overlayManager.remove(this.playerIndicatorsTileOverlay);
/*  93 */     this.overlayManager.remove(this.playerIndicatorsMinimapOverlay);
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onClientTick(ClientTick clientTick) {
/*  99 */     if (this.client.isMenuOpen()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 104 */     MenuEntry[] menuEntries = this.client.getMenuEntries();
/* 105 */     boolean modified = false;
/*     */     
/* 107 */     for (MenuEntry entry : menuEntries) {
/*     */       
/* 109 */       int type = entry.getType();
/*     */       
/* 111 */       if (type >= 2000)
/*     */       {
/* 113 */         type -= 2000;
/*     */       }
/*     */       
/* 116 */       if (type == MenuAction.WALK.getId() || type == MenuAction.SPELL_CAST_ON_PLAYER
/* 117 */         .getId() || type == MenuAction.ITEM_USE_ON_PLAYER
/* 118 */         .getId() || type == MenuAction.PLAYER_FIRST_OPTION
/* 119 */         .getId() || type == MenuAction.PLAYER_SECOND_OPTION
/* 120 */         .getId() || type == MenuAction.PLAYER_THIRD_OPTION
/* 121 */         .getId() || type == MenuAction.PLAYER_FOURTH_OPTION
/* 122 */         .getId() || type == MenuAction.PLAYER_FIFTH_OPTION
/* 123 */         .getId() || type == MenuAction.PLAYER_SIXTH_OPTION
/* 124 */         .getId() || type == MenuAction.PLAYER_SEVENTH_OPTION
/* 125 */         .getId() || type == MenuAction.PLAYER_EIGTH_OPTION
/* 126 */         .getId() || type == MenuAction.RUNELITE_PLAYER
/* 127 */         .getId()) {
/*     */         
/* 129 */         Player[] players = this.client.getCachedPlayers();
/* 130 */         Player player = null;
/*     */         
/* 132 */         int identifier = entry.getIdentifier();
/*     */ 
/*     */ 
/*     */         
/* 136 */         if (type == MenuAction.WALK.getId())
/*     */         {
/* 138 */           identifier--;
/*     */         }
/*     */         
/* 141 */         if (identifier >= 0 && identifier < players.length)
/*     */         {
/* 143 */           player = players[identifier];
/*     */         }
/*     */         
/* 146 */         if (player != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 151 */           Decorations decorations = getDecorations(player);
/*     */           
/* 153 */           if (decorations != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 158 */             String oldTarget = entry.getTarget();
/* 159 */             String newTarget = decorateTarget(oldTarget, decorations);
/*     */             
/* 161 */             entry.setTarget(newTarget);
/* 162 */             modified = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 166 */     }  if (modified)
/*     */     {
/* 168 */       this.client.setMenuEntries(menuEntries);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Decorations getDecorations(Player player) {
/* 174 */     int image = -1;
/* 175 */     Color color = null;
/*     */     
/* 177 */     if (this.config.highlightFriends() && player.isFriend()) {
/*     */       
/* 179 */       color = this.config.getFriendColor();
/*     */     }
/* 181 */     else if (this.config.drawFriendsChatMemberNames() && player.isFriendsChatMember()) {
/*     */       
/* 183 */       color = this.config.getFriendsChatMemberColor();
/*     */       
/* 185 */       FriendsChatRank rank = this.friendChatManager.getRank(player.getName());
/* 186 */       if (rank != FriendsChatRank.UNRANKED)
/*     */       {
/* 188 */         image = this.friendChatManager.getIconNumber(rank);
/*     */       }
/*     */     }
/* 191 */     else if (this.config.highlightTeamMembers() && player
/* 192 */       .getTeam() > 0 && this.client.getLocalPlayer().getTeam() == player.getTeam()) {
/*     */       
/* 194 */       color = this.config.getTeamMemberColor();
/*     */     }
/* 196 */     else if (this.config.highlightOthers() && !player.isFriendsChatMember()) {
/*     */       
/* 198 */       color = this.config.getOthersColor();
/*     */     } 
/*     */     
/* 201 */     if (image == -1 && color == null)
/*     */     {
/* 203 */       return null;
/*     */     }
/*     */     
/* 206 */     return new Decorations(image, color);
/*     */   }
/*     */ 
/*     */   
/*     */   private String decorateTarget(String oldTarget, Decorations decorations) {
/* 211 */     String newTarget = oldTarget;
/*     */     
/* 213 */     if (decorations.getColor() != null && this.config.colorPlayerMenu()) {
/*     */ 
/*     */       
/* 216 */       int idx = oldTarget.indexOf('>');
/* 217 */       if (idx != -1)
/*     */       {
/* 219 */         newTarget = oldTarget.substring(idx + 1);
/*     */       }
/*     */       
/* 222 */       newTarget = ColorUtil.prependColorTag(newTarget, decorations.getColor());
/*     */     } 
/*     */     
/* 225 */     if (decorations.getImage() != -1 && this.config.showFriendsChatRanks())
/*     */     {
/* 227 */       newTarget = "<img=" + decorations.getImage() + ">" + newTarget;
/*     */     }
/*     */     
/* 230 */     return newTarget;
/*     */   } private static final class Decorations {
/*     */     private final int image;
/* 233 */     public Decorations(int image, Color color) { this.image = image; this.color = color; } private final Color color; public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Decorations)) return false;  Decorations other = (Decorations)o; if (getImage() != other.getImage()) return false;  Object this$color = getColor(), other$color = other.getColor(); return !((this$color == null) ? (other$color != null) : !this$color.equals(other$color)); } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getImage(); Object $color = getColor(); return result * 59 + (($color == null) ? 43 : $color.hashCode()); } public String toString() { return "PlayerIndicatorsPlugin.Decorations(image=" + getImage() + ", color=" + getColor() + ")"; }
/*     */ 
/*     */     
/* 236 */     public int getImage() { return this.image; } public Color getColor() {
/* 237 */       return this.color;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicators\PlayerIndicatorsPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */