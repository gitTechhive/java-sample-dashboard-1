package com.sampledashboard1.enums;

public enum EnumForLoginType {

    Google("google"), MOBILE("mobile"), EMAIL("email");

    private final String type;

    private EnumForLoginType(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }

    @Override
    public String toString() {
        return type;
    }
}
