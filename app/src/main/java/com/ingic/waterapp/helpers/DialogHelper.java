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
    private Dialog dialogLogout ,dialogProfile ;
    private Context context;

    public DialogHelper(Context context) {
        this.context = context;
        this.dialogLogout = new Dialog(context);
        this.dialogProfile = new Dialog(context);
    }


    public Dialog initlogout(int layoutID,
                             String title,
                             String message,
                             int rightBtnColor,
                             int leftBtnColor,
                             View.OnClickListener onokclicklistener,
                             View.OnClickListener oncancelclicklistener) {
        dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialogLogout.setContentView(layoutID);

        TextView tvMessage = (TextView) dialogLogout.findViewById(R.id.txt_message);
        TextView tvTitle = (TextView) dialogLogout.findViewById(R.id.txt_title);
        Button btnYes = (Button) dialogLogout.findViewById(R.id.dialog_yes);
        Button btnNo = (Button) dialogLogout.findViewById(R.id.dialog_no);

        btnYes.setTextColor(leftBtnColor);
        btnNo.setTextColor(rightBtnColor);
        btnYes.setOnClickListener(onokclicklistener);
        btnNo.setOnClickListener(oncancelclicklistener);

        if (title.isEmpty()) {
            tvTitle.setVisibility(View.GONE);
        } else
            tvTitle.setText(title);
        tvMessage.setText(message);

        return this.dialogLogout;
    }


    public void showDialog() {
        dialogLogout.setCancelable(false);
        dialogLogout.setCanceledOnTouchOutside(false);
        dialogLogout.show();
    }

    public void setCancelable(boolean isCancelable) {
        dialogLogout.setCancelable(isCancelable);
        dialogLogout.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialogLogout.dismiss();
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
