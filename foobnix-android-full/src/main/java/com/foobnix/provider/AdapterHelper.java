package com.foobnix.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;

public abstract class AdapterHelper<T> {
    Collection<T> list;

    public AdapterHelper(Collection<T> list) {
        this.list = list;
    }

    public List<FModel> getFModels() {
        List<FModel> results = new ArrayList<FModel>();
        int num = 1;
        for (T model : list) {
            FModel fmodel = getModel(model);

			// skip dublicates in search
			if (results.contains(fmodel)) {
				continue;
			}

			fmodel.setType(TYPE.ONLINE);
            fmodel.setTrackNum(num);
            results.add(fmodel);
            num++;
        }
        return results;
    }

    public abstract FModel getModel(T entry);
}