package com.uwaterloo.fydp.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.cache.LruBitmapCache;
import com.novoda.imageloader.core.model.ImageTagFactory;
import com.uwaterloo.fydp.R;

/**
 * Created by Anthony on 15-02-12.
 */
public class ImageUtil {

    // Manager to asynchronously load images
    private static ImageManager imageManager;

    private static ImageTagFactory imageTagFactory;

    public static final ImageManager getImageManager(Context context) {
        if(imageManager == null) {
            // Initialize image manager
            LoaderSettings loaderSettings = new LoaderSettings.SettingsBuilder().withCacheManager(new LruBitmapCache(context)).build(context);
            imageManager = new ImageManager(context, loaderSettings);
        }

        return imageManager;
    }

    public static final ImageTagFactory getImageTagFactory(Context context) {
        if(imageTagFactory == null) {
            imageTagFactory = ImageTagFactory.newInstance(context, R.drawable.image_loading);
            imageTagFactory.setErrorImageId(R.drawable.image_loading);
        }

        return imageTagFactory;
    }
}
