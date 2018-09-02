package com.jr.logic;

import com.jr.TestHelper;
import com.jr.model.IPlayOrder;
import com.jr.model.NormalPlaylist;
import com.jr.model.Song;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class PlayOrderTest {
    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @Before
    public void initMethod() {
        TestHelper.setStandardTestData();
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(Defaults.CONFIG_FOLDER);
    }

    @Test
    public void normalOrder() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder normalOrder = new PlayOrder.Normal();
        List<Long> playingHistory = new ArrayList<>();

        for (Song song : songs) {
            Song chosenSong = normalOrder.getNextSong(metal, playingHistory);
            playingHistory.add(chosenSong.getId());
            Assert.assertEquals(chosenSong, song);
        }
    }

    @Test
    public void repeatTrack() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder repeatTrack = new PlayOrder.RepeatTrack();
        List<Long> playingHistory = new ArrayList<>();

        Song chosenSong = repeatTrack.getNextSong(metal, playingHistory);
        Assert.assertEquals(songs.get(0), chosenSong);

        playingHistory.add(songs.get(2).getId());
        chosenSong = repeatTrack.getNextSong(metal, playingHistory);
        Assert.assertEquals(songs.get(2), chosenSong);
    }
}