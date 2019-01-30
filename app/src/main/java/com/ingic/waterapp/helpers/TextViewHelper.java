package com.ingic.waterapp.helpers;

import android.os.Build;
import android.text.Html;
import android.widget.TextView;

import com.ingic.waterapp.ui.views.Util;


public class TextViewHelper {
    public static void setText(TextView textView, String text) {
        if (textView == null) return;
        if (Util.isNotNullEmpty(text)) {
            textView.setText(text);
        }
    }

    public static void setHtmlText(TextView textView, String text) {
        if (textView == null) return;
        if (Util.isNotNullEmpty(text)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
            } else
                textView.setText(Html.fromHtml(text));
        }else{
            textView.setText("");
        }
    }
}
