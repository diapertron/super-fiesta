/*     */ package net.runelite.client.plugins.playerindicatorscombatlevel;
/*     */ 
/*     */ import java.awt.Color;
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
/*     */ @ConfigGroup("nomplayerindicators")
/*     */ public interface PlayerIndicatorsConfigg
/*     */   extends Config
/*     */ {
/*     */   @ConfigItem(position = 0, keyName = "drawOwnName", name = "Highlight own player", description = "Configures whether or not your own player should be highlighted")
/*     */   default boolean highlightOwnPlayer() {
/*  44 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 1, keyName = "ownNameColor", name = "Own player color", description = "Color of your own player")
/*     */   default Color getOwnPlayerColor() {
/*  55 */     return new Color(0, 184, 212);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 2, keyName = "drawFriendNames", name = "Highlight friends", description = "Configures whether or not friends should be highlighted")
/*     */   default boolean highlightFriends() {
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 3, keyName = "friendNameColor", name = "Friend color", description = "Color of friend names")
/*     */   default Color getFriendColor() {
/*  77 */     return new Color(0, 200, 83);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 4, keyName = "drawClanMemberNames", name = "Highlight clan members", description = "Configures whether or clan members should be highlighted")
/*     */   default boolean drawClanMemberNames() {
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 5, keyName = "clanMemberColor", name = "Clan member color", description = "Color of clan members")
/*     */   default Color getClanMemberColor() {
/*  99 */     return new Color(170, 0, 255);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 6, keyName = "drawTeamMemberNames", name = "Highlight team members", description = "Configures whether or not team members should be highlighted")
/*     */   default boolean highlightTeamMembers() {
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 7, keyName = "teamMemberColor", name = "Team member color", description = "Color of team members")
/*     */   default Color getTeamMemberColor() {
/* 121 */     return new Color(19, 110, 247);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 8, keyName = "drawNonClanMemberNames", name = "Highlight non-clan members", description = "Configures whether or not non-clan members should be highlighted")
/*     */   default boolean highlightNonClanMembers() {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 9, keyName = "nonClanMemberColor", name = "Non-clan member color", description = "Color of non-clan member names")
/*     */   default Color getNonClanMemberColor() {
/* 143 */     return Color.RED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 10, keyName = "drawPlayerTiles", name = "Draw tiles under players", description = "Configures whether or not tiles under highlighted players should be drawn")
/*     */   default boolean drawTiles() {
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 11, keyName = "playerNamePosition", name = "Name position", description = "Configures the position of drawn player names, or if they should be disabled")
/*     */   default PlayerNameLocationn playerNamePosition() {
/* 165 */     return PlayerNameLocationn.ABOVE_HEAD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 12, keyName = "drawMinimapNames", name = "Draw names on minimap", description = "Configures whether or not minimap names for players with rendered names should be drawn")
/*     */   default boolean drawMinimapNames() {
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 13, keyName = "colorPlayerMenu", name = "Colorize player menu", description = "Color right click menu for players")
/*     */   default boolean colorPlayerMenu() {
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 14, keyName = "clanMenuIcons", name = "Show clan ranks", description = "Add clan rank to right click menu and next to player names")
/*     */   default boolean showClanRanks() {
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 15, keyName = "showCombatLevel", name = "Show on Combat Level", description = "Show players within combat bracket")
/*     */   default boolean showWildyPlayers() {
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 16, keyName = "wildyColor", name = "Wildy color", description = "Color of wildy people")
/*     */   default Color getWildyColor() {
/* 221 */     return new Color(170, 0, 255);
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicatorscombatlevel\PlayerIndicatorsConfigg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */