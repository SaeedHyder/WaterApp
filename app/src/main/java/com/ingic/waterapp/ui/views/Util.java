package com.ingic.waterapp.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.helpers.UIHelper;

import java.util.HashMap;
import java.util.Map;


public class Util {
    private static long lastClickTime;
    public static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

    @SuppressLint("StringFormatInvalid")
    public static void setTypeface(AttributeSet attrs, TextView textView) {
        Context context = textView.getContext();

        TypedArray values = context.obtainStyledAttributes(attrs,
                R.styleable.AnyTextView);
        String typefaceName = values
                .getString(R.styleable.AnyTextView_typeface);

        if (typefaceCache.containsKey(typefaceName)) {
            textView.setTypeface(typefaceCache.get(typefaceName));
        } else {
            Typeface typeface;
            try {
                typeface = Typeface.createFromAsset(textView.getContext()
                                .getAssets(),
                        context.getString(R.string.assets_fonts_folder)
                                + typefaceName);
            } catch (Exception e) {
                Log.v(context.getString(R.string.app), String.format(
                        context.getString(R.string.typeface_not_found),
                        typefaceName));
                return;
            }

            typefaceCache.put(typefaceName, typeface);
            textView.setTypeface(typeface);
        }
        values.recycle();
    }

    @SuppressLint("StringFormatInvalid")
    public static void setTypefaceUpdated(AttributeSet attrs, TextView textView) {
        Context context = textView.getContext();

        TypedArray values = context.obtainStyledAttributes(attrs,
                R.styleable.AnyTextView);
        String typefaceName = values
                .getString(R.styleable.AnyTextView_typeface);

        if (typefaceCache.containsKey(typefaceName)) {
            textView.setTypeface(typefaceCache.get(typefaceName));
        } else {
            Typeface typeface;
            try {
                typeface = Typeface.createFromAsset(textView.getContext()
                                .getAssets(),
                        context.getString(R.string.assets_fonts_folder)
                                + typefaceName);
            } catch (Exception e) {
                Log.v(context.getString(R.string.app), String.format(
                        context.getString(R.string.typeface_not_found),
                        typefaceName));
                return;
            }

            typefaceCache.put(typefaceName, typeface);
            textView.setTypeface(typeface);
        }

        values.recycle();
    }

    public static void openCustomChromeTabs(Context context, @NonNull String url) {
        String errorMessage = context.getResources().getString(R.string.error_url_not_valid);
        if (TextUtils.isEmpty(url) && !URLUtil.isValidUrl(url) && !Patterns.WEB_URL.matcher(url).matches()) {
            UIHelper.showLongToastInCenter(context, errorMessage);
            return;
        }

        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder
                    .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
                    .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception ex) {
            ex.printStackTrace();
            UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.error_failure));
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static boolean isNotNullEmpty(@Nullable String string) {
        return !(string == null || string.trim().length() == 0);
    }

    public static int getParsedInteger(String discountAmount) {
        try {
            return Integer.parseInt(discountAmount);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static double getParsedDouble(String val) {
        try {
            return Double.parseDouble(val);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public static long getParsedLong(String val) {
        try {
            return Long.parseLong(val);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static float getParsedFloat(String val) {
        try {
            return Float.parseFloat(val);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static boolean doubleClickCheck() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return false;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }


    public static float getDiscountedValue(float original, float discountPercent) {
        return Math.abs(original - (discountPercent / 100) * original);
    }

}