package com.ingic.waterapp.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ingic.waterapp.R;

public class DialogHelper {
    private Dialog dialog,dialogProfile,dialogeDelete ;
    private Context context;

    public DialogHelper(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialogProfile = new Dialog(context);
        this.dialogeDelete = new Dialog(context);
    }


    public Dialog initlogout(int layoutID,
                             String title,
                             String message,
                             int rightBtnColor,
                             int leftBtnColor,
                             View.OnClickListener onokclicklistener,
                             View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);

        TextView tvMessage = (TextView) dialog.findViewById(R.id.txt_message);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.txt_title);
        Button btnYes = (Button) dialog.findViewById(R.id.dialog_yes);
        Button btnNo = (Button) dialog.findViewById(R.id.dialog_no);

        btnYes.setTextColor(leftBtnColor);
        btnNo.setTextColor(rightBtnColor);
        btnYes.setOnClickListener(onokclicklistener);
        btnNo.setOnClickListener(oncancelclicklistener);

        if (title.isEmpty()) {
            tvTitle.setVisibility(View.GONE);
        } else
            tvTitle.setText(title);
        tvMessage.setText(message);

        return this.dialog;
    }

    public Dialog initDeleteItem(View.OnClickListener onokclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialoge_delete);

        TextView tvMessage = (TextView) dialog.findViewById(R.id.txt_message);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.txt_title);
        Button btnYes = (Button) dialog.findViewById(R.id.dialog_yes);
        Button btnNo = (Button) dialog.findViewById(R.id.dialog_no);

        btnYes.setOnClickListener(onokclicklistener);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

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

    public Dialog initProfile(int layoutID,
                             String message,
                             View.OnClickListener onokclicklistener,
                             View.OnClickListener oncancelclicklistener) {
        dialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialogProfile.setContentView(layoutID);

        TextView tvMessage = (TextView) dialogProfile.findViewById(R.id.txt_message);
        Button btnYes = (Button) dialogProfile.findViewById(R.id.dialog_yes);
        Button btnNo = (Button) dialogProfile.findViewById(R.id.dialog_no);

        btnYes.setOnClickListener(onokclicklistener);
        btnNo.setOnClickListener(oncancelclicklistener);

        tvMessage.setText(message);

        return this.dialogProfile;
    }
    public void showDialogProfile() {
        dialogProfile.setCancelable(false);
        dialogProfile.setCanceledOnTouchOutside(false);
        dialogProfile.show();
    }

    public void setCancelableProfile(boolean isCancelable) {
        dialogProfile.setCancelable(isCancelable);
        dialogProfile.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialogProfile() {
        dialogProfile.dismiss();
    }

}
