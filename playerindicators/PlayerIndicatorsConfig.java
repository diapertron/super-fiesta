/*     */ package net.runelite.client.plugins.playerindicators;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.runelite.client.config.Config;
/*     */ import net.runelite.client.config.ConfigGroup;
/*     */ import net.runelite.client.config.ConfigItem;
/*     */ import net.runelite.client.config.ConfigSection;
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
/*     */ @ConfigGroup("playerindicators")
/*     */ public interface PlayerIndicatorsConfig
/*     */   extends Config
/*     */ {
/*     */   @ConfigSection(name = "Highlight Options", description = "Toggle highlighted players by type (self, friends, etc.) and choose their highlight colors", position = 99)
/*     */   public static final String highlightSection = "section";
/*     */   
/*     */   @ConfigItem(position = 0, keyName = "drawOwnName", name = "Highlight own player", description = "Configures whether or not your own player should be highlighted", section = "section")
/*     */   default boolean highlightOwnPlayer() {
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 1, keyName = "ownNameColor", name = "Own player color", description = "Color of your own player", section = "section")
/*     */   default Color getOwnPlayerColor() {
/*  64 */     return new Color(0, 184, 212);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 2, keyName = "drawFriendNames", name = "Highlight friends", description = "Configures whether or not friends should be highlighted", section = "section")
/*     */   default boolean highlightFriends() {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 3, keyName = "friendNameColor", name = "Friend color", description = "Color of friend names", section = "section")
/*     */   default Color getFriendColor() {
/*  88 */     return new Color(0, 200, 83);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 4, keyName = "drawClanMemberNames", name = "Highlight friends chat members", description = "Configures if friends chat members should be highlighted", section = "section")
/*     */   default boolean drawFriendsChatMemberNames() {
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 5, keyName = "clanMemberColor", name = "Friends chat member color", description = "Color of friends chat members", section = "section")
/*     */   default Color getFriendsChatMemberColor() {
/* 112 */     return new Color(170, 0, 255);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 6, keyName = "drawTeamMemberNames", name = "Highlight team members", description = "Configures whether or not team members should be highlighted", section = "section")
/*     */   default boolean highlightTeamMembers() {
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 7, keyName = "teamMemberColor", name = "Team member color", description = "Color of team members", section = "section")
/*     */   default Color getTeamMemberColor() {
/* 136 */     return new Color(19, 110, 247);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 8, keyName = "drawNonClanMemberNames", name = "Highlight others", description = "Configures whether or not other players should be highlighted", section = "section")
/*     */   default boolean highlightOthers() {
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 9, keyName = "nonClanMemberColor", name = "Others color", description = "Color of other players names", section = "section")
/*     */   default Color getOthersColor() {
/* 160 */     return Color.RED;
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
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 11, keyName = "playerNamePosition", name = "Name position", description = "Configures the position of drawn player names, or if they should be disabled")
/*     */   default PlayerNameLocation playerNamePosition() {
/* 182 */     return PlayerNameLocation.ABOVE_HEAD;
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
/* 193 */     return false;
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
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ConfigItem(position = 14, keyName = "clanMenuIcons", name = "Show friends chat ranks", description = "Add friends chat rank to right click menu and next to player names")
/*     */   default boolean showFriendsChatRanks() {
/* 215 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicators\PlayerIndicatorsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */