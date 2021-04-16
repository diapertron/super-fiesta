/*    */ package net.runelite.client.plugins.playerindicators;
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
/*    */ public class PlayerIndicatorsService
/*    */ {
/*    */   private final Client client;
/*    */   private final PlayerIndicatorsConfig config;
/*    */   
/*    */   @Inject
/*    */   private PlayerIndicatorsService(Client client, PlayerIndicatorsConfig config) {
/* 43 */     this.config = config;
/* 44 */     this.client = client;
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEachPlayer(BiConsumer<Player, Color> consumer) {
/* 49 */     if (!this.config.highlightOwnPlayer() && !this.config.drawFriendsChatMemberNames() && 
/* 50 */       !this.config.highlightFriends() && !this.config.highlightOthers()) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 55 */     Player localPlayer = this.client.getLocalPlayer();
/*    */     
/* 57 */     for (Player player : this.client.getPlayers()) {
/*    */       
/* 59 */       if (player == null || player.getName() == null) {
/*    */         continue;
/*    */       }
/*    */ 
/*    */       
/* 64 */       boolean isFriendsChatMember = player.isFriendsChatMember();
/*    */       
/* 66 */       if (player == localPlayer) {
/*    */         
/* 68 */         if (this.config.highlightOwnPlayer())
/*    */         {
/* 70 */           consumer.accept(player, this.config.getOwnPlayerColor()); } 
/*    */         continue;
/*    */       } 
/* 73 */       if (this.config.highlightFriends() && player.isFriend()) {
/*    */         
/* 75 */         consumer.accept(player, this.config.getFriendColor()); continue;
/*    */       } 
/* 77 */       if (this.config.drawFriendsChatMemberNames() && isFriendsChatMember) {
/*    */         
/* 79 */         consumer.accept(player, this.config.getFriendsChatMemberColor()); continue;
/*    */       } 
/* 81 */       if (this.config.highlightTeamMembers() && localPlayer.getTeam() > 0 && localPlayer.getTeam() == player.getTeam()) {
/*    */         
/* 83 */         consumer.accept(player, this.config.getTeamMemberColor()); continue;
/*    */       } 
/* 85 */       if (this.config.highlightOthers() && !isFriendsChatMember)
/*    */       {
/* 87 */         consumer.accept(player, this.config.getOthersColor());
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\playerindicators\PlayerIndicatorsService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */