package app.smartify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrivacyPolicy extends AppCompatActivity {
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebViewClient());
        openURL();
    }

    /**
     * Opens the URL in a browser
     */
    private void openURL() {
        webview.loadUrl("https://github.com/Blackhat96/Smartify/blob/master/privacy_policy_smartify.md#privacy-policy");
        webview.requestFocus();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
