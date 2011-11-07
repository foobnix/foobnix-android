package com.foobnix.util;

import com.foobnix.broadcast.model.UIBroadcast;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;

public class InfoTextHelper {

    public static String infoLineStatus(UIBroadcast stat) {
        FModel model = stat.getModel();
        String pos = String.format("%s/%s", model.getPosition() + 1, stat.getPlaylistLen());
        String album = model.getAlbum() == null || model.getAlbum().equals("null") ? model.getText() : model.getAlbum();
        if (model.getType() == TYPE.LOCAL) {
            return String.format("%s %s", pos, album);
        }

        if (model.getType() == TYPE.ONLINE) {
            String buffer = stat.getBuffering() + "%";
            if (stat.getBuffering() == -1) {
                buffer = "Erorr";
            }

            return String.format("%s|%s|%s|%s", pos, buffer, model.getSize(), album);
        }

        return stat.getModel().getText();

    }
}
