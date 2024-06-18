package com.be.whereu.model;

public enum isLike {
    T,F;

    public static isLike fromBoolean(boolean isLike) {
        if (isLike) {
            return T;
        } else{
            return F;
        }
    }
}
