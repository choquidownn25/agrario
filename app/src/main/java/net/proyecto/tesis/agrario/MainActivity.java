package net.proyecto.tesis.agrario;

/**
 * Created by choqu_000 on 17/04/2015.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import net.proyecto.tesis.agrario.dispositivos.webcam.WebCam;
import net.proyecto.tesis.agrario.mapa_arcgis.Identify;
import net.proyecto.tesis.agrario.mapa_arcgis.Mapa_Prueba_Base;
import net.proyecto.tesis.agrario.vista.Tabs;
import net.proyecto.tesis.agrario.vista.View_Lista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    int mPosition = -1;
    String mTitle = "";

    // Array of strings storing country names
    String[] mCountries ;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] mFlags = new int[]{

            R.drawable.india,
            R.drawable.bangladesh,
            R.drawable.nepal,
            R.drawable.china,
            R.drawable.afghanistan,
            R.drawable.pakistan,
            R.drawable.profile,
            R.drawable.srilanka,
            R.drawable.nkorea,
            R.drawable.japan

    };
    // Array of strings to initial counts
    String[] mCount = new String[]{
            "", "", "", "", "",
            "", "", "", "", "" };

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawer ;
    private List<HashMap<String,String>> mList ;
    private SimpleAdapter mAdapter;
    final private String COUNTRY = "country";
    final private String FLAG = "flag";
    final private String COUNT = "count";
    private String[] titulos;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting an array of country names
        //mCountries = getResources().getStringArray(R.array.countries);
        //nav_options
        mCountries = getResources().getStringArray(R.array.nav_options);

        // Title of the activity
        mTitle = (String)getTitle();

        // Getting a reference to the drawer listview
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        // Getting a reference to the sidebar drawer ( Title + ListView )
        mDrawer = ( LinearLayout) findViewById(R.id.drawer);
        mActivityTitle = getTitle().toString();
        // Each row in the list stores country name, count and flag
        mList = new ArrayList<HashMap<String,String>>();


        for(int i=0;i<10;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put(COUNTRY, mCountries[i]);
            hm.put(COUNT, mCount[i]);
            hm.put(FLAG, Integer.toString(mFlags[i]) );
            mList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { FLAG,COUNTRY,COUNT };

        // Ids of views in listview_layout
        int[] to = { R.id.flag , R.id.country , R.id.count};

        // Instantiating an adapter to store each items
        // R.layout.drawer_layout defines the layout of each item
        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from, to);

        // Getting reference to DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        // Creating a ToggleButton for NavigationDrawer with drawer event listener
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer , R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                highlightSelectedCountry();
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Tematicas");
                supportInvalidateOptionsMenu();
            }
        };

        // Setting event listener for the drawer
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        //setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Establecemos la accion al clickear sobre cualquier item del menu.
        //De la misma forma que hariamos en una app comun con un listview.
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                MostrarFragment(position);
            }
        });

        //Cuando la aplicacion cargue por defecto mostrar la opcion Home
        MostrarFragment(1);


        // Enabling Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Setting the adapter to the listView
        mDrawerList.setAdapter(mAdapter);

    }


    /*Pasando la posicion de la opcion en el menu nos mostrara el Fragment correspondiente*/
    private void MostrarFragment(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 1:
                //fragment = new Prueba();
                subcargaPruebaVista();
                break;
            case 2:
                subCarga();
                break;

            case 3:
                subArcgisCarga();
                break;

            case 4:
                subcargaCamara();
                break;

            case 5:
                subcargaIdentifale();
                break;

            case 6:
                sucargaForm();
                break;
            default:
                //si no esta la opcion mostrara un toast y nos mandara a Home
                Toast.makeText(getApplicationContext(), "Opcion " + titulos[position - 1] + "no disponible!", Toast.LENGTH_SHORT).show();
                fragment = new Prueba();
                position = 1;
                break;
        }
        //Validamos si el fragment no es nulo
        if(position > 11) { // Show fragment for countries : 0 to 4
            //showFragment(position);
            //subCarga();
            Toast.makeText(getApplicationContext(), mCountries[position], Toast.LENGTH_LONG).show();
        }
        //else{ // Show message box for countries : 5 to 9
        //    Toast.makeText(getApplicationContext(), mCountries[position], Toast.LENGTH_LONG).show();
        //}
    }
    //metodo para cargar la camara
    private void  subcargaCamara(){
        Intent intent = new Intent(MainActivity.this, WebCam.class);
        startActivity(intent);
    }

    private void sucargaForm(){
        Intent intent = new Intent(MainActivity.this, Tabs.class);
        startActivity(intent);
    }
    //metodo para cargar la camara
    private void  subcargaPruebaVista(){
        //Intent intent = new Intent(MainActivity.this, VistaFormularioEjecucion.class);
        //startActivity(intent);
    }
    private void subCarga(){

        Intent intent = new Intent(MainActivity.this, View_Lista.class);
        startActivity(intent);
    }

    private void subArcgisCarga(){
        Intent intent = new Intent(MainActivity.this, Mapa_Prueba_Base.class);
        startActivity(intent);
    }

    private void subcargaIdentifale(){
        Intent intent = new Intent(MainActivity.this, Identify.class);
        startActivity(intent);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    //Menu para cambiar el icono del deslizante a flecha
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void incrementHitCount(int position){
        HashMap<String, String> item = mList.get(position);
        String count = item.get(COUNT);
        item.remove(COUNT);
        if(count.equals("")){
            count = "  1  ";
        }else{
            int cnt = Integer.parseInt(count.trim());
            cnt ++;
            count = "  " + cnt + "  ";
        }
        item.put(COUNT, count);
        mAdapter.notifyDataSetChanged();
    }

    public void showFragment(int position){

        //Currently selected country
        mTitle = mCountries[position];

        // Creating a fragment object
        CountryFragment cFragment = new CountryFragment();

        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putInt("position", position);

        // Setting the position to the fragment
        cFragment.setArguments(data);

        // Getting reference to the FragmentManager
        FragmentManager fragmentManager  = getSupportFragmentManager();

        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, cFragment);

        // Committing the transaction
        ft.commit();
    }

    // Highlight the selected country : 0 to 4
    public void highlightSelectedCountry(){
        int selectedItem = mDrawerList.getCheckedItemPosition();

        if(selectedItem > 4)
            mDrawerList.setItemChecked(mPosition, true);
        else
            mPosition = selectedItem;

        if(mPosition!=-1)
            getSupportActionBar().setTitle(mCountries[mPosition]);
    }

}
