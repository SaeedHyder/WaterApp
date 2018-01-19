package com.ingic.waterapp.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.waterapp.R;

public class TitleBar extends RelativeLayout {

    private RelativeLayout layoutHeader;
    private TextView txtTitle;
    private ImageView btnLeft;
    private ImageView btnRight;
    private ImageView btnCart;


    private View.OnClickListener menuButtonListener;
    private OnClickListener backButtonListener;
    private OnClickListener notificationButtonListener;
    private OnClickListener cartButtonListener;

    private Context context;


    public TitleBar(Context context) {
        super(context);
        this.context = context;
        initLayout(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void bindViews() {
        layoutHeader = (RelativeLayout) this.findViewById(R.id.header_layout);
        txtTitle = (TextView) this.findViewById(R.id.txt_subHead);
        btnRight = (ImageView) this.findViewById(R.id.btnRight);
        btnLeft = (ImageView) this.findViewById(R.id.btnLeft);
        btnCart = (ImageView) this.findViewById(R.id.btnCart);
    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_main, this);
        bindViews();
    }


    public void clearHeaderBackround() {
        layoutHeader.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void hideButtons() {
        txtTitle.setVisibility(View.INVISIBLE);
        btnLeft.setVisibility(View.INVISIBLE);
        btnRight.setVisibility(View.INVISIBLE);
        btnCart.setVisibility(View.INVISIBLE);
    }

    public void showBackButton() {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(backButtonListener);
        btnLeft.setImageResource(R.drawable.ic_back);
        layoutHeader.setBackgroundResource(R.drawable.header);
    }

    public void showMenuButton() {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(menuButtonListener);
        btnLeft.setImageResource(R.drawable.ic_nav);
        btnRight.setVisibility(View.VISIBLE);
        btnCart.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(notificationButtonListener);
        btnCart.setOnClickListener(cartButtonListener);
    }

    public void setSubHeading(String heading) {
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(heading);
    }

    public void showTitleBar() {
        this.setVisibility(View.VISIBLE);
    }

    public void hideTitleBar() {
        this.setVisibility(View.GONE);
    }

    public void setMenuButtonListener(View.OnClickListener listener) {
        menuButtonListener = listener;
    }

    public void setBackButtonListener(View.OnClickListener listener) {
        backButtonListener = listener;
    }

    public void setNotificationButtonListener(View.OnClickListener listener) {
        notificationButtonListener = listener;
    }

    public void setCartButtonListener(View.OnClickListener listener) {
        cartButtonListener = listener;
    }
}
