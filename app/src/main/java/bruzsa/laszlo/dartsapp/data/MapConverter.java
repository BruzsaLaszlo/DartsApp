package bruzsa.laszlo.dartsapp.data;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MapConverter {

    @TypeConverter
    public String fromStringMap(Map<String, String> value) {
        final Map<String, String> sortedMap = new TreeMap<>(value);
        return TextUtils.join(",", sortedMap.keySet())
                .concat("<divider>")
                .concat(TextUtils.join(",", sortedMap.values()));
    }

    @TypeConverter
    public Map<String, String> toStringMap(String value) {
        final String[] keysValsSep = value.split("<divider>");
        final String[] keys = keysValsSep[0].split(",");
        final Iterator<String> valuesIterator = Arrays.asList(keysValsSep[1].split(",")).iterator();
        final Map<String, String> strMap = new HashMap<>();

        for (String key : keys) {
            strMap.put(key, valuesIterator.next());
        }
        return strMap;
    }

}
