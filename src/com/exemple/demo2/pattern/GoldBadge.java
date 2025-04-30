package com.exemple.demo2.pattern;

public class GoldBadge extends BadgeDecorator {

    public GoldBadge(Badge badge) {
        super(badge);
    }

    @Override
    public String getBadge() {
        return " Gold Badge";
    }
}
