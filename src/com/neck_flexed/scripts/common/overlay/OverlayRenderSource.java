package com.neck_flexed.scripts.common.overlay;

import com.neck_flexed.scripts.common.overlay.render.RenderTarget;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public interface OverlayRenderSource {

    Collection<RenderTarget> renderables();

    default Collection<Callable<String>> texts() {
        return List.of();
    }

}
