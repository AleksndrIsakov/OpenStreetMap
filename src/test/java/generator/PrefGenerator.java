package generator;

import model.Preference;

import java.util.ArrayList;
import java.util.List;

public class PrefGenerator {

    public static List<Preference> list(String key_prefix, String val_prefix, int count, boolean isUnique) {
        List<Preference> prefs = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String key = key_prefix + ((isUnique) ? "_" + i : "");
            String value = val_prefix + ((isUnique) ? "_" + i: "");
            prefs.add(generate(key,value));
        }
        return prefs;
    }

    public static Preference generate(String key, String value) {
        Preference preference = new Preference();
        preference.setK(key);
        preference.setV(value);
        return preference;
    }

}
