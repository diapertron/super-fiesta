/*     */ package net.runelite.client.plugins.tileindicators;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Polygon;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.Perspective;
/*     */ import net.runelite.api.coords.LocalPoint;
/*     */ import net.runelite.api.coords.WorldPoint;
/*     */ import net.runelite.client.ui.overlay.Overlay;
/*     */ import net.runelite.client.ui.overlay.OverlayLayer;
/*     */ import net.runelite.client.ui.overlay.OverlayPosition;
/*     */ import net.runelite.client.ui.overlay.OverlayPriority;
/*     */ import net.runelite.client.ui.overlay.OverlayUtil;
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
/*     */ public class TileIndicatorsOverlay
/*     */   extends Overlay
/*     */ {
/*     */   private final Client client;
/*     */   private final TileIndicatorsConfig config;
/*     */   
/*     */   @Inject
/*     */   private TileIndicatorsOverlay(Client client, TileIndicatorsConfig config) {
/*  50 */     this.client = client;
/*  51 */     this.config = config;
/*  52 */     setPosition(OverlayPosition.DYNAMIC);
/*  53 */     setLayer(OverlayLayer.ABOVE_SCENE);
/*  54 */     setPriority(OverlayPriority.MED);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension render(Graphics2D graphics) {
/*  60 */     if (this.config.highlightHoveredTile())
/*     */     {
/*     */       
/*  63 */       if (this.client.getSelectedSceneTile() != null)
/*     */       {
/*  65 */         renderTile(graphics, this.client.getSelectedSceneTile().getLocalLocation(), this.config.highlightHoveredColor());
/*     */       }
/*     */     }
/*     */     
/*  69 */     if (this.config.highlightDestinationTile())
/*     */     {
/*  71 */       renderTile(graphics, this.client.getLocalDestinationLocation(), this.config.highlightDestinationColor());
/*     */     }
/*     */     
/*  74 */     if (this.config.highlightCurrentTile()) {
/*     */       
/*  76 */       WorldPoint playerPos = this.client.getLocalPlayer().getWorldLocation();
/*  77 */       if (playerPos == null)
/*     */       {
/*  79 */         return null;
/*     */       }
/*     */       
/*  82 */       LocalPoint playerPosLocal = LocalPoint.fromWorld(this.client, playerPos);
/*  83 */       if (playerPosLocal == null)
/*     */       {
/*  85 */         return null;
/*     */       }
/*     */       
/*  88 */       renderTile(graphics, playerPosLocal, this.config.highlightCurrentColor());
/*     */     } 
/*     */     
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderTile(Graphics2D graphics, LocalPoint dest, Color color) {
/*  96 */     if (dest == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 101 */     Polygon poly = Perspective.getCanvasTilePoly(this.client, dest);
/*     */     
/* 103 */     if (poly == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 108 */     OverlayUtil.renderPolygon(graphics, poly, color);
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\tileindicators\TileIndicatorsOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */