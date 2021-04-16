/*     */ package net.runelite.client.plugins.npcunaggroarea;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.inject.Provides;
/*     */ import java.awt.Polygon;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.GameState;
/*     */ import net.runelite.api.NPC;
/*     */ import net.runelite.api.NPCComposition;
/*     */ import net.runelite.api.coords.LocalPoint;
/*     */ import net.runelite.api.coords.WorldArea;
/*     */ import net.runelite.api.coords.WorldPoint;
/*     */ import net.runelite.api.events.GameStateChanged;
/*     */ import net.runelite.api.events.GameTick;
/*     */ import net.runelite.api.events.NpcSpawned;
/*     */ import net.runelite.api.geometry.Geometry;
/*     */ import net.runelite.client.Notifier;
/*     */ import net.runelite.client.config.ConfigManager;
/*     */ import net.runelite.client.eventbus.Subscribe;
/*     */ import net.runelite.client.events.ConfigChanged;
/*     */ import net.runelite.client.game.ItemManager;
/*     */ import net.runelite.client.plugins.Plugin;
/*     */ import net.runelite.client.plugins.PluginDescriptor;
/*     */ import net.runelite.client.ui.overlay.Overlay;
/*     */ import net.runelite.client.ui.overlay.OverlayManager;
/*     */ import net.runelite.client.ui.overlay.infobox.InfoBox;
/*     */ import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
/*     */ import net.runelite.client.util.AsyncBufferedImage;
/*     */ import net.runelite.client.util.WildcardMatcher;
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
/*     */ 
/*     */ 
/*     */ @PluginDescriptor(name = "NPC Aggression Timer", description = "Highlights the unaggressive area of NPCs nearby and timer until it becomes active", tags = {"highlight", "lines", "unaggro", "aggro", "aggressive", "npcs", "area", "slayer"}, enabledByDefault = false)
/*     */ public class NpcAggroAreaPlugin
/*     */   extends Plugin
/*     */ {
/*     */   private static final int SAFE_AREA_RADIUS = 10;
/*     */   private static final int UNKNOWN_AREA_RADIUS = 20;
/*     */   private static final int AGGRESSIVE_TIME_SECONDS = 600;
/*  86 */   private static final Splitter NAME_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
/*  87 */   private static final WorldArea WILDERNESS_ABOVE_GROUND = new WorldArea(2944, 3523, 448, 448, 0);
/*  88 */   private static final WorldArea WILDERNESS_UNDERGROUND = new WorldArea(2944, 9918, 320, 442, 0);
/*     */   
/*     */   @Inject
/*     */   private Client client;
/*     */   
/*     */   @Inject
/*     */   private NpcAggroAreaConfig config;
/*     */   
/*     */   @Inject
/*     */   private NpcAggroAreaOverlay overlay;
/*     */   
/*     */   @Inject
/*     */   private NpcAggroAreaNotWorkingOverlay notWorkingOverlay;
/*     */   
/*     */   @Inject
/*     */   private OverlayManager overlayManager;
/*     */   
/*     */   @Inject
/*     */   private ItemManager itemManager;
/*     */   
/*     */   @Inject
/*     */   private InfoBoxManager infoBoxManager;
/*     */   
/*     */   @Inject
/*     */   private ConfigManager configManager;
/*     */   
/*     */   @Inject
/*     */   private Notifier notifier;
/*     */   
/* 117 */   private final WorldPoint[] safeCenters = new WorldPoint[2]; public WorldPoint[] getSafeCenters() { return this.safeCenters; }
/*     */   
/*     */   private boolean active; private AggressionTimer currentTimer; private WorldPoint lastPlayerLocation; private WorldPoint previousUnknownCenter;
/* 120 */   private final GeneralPath[] linesToDisplay = new GeneralPath[4]; private boolean loggingIn; private boolean notifyOnce; private List<String> npcNamePatterns; public GeneralPath[] getLinesToDisplay() { return this.linesToDisplay; }
/*     */   
/*     */   public boolean isActive() {
/* 123 */     return this.active;
/*     */   }
/*     */   public AggressionTimer getCurrentTimer() {
/* 126 */     return this.currentTimer;
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
/*     */   @Provides
/*     */   NpcAggroAreaConfig provideConfig(ConfigManager configManager) {
/* 139 */     return (NpcAggroAreaConfig)configManager.getConfig(NpcAggroAreaConfig.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startUp() throws Exception {
/* 145 */     this.overlayManager.add(this.overlay);
/* 146 */     this.overlayManager.add((Overlay)this.notWorkingOverlay);
/* 147 */     this.npcNamePatterns = NAME_SPLITTER.splitToList(this.config.npcNamePatterns());
/* 148 */     recheckActive();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shutDown() throws Exception {
/* 154 */     removeTimer();
/* 155 */     this.overlayManager.remove(this.overlay);
/* 156 */     this.overlayManager.remove((Overlay)this.notWorkingOverlay);
/* 157 */     Arrays.fill((Object[])this.safeCenters, (Object)null);
/* 158 */     this.lastPlayerLocation = null;
/* 159 */     this.currentTimer = null;
/* 160 */     this.loggingIn = false;
/* 161 */     this.npcNamePatterns = null;
/* 162 */     this.active = false;
/*     */     
/* 164 */     Arrays.fill((Object[])this.linesToDisplay, (Object)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private Area generateSafeArea() {
/* 169 */     Area area = new Area();
/*     */     
/* 171 */     for (WorldPoint wp : this.safeCenters) {
/*     */       
/* 173 */       if (wp != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 178 */         Polygon poly = new Polygon();
/* 179 */         poly.addPoint(wp.getX() - 10, wp.getY() - 10);
/* 180 */         poly.addPoint(wp.getX() - 10, wp.getY() + 10 + 1);
/* 181 */         poly.addPoint(wp.getX() + 10 + 1, wp.getY() + 10 + 1);
/* 182 */         poly.addPoint(wp.getX() + 10 + 1, wp.getY() - 10);
/* 183 */         area.add(new Area(poly));
/*     */       } 
/*     */     } 
/* 186 */     return area;
/*     */   }
/*     */ 
/*     */   
/*     */   private void transformWorldToLocal(float[] coords) {
/* 191 */     LocalPoint lp = LocalPoint.fromWorld(this.client, (int)coords[0], (int)coords[1]);
/* 192 */     coords[0] = lp.getX() - 64.0F;
/* 193 */     coords[1] = lp.getY() - 64.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private void reevaluateActive() {
/* 198 */     if (this.currentTimer != null)
/*     */     {
/* 200 */       this.currentTimer.setVisible((this.active && this.config.showTimer()));
/*     */     }
/*     */     
/* 203 */     calculateLinesToDisplay();
/*     */   }
/*     */ 
/*     */   
/*     */   private void calculateLinesToDisplay() {
/* 208 */     if (!this.active || !this.config.showAreaLines()) {
/*     */       
/* 210 */       Arrays.fill((Object[])this.linesToDisplay, (Object)null);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 215 */     Rectangle sceneRect = new Rectangle(this.client.getBaseX() + 1, this.client.getBaseY() + 1, 102, 102);
/*     */ 
/*     */     
/* 218 */     for (int i = 0; i < this.linesToDisplay.length; i++) {
/*     */       
/* 220 */       GeneralPath lines = new GeneralPath(generateSafeArea());
/* 221 */       lines = Geometry.clipPath(lines, sceneRect);
/* 222 */       lines = Geometry.splitIntoSegments(lines, 1.0F);
/* 223 */       lines = Geometry.transformPath(lines, this::transformWorldToLocal);
/* 224 */       this.linesToDisplay[i] = lines;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeTimer() {
/* 230 */     this.infoBoxManager.removeInfoBox((InfoBox)this.currentTimer);
/* 231 */     this.currentTimer = null;
/* 232 */     this.notifyOnce = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void createTimer(Duration duration) {
/* 237 */     removeTimer();
/* 238 */     AsyncBufferedImage asyncBufferedImage = this.itemManager.getImage(13501);
/* 239 */     this.currentTimer = new AggressionTimer(duration, (BufferedImage)asyncBufferedImage, this, (this.active && this.config.showTimer()));
/* 240 */     this.infoBoxManager.addInfoBox((InfoBox)this.currentTimer);
/* 241 */     this.notifyOnce = true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetTimer() {
/* 246 */     createTimer(Duration.ofSeconds(600L));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isInWilderness(WorldPoint location) {
/* 251 */     return (WILDERNESS_ABOVE_GROUND.distanceTo2D(location) == 0 || WILDERNESS_UNDERGROUND.distanceTo2D(location) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isNpcMatch(NPC npc) {
/* 256 */     NPCComposition composition = npc.getTransformedComposition();
/* 257 */     if (composition == null)
/*     */     {
/* 259 */       return false;
/*     */     }
/*     */     
/* 262 */     if (Strings.isNullOrEmpty(composition.getName()))
/*     */     {
/* 264 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 269 */     int playerLvl = this.client.getLocalPlayer().getCombatLevel();
/* 270 */     int npcLvl = composition.getCombatLevel();
/* 271 */     String npcName = composition.getName().toLowerCase();
/* 272 */     if (npcLvl > 0 && playerLvl > npcLvl * 2 && !isInWilderness(npc.getWorldLocation()))
/*     */     {
/* 274 */       return false;
/*     */     }
/*     */     
/* 277 */     for (String pattern : this.npcNamePatterns) {
/*     */       
/* 279 */       if (WildcardMatcher.matches(pattern, npcName))
/*     */       {
/* 281 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 285 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkAreaNpcs(NPC... npcs) {
/* 290 */     for (NPC npc : npcs) {
/*     */       
/* 292 */       if (npc != null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 297 */         if (isNpcMatch(npc)) {
/*     */           
/* 299 */           this.active = true;
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/* 304 */     reevaluateActive();
/*     */   }
/*     */ 
/*     */   
/*     */   private void recheckActive() {
/* 309 */     this.active = this.config.alwaysActive();
/* 310 */     checkAreaNpcs(this.client.getCachedNPCs());
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onNpcSpawned(NpcSpawned event) {
/* 316 */     if (this.config.alwaysActive()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 321 */     checkAreaNpcs(new NPC[] { event.getNpc() });
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGameTick(GameTick event) {
/* 327 */     WorldPoint newLocation = this.client.getLocalPlayer().getWorldLocation();
/*     */     
/* 329 */     if (this.active && this.currentTimer != null && this.currentTimer.cull() && this.notifyOnce) {
/*     */       
/* 331 */       if (this.config.notifyExpire())
/*     */       {
/* 333 */         this.notifier.notify("NPC aggression has expired!");
/*     */       }
/*     */       
/* 336 */       this.notifyOnce = false;
/*     */     } 
/*     */     
/* 339 */     if (this.lastPlayerLocation != null)
/*     */     {
/* 341 */       if (this.safeCenters[1] == null && newLocation.distanceTo2D(this.lastPlayerLocation) > 40) {
/*     */         
/* 343 */         this.safeCenters[0] = null;
/* 344 */         this.safeCenters[1] = newLocation;
/* 345 */         resetTimer();
/* 346 */         calculateLinesToDisplay();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 351 */         this.previousUnknownCenter = this.lastPlayerLocation;
/*     */       } 
/*     */     }
/*     */     
/* 355 */     if (this.safeCenters[0] == null && this.previousUnknownCenter != null && this.previousUnknownCenter
/* 356 */       .distanceTo2D(newLocation) <= 20) {
/*     */ 
/*     */ 
/*     */       
/* 360 */       this.safeCenters[1] = null;
/* 361 */       removeTimer();
/* 362 */       calculateLinesToDisplay();
/*     */     } 
/*     */     
/* 365 */     if (this.safeCenters[1] != null)
/*     */     {
/* 367 */       if (Arrays.<WorldPoint>stream(this.safeCenters).noneMatch(x -> 
/* 368 */           (x != null && x.distanceTo2D(newLocation) <= 10))) {
/*     */         
/* 370 */         this.safeCenters[0] = this.safeCenters[1];
/* 371 */         this.safeCenters[1] = newLocation;
/* 372 */         resetTimer();
/* 373 */         calculateLinesToDisplay();
/* 374 */         this.previousUnknownCenter = null;
/*     */       } 
/*     */     }
/*     */     
/* 378 */     this.lastPlayerLocation = newLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onConfigChanged(ConfigChanged event) {
/* 384 */     String key = event.getKey();
/* 385 */     switch (key) {
/*     */       
/*     */       case "npcUnaggroAlwaysActive":
/* 388 */         recheckActive();
/*     */         break;
/*     */       case "npcUnaggroShowTimer":
/* 391 */         if (this.currentTimer != null)
/*     */         {
/* 393 */           this.currentTimer.setVisible((this.active && this.config.showTimer()));
/*     */         }
/*     */         break;
/*     */       case "npcUnaggroCollisionDetection":
/*     */       case "npcUnaggroShowAreaLines":
/* 398 */         calculateLinesToDisplay();
/*     */         break;
/*     */       case "npcUnaggroNames":
/* 401 */         this.npcNamePatterns = NAME_SPLITTER.splitToList(this.config.npcNamePatterns());
/* 402 */         recheckActive();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadConfig() {
/* 409 */     this.safeCenters[0] = (WorldPoint)this.configManager.getConfiguration("npcUnaggroArea", "center1", WorldPoint.class);
/* 410 */     this.safeCenters[1] = (WorldPoint)this.configManager.getConfiguration("npcUnaggroArea", "center2", WorldPoint.class);
/* 411 */     this.lastPlayerLocation = (WorldPoint)this.configManager.getConfiguration("npcUnaggroArea", "location", WorldPoint.class);
/*     */     
/* 413 */     Duration timeLeft = (Duration)this.configManager.getConfiguration("npcUnaggroArea", "duration", Duration.class);
/* 414 */     if (timeLeft != null && !timeLeft.isNegative())
/*     */     {
/* 416 */       createTimer(timeLeft);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetConfig() {
/* 422 */     this.configManager.unsetConfiguration("npcUnaggroArea", "center1");
/* 423 */     this.configManager.unsetConfiguration("npcUnaggroArea", "center2");
/* 424 */     this.configManager.unsetConfiguration("npcUnaggroArea", "location");
/* 425 */     this.configManager.unsetConfiguration("npcUnaggroArea", "duration");
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveConfig() {
/* 430 */     if (this.safeCenters[0] == null || this.safeCenters[1] == null || this.lastPlayerLocation == null || this.currentTimer == null) {
/*     */       
/* 432 */       resetConfig();
/*     */     }
/*     */     else {
/*     */       
/* 436 */       this.configManager.setConfiguration("npcUnaggroArea", "center1", this.safeCenters[0]);
/* 437 */       this.configManager.setConfiguration("npcUnaggroArea", "center2", this.safeCenters[1]);
/* 438 */       this.configManager.setConfiguration("npcUnaggroArea", "location", this.lastPlayerLocation);
/* 439 */       this.configManager.setConfiguration("npcUnaggroArea", "duration", Duration.between(Instant.now(), this.currentTimer.getEndTime()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void onLogin() {
/* 445 */     loadConfig();
/* 446 */     resetConfig();
/*     */     
/* 448 */     WorldPoint newLocation = this.client.getLocalPlayer().getWorldLocation();
/* 449 */     assert newLocation != null;
/*     */ 
/*     */ 
/*     */     
/* 453 */     if (this.lastPlayerLocation == null || newLocation.distanceTo(this.lastPlayerLocation) != 0) {
/*     */       
/* 455 */       this.safeCenters[0] = null;
/* 456 */       this.safeCenters[1] = null;
/* 457 */       this.lastPlayerLocation = newLocation;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGameStateChanged(GameStateChanged event) {
/* 464 */     switch (event.getGameState()) {
/*     */       
/*     */       case LOGGED_IN:
/* 467 */         if (this.loggingIn) {
/*     */           
/* 469 */           this.loggingIn = false;
/* 470 */           onLogin();
/*     */         } 
/*     */         
/* 473 */         recheckActive();
/*     */         break;
/*     */       
/*     */       case LOGGING_IN:
/* 477 */         this.loggingIn = true;
/*     */         break;
/*     */       
/*     */       case LOGIN_SCREEN:
/* 481 */         if (this.lastPlayerLocation != null)
/*     */         {
/* 483 */           saveConfig();
/*     */         }
/*     */         
/* 486 */         this.safeCenters[0] = null;
/* 487 */         this.safeCenters[1] = null;
/* 488 */         this.lastPlayerLocation = null;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npcunaggroarea\NpcAggroAreaPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */