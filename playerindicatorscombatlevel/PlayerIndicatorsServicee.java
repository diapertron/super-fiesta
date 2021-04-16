/*    */ package net.runelite.client.plugins.playerindicatorscombatlevel;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.function.BiConsumer;
/*    */ import javax.inject.Inject;
/*    */ import javax.inject.Singleton;
/*    */ import net.runelite.api.Client;
/*    */ import net.runelite.api.Player;
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
/*    */ @Singleton
/*    */ public class PlayerIndicatorsServicee
/*    */ {
/*    */   private final Client client;
/*    */   private final PlayerIndicatorsPluginn plugin;
/*    */   private final PlayerIndicatorsConfigg config;
/*    */   
/*    */   @Inject
/*    */   private PlayerIndicatorsServicee(Client client, PlayerIndicatorsPluginn plugin, PlayerIndicatorsConfigg config) {
/* 44 */     this.plugin = plugin;
/* 45 */     this.config = config;
/* 46 */     this.client = client;
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEachPlayer(BiConsumer<Player, Color> consumer) {
/* 51 */     if (!this.config.highlightOwnPlayer() && !this.config.drawClanMemberNames() && 
/* 52 */       !this.config.highlightFriends() && !this.config.highlightNonClanMembers()) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 57 */     Player localPlayer = this.client.getLocalPlayer();
/*    */     
/* 59 */     for (Player player : this.client.getPlayers()) {
/*    */       
/* 61 */       if (player == null || player.getName() == null) {
/*    */         continue;
/*    */       }
/*    */ 
/*    */       
/* 66 */       boolean isClanMember = player.isFriendsChatMember();
/*    */       
/* 68 */       if (player == localPlayer) {
/*    */         
/* 70 */         if (this.config.highlightOwnPlayer())
/*    */         {
/* 72 */           consumer.accept(player, this.config.getOwnPlayerColor()); } 
/*    */         continue;
/*    */       } 
/* 75 */       if (this.config.highlightFriends() && player.isFriend()) {
/*    */         
/* 77 */         consumer.accept(player, this.config.getFriendColor()); continue;
/*    */       } 
/* 79 */       if (this.config.drawClanMemberNames() && isClanMember) {
/*    */         
/* 81 */         consumer.accept(player, this.config.getClanMemberColor()); continue;
/*    */       } 
/* 83 */       if (this.config.highlightTeamMembers() && localPlayer.getTeam() > 0 && localPlayer.getTeam() == player.getTeam()) {
/*    */         
/* 85 */         consumer.accept(player, this.config.getTeamMemberColor()); continue;
/*    */       } 
/* 87 */       if (this.config.highlightNonClanMembers() && !isClanMember) {
/*    */         
/* 89 */         consumer.accept(player, this.config.getNonClanMemberColor()); continue;
/*    */       } 
/* 91 */       if (this.config.showWildyPlayers() && this.plugin.withinRange(player))
/*    */       {
/* 93 */         consumer.accept(player, this.config.getWildyColor());
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicatorscombatlevel\PlayerIndicatorsServicee.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */