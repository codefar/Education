package com.education.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.education.EduApp;
import com.education.R;
import com.education.entity.Share;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
* Created by su on 2015/1/28.
*/
public class ShareDialog extends Dialog {

    private IWXAPI api;

    private static final int THUMB_SIZE = 150;

    public ShareDialog(Context context, final Share share) {
        super(context);

        api = WXAPIFactory.createWXAPI(context, EduApp.WX_APP_ID);
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
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = makeWXMediaMessage(share);
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);

                dismiss();
            }
        });

        view.findViewById(R.id.webchat_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = makeWXMediaMessage(share);
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);

                dismiss();
            }
        });
        setContentView(view);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private WXMediaMessage makeWXMediaMessage(Share share) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = share.getUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share.getTitle();
        msg.description = share.getDescription();
        if (share.getImgUrl() != null) {
            try {
                URL url = new URL(share.getImgUrl());
                Bitmap bmp = BitmapFactory.decodeStream(url.openStream());
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                bmp.recycle();
                msg.thumbData = bmpToByteArray(thumbBmp, true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
