package test.axxa.gnometown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marcus on 06/11/2017.
 */

public class ProfessionAdapter extends ArrayAdapter<String> {
    public GnomeListActivity ga;

    public ProfessionAdapter(Context context, ArrayList<String> professions) {
        super(context, 0, professions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String profession = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gnome_profession_adapter, parent, false);
        }

        TextView gnomeProfession = (TextView) convertView.findViewById(R.id.gnomeProfession);
        gnomeProfession.setText(profession);

        return convertView;
    }
}
