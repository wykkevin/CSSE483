package edu.rosehulman.wangy16.exam2bywangy16;

/**
 * Created by localmgr on 3/31/2018.
 */

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Matt Boutell on 3/28/2018.
 * Rose-Hulman Institute of Technology.
 * Covered by MIT license.
 */
public class FileUtils {
    public static List<Territory> loadFromJsonArray(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.state_info_json);
        List<Territory> territories = null;
        try {
            territories = new ObjectMapper().readValue(is, new TypeReference<List<Territory>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return territories;
    }

}

