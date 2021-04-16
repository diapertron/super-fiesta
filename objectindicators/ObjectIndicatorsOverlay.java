/*     */ package net.runelite.client.plugins.objectindicators;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.DecorativeObject;
/*     */ import net.runelite.api.GameObject;
/*     */ import net.runelite.api.GroundObject;
/*     */ import net.runelite.api.Player;
/*     */ import net.runelite.api.TileObject;
/*     */ import net.runelite.api.WallObject;
/*     */ import net.runelite.client.plugins.SquareOverlay;
/*     */ import net.runelite.client.ui.overlay.Overlay;
/*     */ import net.runelite.client.ui.overlay.OverlayPosition;
/*     */ import net.runelite.client.ui.overlay.OverlayPriority;
/*     */ import net.runelite.client.ui.overlay.OverlayUtil;
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
/*     */ class ObjectIndicatorsOverlay
/*     */   extends Overlay
/*     */ {
/*  35 */   private static final Logger log = LoggerFactory.getLogger(ObjectIndicatorsOverlay.class);
/*     */   
/*     */   private final Client client;
/*     */   
/*     */   private final ObjectIndicatorsConfig config;
/*     */   
/*     */   private final ObjectIndicatorsPlugin plugin;
/*     */   
/*     */   @Inject
/*     */   private ObjectIndicatorsOverlay(Client client, ObjectIndicatorsConfig config, ObjectIndicatorsPlugin plugin) {
/*  45 */     this.client = client;
/*  46 */     this.config = config;
/*  47 */     this.plugin = plugin;
/*  48 */     setPosition(OverlayPosition.DYNAMIC);
/*  49 */     setPriority(OverlayPriority.LOW);
/*  50 */     setLayer(SquareOverlay.OVERLAY_LAYER2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension render(Graphics2D graphics) {
/*  56 */     if (this.config.checkAnimation()) {
/*     */       
/*  58 */       Player p = this.client.getLocalPlayer();
/*  59 */       if (p != null && p.getAnimation() != -1) return null;
/*     */     
/*     */     } 
/*  62 */     for (ColorTileObject colorTileObject : this.plugin.getObjects()) {
/*     */       Shape polygon;
/*  64 */       TileObject object = colorTileObject.getTileObject();
/*  65 */       Color color = colorTileObject.getColor();
/*     */ 
/*     */ 
/*     */       
/*  69 */       if (this.config.checkHash())
/*     */       {
/*  71 */         if (!this.plugin.getAddedIds().contains(Integer.valueOf(object.getId())))
/*     */           continue; 
/*     */       }
/*  74 */       if (object.getPlane() != this.client.getPlane()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/*  79 */       if (color == null || !this.config.rememberObjectColors())
/*     */       {
/*     */         
/*  82 */         color = this.config.markerColor();
/*     */       }
/*     */ 
/*     */       
/*  86 */       Shape polygon2 = null;
/*     */       
/*  88 */       if (object instanceof GameObject) {
/*     */         
/*  90 */         polygon = ((GameObject)object).getConvexHull();
/*     */       }
/*  92 */       else if (object instanceof WallObject) {
/*     */         
/*  94 */         polygon = ((WallObject)object).getConvexHull();
/*  95 */         polygon2 = ((WallObject)object).getConvexHull2();
/*     */       }
/*  97 */       else if (object instanceof DecorativeObject) {
/*     */         
/*  99 */         polygon = ((DecorativeObject)object).getConvexHull();
/* 100 */         polygon2 = ((DecorativeObject)object).getConvexHull2();
/*     */       }
/* 102 */       else if (object instanceof GroundObject) {
/*     */         
/* 104 */         polygon = ((GroundObject)object).getConvexHull();
/*     */       }
/*     */       else {
/*     */         
/* 108 */         polygon = object.getCanvasTilePoly();
/*     */       } 
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
/* 120 */       drawPolygon(graphics, polygon, color);
/* 121 */       drawPolygon(graphics, polygon2, color);
/*     */     } 
/*     */     
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawPolygon(Graphics2D g, Shape s, Color color) {
/* 130 */     if (s == null)
/* 131 */       return;  if (this.config.fillSolidColor()) {
/*     */       
/* 133 */       g.setColor(color);
/* 134 */       if (this.config.solidSquare() > 0)
/*     */       {
/* 136 */         Rectangle bounds = s.getBounds();
/* 137 */         SquareOverlay.drawCenterSquare(g, bounds.getCenterX(), bounds.getCenterY(), this.config.solidSquare(), color);
/*     */       }
/*     */       else
/*     */       {
/* 141 */         g.fill(s);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 146 */       OverlayUtil.renderPolygon(g, s, color);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\objectindicators\ObjectIndicatorsOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */