package com.neck_flexed.scripts.sarachnis;

import com.neck_flexed.scripts.common.*;
import com.neck_flexed.scripts.common.breaking.BreakHandlerState;
import com.neck_flexed.scripts.common.breaking.BreakManager;
import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.HoppingState;
import com.neck_flexed.scripts.common.state.IStateManager;
import com.neck_flexed.scripts.common.state.LoopState;
import com.neck_flexed.scripts.common.state.RestoreState;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.ui.DefaultUI;
import com.runemate.ui.setting.annotation.open.SettingsProvider;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Triple;

import java.util.EventListener;
import java.util.regex.Pattern;

@Log4j2(topic = "SarachnisMain")
public class bot extends NeckBot<SarachnisSettings, SarachnisState> {
    public static final String Sarachnis = "Sarachnis";
    private SarachnisListener sarachnisListener;
    private Consumeables<SarachnisSettings, SarachnisState> consumeables;
    @SettingsProvider(updatable = true)
    private SarachnisSettings settings;


    public boolean doWeNeedToRestock() {
        if (settings() == null) return false;
        var boost = settings().boost();
        if (boost != Boost.None && boost != Boost.ImbuedHeart && Inventory.getItems(boost.patternAny()).isEmpty()) {
            return true;
        }
        if (Food.countInventory() < settings().minFood()) {
            return true;
        }
        return false;
    }


    public void resetKill() {
        Action.set(Action.None);
        this.sarachnisListener.reset();
    }

    @Override
    protected void initFromSettings(SarachnisSettings settings) {
        this.loadouts = Loadouts.fromEquipmentLoadouts(this,
                Triple.of(settings().specEquipment(), Loadout.LoadoutRole.SpecDps, "Spec"),
                Triple.of(settings().equipment(), Loadout.LoadoutRole.Combat, "Combat")
        );
        consumeables.setDharok(this.loadouts.getDharoks() != null);

        this.breakManager = new BreakManager(settings, this);
        updateItemFilter(util.concatenate(
                util.toPatternArray(new String[]{
                        settings.food().gameName()}),
                new Pattern[]{Pattern.compile("Stamina potion.*"), items.ringOfDueling, items.dramenStaff, items.houseTab,},
                util.toItemList(settings.equipment()),
                util.toItemList(settings.specEquipment())
        ));
    }

    @Override
    protected EventListener[] getEventListenerForRunning() {
        return new EventListener[]{prayerFlicker, consumeables, sarachnisListener, junkDropper};
    }

    @Override
    public void onStart(String... arguments) {
        super.onStart(arguments);
        log.debug(String.format("onStart"));
        sarachnisListener = new SarachnisListener(this);
        consumeables = new Consumeables(this, 35, SarachnisState.RESTORING, false);

        this.getEventDispatcher().addListener(this);
        this.breakManager = new BreakManager(settings, this);
        this.setLoopDelay(10, 25);
        DefaultUI.addPanel(0, this, "Instructions", new Text(
                "Requires a slash weapon or a knife to slash the webs on the way\n" +
                        "House: Requires house teleport, ornate pool, and jewellery box\n" +
                        "Ferox: Requires dueling ring\n" +
                        "Traverses (in order of use): \n" +
                        " - Xeric's Talisman\n" +
                        " - Mounted Xeric's\n" +
                        " - Book of the dead / Kharedst's memoirs\n" +
                        " - Construction Cape\n" +
                        " - House in Hosidius"
        ));
    }


    protected LoopState<SarachnisState> getState(SarachnisState s) {
        switch (s) {
            case STARTING:
                return new StartingState(this);
            case TRAVERSING:
                return new TraverseState(this);
            case FIGHTING:
                return new FightState(this, this.sarachnisListener, this.prayerFlicker);
            case LOOTING:
                return new LootState(this.sarachnisListener, this, this.breakManager);
            case POST_LOOT:
                return new PostLootState(this, this.sarachnisListener);
            case RESTORING:
                var task = SlayerTask.getCurrent();
                return new RestoreState<>(this, SarachnisState.BREAKING, this.getHouseConfig(),
                        task != null && (task.equalsIgnoreCase(Sarachnis) || task.equalsIgnoreCase("Spiders")));
            case HOPPING:
                return new HoppingState<>(this, settings().worldRegion(), SarachnisState.HOPPING);
            case RESTOCKING:
                return new RestockState(this);
            case BREAKING:
                return new BreakHandlerState<>(this.breakManager, this, SarachnisState.BREAKING);
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }

    @Override
    protected LoopState<SarachnisState> getStartingState() {
        return new StartingState(this);
    }

    @Override
    protected SarachnisState getBreakStateEnum() {
        return SarachnisState.BREAKING;
    }

    @Override
    protected SarachnisState getStartStateEnum() {
        return SarachnisState.STARTING;
    }

    @Override
    protected IStateManager<SarachnisState> createStateManager() {
        return new SarachnisStateManager(this, sarachnisListener);
    }

    @Override
    public void updateStatus(String s) {
        var debugString = String.format("%s - Sarachnis KC: %d", s, this.getKillCount());
        log.debug(debugString);
        DefaultUI.setStatus(this, debugString);
    }

    @Override
    public SarachnisSettings settings() {
        return settings;
    }

    @Override
    public HouseConfig getHouseConfig() {
        return new HouseConfig(
                settings.hasOrnateJewelleryBox() ? HouseConfig.JewelleryBox.Ornate : HouseConfig.JewelleryBox.None,
                HouseConfig.Altar.None,
                settings.hasOrnatePool() ? HouseConfig.Pool.Ornate : HouseConfig.Pool.None,
                false, false, settings.hasMountedXerics(), false);
    }
}
