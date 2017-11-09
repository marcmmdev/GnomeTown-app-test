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

public class FriendsAdapter extends ArrayAdapter<String> {
    public GnomeListActivity ga;

    public FriendsAdapter(Context context, ArrayList<String> professions) {
        super(context, 0, professions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String profession = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gnome_friend_adapter, parent, false);
        }

        TextView gnomeProfession = (TextView) convertView.findViewById(R.id.gnomeProfile_name_friendList);
        gnomeProfession.setText(profession);

        return convertView;
    }
}
