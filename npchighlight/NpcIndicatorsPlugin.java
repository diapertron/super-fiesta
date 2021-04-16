/*     */ package net.runelite.client.plugins.npchighlight;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Provides;
/*     */ import java.awt.Color;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.GameState;
/*     */ import net.runelite.api.GraphicsObject;
/*     */ import net.runelite.api.MenuAction;
/*     */ import net.runelite.api.MenuEntry;
/*     */ import net.runelite.api.NPC;
/*     */ import net.runelite.api.coords.WorldPoint;
/*     */ import net.runelite.api.events.GameStateChanged;
/*     */ import net.runelite.api.events.GameTick;
/*     */ import net.runelite.api.events.GraphicsObjectCreated;
/*     */ import net.runelite.api.events.MenuEntryAdded;
/*     */ import net.runelite.api.events.MenuOptionClicked;
/*     */ import net.runelite.api.events.NpcDespawned;
/*     */ import net.runelite.api.events.NpcSpawned;
/*     */ import net.runelite.client.callback.ClientThread;
/*     */ import net.runelite.client.config.ConfigManager;
/*     */ import net.runelite.client.eventbus.Subscribe;
/*     */ import net.runelite.client.events.ConfigChanged;
/*     */ import net.runelite.client.input.KeyManager;
/*     */ import net.runelite.client.plugins.Plugin;
/*     */ import net.runelite.client.plugins.PluginDescriptor;
/*     */ import net.runelite.client.ui.overlay.OverlayManager;
/*     */ import net.runelite.client.util.ColorUtil;
/*     */ import net.runelite.client.util.Text;
/*     */ import net.runelite.client.util.WildcardMatcher;
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
/*     */ @PluginDescriptor(name = "NPC Indicators", description = "Highlight NPCs on-screen and/or on the minimap", tags = {"highlight", "minimap", "npcs", "overlay", "respawn", "tags"})
/*     */ public class NpcIndicatorsPlugin
/*     */   extends Plugin
/*     */ {
/*  62 */   private static final Logger log = LoggerFactory.getLogger(NpcIndicatorsPlugin.class);
/*     */   
/*     */   private static final int MAX_ACTOR_VIEW_RANGE = 15;
/*     */   
/*     */   private static final String TAG = "Tag";
/*     */   
/*     */   private static final String UNTAG = "Un-tag";
/*     */   
/*     */   private static final String TAG_ALL = "Tag-All";
/*     */   
/*     */   private static final String UNTAG_ALL = "Un-tag-All";
/*     */   
/*  74 */   private static final Set<MenuAction> NPC_MENU_ACTIONS = (Set<MenuAction>)ImmutableSet.of(MenuAction.NPC_FIRST_OPTION, MenuAction.NPC_SECOND_OPTION, MenuAction.NPC_THIRD_OPTION, MenuAction.NPC_FOURTH_OPTION, MenuAction.NPC_FIFTH_OPTION, MenuAction.SPELL_CAST_ON_NPC, (Object[])new MenuAction[] { MenuAction.ITEM_USE_ON_NPC });
/*     */ 
/*     */   
/*     */   @Inject
/*     */   private Client client;
/*     */ 
/*     */   
/*     */   @Inject
/*     */   private NpcIndicatorsConfig config;
/*     */ 
/*     */   
/*     */   @Inject
/*     */   private OverlayManager overlayManager;
/*     */ 
/*     */   
/*     */   @Inject
/*     */   private NpcSceneOverlay npcSceneOverlay;
/*     */ 
/*     */   
/*     */   @Inject
/*     */   private NpcMinimapOverlay npcMinimapOverlay;
/*     */   
/*     */   @Inject
/*     */   private KeyManager keyManager;
/*     */   
/*     */   @Inject
/*     */   private ClientThread clientThread;
/*     */   
/* 102 */   private final Set<NPC> highlightedNpcs = new HashSet<>(); Set<NPC> getHighlightedNpcs() { return this.highlightedNpcs; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private final Map<Integer, MemorizedNpc> deadNpcsToDisplay = new HashMap<>(); Map<Integer, MemorizedNpc> getDeadNpcsToDisplay() { return this.deadNpcsToDisplay; }
/*     */ 
/*     */   
/*     */   private Instant lastTickUpdate;
/*     */   
/*     */   Instant getLastTickUpdate() {
/* 114 */     return this.lastTickUpdate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   private final Map<Integer, MemorizedNpc> memorizedNpcs = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   private List<String> highlights = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   private final Set<Integer> npcTags = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private final List<NPC> spawnedNpcsThisTick = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private final List<NPC> despawnedNpcsThisTick = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   private final Set<WorldPoint> teleportGraphicsObjectSpawnedThisTick = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WorldPoint lastPlayerLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean skipNextSpawnCheck = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Provides
/*     */   NpcIndicatorsConfig provideConfig(ConfigManager configManager) {
/* 165 */     return (NpcIndicatorsConfig)configManager.getConfig(NpcIndicatorsConfig.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startUp() throws Exception {
/* 171 */     this.overlayManager.add(this.npcSceneOverlay);
/* 172 */     this.overlayManager.add(this.npcMinimapOverlay);
/* 173 */     this.clientThread.invoke(() -> {
/*     */           this.skipNextSpawnCheck = true;
/*     */           rebuildAllNpcs();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shutDown() throws Exception {
/* 183 */     this.overlayManager.remove(this.npcSceneOverlay);
/* 184 */     this.overlayManager.remove(this.npcMinimapOverlay);
/* 185 */     this.clientThread.invoke(() -> {
/*     */           this.deadNpcsToDisplay.clear();
/*     */           this.memorizedNpcs.clear();
/*     */           this.spawnedNpcsThisTick.clear();
/*     */           this.despawnedNpcsThisTick.clear();
/*     */           this.teleportGraphicsObjectSpawnedThisTick.clear();
/*     */           this.npcTags.clear();
/*     */           this.highlightedNpcs.clear();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGameStateChanged(GameStateChanged event) {
/* 200 */     if (event.getGameState() == GameState.LOGIN_SCREEN || event
/* 201 */       .getGameState() == GameState.HOPPING) {
/*     */       
/* 203 */       this.highlightedNpcs.clear();
/* 204 */       this.deadNpcsToDisplay.clear();
/* 205 */       this.memorizedNpcs.forEach((id, npc) -> npc.setDiedOnTick(-1));
/* 206 */       this.lastPlayerLocation = null;
/* 207 */       this.skipNextSpawnCheck = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onConfigChanged(ConfigChanged configChanged) {
/* 214 */     if (!configChanged.getGroup().equals("npcindicators")) {
/*     */       return;
/*     */     }
/*     */     
/* 218 */     this.npcSceneOverlay.setLayer(this.config.overlayLayer());
/*     */     
/* 220 */     this.clientThread.invoke(this::rebuildAllNpcs);
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onMenuEntryAdded(MenuEntryAdded event) {
/* 226 */     int type = event.getType();
/*     */     
/* 228 */     if (type >= 2000)
/*     */     {
/* 230 */       type -= 2000;
/*     */     }
/*     */     
/* 233 */     MenuAction menuAction = MenuAction.of(type);
/*     */     
/* 235 */     if (NPC_MENU_ACTIONS.contains(menuAction)) {
/*     */       
/* 237 */       NPC npc = this.client.getCachedNPCs()[event.getIdentifier()];
/*     */       
/* 239 */       Color color = null;
/* 240 */       if (npc.isDead())
/*     */       {
/* 242 */         color = this.config.deadNpcMenuColor();
/*     */       }
/*     */       
/* 245 */       if (color == null && this.highlightedNpcs.contains(npc) && this.config.highlightMenuNames() && (!npc.isDead() || !this.config.ignoreDeadNpcs()))
/*     */       {
/* 247 */         color = this.config.getHighlightColor();
/*     */       }
/*     */       
/* 250 */       if (color != null)
/*     */       {
/* 252 */         MenuEntry[] menuEntries = this.client.getMenuEntries();
/* 253 */         MenuEntry menuEntry = menuEntries[menuEntries.length - 1];
/* 254 */         String target = ColorUtil.prependColorTag(Text.removeTags(event.getTarget()), color);
/* 255 */         menuEntry.setTarget(target);
/* 256 */         this.client.setMenuEntries(menuEntries);
/*     */       }
/*     */     
/* 259 */     } else if (menuAction == MenuAction.EXAMINE_NPC && this.client.isKeyPressed(81)) {
/*     */ 
/*     */       
/* 262 */       int id = event.getIdentifier();
/* 263 */       NPC[] cachedNPCs = this.client.getCachedNPCs();
/* 264 */       NPC npc = cachedNPCs[id];
/*     */       
/* 266 */       if (npc == null || npc.getName() == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 271 */       String npcName = npc.getName();
/*     */ 
/*     */       
/* 274 */       boolean matchesList = this.highlights.stream().filter(highlight -> !highlight.equalsIgnoreCase(npcName)).anyMatch(highlight -> WildcardMatcher.matches(highlight, npcName));
/*     */       
/* 276 */       MenuEntry[] menuEntries = this.client.getMenuEntries();
/*     */ 
/*     */       
/* 279 */       if (!matchesList) {
/*     */         
/* 281 */         menuEntries = Arrays.<MenuEntry>copyOf(menuEntries, menuEntries.length + 2);
/* 282 */         MenuEntry tagAllEntry = menuEntries[menuEntries.length - 2] = new MenuEntry();
/* 283 */         Objects.requireNonNull(npcName); tagAllEntry.setOption(this.highlights.stream().anyMatch(npcName::equalsIgnoreCase) ? "Un-tag-All" : "Tag-All");
/* 284 */         tagAllEntry.setTarget(event.getTarget());
/* 285 */         tagAllEntry.setParam0(event.getActionParam0());
/* 286 */         tagAllEntry.setParam1(event.getActionParam1());
/* 287 */         tagAllEntry.setIdentifier(event.getIdentifier());
/* 288 */         tagAllEntry.setType(MenuAction.RUNELITE.getId());
/*     */       }
/*     */       else {
/*     */         
/* 292 */         menuEntries = Arrays.<MenuEntry>copyOf(menuEntries, menuEntries.length + 1);
/*     */       } 
/*     */       
/* 295 */       MenuEntry tagEntry = menuEntries[menuEntries.length - 1] = new MenuEntry();
/* 296 */       tagEntry.setOption(this.npcTags.contains(Integer.valueOf(npc.getIndex())) ? "Un-tag" : "Tag");
/* 297 */       tagEntry.setTarget(event.getTarget());
/* 298 */       tagEntry.setParam0(event.getActionParam0());
/* 299 */       tagEntry.setParam1(event.getActionParam1());
/* 300 */       tagEntry.setIdentifier(event.getIdentifier());
/* 301 */       tagEntry.setType(MenuAction.RUNELITE.getId());
/*     */       
/* 303 */       this.client.setMenuEntries(menuEntries);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onMenuOptionClicked(MenuOptionClicked click) {
/* 310 */     if (click.getMenuAction() != MenuAction.RUNELITE || (
/* 311 */       !click.getMenuOption().equals("Tag") && !click.getMenuOption().equals("Un-tag") && 
/* 312 */       !click.getMenuOption().equals("Tag-All") && !click.getMenuOption().equals("Un-tag-All"))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 317 */     int id = click.getId();
/* 318 */     NPC[] cachedNPCs = this.client.getCachedNPCs();
/* 319 */     NPC npc = cachedNPCs[id];
/*     */     
/* 321 */     if (npc == null || npc.getName() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 326 */     if (click.getMenuOption().equals("Tag") || click.getMenuOption().equals("Un-tag")) {
/*     */       
/* 328 */       boolean removed = this.npcTags.remove(Integer.valueOf(id));
/*     */       
/* 330 */       if (removed) {
/*     */         
/* 332 */         if (!highlightMatchesNPCName(npc.getName()))
/*     */         {
/* 334 */           this.highlightedNpcs.remove(npc);
/* 335 */           this.memorizedNpcs.remove(Integer.valueOf(npc.getIndex()));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 340 */         if (!this.client.isInInstancedRegion()) {
/*     */           
/* 342 */           memorizeNpc(npc);
/* 343 */           this.npcTags.add(Integer.valueOf(id));
/*     */         } 
/* 345 */         this.highlightedNpcs.add(npc);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 350 */       String name = npc.getName();
/* 351 */       updateNpcsToHighlight(name);
/*     */     } 
/*     */     
/* 354 */     click.consume();
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onNpcSpawned(NpcSpawned npcSpawned) {
/* 360 */     NPC npc = npcSpawned.getNpc();
/* 361 */     String npcName = npc.getName();
/*     */     
/* 363 */     if (npcName == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 368 */     if (this.npcTags.contains(Integer.valueOf(npc.getIndex()))) {
/*     */       
/* 370 */       memorizeNpc(npc);
/* 371 */       this.highlightedNpcs.add(npc);
/* 372 */       this.spawnedNpcsThisTick.add(npc);
/*     */       
/*     */       return;
/*     */     } 
/* 376 */     if (highlightMatchesNPCName(npcName)) {
/*     */       
/* 378 */       this.highlightedNpcs.add(npc);
/* 379 */       if (!this.client.isInInstancedRegion()) {
/*     */         
/* 381 */         memorizeNpc(npc);
/* 382 */         this.spawnedNpcsThisTick.add(npc);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onNpcDespawned(NpcDespawned npcDespawned) {
/* 390 */     NPC npc = npcDespawned.getNpc();
/*     */     
/* 392 */     if (this.memorizedNpcs.containsKey(Integer.valueOf(npc.getIndex())))
/*     */     {
/* 394 */       this.despawnedNpcsThisTick.add(npc);
/*     */     }
/*     */     
/* 397 */     this.highlightedNpcs.remove(npc);
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGraphicsObjectCreated(GraphicsObjectCreated event) {
/* 403 */     GraphicsObject go = event.getGraphicsObject();
/*     */     
/* 405 */     if (go.getId() == 86)
/*     */     {
/* 407 */       this.teleportGraphicsObjectSpawnedThisTick.add(WorldPoint.fromLocal(this.client, go.getLocation()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGameTick(GameTick event) {
/* 414 */     removeOldHighlightedRespawns();
/* 415 */     validateSpawnedNpcs();
/* 416 */     this.lastTickUpdate = Instant.now();
/* 417 */     this.lastPlayerLocation = this.client.getLocalPlayer().getWorldLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNpcsToHighlight(String npc) {
/* 422 */     List<String> highlightedNpcs = new ArrayList<>(this.highlights);
/*     */     
/* 424 */     Objects.requireNonNull(npc); if (!highlightedNpcs.removeIf(npc::equalsIgnoreCase))
/*     */     {
/* 426 */       highlightedNpcs.add(npc);
/*     */     }
/*     */ 
/*     */     
/* 430 */     this.config.setNpcToHighlight(Text.toCSV(highlightedNpcs));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isInViewRange(WorldPoint wp1, WorldPoint wp2) {
/* 435 */     int distance = wp1.distanceTo(wp2);
/* 436 */     return (distance < 15);
/*     */   }
/*     */ 
/*     */   
/*     */   private static WorldPoint getWorldLocationBehind(NPC npc) {
/* 441 */     int orientation = npc.getOrientation() / 256;
/* 442 */     int dx = 0, dy = 0;
/*     */     
/* 444 */     switch (orientation) {
/*     */       
/*     */       case 0:
/* 447 */         dy = -1;
/*     */         break;
/*     */       case 1:
/* 450 */         dx = -1;
/* 451 */         dy = -1;
/*     */         break;
/*     */       case 2:
/* 454 */         dx = -1;
/*     */         break;
/*     */       case 3:
/* 457 */         dx = -1;
/* 458 */         dy = 1;
/*     */         break;
/*     */       case 4:
/* 461 */         dy = 1;
/*     */         break;
/*     */       case 5:
/* 464 */         dx = 1;
/* 465 */         dy = 1;
/*     */         break;
/*     */       case 6:
/* 468 */         dx = 1;
/*     */         break;
/*     */       case 7:
/* 471 */         dx = 1;
/* 472 */         dy = -1;
/*     */         break;
/*     */     } 
/*     */     
/* 476 */     WorldPoint currWP = npc.getWorldLocation();
/* 477 */     return new WorldPoint(currWP.getX() - dx, currWP.getY() - dy, currWP.getPlane());
/*     */   }
/*     */ 
/*     */   
/*     */   private void memorizeNpc(NPC npc) {
/* 482 */     int npcIndex = npc.getIndex();
/* 483 */     this.memorizedNpcs.putIfAbsent(Integer.valueOf(npcIndex), new MemorizedNpc(npc));
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeOldHighlightedRespawns() {
/* 488 */     this.deadNpcsToDisplay.values().removeIf(x -> (x.getDiedOnTick() + x.getRespawnTime() <= this.client.getTickCount() + 1));
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   List<String> getHighlights() {
/* 494 */     String configNpcs = this.config.getNpcToHighlight();
/*     */     
/* 496 */     if (configNpcs.isEmpty())
/*     */     {
/* 498 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 501 */     return Text.fromCSV(configNpcs);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   void rebuildAllNpcs() {
/* 507 */     this.highlights = getHighlights();
/* 508 */     this.highlightedNpcs.clear();
/*     */     
/* 510 */     if (this.client.getGameState() != GameState.LOGGED_IN && this.client
/* 511 */       .getGameState() != GameState.LOADING) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 518 */     for (NPC npc : this.client.getNpcs()) {
/*     */       
/* 520 */       String npcName = npc.getName();
/*     */       
/* 522 */       if (npcName == null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 527 */       if (this.npcTags.contains(Integer.valueOf(npc.getIndex()))) {
/*     */         
/* 529 */         this.highlightedNpcs.add(npc);
/*     */         
/*     */         continue;
/*     */       } 
/* 533 */       if (highlightMatchesNPCName(npcName)) {
/*     */         
/* 535 */         if (!this.client.isInInstancedRegion())
/*     */         {
/* 537 */           memorizeNpc(npc);
/*     */         }
/* 539 */         this.highlightedNpcs.add(npc);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 544 */       this.memorizedNpcs.remove(Integer.valueOf(npc.getIndex()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean highlightMatchesNPCName(String npcName) {
/* 550 */     for (String highlight : this.highlights) {
/*     */       
/* 552 */       if (WildcardMatcher.matches(highlight, npcName))
/*     */       {
/* 554 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 558 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void validateSpawnedNpcs() {
/* 563 */     if (this.skipNextSpawnCheck) {
/*     */       
/* 565 */       this.skipNextSpawnCheck = false;
/*     */     }
/*     */     else {
/*     */       
/* 569 */       for (NPC npc : this.despawnedNpcsThisTick) {
/*     */         
/* 571 */         if (!this.teleportGraphicsObjectSpawnedThisTick.isEmpty())
/*     */         {
/* 573 */           if (this.teleportGraphicsObjectSpawnedThisTick.contains(npc.getWorldLocation())) {
/*     */             continue;
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 580 */         if (isInViewRange(this.client.getLocalPlayer().getWorldLocation(), npc.getWorldLocation())) {
/*     */           
/* 582 */           MemorizedNpc mn = this.memorizedNpcs.get(Integer.valueOf(npc.getIndex()));
/*     */           
/* 584 */           if (mn != null) {
/*     */             
/* 586 */             mn.setDiedOnTick(this.client.getTickCount() + 1);
/*     */             
/* 588 */             if (!mn.getPossibleRespawnLocations().isEmpty()) {
/*     */               
/* 590 */               log.debug("Starting {} tick countdown for {}", Integer.valueOf(mn.getRespawnTime()), mn.getNpcName());
/* 591 */               this.deadNpcsToDisplay.put(Integer.valueOf(mn.getNpcIndex()), mn);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 597 */       for (Iterator<NPC> iterator = this.spawnedNpcsThisTick.iterator(); iterator.hasNext(); ) { NPC npc = iterator.next();
/*     */         
/* 599 */         if (!this.teleportGraphicsObjectSpawnedThisTick.isEmpty())
/*     */         {
/* 601 */           if (this.teleportGraphicsObjectSpawnedThisTick.contains(npc.getWorldLocation()) || this.teleportGraphicsObjectSpawnedThisTick
/* 602 */             .contains(getWorldLocationBehind(npc))) {
/*     */             continue;
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 609 */         if (this.lastPlayerLocation != null && isInViewRange(this.lastPlayerLocation, npc.getWorldLocation())) {
/*     */           
/* 611 */           MemorizedNpc mn = this.memorizedNpcs.get(Integer.valueOf(npc.getIndex()));
/*     */           
/* 613 */           if (mn.getDiedOnTick() != -1) {
/*     */             
/* 615 */             int respawnTime = this.client.getTickCount() + 1 - mn.getDiedOnTick();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 620 */             if (mn.getRespawnTime() == -1 || respawnTime < mn.getRespawnTime())
/*     */             {
/* 622 */               mn.setRespawnTime(respawnTime);
/*     */             }
/*     */             
/* 625 */             mn.setDiedOnTick(-1);
/*     */           } 
/*     */           
/* 628 */           WorldPoint npcLocation = npc.getWorldLocation();
/*     */ 
/*     */ 
/*     */           
/* 632 */           WorldPoint possibleOtherNpcLocation = getWorldLocationBehind(npc);
/*     */           
/* 634 */           mn.getPossibleRespawnLocations().removeIf(x -> 
/* 635 */               (x.distanceTo(npcLocation) != 0 && x.distanceTo(possibleOtherNpcLocation) != 0));
/*     */           
/* 637 */           if (mn.getPossibleRespawnLocations().isEmpty()) {
/*     */             
/* 639 */             mn.getPossibleRespawnLocations().add(npcLocation);
/* 640 */             mn.getPossibleRespawnLocations().add(possibleOtherNpcLocation);
/*     */           } 
/*     */         }  }
/*     */     
/*     */     } 
/*     */     
/* 646 */     this.spawnedNpcsThisTick.clear();
/* 647 */     this.despawnedNpcsThisTick.clear();
/* 648 */     this.teleportGraphicsObjectSpawnedThisTick.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\npchighlight\NpcIndicatorsPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */