package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.Varp;
import com.runemate.game.api.hybrid.local.VarpID;
import com.runemate.game.api.hybrid.local.Varps;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.GameObjectListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Log4j2(topic = "Cannon")
public class Cannon implements GameObjectListener, ChatboxListener, EngineListener {

    public final static int RELOAD_THRESHOLD = 5;
    private static final Varp cannonAmmo = Varps.getAt(VarpID.CANNON_AMMO.getId());
    private static final Pattern BASE = Pattern.compile("Cannon base.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern MULTICANNON = Pattern.compile("Dwarf multicannon.*", Pattern.CASE_INSENSITIVE);
    private static final Pattern BROKEN_MULTICANNON = Pattern.compile("Broken multicannon.*", Pattern.CASE_INSENSITIVE);
    private static final List<Integer> MAX_CBALLS = List.of(30, 35, 45, 60);
    public final DI di;
    private boolean firstCannonLoad;
    @Getter
    private boolean cannonPlaced;
    @Getter
    private Area cannonPosition;
    @Getter
    private int cannonWorld = -1;
    @Getter
    private boolean started;
    private int tick;
    private boolean isReloading = false;
    private int reloadingStart;
    private int prevAnimation;
    private int cannonAnimationStallCounter = 0;
    private boolean broken = false;

    private static Area buildCannonWorldArea(Coordinate worldPoint) {
        var x = worldPoint.getX() - 1;
        var y = worldPoint.getY() - 1;
        var z = worldPoint.getPlane();
        return new Area.Rectangular(new Coordinate(x, y, z), new Coordinate(x + 3, y + 3, z));
    }

    public static boolean hasCannonballs() {
        return Inventory.contains("Cannonball") || Inventory.contains("Granite cannonball");
    }

    public static boolean hasCannon() {
        return hasCannonballs() && (Inventory.contains(BASE)
                || !GameObjects.newQuery()
                .names(MULTICANNON)
                .results().isEmpty());
    }

    @Nullable
    public GameObject getCannon() {
        var q = GameObjects.newQuery().names(MULTICANNON);
        if (isCannonPlaced() && cannonPosition != null) {
            q = q.within(cannonPosition);
        }
        return q.results().nearest();
    }

    public void log() {
        var c = getCannon();
        if (c != null) {
            log.debug("Cannon {}, Animation {}, Balls {}", c, c.getAnimationId(), getCballsLeft());
        }
    }

    private GameObject getBrokenCannon() {
        if (!isCannonPlaced() || cannonPosition == null) return null;
        return GameObjects.newQuery().within(cannonPosition).names(BROKEN_MULTICANNON).results().nearest();
    }

    public boolean needsReload() {
        var cannon = this.getCannon();
        if (cannon == null || !this.isCannonPlaced()) return false;
        if (this.isReloading) return false;
        if (cannon.getAnimationId() == -1) {
            log.debug("No animation needs reload");
            return true;
        }

        if (this.broken) {
            log.debug("broken reload");
            return true;
        } else if (getCballsLeft() <= RELOAD_THRESHOLD && hasCannonballs()) {
            log.debug("Cannonballs under threshold {}", getCballsLeft());
            return true;
        } else if (cannonAnimationStallCounter > 5) {
            log.debug("cannon animation stall counter {}", cannonAnimationStallCounter);
            return true;
        }
        return false;
    }

    @Override
    public void onTickStart() {
        try {
            if (Environment.isDevMode() && isCannonPlaced()) log();
            tick++;
            if (reloadingStart + 10 < tick && isReloading) isReloading = false;
            if (!started) return;
            var cannon = this.getCannon();
            if (cannon == null) return;
            if ((prevAnimation == cannon.getAnimationId()) && (MAX_CBALLS.contains(getCballsLeft()))) {
                cannonAnimationStallCounter++;
            } else {
                cannonAnimationStallCounter = 0;
                if (this.broken && cannon != null) {
                    this.broken = false;
                }
            }
            this.prevAnimation = cannon.getAnimationId();

            if (!cannon.isVisible()) return;
            if (isReloading && getCballsLeft() > RELOAD_THRESHOLD) {
                // next tick
                reloadingStart = -1;
            }

        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public void place() {
        log.debug("try set up cannon");
        var base = Inventory.getItems(Cannon.BASE).first();
        if (base == null) {
            startUp();
            return;
        }
        ControlPanelTab.INVENTORY.open();
        Execution.delay(10);
        if (!di.send(MenuAction.forSpriteItem(base, "Set-up")))
            return;

        Execution.delayUntil(
                () -> isCannonPlaced() && getCannon() != null,
                util::playerMoving,
                4000, 5000);
        Execution.delay(600);
    }

    public boolean pickup() {
        log.debug("try to pick up cannon");
        var cannon = this.getCannon();
        if (cannon == null) return false;
        var def = cannon.getActiveDefinition();
        if (def == null) return false;
        if (!def.getActions().contains("Pick-up"))
            log.error("unable to pick up cannon");
        di.send(MenuAction.forGameObject(cannon, "Pick-up"));
        var r = Execution.delayUntil(
                () -> {
                    var q = GameObjects.newQuery();
                    if (cannonPosition != null)
                        q = q.within(cannonPosition);
                    return q.names(MULTICANNON)
                            .results().isEmpty();
                }, util::playerMoving, 1000, 2000);
        if (r) {
            shutDown();
        }
        return r;
    }

    public void reload() {
        log.debug("reload isAlreadyReloading:{}", isReloading);
        if (isReloading) return;
        var cannon = this.getCannon();
        var cballs = Inventory.getItems("Cannonball", "Granite cannonball").first();
        if (cballs == null) {
            log.error("no cannonballs");
            this.pickup();
            this.shutDown();
            return;
        }
        isReloading = true;
        reloadingStart = tick;

        if (this.broken) {
            var brokenCannon = getBrokenCannon();
            log.debug("REPAIRING cannon {}", brokenCannon);
            if (brokenCannon == null) {
                this.broken = false;
                log.debug("Repaired cannon {}", this.getCannon());
                return;
            }
            di.send(MenuAction.forGameObject(brokenCannon, "Repair"));
            this.broken = Execution.delayUntil(() -> getCannon() != null, util::playerMoving, 2400, 3600);
            if (!this.broken) {
                cannon = getCannon();
            }
        } else {
            log.debug("reloading cannon {} with {}", cannon, cballs);
            di.send(MenuAction.forGameObject(cannon, "Fire"));
        }
    }

    private int getCballsLeft() {
        return cannonAmmo.getValue();
    }

    public void startUp() {
        log.debug("cannon start");
        var cannon = getCannon();
        if (cannon == null) {
            log.debug("no cannon in start, trying to find");
            GameObject object = getCannon();
            if (object != null && object.isValid() && Players.getLocal().getServerPosition().distanceTo(object.getPosition()) <= 10) {
                var pos = object.getPosition();
                log.debug("cannon found on {}", pos);
                cannonPlaced = true;
                cannonWorld = Worlds.getCurrent();
                cannonPosition = buildCannonWorldArea(pos);
            }
        }
        if (cannon != null) {
            log.debug("Cannon initial start {}", cannon.getAnimationId());
            if (needsReload()) {
                reload();
                this.started = Execution.delayUntil(() -> !needsReload(), 1400, 2600);
            } else {
                this.started = true;
            }
        }
    }

    public void shutDown() {
        this.started = false;
        cannonPlaced = false;
        cannonWorld = -1;
        cannonPosition = null;
    }


    @Override
    public void onMessageReceived(MessageEvent event) {
        if (!Objects.equals(event.getType(), Chatbox.Message.Type.SERVER)) {
            return;
        }

        if (event.getMessage().equals("You add the furnace.")) {
            cannonPlaced = true;
            firstCannonLoad = true;
        } else if (event.getMessage().equals("Your cannon has broken!")) {
            this.broken = true;
        } else if (event.getMessage().contains("You pick up the cannon")
                || event.getMessage().contains("Your cannon has decayed. Speak to Nulodion to get a new one!")
                || event.getMessage().contains("Your cannon has been destroyed!")) {
            cannonPlaced = false;
            cannonPosition = null;
        } else if (event.getMessage().startsWith("You load the cannon with")) {
            // Set the cannon's position and object if the player's animation was interrupted during setup
            if (cannonPosition == null) {
                if (firstCannonLoad) {
                    GameObject object = getCannon();
                    if (object != null && Players.getLocal().getServerPosition().distanceTo(object.getPosition()) <= 2) {
                        var pos = object.getPosition();
                        log.debug("cannon placed from message on {}", pos);
                        cannonPlaced = true;
                        cannonWorld = Worlds.getCurrent();
                        cannonPosition = buildCannonWorldArea(pos);
                    }
                    firstCannonLoad = false;
                }
            }
        } else if (event.getMessage().contains("Your cannon is out of ammo!")) {
            log.debug("Your cannon is out of ammo!");
            isReloading = false;
            if (started) {
                reload();
            }
        } else if (event.getMessage().equals("This isn't your cannon!") || event.getMessage().equals("This is not your cannon.")) {
            log.error("Not your cannon");
            shutDown();
        }
    }

    public boolean isReloading() {
        return this.isReloading;
    }

    public void refresh() {
        var cannon = getCannon();
        if (cannon == null || !cannon.isVisible() || !cannon.isValid()) {
            shutDown();
        }
    }
}