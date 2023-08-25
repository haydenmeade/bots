package com.neck_flexed.scripts.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.KeyEvent;

@Getter
@RequiredArgsConstructor
public enum Xeric {
    Lookout(1, "Lookout", KeyEvent.VK_1),
    Glade(2, "Glade", KeyEvent.VK_2),
    Inferno(3, "Inferno", KeyEvent.VK_3),
    Heart(4, "Heart", KeyEvent.VK_4),
    Honour(5, "Honour", KeyEvent.VK_5);
    private final int index;
    private final String rightClick;
    private final int key;
}
