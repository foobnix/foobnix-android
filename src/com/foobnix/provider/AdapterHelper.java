package com.foobnix.provider;

import java.util.ArrayList;
import java.util.List;

import com.foobnix.model.FModel;

public abstract class AdapterHelper<T> {
		List<T> list;

		public AdapterHelper(List<T> list) {
			this.list = list;
		}

		public List<FModel> getFModels() {
			List<FModel> results = new ArrayList<FModel>();
			int num = 1;
			for (T model : list) {
				FModel fmodel = getModel(model);

				fmodel.setTrackNum(num);
				results.add(fmodel);
				num++;
			}
			return results;
		}

		public abstract FModel getModel(T entry);
	}