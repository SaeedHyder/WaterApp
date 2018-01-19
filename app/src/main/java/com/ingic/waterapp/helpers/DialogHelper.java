package com.ingic.waterapp.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.waterapp.R;

public class DialogHelper {
    private Dialog dialog;
    private Context context;

    public DialogHelper(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }


    public Dialog initlogout(int layoutID,String title, String message, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);

        TextView tvMessage = (TextView) dialog.findViewById(R.id.txt_message);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.txt_title);
        Button btnYes = (Button) dialog.findViewById(R.id.dialog_yes);
        Button btnNo = (Button) dialog.findViewById(R.id.dialog_no);

        btnYes.setOnClickListener(onokclicklistener);
        btnNo.setOnClickListener(oncancelclicklistener);

        tvTitle.setText(title);
        tvMessage.setText(message);

        return this.dialog;
    }


    public void showDialog() {
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialog.dismiss();
    }

}
