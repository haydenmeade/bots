package com.neck_flexed.scripts.sarachnis;

import com.neck_flexed.scripts.common.items;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public enum SlashThing {
    None("None", Pattern.compile("")),
    Knife("Knife", Pattern.compile("Knife")),
    WildySword("Wildy Sword", items.wildySword);

    private final String label;
    private final Pattern pattern;

    @Override
    public String toString() {
        return this.label;
    }
}
