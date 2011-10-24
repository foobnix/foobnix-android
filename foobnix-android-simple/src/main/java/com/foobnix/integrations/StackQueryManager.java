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
package com.foobnix.integrations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.foobnix.commons.LOG;
import com.foobnix.exception.VkErrorException;
import com.foobnix.mediaengine.MediaModel;

public abstract class StackQueryManager {

	private Stack<SearchQuery> stack;

    public StackQueryManager() {
        stack = new Stack<SearchQuery>();
    }

    public void emtyStack() {
		stack.clear();
    }

    public List<MediaModel> previousPage() {
        if (stack.size() <= 1) {
            return Collections.EMPTY_LIST;
        }

        stack.pop();
        SearchQuery back = stack.pop();
        LOG.d("previous result page:", back.getSearchBy(), back.getParam1(), back.getParam2());
        try {
            return getSearchResult(back);
        } catch (VkErrorException e) {
            return Collections.emptyList();
        }
    }

    public List<MediaModel> getSearchResult(SearchQuery searchQuery, boolean clean) throws VkErrorException {
        stack.clear();
        return getSearchResult(searchQuery);
    }

    public List<MediaModel> getSearchResult(SearchQuery searchQuery) throws VkErrorException {
		if (searchQuery == null) {
            LOG.d("SearchQuery is empty");
            return Collections.emptyList();
		}

        LOG.d("Search By L1", searchQuery.getSearchBy(), searchQuery.getParam1(), searchQuery.getParam2());

        if (searchQuery.getSearchBy() == SearchBy.BACK_BUTTON) {
			if (stack.size() >= 2) {
				stack.pop();
			}
            SearchQuery back = stack.pop();
            LOG.d("back", back.getSearchBy(), back.getParam1(), back.getParam2());
            return getSearchResult(back);
        } else {
            List<MediaModel> result = new ArrayList<MediaModel>();

            if (!stack.isEmpty()) {
                MediaModel back = MediaModelBuilder.SearchFolder("..", new SearchQuery(SearchBy.BACK_BUTTON, ".."));
                result.add(back);
            }
            stack.push(searchQuery);
            result.addAll(getSearchResultProccess(searchQuery));
            return result;
        }
    }

    protected abstract List<MediaModel> getSearchResultProccess(SearchQuery searchQuery) throws VkErrorException;

	public Stack<SearchQuery> getStack() {
	    return stack;
    }

	public void setStack(Stack<SearchQuery> stack) {
		this.stack = stack;
	}

}
