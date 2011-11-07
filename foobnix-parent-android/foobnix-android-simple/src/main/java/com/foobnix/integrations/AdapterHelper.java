package com.foobnix.integrations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.foobnix.mediaengine.MediaModel;

public abstract class AdapterHelper<T> {
    Collection<T> list;

    public AdapterHelper(Collection<T> list) {
        this.list = list;
    }

    public List<MediaModel> getMediaModels() {
        List<MediaModel> results = new ArrayList<MediaModel>();
        int num = 1;
        for (T model : list) {
            MediaModel fmodel = getModel(model);

			// skip dublicates in search
			if (results.contains(fmodel)) {
				continue;
			}

            // fmodel.setType(TYPE.ONLINE);
            // fmodel.setTrackNum(num);
            results.add(fmodel);
            num++;
        }
        return results;
    }

    public abstract MediaModel getModel(T entry);
}