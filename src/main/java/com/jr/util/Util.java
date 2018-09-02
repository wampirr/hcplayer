package com.jr.util;

import com.jr.dao.CritRepositoryFile;
import com.jr.dao.NormalPlaylistRepositoryFile;
import com.jr.dao.SongRepositoryFile;
import com.jr.execution.HCPlayer;
import com.jr.logic.CritHardcode;
import com.jr.model.Flavor;
import com.jr.model.NormalPlaylist;
import com.jr.model.Playlist;
import com.jr.service.NormalPlaylistService;
import com.jr.service.SongService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * @author Galatyuk Ilya
 */
public class Util {
    private static boolean isInitialized = false;
    private static final Logger log = LogManager.getLogger(Util.class);

    public static long roll(long max) {
        return (long) (Math.random() * max + 1);
    }

    public static boolean isNameBad(String name) {
        return !Pattern.matches(Defaults.GOOD_NAME_PATTERN, name);
    }

    public static void init() {
        if (isInitialized) return;

        log.info("init() called");
        CritHardcode.saveStandardCrits();

        try {
            Class.forName("com.jr.execution.HCPlayer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        isInitialized = true;
    }

    public static void shutdown() {
        log.info("shutdown() called\n");
        HCPlayer.stop();
        saveData();
    }

    public static void saveData() {
        CritRepositoryFile.rewriteFile();
        SongRepositoryFile.rewriteFile();
        NormalPlaylistRepositoryFile.rewriteFile();
        Settings.saveSettings();
    }

    public static Playlist getInitialPlaylist() {
        Playlist playlist = null;
        Long id = Settings.getPlaylistId();
        if (id != null) {
            playlist = NormalPlaylistService.getOne(id);
            if (playlist == null) { //todo ask filtered playlist service
            }
        }
        if (playlist != null) return playlist;

        return new NormalPlaylist(-1, "", new Flavor(), true, SongService.getAll());
    }

}
