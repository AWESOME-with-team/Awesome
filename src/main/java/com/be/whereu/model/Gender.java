package com.be.whereu.model;

public enum Gender {
    M,F;

    public static Gender fromString(String gender) {
        if ("남자".equals(gender)) {
            return M;
        } else if ("여자".equals(gender)) {
            return F;
        } else {
            throw new RuntimeException("Invalid gender value: " + gender);
        }
    }
}
