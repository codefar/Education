package com.education.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.education.R;

/**
* Created by su on 2015/1/28.
*/
public class ShareDialog extends Dialog {

    public static final int SHARE_WEBCHAT_FRIEND_CIRCLE = 1;
    public static final int SHARE_WEBCHAT_FRIEND = 2;
    public static final int SHARE_SMS = 3;
    private OnDismissListener l;
    private int mType;

    public ShareDialog(Context context) {
        super(context);

        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.ShareDialogAnimation);
        window.setBackgroundDrawableResource(R.color.share_bg_transparent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_share, null);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.webchat_friend_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = SHARE_WEBCHAT_FRIEND_CIRCLE;
                dismiss();
            }
        });

        view.findViewById(R.id.webchat_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = SHARE_WEBCHAT_FRIEND;
                dismiss();
            }
        });

        view.findViewById(R.id.sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = SHARE_SMS;
                dismiss();
            }
        });
        setContentView(view);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (l != null) {
                    l.onDismiss(mType);
                }
            }
        });
    }

    public interface OnDismissListener {
        public void onDismiss(int type);
    }

    public void setOnDismissListener (OnDismissListener onDismissListener) {
        l = onDismissListener;
    }
}
