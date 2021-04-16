/*     */ package net.runelite.client.plugins.objectindicators;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.inject.Provides;
/*     */ import java.awt.Color;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.inject.Inject;
/*     */ import net.runelite.api.Client;
/*     */ import net.runelite.api.DecorativeObject;
/*     */ import net.runelite.api.GameObject;
/*     */ import net.runelite.api.GameState;
/*     */ import net.runelite.api.GroundObject;
/*     */ import net.runelite.api.MenuAction;
/*     */ import net.runelite.api.MenuEntry;
/*     */ import net.runelite.api.ObjectComposition;
/*     */ import net.runelite.api.Player;
/*     */ import net.runelite.api.Scene;
/*     */ import net.runelite.api.Tile;
/*     */ import net.runelite.api.TileObject;
/*     */ import net.runelite.api.WallObject;
/*     */ import net.runelite.api.coords.WorldPoint;
/*     */ import net.runelite.api.events.DecorativeObjectDespawned;
/*     */ import net.runelite.api.events.DecorativeObjectSpawned;
/*     */ import net.runelite.api.events.FocusChanged;
/*     */ import net.runelite.api.events.GameObjectDespawned;
/*     */ import net.runelite.api.events.GameObjectSpawned;
/*     */ import net.runelite.api.events.GameStateChanged;
/*     */ import net.runelite.api.events.GameTick;
/*     */ import net.runelite.api.events.GroundObjectDespawned;
/*     */ import net.runelite.api.events.GroundObjectSpawned;
/*     */ import net.runelite.api.events.MenuEntryAdded;
/*     */ import net.runelite.api.events.MenuOptionClicked;
/*     */ import net.runelite.api.events.WallObjectChanged;
/*     */ import net.runelite.api.events.WallObjectDespawned;
/*     */ import net.runelite.api.events.WallObjectSpawned;
/*     */ import net.runelite.client.config.ConfigManager;
/*     */ import net.runelite.client.eventbus.Subscribe;
/*     */ import net.runelite.client.input.KeyListener;
/*     */ import net.runelite.client.input.KeyManager;
/*     */ import net.runelite.client.plugins.Plugin;
/*     */ import net.runelite.client.plugins.PluginDescriptor;
/*     */ import net.runelite.client.ui.overlay.OverlayManager;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ @PluginDescriptor(name = "Object Markers", description = "Enable marking of objects using the Shift key", tags = {"overlay", "objects", "mark", "marker", "nomscripts"}, enabledByDefault = false)
/*     */ public class ObjectIndicatorsPlugin
/*     */   extends Plugin implements KeyListener {
/*  59 */   private static final Logger log = LoggerFactory.getLogger(ObjectIndicatorsPlugin.class);
/*     */   
/*     */   private static final String CONFIG_GROUP = "objectindicators";
/*     */   
/*     */   private static final String MARK = "Mark object";
/*     */   
/*     */   private static final String UNMARK = "Unmark object";
/*  66 */   private final Gson GSON = new Gson();
/*  67 */   private final List<ColorTileObject> objects = new ArrayList<>(); List<ColorTileObject> getObjects() { return this.objects; }
/*     */   
/*  69 */   private final HashSet<Integer> addedIds = new HashSet<>(); HashSet<Integer> getAddedIds() { return this.addedIds; }
/*     */   
/*  71 */   private final Map<Integer, Set<ObjectPoint>> points = new HashMap<>();
/*     */   
/*     */   private boolean hotKeyPressed;
/*     */   
/*     */   @Inject
/*     */   private Client client;
/*     */   
/*     */   @Inject
/*     */   private ConfigManager configManager;
/*     */   
/*     */   @Inject
/*     */   private OverlayManager overlayManager;
/*     */   
/*     */   @Inject
/*     */   private ObjectIndicatorsOverlay overlay;
/*     */   
/*     */   @Inject
/*     */   private KeyManager keyManager;
/*     */   
/*     */   @Inject
/*     */   private ObjectIndicatorsConfig config;
/*     */   
/*     */   @Provides
/*     */   ObjectIndicatorsConfig provideConfig(ConfigManager configManager) {
/*  95 */     return (ObjectIndicatorsConfig)configManager.getConfig(ObjectIndicatorsConfig.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startUp() {
/* 101 */     this.overlayManager.add(this.overlay);
/* 102 */     this.keyManager.registerKeyListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shutDown() {
/* 108 */     this.overlayManager.remove(this.overlay);
/* 109 */     this.keyManager.unregisterKeyListener(this);
/* 110 */     this.points.clear();
/* 111 */     this.objects.clear();
/* 112 */     this.hotKeyPressed = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyTyped(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyPressed(KeyEvent e) {
/* 124 */     if (e.getKeyCode() == 16)
/*     */     {
/* 126 */       this.hotKeyPressed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyReleased(KeyEvent e) {
/* 133 */     if (e.getKeyCode() == 16)
/*     */     {
/* 135 */       this.hotKeyPressed = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onFocusChanged(FocusChanged event) {
/* 142 */     if (!event.isFocused())
/*     */     {
/* 144 */       this.hotKeyPressed = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGameStateChanged(GameStateChanged gameStateChanged) {
/* 151 */     GameState gameState = gameStateChanged.getGameState();
/* 152 */     if (gameState == GameState.LOADING)
/*     */     {
/* 154 */       reload();
/*     */     }
/*     */     
/* 157 */     if (gameStateChanged.getGameState() != GameState.LOGGED_IN)
/*     */     {
/* 159 */       this.objects.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onMenuEntryAdded(MenuEntryAdded event) {
/* 166 */     if (!this.hotKeyPressed || event.getType() != MenuAction.EXAMINE_OBJECT.getId()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 171 */     Tile tile = this.client.getScene().getTiles()[this.client.getPlane()][event.getActionParam0()][event.getActionParam1()];
/* 172 */     TileObject tileObject = findTileObject(tile, event.getIdentifier());
/*     */     
/* 174 */     if (tileObject == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 179 */     MenuEntry[] menuEntries = this.client.getMenuEntries();
/* 180 */     menuEntries = Arrays.<MenuEntry>copyOf(menuEntries, menuEntries.length + 1);
/* 181 */     MenuEntry menuEntry = menuEntries[menuEntries.length - 1] = new MenuEntry();
/* 182 */     menuEntry.setOption(this.objects.stream().anyMatch(o -> (o.getTileObject() == tileObject)) ? "Unmark object" : "Mark object");
/* 183 */     menuEntry.setTarget(event.getTarget());
/* 184 */     menuEntry.setParam0(event.getActionParam0());
/* 185 */     menuEntry.setParam1(event.getActionParam1());
/* 186 */     menuEntry.setIdentifier(event.getIdentifier());
/* 187 */     menuEntry.setType(MenuAction.RUNELITE.getId());
/* 188 */     this.client.setMenuEntries(menuEntries);
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onMenuOptionClicked(MenuOptionClicked event) {
/* 194 */     if (event.getMenuAction() != MenuAction.RUNELITE || (
/* 195 */       !event.getMenuOption().equals("Mark object") && !event.getMenuOption().equals("Unmark object"))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 200 */     Scene scene = this.client.getScene();
/* 201 */     Tile[][][] tiles = scene.getTiles();
/* 202 */     int x = event.getActionParam();
/* 203 */     int y = event.getWidgetId();
/* 204 */     int z = this.client.getPlane();
/* 205 */     Tile tile = tiles[z][x][y];
/*     */     
/* 207 */     TileObject object = findTileObject(tile, event.getId());
/* 208 */     if (object == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     ObjectComposition objectDefinition = getObjectComposition(object.getId());
/* 216 */     String name = objectDefinition.getName();
/*     */ 
/*     */     
/* 219 */     if (Strings.isNullOrEmpty(name) || name.equals("null")) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 224 */     markObject(objectDefinition, name, object);
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkObjectPoints(TileObject object) {
/* 229 */     WorldPoint worldPoint = WorldPoint.fromLocalInstance(this.client, object.getLocalLocation());
/* 230 */     Set<ObjectPoint> objectPoints = this.points.get(Integer.valueOf(worldPoint.getRegionID()));
/*     */     
/* 232 */     if (objectPoints == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 237 */     for (ObjectPoint objectPoint : objectPoints) {
/*     */       
/* 239 */       if (worldPoint.getRegionX() == objectPoint.getRegionX() && worldPoint
/* 240 */         .getRegionY() == objectPoint.getRegionY() && worldPoint
/* 241 */         .getPlane() == objectPoint.getZ())
/*     */       {
/*     */         
/* 244 */         if (objectPoint.getName().equals(getObjectComposition(object.getId()).getName())) {
/*     */           
/* 246 */           log.debug("Marking object {} due to matching {}", object, objectPoint);
/* 247 */           this.objects.add(new ColorTileObject(object, objectPoint.getColor()));
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private TileObject findTileObject(Tile tile, int id) {
/* 256 */     if (tile == null)
/*     */     {
/* 258 */       return null;
/*     */     }
/*     */     
/* 261 */     GameObject[] tileGameObjects = tile.getGameObjects();
/* 262 */     DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
/* 263 */     WallObject tileWallObject = tile.getWallObject();
/* 264 */     GroundObject groundObject = tile.getGroundObject();
/*     */     
/* 266 */     if (objectIdEquals((TileObject)tileWallObject, id))
/*     */     {
/* 268 */       return (TileObject)tileWallObject;
/*     */     }
/*     */     
/* 271 */     if (objectIdEquals((TileObject)tileDecorativeObject, id))
/*     */     {
/* 273 */       return (TileObject)tileDecorativeObject;
/*     */     }
/*     */     
/* 276 */     if (objectIdEquals((TileObject)groundObject, id))
/*     */     {
/* 278 */       return (TileObject)groundObject;
/*     */     }
/*     */     
/* 281 */     for (GameObject object : tileGameObjects) {
/*     */       
/* 283 */       if (objectIdEquals((TileObject)object, id))
/*     */       {
/* 285 */         return (TileObject)object;
/*     */       }
/*     */     } 
/*     */     
/* 289 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean objectIdEquals(TileObject tileObject, int id) {
/* 294 */     if (tileObject == null)
/*     */     {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     if (tileObject.getId() == id)
/*     */     {
/* 301 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 306 */     ObjectComposition comp = this.client.getObjectDefinition(tileObject.getId());
/*     */     
/* 308 */     if (comp.getImpostorIds() != null)
/*     */     {
/* 310 */       for (int impostorId : comp.getImpostorIds()) {
/*     */         
/* 312 */         if (impostorId == id)
/*     */         {
/* 314 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 319 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void markObject(ObjectComposition objectComposition, String name, TileObject object) {
/* 330 */     WorldPoint worldPoint = WorldPoint.fromLocalInstance(this.client, object.getLocalLocation());
/* 331 */     int regionId = worldPoint.getRegionID();
/* 332 */     Color color = this.config.markerColor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 339 */     ObjectPoint point = new ObjectPoint(object.getId(), name, regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), color);
/*     */ 
/*     */     
/* 342 */     Set<ObjectPoint> objectPoints = this.points.computeIfAbsent(Integer.valueOf(regionId), k -> new HashSet());
/*     */     
/* 344 */     if (this.objects.removeIf(o -> (o.getTileObject() == object))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 350 */       if (!objectPoints.removeIf(op -> ((op.getId() == -1 || op.getId() == object.getId() || op.getName().equals(objectComposition.getName())) && op.getRegionX() == worldPoint.getRegionX() && op.getRegionY() == worldPoint.getRegionY() && op.getZ() == worldPoint.getPlane())))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 355 */         log.warn("unable to find object point for unmarked object {}", Integer.valueOf(object.getId()));
/*     */       }
/*     */       
/* 358 */       log.debug("Unmarking object: {}", point);
/* 359 */       this.addedIds.remove(Integer.valueOf(object.getId()));
/*     */     }
/*     */     else {
/*     */       
/* 363 */       objectPoints.add(point);
/* 364 */       this.objects.add(new ColorTileObject(object, color));
/* 365 */       this.addedIds.add(Integer.valueOf(object.getId()));
/* 366 */       log.debug("Marking object: {}", point);
/*     */     } 
/*     */     
/* 369 */     savePoints(regionId, objectPoints);
/* 370 */     saveIds();
/*     */   }
/*     */ 
/*     */   
/*     */   private void savePoints(int id, Set<ObjectPoint> points) {
/* 375 */     if (points.isEmpty()) {
/*     */       
/* 377 */       this.configManager.unsetConfiguration("objectindicators", "region_" + id);
/*     */     }
/*     */     else {
/*     */       
/* 381 */       String json = this.GSON.toJson(points);
/* 382 */       this.configManager.setConfiguration("objectindicators", "region_" + id, json);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<ObjectPoint> loadPoints(int id) {
/* 388 */     String json = this.configManager.getConfiguration("objectindicators", "region_" + id);
/*     */     
/* 390 */     if (Strings.isNullOrEmpty(json))
/*     */     {
/* 392 */       return null;
/*     */     }
/*     */     
/* 395 */     Set<ObjectPoint> points = (Set<ObjectPoint>)this.GSON.fromJson(json, (new TypeToken<Set<ObjectPoint>>() {
/*     */         
/* 397 */         }).getType());
/*     */ 
/*     */ 
/*     */     
/* 401 */     return (Set<ObjectPoint>)points.stream()
/* 402 */       .filter(point -> !point.getName().equals("null"))
/* 403 */       .collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveIds() {
/* 408 */     if (this.points.isEmpty()) {
/*     */       
/* 410 */       this.configManager.unsetConfiguration("objectindicators", "ids");
/*     */     }
/*     */     else {
/*     */       
/* 414 */       String json = this.GSON.toJson(this.addedIds);
/* 415 */       this.configManager.setConfiguration("objectindicators", "ids", json);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadIds() {
/* 421 */     String json = this.configManager.getConfiguration("objectindicators", "ids");
/*     */     
/* 423 */     if (Strings.isNullOrEmpty(json)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 428 */     HashSet<Integer> ids = (HashSet<Integer>)this.GSON.fromJson(json, (new TypeToken<HashSet<Integer>>() {  }).getType());
/*     */     
/* 430 */     this.addedIds.addAll(ids);
/*     */   }
/*     */   
/*     */   private void reload() {
/* 434 */     log.info("Reloading...");
/* 435 */     loadIds();
/*     */     
/* 437 */     this.points.clear();
/* 438 */     for (int regionId : this.client.getMapRegions()) {
/*     */ 
/*     */       
/* 441 */       Set<ObjectPoint> regionPoints = loadPoints(regionId);
/* 442 */       if (regionPoints != null)
/*     */       {
/* 444 */         this.points.put(Integer.valueOf(regionId), regionPoints);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/* 449 */   private int lastFloor = -1;
/*     */   
/*     */   @Subscribe
/*     */   public void onGameTick(GameTick event) {
/* 453 */     Player p = this.client.getLocalPlayer();
/* 454 */     if (p == null)
/* 455 */       return;  WorldPoint worldLocation = p.getWorldLocation();
/* 456 */     if (worldLocation == null)
/* 457 */       return;  if (worldLocation.getPlane() != this.lastFloor) {
/* 458 */       this.lastFloor = worldLocation.getPlane();
/* 459 */       checkTileObjects();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkTileObjects() {
/* 465 */     Scene scene = this.client.getScene();
/* 466 */     Tile[][][] tiles = scene.getTiles();
/*     */     
/* 468 */     int z = this.client.getPlane();
/*     */     
/* 470 */     for (int x = 0; x < 104; x++) {
/*     */       
/* 472 */       for (int y = 0; y < 104; y++) {
/*     */         
/* 474 */         Tile tile = tiles[z][x][y];
/*     */         
/* 476 */         if (tile != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 481 */           Player player = this.client.getLocalPlayer();
/* 482 */           if (player != null)
/*     */           {
/*     */ 
/*     */             
/* 486 */             checkTileObjects(tile);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkTileObjects(Tile tile) {
/* 495 */     GameObject[] gameObjects = tile.getGameObjects();
/* 496 */     if (gameObjects != null)
/*     */     {
/* 498 */       for (GameObject gameObject : gameObjects) {
/*     */         
/* 500 */         if (gameObject != null)
/*     */         {
/* 502 */           if (this.addedIds.contains(Integer.valueOf(gameObject.getId())))
/* 503 */             checkObjectPoints((TileObject)gameObject); 
/*     */         }
/*     */       } 
/*     */     }
/* 507 */     WallObject wallObject = tile.getWallObject();
/*     */     
/* 509 */     if (wallObject != null)
/*     */     {
/* 511 */       if (this.addedIds.contains(Integer.valueOf(wallObject.getId())))
/* 512 */         checkObjectPoints((TileObject)wallObject); 
/*     */     }
/* 514 */     DecorativeObject decorObject = tile.getDecorativeObject();
/*     */     
/* 516 */     if (decorObject != null)
/*     */     {
/* 518 */       if (this.addedIds.contains(Integer.valueOf(decorObject.getId()))) {
/* 519 */         checkObjectPoints((TileObject)decorObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private ObjectComposition getObjectComposition(int id) {
/* 526 */     ObjectComposition objectComposition = this.client.getObjectDefinition(id);
/* 527 */     return (objectComposition.getImpostorIds() == null) ? objectComposition : objectComposition.getImpostor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onWallObjectSpawned(WallObjectSpawned event) {
/* 534 */     checkObjectPoints((TileObject)event.getWallObject());
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onWallObjectChanged(WallObjectChanged event) {
/* 540 */     WallObject previous = event.getPrevious();
/* 541 */     WallObject wallObject = event.getWallObject();
/*     */     
/* 543 */     this.objects.removeIf(o -> (o.getTileObject() == previous));
/* 544 */     checkObjectPoints((TileObject)wallObject);
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGameObjectSpawned(GameObjectSpawned event) {
/* 550 */     checkObjectPoints((TileObject)event.getGameObject());
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
/* 556 */     checkObjectPoints((TileObject)event.getDecorativeObject());
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGroundObjectSpawned(GroundObjectSpawned event) {
/* 562 */     checkObjectPoints((TileObject)event.getGroundObject());
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGameObjectDespawned(GameObjectDespawned event) {
/* 568 */     this.objects.removeIf(o -> (o.getTileObject() == event.getGameObject()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
/* 574 */     this.objects.removeIf(o -> (o.getTileObject() == event.getDecorativeObject()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onWallObjectDespawned(WallObjectDespawned event) {
/* 580 */     this.objects.removeIf(o -> (o.getTileObject() == event.getWallObject()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onGroundObjectDespawned(GroundObjectDespawned event) {
/* 586 */     this.objects.removeIf(o -> (o.getTileObject() == event.getGroundObject()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Gabriel\Downloads\Runelite.jar!\net\runelite\client\plugins\objectindicators\ObjectIndicatorsPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */