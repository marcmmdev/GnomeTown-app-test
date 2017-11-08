package test.axxa.gnometown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class GnomeListActivity extends Activity {

    private static final String TAG = "GnomeListActivity";
    public static final TownManager tm = new TownManager();
    public ArrayList<Gnome> population = new ArrayList<Gnome>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).delayBeforeLoading(1).showImageOnLoading(R.drawable.light_blue_material_design_loading).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        try {
            new AsyncGnomeFetcher(GnomeListActivity.this).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_gnome_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = this;
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
                final GnomeAdapter adapter = new GnomeAdapter(context, (ArrayList<Gnome>)population);
                ListView listView = (ListView) findViewById(R.id.gnome_list);
                listView.setAdapter(adapter);
                listView.setTextFilterEnabled(true);

                EditText inputSearch = (EditText) findViewById(R.id.search_bar);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                        Gnome currentGnome = new Gnome();
                        currentGnome = tm.town.getPopulation().get(position);
                        Intent appInfo = new Intent(context, GnomeProfile.class);
                        System.out.println(currentGnome.getName());
                        appInfo.putExtra("Gnome",currentGnome);
                        startActivity(appInfo);
                    }
                });

                inputSearch.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        adapter.getFilter().filter(cs);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

                    @Override
                    public void afterTextChanged(Editable arg0) {}
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

    public void reloadActivity(){
        finish();
        startActivity(getIntent());
    }
}
