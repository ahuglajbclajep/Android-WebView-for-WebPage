package ahuglajbclajep.android.webview.showmypage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            private ProgressDialog loading;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Uri.parse(getString(R.string.url)).getHost().equals(Uri.parse(url).getHost())) return false;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
                return true;
            }

            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon) {
                loading = ProgressDialog.show(MainActivity.this, "Now Loading", "please wait", true, true,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            webView.stopLoading();
                            onBackPressed();
                        }
                    });
            }

            @Override
            public void onPageFinished (WebView view, String url) {
                loading.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, "Connection failed...", Toast.LENGTH_SHORT).show();
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }
        });

        webView.loadUrl(getString(R.string.url));
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
