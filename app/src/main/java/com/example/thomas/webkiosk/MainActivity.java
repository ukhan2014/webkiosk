package com.example.thomas.webkiosk;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    final Context context = this;
    View promptsView;
    AlertDialog alertDialog;
    WebView webv = null;
    DownloadManager file;
    Long q_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        webv = (WebView) findViewById(R.id.webview);

        webv.setWebChromeClient(new WebChromeClient());
        webv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("rtsp")) {
                    Log.d(TAG, "found rtsp link");
                    Uri uri = Uri.parse(url);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //startActivity(intent);

                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    intent.putExtra("key", url); //Optional parameters
                    startActivity(intent);
                    return true;
                }

                if(url.equals("https://www.hpibet.com/UnsupportedBrowser")){
                    view.loadUrl("https://www.hpibet.com");
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });

        webv.clearCache(true);
        webv.clearHistory();
        webv.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        //Set all the settings
        WebSettings webSettings = webv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.2; msm8960; Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version 4.0 Chrome/30.0.0.0 Safari/537.36");
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4; Nexus 7 Build/KOT24) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.105 Safari/537.36");

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccessFromFileURLs(true); //Maybe you don't need this rule
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowContentAccess(true);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        //webv.loadUrl("http://www.sednove.com/");
        webv.loadUrl("https://www.hpibet.com/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"You Are Not Allowed to Exit the App", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.showAlert) {
            showAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    public void showAlert(){

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String password = userInput.getText().toString();

                if(password.equals("thomas")) {
                    getPackageManager().clearPackagePreferredActivities(getPackageName());
                    final Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    alertDialog.dismiss();
                } else {
                    TextView tv = (TextView) promptsView.findViewById(R.id.errorMessage);
                    tv.setText("Invalid password");
                }
            }
        });
    }
}
