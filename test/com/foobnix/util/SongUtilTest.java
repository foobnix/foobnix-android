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
package com.foobnix.util;

import junit.framework.TestCase;

import com.foobnix.model.FModel;

public class SongUtilTest extends TestCase {

	public void testNormalLine() {
		String line = "Artist - Title";
		assertEquals("Artist", SongUtil.getArtist(line));
		assertEquals("Title", SongUtil.getTitle(line));
	}

	public void testNormalLine1() {
		String line = "Artist-Title";
		assertEquals("Artist", SongUtil.getArtist(line));
		assertEquals("Title", SongUtil.getTitle(line));
	}

	public void testNormalLine2() {
		String line = "Artist- Title - YES";
		assertEquals("Artist", SongUtil.getArtist(line));
		assertEquals("Title - YES", SongUtil.getTitle(line));
	}

	public void testNotFound() {
		String line = "ArtistTitle";
		assertEquals("UNDEFINED_VALUE", SongUtil.getArtist(line));
		assertEquals("UNDEFINED_VALUE", SongUtil.getTitle(line));
	}

	public void testFModel() {
		FModel model = FModel.File("go go").addPath("path");
		assertEquals("go go", model.getText());
		assertEquals("path", model.getPath());
	}

	public void testEmptyModel() {
		FModel model = FModel.Empty();
		FModel model2 = FModel.Empty();
		assertEquals(model.getUUID(), model2.getUUID());
		assertEquals(model.hashCode(), model2.hashCode());
		assertEquals(model, model2);
		assertNotSame(model, null);

	}

}
