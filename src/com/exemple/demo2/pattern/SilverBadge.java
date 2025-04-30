package com.exemple.demo2.pattern;

public class SilverBadge extends BadgeDecorator {

    public SilverBadge(Badge badge) {
        super(badge);
    }

    @Override
    public String getBadge() {
        return "Silver Badge";
    }
}
