package org.barcamprd.android.utils;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vinrosa on 10/19/13.
 */
public class ImageCache {
    private static Map<String, Drawable> imageCache = new HashMap<String, Drawable>();

    public static Drawable getImage(String photoUrl) {
        if (imageCache.get(photoUrl) != null) return imageCache.get(photoUrl);
        try {
            URL thumb_u = new URL(photoUrl);
            InputStream stream = thumb_u.openStream();
            Drawable d = Drawable.createFromStream(stream, "src");
            stream.close();
            imageCache.put(photoUrl, d);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static Drawable getCachedImage(String photoUrl){
        return imageCache.get(photoUrl);
    }
}
