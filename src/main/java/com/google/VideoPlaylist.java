package com.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class used to represent a Playlist
 */
class VideoPlaylist {
    private final String name;
    private final ArrayList<Video> videos;
    VideoPlaylist(String name, ArrayList<Video> videos) {
        this.name = name;
        this.videos = videos;
    }

    /** Returns the title of the video. */
    String getTitle() {
        return name;
    }


    /** Returns a readonly collection of the tags of the video. */
    List<Video> getTags() {
        return videos;
    }
}