package com.jr.util;

/**
 * @author Galatyuk Ilya
 */
public class FileOps {
    private static volatile long maxId;

    static {
        //todo extract values from file
        maxId = 0;
    }

    public static synchronized long getId() {
        return ++maxId;
    }
}
