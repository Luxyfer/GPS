package com.isec.proto.heliumblue2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class MenuP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridView;
    ImageButton procura;

    int screenSize;
    int searchSize = 4;

    public MenuProcura procura_menu;

    public boolean online = true;
    public boolean usingCache = false;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_p);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);                //Botao de email, podemos alterar para enviar feedback sobre a app ??
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        context = this;
        Ion.getDefault(this).configure().setLogging("Main", Log.DEBUG);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gridView = (GridView) findViewById(R.id.gridView);

        //Metodos para determinar tamanho do ecra e determinar num de colunas a mostrar
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        screenSize = display.getWidth();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;

        if (dpWidth < 400) {
            gridView.setNumColumns(3);
            searchSize = 3;
        } else if (dpWidth > 600) {
            gridView.setNumColumns(5);
            searchSize = 5;
        }

        procura = (ImageButton) findViewById(R.id.procura);
        procura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procura_menu.procura();
            }
        });

        if (!Util.isOnline(this)) {
            online = false;

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Sem ligação à Internet !", Snackbar.LENGTH_LONG)
                    .setAction("CONFIRMAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            usingCache = true;
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridView.setAdapter(new AdaptadorGridP(context, new Parser().maisVistas()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("Main", "A iniciar menu de settings");
            Intent myIntent = new Intent(MenuP.this, SettingsActivity.class);
            myIntent.putExtra("Online", usingCache);
            this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            findViewById(R.id.menu1).setVisibility(View.GONE);
            findViewById(R.id.menu2).setVisibility(View.VISIBLE);
            procura_menu = new MenuProcura();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            findViewById(R.id.menu1).setVisibility(View.VISIBLE);
            findViewById(R.id.menu2).setVisibility(View.GONE);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Classe personalizada da GridView para alojar um relativelayout com o botao e a label; Storm 21/11/15
    public class AdaptadorGridP extends BaseAdapter {

        private Context mContext;
        private ArrayList<Serie> series;
        private boolean inicial = false;

        private Integer[] mThumbIds = {
                R.drawable.android_2
        };

        public AdaptadorGridP(Context c, ArrayList<Serie> s) {
            mContext = c;
            series = s;
            inicial = true;

            Integer[] temp = new Integer[s.size()];

            for (int i = 0; i < s.size(); i++) {
                temp[i] = mThumbIds[0];
            }

            mThumbIds = temp;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int arg0) {
            return series.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View grid;

            if(convertView==null){
                grid = new View(mContext);
                LayoutInflater inflater=getLayoutInflater();
                grid=inflater.inflate(R.layout.gridview_custom, parent, false);
            }else{
                grid = convertView;
            }

            final ImageView imageButton = (ImageView) grid.findViewById(R.id.image1);
            imageButton.setImageResource(mThumbIds[position]);

            if (position < series.size()) {
                TextView tx = (TextView) grid.findViewById(R.id.textView2);
                tx.setText(series.get(position).getTitulo());
                tx.setText("");
                UrlImageViewHelper.setUrlDrawable(imageButton, series.get(position).getUrl_imagem().toString());

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView img1;
                        TextView txt_desc;
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        PopupWindow pw1 = new PopupWindow(inflater.inflate(R.layout.popup_serie_1, null, false), 400, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                        pw1.setBackgroundDrawable(new BitmapDrawable());
                        pw1.setOutsideTouchable(true);
                        pw1.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                        pw1.setAnimationStyle(R.style.animation);

                        img1 = (ImageView) pw1.getContentView().findViewById(R.id.imagem_de_serie_pp);
                        txt_desc = (TextView) pw1.getContentView().findViewById(R.id.desc_de_serie);
                        Ion.with(img1)
                                .placeholder(android.R.drawable.spinner_background)
                                .error(android.R.drawable.stat_notify_error)
                                .load(series.get(position).getUrl_imagem().toString());

                        txt_desc.setText(Html.fromHtml(series.get(position).getDesc()));

                        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                        pw1.setHeight(Math.round(dpHeight) + Math.round((dpHeight / 12)));

                        Log.i("Grid view item", "A abrir info de serie com ID " + series.get(position).getId());
                        pw1.update();
                        pw1.showAtLocation(imageButton, Gravity.CENTER, 0, 0);
                    }
                });
            }

            return grid;
        }
    }

    public class MenuProcura {
        private EditText caixa_texto;
        private GridView gv;
        private ArrayList<Serie> s;

        public MenuProcura() {
            caixa_texto = (EditText) findViewById(R.id.nomeSerie1);
            gv = (GridView) findViewById(R.id.gv_pesquisa);
        }

        public void procura(){
            s = new Parser().procurarSeries(caixa_texto.getText().toString());
            if (s.size() != 0) {
                Log.i("Procura", "Encontrou " + s.size() + " series, a primeira sendo " + s.get(0).getTitulo());

                gv.setNumColumns(searchSize);
                gv.setVisibility(View.VISIBLE);
                gv.setAdapter(new AdaptadorGridP(context, s));
            } else {
                gv.setVisibility(View.GONE);
                Log.i("Procura", "Não foram encontradas séries com o nome " + caixa_texto.getText().toString());
                Toast.makeText(context, getString(R.string.toast_search_not_found) + caixa_texto.getText().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}


