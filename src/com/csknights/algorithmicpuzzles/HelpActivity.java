package com.csknights.algorithmicpuzzles;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class HelpActivity extends Activity {

	private WebView webview;

	

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.web_view);

        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

       
        webview.loadUrl("file:///android_asset/" +
			  	getResources().getString(R.string.help_file_name));


			}
}
