/*     */ package net.runelite.client.plugins.playerindicatorscombatlevel;
/*     */ 
/*     */ import com.google.inject.Provides;
/*     */ import java.awt.Color;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.FriendsChatRank;
/*     */ import net.runelite.api.MenuAction;
/*     */ import net.runelite.api.MenuEntry;
/*     */ import net.runelite.api.Player;
/*     */ import net.runelite.api.events.ClientTick;
/*     */ import net.runelite.api.events.ScriptPostFired;
/*     */ import net.runelite.api.events.WidgetLoaded;
/*     */ import net.runelite.api.widgets.Widget;
/*     */ import net.runelite.api.widgets.WidgetInfo;
/*     */ import net.runelite.client.config.ConfigManager;
/*     */ import net.runelite.client.eventbus.Subscribe;
/*     */ import net.runelite.client.game.FriendChatManager;
/*     */ import net.runelite.client.plugins.Plugin;
/*     */ import net.runelite.client.plugins.PluginDescriptor;
/*     */ import net.runelite.client.ui.overlay.OverlayManager;
/*     */ import net.runelite.client.util.ColorUtil;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ @PluginDescriptor(name = "Nom Player Indicators", description = "Highlight players on-screen and/or on the minimap", tags = {"highlight", "minimap", "overlay", "players", "nomscripts"})
/*     */ public class PlayerIndicatorsPluginn
/*     */   extends Plugin
/*     */ {
/*  60 */   private static final Logger log = LoggerFactory.getLogger(PlayerIndicatorsPluginn.class);
/*     */ 
/*     */   
/*     */   @Inject
/*     */   private OverlayManager overlayManager;
/*     */   
/*     */   @Inject
/*     */   private PlayerIndicatorsConfigg config;
/*     */   
/*     */   @Inject
/*     */   private PlayerIndicatorsOverlayy playerIndicatorsOverlayy;
/*     */   
/*     */   @Inject
/*     */   private PlayerIndicatorsTileOverlayy playerIndicatorsTileOverlayy;
/*     */   
/*     */   @Inject
/*     */   private PlayerIndicatorsMinimapOverlayy playerIndicatorsMinimapOverlayy;
/*     */   
/*     */   @Inject
/*     */   private Client client;
/*     */   
/*     */   @Inject
/*     */   private FriendChatManager clanManager;
/*     */ 
/*     */   
/*     */   @Provides
/*     */   PlayerIndicatorsConfigg provideConfig(ConfigManager configManager) {
/*  87 */     return (PlayerIndicatorsConfigg)configManager.getConfig(PlayerIndicatorsConfigg.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startUp() throws Exception {
/*  93 */     this.overlayManager.add(this.playerIndicatorsOverlayy);
/*  94 */     this.overlayManager.add(this.playerIndicatorsTileOverlayy);
/*  95 */     this.overlayManager.add(this.playerIndicatorsMinimapOverlayy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shutDown() throws Exception {
/* 101 */     this.overlayManager.remove(this.playerIndicatorsOverlayy);
/* 102 */     this.overlayManager.remove(this.playerIndicatorsTileOverlayy);
/* 103 */     this.overlayManager.remove(this.playerIndicatorsMinimapOverlayy);
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onClientTick(ClientTick clientTick) {
/* 109 */     if (this.client.isMenuOpen()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 114 */     MenuEntry[] menuEntries = this.client.getMenuEntries();
/* 115 */     boolean modified = false;
/*     */     
/* 117 */     for (MenuEntry entry : menuEntries) {
/*     */       
/* 119 */       int type = entry.getType();
/*     */       
/* 121 */       if (type >= 2000)
/*     */       {
/* 123 */         type -= 2000;
/*     */       }
/*     */       
/* 126 */       if (type == MenuAction.WALK.getId() || type == MenuAction.SPELL_CAST_ON_PLAYER
/* 127 */         .getId() || type == MenuAction.ITEM_USE_ON_PLAYER
/* 128 */         .getId() || type == MenuAction.PLAYER_FIRST_OPTION
/* 129 */         .getId() || type == MenuAction.PLAYER_SECOND_OPTION
/* 130 */         .getId() || type == MenuAction.PLAYER_THIRD_OPTION
/* 131 */         .getId() || type == MenuAction.PLAYER_FOURTH_OPTION
/* 132 */         .getId() || type == MenuAction.PLAYER_FIFTH_OPTION
/* 133 */         .getId() || type == MenuAction.PLAYER_SIXTH_OPTION
/* 134 */         .getId() || type == MenuAction.PLAYER_SEVENTH_OPTION
/* 135 */         .getId() || type == MenuAction.PLAYER_EIGTH_OPTION
/* 136 */         .getId() || type == MenuAction.RUNELITE_PLAYER
/* 137 */         .getId()) {
/*     */         
/* 139 */         Player[] players = this.client.getCachedPlayers();
/* 140 */         Player player = null;
/*     */         
/* 142 */         int identifier = entry.getIdentifier();
/*     */ 
/*     */ 
/*     */         
/* 146 */         if (type == MenuAction.WALK.getId())
/*     */         {
/* 148 */           identifier--;
/*     */         }
/*     */         
/* 151 */         if (identifier >= 0 && identifier < players.length)
/*     */         {
/* 153 */           player = players[identifier];
/*     */         }
/*     */         
/* 156 */         if (player != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 161 */           Decorations decorations = getDecorations(player);
/*     */           
/* 163 */           if (decorations != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 168 */             String oldTarget = entry.getTarget();
/* 169 */             String newTarget = decorateTarget(oldTarget, decorations);
/*     */             
/* 171 */             entry.setTarget(newTarget);
/* 172 */             modified = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 176 */     }  if (modified)
/*     */     {
/* 178 */       this.client.setMenuEntries(menuEntries);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Decorations getDecorations(Player player) {
/* 184 */     int image = -1;
/* 185 */     Color color = null;
/*     */     
/* 187 */     if (this.config.highlightFriends() && player.isFriend()) {
/*     */       
/* 189 */       color = this.config.getFriendColor();
/*     */     }
/* 191 */     else if (this.config.drawClanMemberNames() && player.isFriendsChatMember()) {
/*     */       
/* 193 */       color = this.config.getClanMemberColor();
/*     */       
/* 195 */       FriendsChatRank rank = this.clanManager.getRank(player.getName());
/* 196 */       if (rank != FriendsChatRank.UNRANKED)
/*     */       {
/* 198 */         image = this.clanManager.getIconNumber(rank);
/*     */       }
/*     */     }
/* 201 */     else if (this.config.highlightTeamMembers() && player
/* 202 */       .getTeam() > 0 && this.client.getLocalPlayer().getTeam() == player.getTeam()) {
/*     */       
/* 204 */       color = this.config.getTeamMemberColor();
/*     */     }
/* 206 */     else if (this.config.highlightNonClanMembers() && !player.isFriendsChatMember()) {
/*     */       
/* 208 */       color = this.config.getNonClanMemberColor();
/*     */     } 
/*     */     
/* 211 */     if (image == -1 && color == null)
/*     */     {
/* 213 */       return null;
/*     */     }
/*     */     
/* 216 */     return new Decorations(image, color);
/*     */   }
/*     */ 
/*     */   
/*     */   private String decorateTarget(String oldTarget, Decorations decorations) {
/* 221 */     String newTarget = oldTarget;
/*     */     
/* 223 */     if (decorations.getColor() != null && this.config.colorPlayerMenu()) {
/*     */ 
/*     */       
/* 226 */       int idx = oldTarget.indexOf('>');
/* 227 */       if (idx != -1)
/*     */       {
/* 229 */         newTarget = oldTarget.substring(idx + 1);
/*     */       }
/*     */       
/* 232 */       newTarget = ColorUtil.prependColorTag(newTarget, decorations.getColor());
/*     */     } 
/*     */     
/* 235 */     if (decorations.getImage() != -1 && this.config.showClanRanks())
/*     */     {
/* 237 */       newTarget = "<img=" + decorations.getImage() + ">" + newTarget;
/*     */     }
/*     */     
/* 240 */     return newTarget;
/*     */   } private static final class Decorations {
/*     */     private final int image;
/* 243 */     public Decorations(int image, Color color) { this.image = image; this.color = color; } private final Color color; public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Decorations)) return false;  Decorations other = (Decorations)o; if (getImage() != other.getImage()) return false;  Object this$color = getColor(), other$color = other.getColor(); return !((this$color == null) ? (other$color != null) : !this$color.equals(other$color)); } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getImage(); Object $color = getColor(); return result * 59 + (($color == null) ? 43 : $color.hashCode()); } public String toString() { return "PlayerIndicatorsPluginn.Decorations(image=" + getImage() + ", color=" + getColor() + ")"; }
/*     */ 
/*     */     
/* 246 */     public int getImage() { return this.image; } public Color getColor() {
/* 247 */       return this.color;
/*     */     }
/*     */   }
/*     */   
/*     */   @Subscribe
/*     */   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
/* 253 */     if (scriptPostFired.getScriptId() == 388)
/*     */     {
/* 255 */       appendAttackLevelRangeText();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onWidgetLoaded(WidgetLoaded event) {
/* 263 */     if (event.getGroupId() == 90)
/*     */     {
/* 265 */       appendAttackLevelRangeText();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean withinRange(Player p) {
/* 270 */     return (p.getCombatLevel() >= this.minCombatLevel && p.getCombatLevel() <= this.maxCombatLevel);
/*     */   }
/*     */   
/* 273 */   private int minCombatLevel = 0;
/* 274 */   private int maxCombatLevel = 0;
/*     */ 
/*     */   
/*     */   private void combatAttackRange(int combatLevel, int wildernessLevel) {
/* 278 */     this.minCombatLevel = Math.max(3, combatLevel - wildernessLevel);
/* 279 */     this.maxCombatLevel = Math.min(126, combatLevel + wildernessLevel);
/*     */   }
/*     */   
/* 282 */   private final Pattern WILDERNESS_LEVEL_PATTERN = Pattern.compile(".*?(\\d+)-(\\d+).*");
/*     */   private void appendAttackLevelRangeText() {
/* 284 */     Widget wildernessLevelWidget = this.client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
/* 285 */     Widget pvpWorldWidget = this.client.getWidget(90, 58);
/*     */     
/* 287 */     String wildernessLevelText = "";
/* 288 */     if (pvpWorldWidget != null && !pvpWorldWidget.isHidden()) {
/* 289 */       wildernessLevelText = pvpWorldWidget.getText();
/*     */     }
/* 291 */     if (wildernessLevelText.isEmpty() && wildernessLevelWidget != null && !wildernessLevelWidget.isHidden()) {
/* 292 */       wildernessLevelText = wildernessLevelWidget.getText();
/*     */     }
/* 294 */     if (wildernessLevelText.isEmpty()) {
/* 295 */       this.minCombatLevel = 0;
/* 296 */       this.maxCombatLevel = 0;
/*     */       
/*     */       return;
/*     */     } 
/* 300 */     log.info("text " + wildernessLevelText);
/* 301 */     log.info("wildy level " + this.minCombatLevel + " " + this.maxCombatLevel);
/* 302 */     Matcher m = this.WILDERNESS_LEVEL_PATTERN.matcher(wildernessLevelText);
/* 303 */     if (!m.matches()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 309 */     this.minCombatLevel = Integer.parseInt(m.group(1));
/* 310 */     this.maxCombatLevel = Integer.parseInt(m.group(2));
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicatorscombatlevel\PlayerIndicatorsPluginn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */