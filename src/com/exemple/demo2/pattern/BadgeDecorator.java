package com.exemple.demo2.pattern;

public abstract class BadgeDecorator implements Badge {
    protected Badge badge;

    public BadgeDecorator(Badge badge) {
        this.badge = badge;
    }

    @Override
    public String getBadge() {
        return badge.getBadge();
    }
}
