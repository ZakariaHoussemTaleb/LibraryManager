package com.exemple.demo2.pattern;

public class DiamondBadge extends BadgeDecorator {

    public DiamondBadge(Badge badge) {
        super(badge);
    }

    @Override
    public String getBadge() {
        return " Diamond Badge";
    }
}
