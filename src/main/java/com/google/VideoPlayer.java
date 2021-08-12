package com.google;

import org.apache.commons.io.serialization.ValidatingObjectInputStream;

import java.beans.Visibility;
import java.util.*;

public class VideoPlayer {
    HashMap<String, List<Video>> allPlaylist = new HashMap<>();
    private final VideoLibrary videoLibrary;
    Video previouslyPlayedVideo;
    Video currentlyPlayingVideo;
    //Video PreviouslyPausedVideo;
    //static int running = 0;
    List<Video> allVideos;
    int size;

    public VideoPlayer() {
        this.videoLibrary = new VideoLibrary();
        allVideos = videoLibrary.getVideos();
        size = allVideos.size();
    }

    public void numberOfVideos() {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    public void printVideo(Video video) {
        List<String> allTags;
        System.out.print(video.getTitle() + " (" + video.getVideoId() + ") ");
        allTags = video.getTags();
        if (allTags.size() > 0) {
            int tagSize = allTags.size();
            System.out.print("[");
            for (int j = 0; j < tagSize; j++) {
                System.out.print(allTags.get(j));
                if (j != tagSize - 1) System.out.print(" ");
            }
            if (video.getPaused()) System.out.println("] - PAUSED");
            else System.out.println("]");
        } else System.out.println("[]");
    }

    public void showAllVideos() {
        System.out.print("Here's a list of all available videos:\n");
        sort(allVideos);
        for (int i = 0; i < size; i++) {
            printVideo(allVideos.get(i));
        }
    }

    public void playVideo(String videoId) {
        boolean exists = false;

        for (Video chosenVideo : videoLibrary.getVideos()) {
            if (chosenVideo.getVideoId().equals(videoId)) {
                if (currentlyPlayingVideo != null) {
                    stopVideo();
                }
                System.out.println("Playing video: " + chosenVideo.getTitle());
                chosenVideo.setPaused(false);
                currentlyPlayingVideo = chosenVideo;
                exists = true;
                previouslyPlayedVideo = null;
            }
        }
        if (!exists) {
            System.out.println("Cannot play video: Video does not exist");
        }
    }

    public void stopVideo() {
        if (currentlyPlayingVideo != null) {
            System.out.println("Stopping video: " + currentlyPlayingVideo.getTitle());
            currentlyPlayingVideo = null;
        } else {
            System.out.println("Cannot stop video: No video is currently playing");
        }
    }

    public void playRandomVideo() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(size);
        playVideo(allVideos.get(randomIndex).getVideoId());
    }

    public void pauseVideo() {
        if (currentlyPlayingVideo == null) {
            System.out.println("Cannot pause video: No video is currently playing");
        } else if (!currentlyPlayingVideo.getPaused() && !currentlyPlayingVideo.getTitle().equals("")) {
            System.out.println("Pausing video: " + currentlyPlayingVideo.getTitle());
            currentlyPlayingVideo.setPaused(true);
        } else if (currentlyPlayingVideo.getPaused()) {
            System.out.println("Video already paused: " + currentlyPlayingVideo.getTitle());
        } else System.out.println("Cannot pause video: No video is currently playing");
    }

    public void continueVideo() {
        if (currentlyPlayingVideo == null) {
            System.out.println("Cannot continue video: No video is currently playing");
        } else if (currentlyPlayingVideo.getPaused()) {
            currentlyPlayingVideo.setPaused(false);
            System.out.println("Continuing video: " + currentlyPlayingVideo.getTitle());
        } else if (!currentlyPlayingVideo.getPaused() && !currentlyPlayingVideo.getTitle().equals("")) {
            System.out.println("Cannot continue video: Video is not paused");
        } else if (currentlyPlayingVideo.getTitle().equals("")) {
            System.out.println("Cannot continue video: No video is currently playing");
        }
    }

    public void showPlaying() {
        if (currentlyPlayingVideo == null)
            System.out.println("No video is currently playing");
        else if (!currentlyPlayingVideo.getTitle().equals("")) {
            System.out.print("Currently playing: ");
            printVideo(currentlyPlayingVideo);
        } else System.out.println("No video is currently playing");
    }

