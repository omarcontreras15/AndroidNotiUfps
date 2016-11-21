package ufps.edu.co.appnotiufps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by omara on 20/11/2016.
 */

public class ViewEvento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activar el boton de ir hacia atras en el menubar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.view_evento);
        SharedPreferences config = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        String url_server= config.getString("url_server", "")+getIntent().getExtras().getString("url_evento");
        this.cargarWeb(url_server);
    }


    private void cargarWeb(String url_server){
        WebView myWebView = (WebView) this.findViewById(R.id.web_view_evento);
        WebSettings config= myWebView.getSettings();
        config.setJavaScriptEnabled(true);
        myWebView.loadUrl(url_server);
        myWebView.setWebViewClient(new WebViewClient());

    }

    //metodo sobreescrito para activar el boton hacia atras de titleBar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_reportar_problema) {
            //redirigimos al usuario a su cliente de correo y le indicamos
            // el correo de soporte tecnico y el asunto del correo
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "omaarturocv@ufps.edu.co", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Soporte Tecnico NotiUFPS");
            startActivity(Intent.createChooser(emailIntent, null));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
