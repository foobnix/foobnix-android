/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */
package com.foobnix.engine.media;

import com.foobnix.model.FModel;

public class EnginesManager implements MediaEngine {

	private final MediaEngine mediaPlayer;
	private final MediaEngine loseless;
	private MediaEngine instanse;

	public EnginesManager() {
		mediaPlayer = new MediaPlayerEngine();
		loseless = new AndLessMediaEngine();
		setInstanse(mediaPlayer);
	}

	public void playPause() {
		if (isPlaying()) {
			pause();
		} else {
			play();
		}

	}

	public void playModel(FModel model) throws Exception {
		if (model == null) {
			throw new IllegalArgumentException();
		}
		instanse.stop();

		String path = model.getPath();

		for (String ext : mediaPlayer.supportedExts()) {
			if (path.toLowerCase().endsWith(ext)) {
				setInstanse(mediaPlayer);
				break;
			}
		}

		for (String ext : loseless.supportedExts()) {
			if (path.toLowerCase().endsWith(ext)) {
				setInstanse(loseless);
				break;
			}
		}

		instanse.play(path);

	}

	public void setInstanse(MediaEngine instanse) {
		this.instanse = instanse;
	}

	@Override
	public String[] supportedExts() {
		return null;
	}

	@Override
	public void play(String path) throws Exception {
	}

	@Override
	public void pause() {
		instanse.pause();
	}

	public void stop() {
		instanse.stop();
	}

	@Override
	public void play() {
		instanse.play();

	}

	@Override
	public boolean isPlaying() {
		return instanse.isPlaying();
	}

	@Override
	public int getDuration() {
		return instanse.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return instanse.getCurrentPosition();
	}

	@Override
	public int getBuffering() {
		return instanse.getBuffering();
	}

	@Override
	public void seekTo(int pos) {
		instanse.seekTo(pos);
	}

}
