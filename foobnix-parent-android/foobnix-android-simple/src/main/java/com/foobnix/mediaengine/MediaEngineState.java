package com.foobnix.mediaengine;

import java.io.Serializable;

public class MediaEngineState implements Serializable {

    private MediaModel model;
	private int duration;
    private int currentPosition;
	private boolean isPlaying;
	private int buffering;
	private int playlistLen;

    public static MediaEngineState build(MediaPlayerCore core) {
        MediaEngineState state = new MediaEngineState();
        state.setModel(core.getPlaylistCtr().getCurrentModel());
        state.setCurrentPosition(core.getCurrentPosition());
        state.setDuration(core.getDuration());
        state.setPlaying(core.isPlaying());
        state.setBuffering(core.getBuffering());
        return state;
    }

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setPlaying(boolean isPlaying) {
	    this.isPlaying = isPlaying;
    }
	public boolean isPlaying() {
	    return isPlaying;
    }
	public void setBuffering(int buffering) {
	    this.buffering = buffering;
    }
	public int getBuffering() {
	    return buffering;
    }

    public void setModel(MediaModel model) {
	    this.model = model;
    }

    public MediaModel getModel() {
	    return model;
    }

	public void setPlaylistLen(int playlistLen) {
		this.playlistLen = playlistLen;
	}

	public int getPlaylistLen() {
		return playlistLen;
	}

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
