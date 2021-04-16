/*     */ package net.runelite.client.plugins.npchighlight;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Polygon;
/*     */ import java.awt.Shape;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.time.Instant;
/*     */ import java.util.Locale;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Actor;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.NPC;
/*     */ import net.runelite.api.NPCComposition;
/*     */ import net.runelite.api.Perspective;
/*     */ import net.runelite.api.Player;
/*     */ import net.runelite.api.Point;
/*     */ import net.runelite.api.coords.LocalPoint;
/*     */ import net.runelite.api.coords.WorldPoint;
/*     */ import net.runelite.client.plugins.SquareOverlay;
/*     */ import net.runelite.client.ui.overlay.Overlay;
/*     */ import net.runelite.client.ui.overlay.OverlayPosition;
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
/*     */ public class NpcSceneOverlay
/*     */   extends Overlay
/*     */ {
/*  50 */   private static final Color TEXT_COLOR = Color.WHITE;
/*     */   
/*  52 */   private static final NumberFormat TIME_LEFT_FORMATTER = DecimalFormat.getInstance(Locale.US); private final Client client; private final NpcIndicatorsConfig config; private final NpcIndicatorsPlugin plugin; Instant interactTime;
/*     */   Actor lastActor;
/*     */   
/*     */   static {
/*  56 */     ((DecimalFormat)TIME_LEFT_FORMATTER).applyPattern("#0.0");
/*     */   }
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
/*     */   @Inject
/*     */   NpcSceneOverlay(Client client, NpcIndicatorsConfig config, NpcIndicatorsPlugin plugin) {
/*  73 */     this.interactTime = Instant.now();
/*  74 */     this.lastActor = null; this.client = client;
/*     */     this.config = config;
/*     */     this.plugin = plugin;
/*     */     setPosition(OverlayPosition.DYNAMIC);
/*  78 */     setLayer(SquareOverlay.OVERLAY_LAYER); } public Dimension render(Graphics2D graphics) { if (this.config.showRespawnTimer())
/*     */     {
/*  80 */       this.plugin.getDeadNpcsToDisplay().forEach((id, npc) -> renderNpcRespawn(npc, graphics));
/*     */     }
/*     */ 
/*     */     
/*  84 */     Player local = this.client.getLocalPlayer();
/*  85 */     if (local == null) return null; 
/*  86 */     Actor actor = local.getInteracting();
/*     */     
/*  88 */     if (this.config.checkCombat() && actor != null) {
/*  89 */       return null;
/*     */     }
/*     */     
/*  92 */     for (NPC npc : this.plugin.getHighlightedNpcs())
/*     */     {
/*  94 */       renderNpcOverlay(graphics, npc, this.config.getHighlightColor());
/*     */     }
/*  96 */     if (this.config.interacting()) {
/*  97 */       if (actor != null) {
/*  98 */         this.interactTime = Instant.now();
/*  99 */         this.lastActor = actor;
/* 100 */         SquareOverlay.drawCenterSquare(graphics, actor, this.config.solidSquare(), this.config.getHighlightColor());
/* 101 */       } else if (this.lastActor != null && this.interactTime.plusMillis(this.config.interactingDelay()).isAfter(Instant.now())) {
/* 102 */         SquareOverlay.drawCenterSquare(graphics, this.lastActor, this.config.solidSquare(), this.config.getHighlightColor());
/*     */       } 
/*     */     }
/* 105 */     return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderNpcRespawn(MemorizedNpc npc, Graphics2D graphics) {
/* 110 */     if (npc.getPossibleRespawnLocations().isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 115 */     WorldPoint respawnLocation = npc.getPossibleRespawnLocations().get(0);
/* 116 */     LocalPoint lp = LocalPoint.fromWorld(this.client, respawnLocation.getX(), respawnLocation.getY());
/*     */     
/* 118 */     if (lp == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 123 */     Color color = this.config.getHighlightColor();
/*     */ 
/*     */ 
/*     */     
/* 127 */     LocalPoint centerLp = new LocalPoint(lp.getX() + 128 * (npc.getNpcSize() - 1) / 2, lp.getY() + 128 * (npc.getNpcSize() - 1) / 2);
/*     */     
/* 129 */     Polygon poly = Perspective.getCanvasTileAreaPoly(this.client, centerLp, npc.getNpcSize());
/*     */     
/* 131 */     if (poly != null)
/*     */     {
/* 133 */       OverlayUtil.renderPolygon(graphics, poly, color);
/*     */     }
/*     */     
/* 136 */     Instant now = Instant.now();
/* 137 */     double baseTick = (npc.getDiedOnTick() + npc.getRespawnTime() - this.client.getTickCount()) * 0.6D;
/* 138 */     double sinceLast = (now.toEpochMilli() - this.plugin.getLastTickUpdate().toEpochMilli()) / 1000.0D;
/* 139 */     double timeLeft = Math.max(0.0D, baseTick - sinceLast);
/* 140 */     String timeLeftStr = TIME_LEFT_FORMATTER.format(timeLeft);
/*     */     
/* 142 */     int textWidth = graphics.getFontMetrics().stringWidth(timeLeftStr);
/* 143 */     int textHeight = graphics.getFontMetrics().getAscent();
/*     */ 
/*     */     
/* 146 */     Point canvasPoint = Perspective.localToCanvas(this.client, centerLp, respawnLocation.getPlane());
/*     */     
/* 148 */     if (canvasPoint != null) {
/*     */ 
/*     */ 
/*     */       
/* 152 */       Point canvasCenterPoint = new Point(canvasPoint.getX() - textWidth / 2, canvasPoint.getY() + textHeight / 2);
/*     */       
/* 154 */       OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, timeLeftStr, TEXT_COLOR);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderNpcOverlay(Graphics2D graphics, NPC actor, Color color) {
/* 160 */     NPCComposition npcComposition = actor.getTransformedComposition();
/* 161 */     if (npcComposition == null || !npcComposition.isInteractible() || (actor
/* 162 */       .isDead() && this.config.ignoreDeadNpcs())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 167 */     if (this.config.solidSquare() > 0) {
/* 168 */       SquareOverlay.drawCenterSquare(graphics, (Actor)actor, this.config.solidSquare(), color);
/*     */       
/*     */       return;
/*     */     } 
/* 172 */     if (this.config.highlightHull()) {
/*     */       
/* 174 */       Shape objectClickbox = actor.getConvexHull();
/* 175 */       renderPoly(graphics, color, objectClickbox);
/*     */     } 
/*     */     
/* 178 */     if (this.config.highlightTile()) {
/*     */       
/* 180 */       int size = npcComposition.getSize();
/* 181 */       LocalPoint lp = actor.getLocalLocation();
/* 182 */       Polygon tilePoly = Perspective.getCanvasTileAreaPoly(this.client, lp, size);
/*     */       
/* 184 */       renderPoly(graphics, color, tilePoly);
/*     */     } 
/*     */     
/* 187 */     if (this.config.highlightSouthWestTile()) {
/*     */       
/* 189 */       int size = npcComposition.getSize();
/* 190 */       LocalPoint lp = actor.getLocalLocation();
/*     */       
/* 192 */       int x = lp.getX() - (size - 1) * 128 / 2;
/* 193 */       int y = lp.getY() - (size - 1) * 128 / 2;
/*     */       
/* 195 */       Polygon southWestTilePoly = Perspective.getCanvasTilePoly(this.client, new LocalPoint(x, y));
/*     */       
/* 197 */       renderPoly(graphics, color, southWestTilePoly);
/*     */     } 
/*     */     
/* 200 */     if (this.config.drawNames() && actor.getName() != null) {
/*     */       
/* 202 */       String npcName = Text.removeTags(actor.getName());
/* 203 */       Point textLocation = actor.getCanvasTextLocation(graphics, npcName, actor.getLogicalHeight() + 40);
/*     */       
/* 205 */       if (textLocation != null)
/*     */       {
/* 207 */         OverlayUtil.renderTextLocation(graphics, textLocation, npcName, color);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderPoly(Graphics2D graphics, Color color, Shape polygon) {
/* 214 */     if (polygon != null) {
/*     */       
/* 216 */       graphics.setColor(color);
/* 217 */       graphics.setStroke(new BasicStroke(2.0F));
/* 218 */       graphics.draw(polygon);
/* 219 */       graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
/* 220 */       graphics.fill(polygon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npchighlight\NpcSceneOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */