package com.example.istreampersonalvideoplaylistapp_51c.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "playlist",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "userId",
                childColumns = "userOwnerId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("userOwnerId")}
)
public class PlaylistEntity {
    @PrimaryKey(autoGenerate = true)
    private int playlistId;
    private int userOwnerId;
    private String videoUrl;
    private long timestamp;

    public PlaylistEntity(int userOwnerId, String videoUrl, long timestamp) {
        this.userOwnerId = userOwnerId;
        this.videoUrl = videoUrl;
        this.timestamp = timestamp;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getUserOwnerId() {
        return userOwnerId;
    }

    public void setUserOwnerId(int userOwnerId) {
        this.userOwnerId = userOwnerId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
