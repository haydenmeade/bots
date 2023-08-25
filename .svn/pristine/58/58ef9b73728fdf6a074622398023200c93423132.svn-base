package com.neck_flexed.scripts.kq;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.traverse.KalphiteLairTraverse;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "TraverseState")
public class TraverseState extends BaseState<KqState> {
    private final kq bot;
    private final Traverser traverser;
    private String error;
    private Traverser tunnelTraverser;

    public TraverseState(kq bot) {
        super(bot, KqState.TRAVERSING);
        this.bot = bot;
        this.traverser = new Traverser(
                bot,
                HouseConfig.parse(bot.settings()),
                new Coordinate(3250, 3150, 2),
                new int[]{KalphiteLairTraverse.LAIR_REGION_ID},
                null,
                new KalphiteLairTraverse()
        );
    }

    @Override
    public void activate() {
        bot.updateStatus("Traversing");
    }

    @Override
    public String fatalError() {
        return error;
    }

    @Override
    public boolean done() {
        return kq.atLairEntrance()
                || kq.isInLair()
                || (kq.isInTunnels() && kq.canUseCreviceShortcut());
    }

    @Override
    public void executeLoop() {
        if (kq.tunnels.contains(Players.getLocal()) && !kq.canUseCreviceShortcut()) {
            if (this.tunnelTraverser == null)
                this.tunnelTraverser = Traverser.regionPathTraverser(kq.crackUseTile);
            tunnelTraverser.executeLoop();
            return;
        }
        this.traverser.executeLoop();
    }
}
