package ufps.edu.co.appnotiufps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

/**
 * Created by omara on 17/11/2016.
 */

public class Calendario extends Fragment {
    private static String url_server;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences config = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        this.url_server= config.getString("url_server", "")+"eventos_android.php";
        this.view=inflater.inflate(R.layout.calendario, container, false);
        this.cargarWeb();
        return this.view;
    }


    private void cargarWeb(){
        WebView myWebView = (WebView) this.view.findViewById(R.id.webViewEventos);
        WebSettings config= myWebView.getSettings();
        config.setJavaScriptEnabled(true);

        myWebView.loadUrl(this.url_server);
        myWebView.setWebViewClient(new WebViewClient());

    }
}
