package com.example.androidexample;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Custom Glide module for the application.
 * This class is used to configure and customize Glide, a popular image loading and caching library.
 * By extending {@link AppGlideModule}, it enables advanced Glide features and optimizations.
 *
 * Note: The {@link GlideModule} annotation is required for Glide to recognize this module.
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    // No additional methods or configurations are required unless specific customizations are needed.
}