    public void createPlaylist(String playlistName) {
        ArrayList<Video> videoList = new ArrayList<Video>();
        if (!allPlaylist.containsKey(playlistName)) {
            allPlaylist.put(playlistName, videoList);
            System.out.println("Successfully created new playlist: " + playlistName);
        } else {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
        }
    }

    public void addVideoToPlaylist(String playlistName, String videoId) {
        int index = -1;
        for (Map.Entry<String, List<Video>> entry : allPlaylist.entrySet()) {
            index++;
            if (!entry.getKey().equalsIgnoreCase(playlistName)) {
                System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
            } else if (entry.getValue() == null
                    && allVideos.get(index).getVideoId().equalsIgnoreCase(videoId)){
                System.out.println("Does not exist in an empty playlist and exists in the DB");
            }
            else if (!entry.getValue().get(index).getVideoId().equalsIgnoreCase(videoId)
                    && allVideos.get(index).getVideoId().equalsIgnoreCase(videoId)) {
                System.out.println("Does not exist in a non empty playlist and exists in the DB");
            }
        }
    }

    public void showAllPlaylists() {
        System.out.println("showAllPlaylists needs implementation");
    }

    public void showPlaylist(String playlistName) {
        System.out.println("showPlaylist needs implementation");
    }

    public void removeFromPlaylist(String playlistName, String videoId) {
        System.out.println("removeFromPlaylist needs implementation");
    }

    public void clearPlaylist(String playlistName) {
        System.out.println("clearPlaylist needs implementation");
    }

    public void deletePlaylist(String playlistName) {
        System.out.println("deletePlaylist needs implementation");
    }

    public ArrayList<Video> search(String keyword, boolean type) {
        ArrayList<Video> matchedVideos = new ArrayList<>();
        if (type) {
            for (Video video : allVideos) {
                if (video.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    matchedVideos.add(video);
                }
            }
        } else {
            for (Video video : allVideos) {
                List<String> tags = video.getTags();
                for (String tag : tags) {
                    if (tag.equalsIgnoreCase(keyword))
                        matchedVideos.add(video);
                }
            }
        }
        sort(matchedVideos);
        return matchedVideos;
    }

    public void printAndPlay(ArrayList<Video> videoArrayList, String searchTerm) {
        int formatting = 1;
        if (!videoArrayList.isEmpty()) {
            System.out.println("Here are the results for " + searchTerm + ":");
            for (Video video : videoArrayList) {
                System.out.print(formatting + ") " + video.getTitle() + " (" + video.getVideoId() + ") ");
                List<String> allTags = video.getTags();
                if (allTags.size() > 0) {
                    int tagSize = allTags.size();
                    System.out.print("[");
                    for (int j = 0; j < tagSize; j++) {
                        System.out.print(allTags.get(j));
                        if (j != tagSize - 1) System.out.print(" ");
                    }
                    if (video.getPaused()) System.out.println("] - PAUSED");
                    else System.out.println("]");
                    //System.out.println(formatting + ") " + video.getTitle());
                    formatting++;
                }
            }
            System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n" +
                    "If your answer is not a valid number, we will assume it's a no.");
            Scanner sc = new Scanner(System.in);
            //System.out.print("Your option: ");
            String input = sc.next();
            try {
                int option = Integer.parseInt(input);
                playVideo(videoArrayList.get(--option).getVideoId());
            } catch (NumberFormatException e) {
                //System.out.println();
            }
        } else System.out.println("No search results for " + searchTerm);
    }

    public void searchVideos(String searchTerm) {
        ArrayList<Video> matchedVideos = search(searchTerm, true);
        printAndPlay(matchedVideos, searchTerm);
    }

    public void searchVideosWithTag(String videoTag) {
        ArrayList<Video> matchedVideos = search(videoTag, false);
        printAndPlay(matchedVideos, videoTag);
        //System.out.println("searchVideosWithTag needs implementation");
    }

    public void flagVideo(String videoId) {
        System.out.println("flagVideo needs implementation");
    }

    public void flagVideo(String videoId, String reason) {
        System.out.println("flagVideo needs implementation");
    }

    public void allowVideo(String videoId) {
        System.out.println("allowVideo needs implementation");
    }

    public static void sort(List<Video> list) {
        list.sort((title1, title2)
                -> title1.getTitle().compareTo(
                title2.getTitle()));
    }
}