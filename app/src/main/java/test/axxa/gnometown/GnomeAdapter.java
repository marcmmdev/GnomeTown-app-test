package test.axxa.gnometown;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Marcus on 06/11/2017.
 */

public class GnomeAdapter extends ArrayAdapter<Gnome> implements Filterable {
    public GnomeListActivity ga;
    public ArrayList<Gnome> gnomes;
    public ArrayList<Gnome> filteredData;
    public ArrayList<Gnome> original;
    private Filter filter;

    public GnomeAdapter(Context context, ArrayList<Gnome> items) {
        super(context, 0, items);
        this.original = items;
        this.gnomes = items;
        this.filteredData = items;
        this.filter = new GnomeFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Gnome gnome = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gnome_list_adapter, parent, false);
        }


        TextView gnomeName = (TextView) convertView.findViewById(R.id.gnomeName);
        TextView gnomeAge = (TextView) convertView.findViewById(R.id.gnomeAge);
        ImageView gnomeImg = (ImageView) convertView.findViewById(R.id.image);
        // Populate the data into the template view using the data object
        gnomeName.setText(gnome.getName());
        gnomeAge.setText(Integer.toString(gnome.getAge()));
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

        imageLoader.displayImage(gnome.getThumbnail(), gnomeImg);

        return convertView;
    }


    @Override
    public Filter getFilter(){

        if(filter == null){
            filter = new GnomeFilter();
        }
        return filter;
    }

    private class GnomeFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix == null || prefix.length() == 0){
                ArrayList<Gnome> list = new ArrayList<Gnome>(gnomes);
                results.values = list;
                results.count = list.size();
            }else{
                final ArrayList<Gnome> list = new ArrayList<Gnome>(gnomes);
                final ArrayList<Gnome> nlist = new ArrayList<Gnome>();
                int count = list.size();

                for (int i = 0; i<count; i++){
                    final Gnome gnome = list.get(i);
                    final String value = gnome.getName().toLowerCase();

                    if(value.contains(prefix)){
                        nlist.add(gnome);
                    }
                    results.values = nlist;
                    results.count = nlist.size();
                }
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Gnome>)results.values;
            notifyDataSetChanged();
            clear();
            if(filteredData != null){
                int count = filteredData.size();
                for(int i = 0; i<count; i++){
                    add(filteredData.get(i));
                    notifyDataSetInvalidated();
                }
            }else{
            }
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
