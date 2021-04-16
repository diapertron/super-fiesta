/*     */ package net.runelite.client.plugins.npcunaggroarea;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.time.Instant;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.Perspective;
/*     */ import net.runelite.api.Point;
/*     */ import net.runelite.api.coords.LocalPoint;
/*     */ import net.runelite.api.geometry.Geometry;
/*     */ import net.runelite.client.ui.overlay.Overlay;
/*     */ import net.runelite.client.ui.overlay.OverlayLayer;
/*     */ import net.runelite.client.ui.overlay.OverlayPosition;
/*     */ import net.runelite.client.ui.overlay.OverlayPriority;
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
/*     */ class NpcAggroAreaOverlay
/*     */   extends Overlay
/*     */ {
/*     */   private static final int MAX_LOCAL_DRAW_LENGTH = 2560;
/*     */   private final Client client;
/*     */   private final NpcAggroAreaConfig config;
/*     */   private final NpcAggroAreaPlugin plugin;
/*     */   
/*     */   @Inject
/*     */   private NpcAggroAreaOverlay(Client client, NpcAggroAreaConfig config, NpcAggroAreaPlugin plugin) {
/*  56 */     this.client = client;
/*  57 */     this.config = config;
/*  58 */     this.plugin = plugin;
/*     */     
/*  60 */     setLayer(OverlayLayer.ABOVE_SCENE);
/*  61 */     setPriority(OverlayPriority.LOW);
/*  62 */     setPosition(OverlayPosition.DYNAMIC);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension render(Graphics2D graphics) {
/*  68 */     if (!this.plugin.isActive() || this.plugin.getSafeCenters()[1] == null)
/*     */     {
/*  70 */       return null;
/*     */     }
/*     */     
/*  73 */     GeneralPath lines = this.plugin.getLinesToDisplay()[this.client.getPlane()];
/*  74 */     if (lines == null)
/*     */     {
/*  76 */       return null;
/*     */     }
/*     */     
/*  79 */     Color outlineColor = this.config.unaggroAreaColor();
/*  80 */     AggressionTimer timer = this.plugin.getCurrentTimer();
/*  81 */     if (outlineColor == null || timer == null || Instant.now().compareTo(timer.getEndTime()) < 0)
/*     */     {
/*  83 */       outlineColor = this.config.aggroAreaColor();
/*     */     }
/*     */     
/*  86 */     renderPath(graphics, lines, outlineColor);
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderPath(Graphics2D graphics, GeneralPath path, Color color) {
/*  92 */     LocalPoint playerLp = this.client.getLocalPlayer().getLocalLocation();
/*     */ 
/*     */     
/*  95 */     Rectangle viewArea = new Rectangle(playerLp.getX() - 2560, playerLp.getY() - 2560, 5120, 5120);
/*     */ 
/*     */ 
/*     */     
/*  99 */     graphics.setColor(color);
/* 100 */     graphics.setStroke(new BasicStroke(1.0F));
/*     */     
/* 102 */     path = Geometry.clipPath(path, viewArea);
/* 103 */     path = Geometry.filterPath(path, (p1, p2) -> 
/* 104 */         (Perspective.localToCanvas(this.client, new LocalPoint((int)p1[0], (int)p1[1]), this.client.getPlane()) != null && Perspective.localToCanvas(this.client, new LocalPoint((int)p2[0], (int)p2[1]), this.client.getPlane()) != null));
/*     */     
/* 106 */     path = Geometry.transformPath(path, coords -> {
/*     */           Point point = Perspective.localToCanvas(this.client, new LocalPoint((int)coords[0], (int)coords[1]), this.client.getPlane());
/*     */           
/*     */           coords[0] = point.getX();
/*     */           
/*     */           coords[1] = point.getY();
/*     */         });
/* 113 */     graphics.draw(path);
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npcunaggroarea\NpcAggroAreaOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */