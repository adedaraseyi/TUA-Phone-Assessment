package com.tua.apps.tuaphoneapi.configuration;

public class Profiles {

    public static final String HEROKU = "heroku";
    public static final String DEV = "dev";
    public static final String TEST = "test";

    private Profiles() {
        throw new IllegalStateException("Utility class");
    }

}
