package ufps.edu.co.appnotiufps;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Stack;

public class Inicio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String url_sitio="http://sandbox1.ufps.edu.co/~ufps_17/notiufps/";
    private Stack<MenuItem> pilaItem;
    private MenuItem itemActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Guardar url del servidor
        SharedPreferences config = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editar = config.edit();
        editar.putString("url_server","http://sandbox1.ufps.edu.co/~ufps_17/notiufps/");
        editar.commit();
        //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //permisos para conectarse a internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //

        //cargar el frament de inicio

        Fragment fragment=new ViewInicio();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
        getSupportActionBar().setTitle("Inicio");
        //en esta pila se guarda los item de los menus a medida que se clickean
        this.pilaItem=new Stack<MenuItem>();
        //se guarda el item del menu de inicio por primera vez en la pila
        this.pilaItem.push(navigationView.getMenu().getItem(0));

    }


    //este metodo me permite habilitar el poder devolverme para una anterior activity
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            // verificamos que la pila de los item no se encuentre vacia y ademas si el item actual
            //corresponde al del tope de la pila
            if(!this.pilaItem.empty() && this.itemActual==this.pilaItem.get(this.pilaItem.size()-1)) {
                //si la condicion anterior de cumple se eliminar de la pila un item
                this.pilaItem.pop();
            }
            //verificamos que la pila no se encuentre vacia
                if (!this.pilaItem.empty()) {
                    //agregamos el nuevo item a la pila
                    MenuItem item=this.pilaItem.pop();
                    this.itemActual=item;
                    this.reescribirTitleBar(item);
                    this.pilaItem.push(item);
                }

            super.onBackPressed();
        }
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

    //este meotodo me permite seleccionar el evento de una de las opciones del menu de la parte
    //izquierda de la aplicacion
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;
        switch(id){
            case R.id.nav_configuracion:
                fragment=new Configuracion();
                getSupportActionBar().setTitle("Preferencias del usuario");
                break;
            case R.id.nav_calendario:
                fragment=new Calendario();
                getSupportActionBar().setTitle("Calendario de eventos");
                break;
            case R.id.nav_internet:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(this.url_sitio));
                this.startActivity(intent);
                break;
            case R.id.nav_ayuda:
                fragment=new AcercaDe();
                getSupportActionBar().setTitle("Acerca de NotiUFPS");
                break;
            case R.id.nav_inicio:
                fragment=new ViewInicio();
                getSupportActionBar().setTitle("Inicio");
                break;
        }

        if(this.itemActual!=item && fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
            this.pilaItem.push(item);
            this.itemActual=item;
            item.setChecked(true);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //metodo que se utiliza cuando el usuario le da boton hacia atras
    //este metodo reecribe el titulo de el MenuBar
    private void reescribirTitleBar(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.nav_configuracion:
                getSupportActionBar().setTitle("Preferencias del usuario");
                break;
            case R.id.nav_calendario:
                getSupportActionBar().setTitle("Calendario de eventos");
                break;

            case R.id.nav_ayuda:
                getSupportActionBar().setTitle("Acerca de NotiUFPS");
                break;
            case R.id.nav_inicio:
                getSupportActionBar().setTitle("Inicio");
                break;
        }
            item.setChecked(true);
    }
    //metodo para pasar a una siquiete activity
    private void pasarActivity(Class clase){
        Intent activity=new Intent(getApplicationContext(),clase);
        startActivity(activity);
    }


}
