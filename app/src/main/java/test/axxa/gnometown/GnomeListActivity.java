package test.axxa.gnometown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GnomeListActivity extefnds Activity {

    private static final String TAG = "GnomeListActivity";
    public static final TownManager tm = new TownManager();
    public ArrayList<Gnome> population = new ArrayList<Gnome>();
    public ArrayList<Gnome> population_raw = new ArrayList<Gnome>();
    static GnomeListActivity ga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        setContentView(R.layout.activity_gnome_list);
        ga = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = this;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).delayBeforeLoading(1).showImageOnLoading(R.drawable.light_blue_material_design_loading).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        try {
            if(population.isEmpty()){
                new AsyncGnomeFetcher(GnomeListActivity.this).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Async task manager*/
    class AsyncGnomeFetcher extends AsyncTask<String, String, Void> {

        private ListView lv;
        private ProgressDialog progressDialog = new ProgressDialog(GnomeListActivity.this);
        private Context context;
        InputStream inputStream = null;
        String result = "";

        public AsyncGnomeFetcher(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            progressDialog.setMessage("Reading towns census, searching for gnomes...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    AsyncGnomeFetcher.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {
            URL population_url;
            HttpURLConnection urlConnection = null;
            try {
                population_url = new URL(Constants.TOWN_JSON_URL);

                urlConnection = (HttpURLConnection) population_url.openConnection();

                inputStream = urlConnection.getInputStream();

                InputStreamReader isr = new InputStreamReader(inputStream);

                try {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }

                    inputStream.close();
                    result = sBuilder.toString();

                } catch (Exception e) {
                    Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            progressDialog.dismiss();
            try{
                Log.e("X","RESULT: " + result);
                InputStream is = new ByteArrayInputStream( result.getBytes("UTF-8") );
                tm.readPopulation(is);

                population = (ArrayList<Gnome>) tm.town.getPopulation();
                for(int i=0;i<population.size();i++){
                    population_raw.add(population.get(i));
                }

                final GnomeAdapter adapter = new GnomeAdapter(context, (ArrayList<Gnome>)population);

                final ListView listView_filtered = (ListView) findViewById(R.id.gnome_filtered_list);
                listView_filtered.setAdapter(adapter);

                final EditText inputSearch = (EditText) findViewById(R.id.search_bar);
                Button searchButton  = (Button) findViewById(R.id.searchButton);

                listView_filtered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                        Gnome currentGnome = new Gnome();
                        currentGnome = population.get(position);
                        Intent appInfo = new Intent(context, GnomeProfile.class);
                        System.out.println(currentGnome.getName());
                        appInfo.putExtra("Gnome",currentGnome);
                        startActivity(appInfo);
                    }
                });

                searchButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    CharSequence cs = inputSearch.getText();
                    if(!inputSearch.getText().toString().matches("")){
                        adapter.clear();
                        adapter.addAll(population_raw);
                        adapter.notifyDataSetChanged();
                        //Cleaning the adapter so we populate it again with raw data (letting it to filter every gnome again)
                        adapter.getFilter().filter(cs);
                        adapter.notifyDataSetChanged();

                    }
                    else{
                        //Repopulating the adapter if the filter is empty
                        adapter.clear();
                        adapter.addAll(population_raw);
                        adapter.notifyDataSetChanged();
                    }
                    }

                });
            }catch(UnsupportedEncodingException e ){
                e.printStackTrace();
            }catch(IOException ei){
                ei.printStackTrace();
            }
        }
    }

    public void showProfile(){
        Intent intent = new Intent(this, GnomeProfile.class);
        startActivity(intent);
    }

    public static GnomeListActivity getInstance(){
        return   ga;
    }
}
