package com.education;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.education.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by su on 2014/9/28.
 */
public class WebViewActivity extends CommonBaseActivity {

    private static final String TAG = "WebViewActivity";
    private Resources mResources;
    private ProgressBar progressbar;
    private String mTitle;
    private WebView mWebView;
    private HashMap<String, String> mHeader;
    private List<String> mHeaderList = new ArrayList<String>();
    private long mLastTime = 0;
    private long mInterval = 40L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        setContentView(mWebView);
        mResources = getResources();

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");

        setupTitleBar();
        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        addContentView(progressbar, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mResources.getDimensionPixelOffset(R.dimen.dimen_3_dip)));

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        webSettings.setDomStorageEnabled(true);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) { //设置 加载进程
                if (progress == 100) {
                    progressbar.setVisibility(View.GONE);
                } else {
                    if (progressbar.getVisibility() == View.GONE) {
                        progressbar.setVisibility(View.VISIBLE);
                    }
                    progressbar.setProgress(progress);
                }
            }
        });
        webSettings.setBuiltInZoomControls(true);
        mWebView.setInitialScale(50);//25%为最小缩放等级.
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.stopLoading();
                view.clearView();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url, mHeader);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (mHeaderList.size() == 0) {
                    mHeaderList.add(url);
                } else {
                    String lastUrl = mHeaderList.get(mHeaderList.size() - 1);
                    if (!lastUrl.equals(url)) {
                        mHeaderList.add(url);
                    }
                }
            }
        });

        mHeader = new HashMap<String, String>();
        mWebView.loadUrl(url, mHeader);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void unLoginForward(User user) {

    }


    @Override
    protected void forceUpdateForward() {

    }

    @Override
    protected void setupTitleBar() {
        ActionBar bar = getActionBar();
        bar.setTitle(mTitle);
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
