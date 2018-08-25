package com.jr.structure.dao;

import com.jr.service.CritService;
import com.jr.service.SongService;
import com.jr.structure.model.Crit;
import com.jr.structure.model.Flavor;
import com.jr.structure.model.NormalPlaylist;
import com.jr.structure.model.Song;
import com.jr.util.FileOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class NormalPlaylistRepositoryFile implements NormalPlaylistRepository {
    private static final String ID_NAME = "id";
    private static final String NAME_NAME = "name";
    private static final String FLAVOR_NAME = "flavor";
    private static final String SONG_LIST_NAME = "songs";
    private static final List<NormalPlaylist> playlists;

    static {
        List<Map<String, String>> allPlaylistsMap = FileOps.getAll(FileOps.getNormalPlaylistsName());
        playlists = new ArrayList<>();

        for (Map<String, String> playlistMap : allPlaylistsMap) {
            long id = Long.parseLong(playlistMap.get(ID_NAME));
            String name = playlistMap.get(NAME_NAME);
            String flavorMap = playlistMap.get(FLAVOR_NAME);
            Flavor flavor = parseFlavorMap(flavorMap);
            List<Song> songs = parseSongsFromIds(playlistMap.get(SONG_LIST_NAME));

            playlists.add(new NormalPlaylist(id, name, flavor, songs));
        }
    }

    public static Flavor parseFlavorMap(String flavorMap) {
        Flavor result = new Flavor();
        if (flavorMap == null) return result;

        String[] flavors = flavorMap.split("[,]");
        for (String flavorString : flavors) {
            String[] elements = flavorString.split("[']");
            Crit crit = CritService.getByName(elements[0]);
            Integer influence = Integer.parseInt(elements[1]);

            result.getFlavorMap().put(crit, influence);
        }
        return result;
    }

    private static List<Song> parseSongsFromIds(String songIds) {
        if (songIds == null) return new ArrayList<>();

        List<Long> ids = new ArrayList<>();
        String[] idsString = songIds.split("[,]");

        for (String idString : idsString)
            ids.add(Long.parseLong(idString));

        return SongService.getByIds(ids);
    }

    @Override
    public List<NormalPlaylist> findAll() {
        return playlists;
    }

    @Override
    public List<NormalPlaylist> save(Iterable<NormalPlaylist> items) {
        for (NormalPlaylist playlistToSave : items) {
            for (int i = playlists.size(); i > 0; i--) {
                NormalPlaylist playlist = playlists.get(i - 1);
                if (playlistToSave.getId() == playlist.getId()
                        || playlistToSave.getName().equals(playlist.getName())) {
                    playlistToSave.setId(playlist.getId());
                    playlists.remove(playlist);
                }
            }
            playlists.add(playlistToSave);
        }
        rewriteFile();
        return (List<NormalPlaylist>) items;
    }

    @Override
    public void delete(Iterable<NormalPlaylist> items) {
        for (NormalPlaylist playlistToRemove : items) {
            playlists.remove(playlistToRemove);
        }
        rewriteFile();
    }

    public static synchronized void rewriteFile() {
        List<Map<String, String>> listToSave = new ArrayList<>();
        for (NormalPlaylist playlist : playlists) {
            Map<String, String> mapOfPlaylist = new HashMap<>();

            mapOfPlaylist.put(ID_NAME, Long.toString(playlist.getId()));
            mapOfPlaylist.put(NAME_NAME, playlist.getName());
            if (playlist.getFlavor().getFlavorMap() != null)
                mapOfPlaylist.put(FLAVOR_NAME, playlist.getFlavor().toString());

            if (playlist.getSongs().size() > 0) {
                StringBuilder songIds = new StringBuilder();
                for (Song song : playlist.getSongs())
                    if (song != null)
                        songIds.append(song.getId()).append(",");
                songIds.deleteCharAt(songIds.length() - 1);
                mapOfPlaylist.put(SONG_LIST_NAME, songIds.toString());
            }

            listToSave.add(mapOfPlaylist);
        }

        FileOps.put(FileOps.getNormalPlaylistsName(), listToSave, false);
    }
}
