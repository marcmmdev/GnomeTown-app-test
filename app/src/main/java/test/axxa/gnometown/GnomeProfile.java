package test.axxa.gnometown;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class GnomeProfile extends Activity {

    Gnome gnome = new Gnome();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gnome_profile);

        //receive
        gnome = (Gnome) getIntent().getSerializableExtra("Gnome");
        TextView gnomeName = (TextView) findViewById(R.id.gnomeProfile_name);
        TextView gnomeAge = (TextView) findViewById(R.id.gnomeProfile_age);
        TextView gnomeWeight = (TextView) findViewById(R.id.gnomeProfile_weight);
        TextView gnomeHeight = (TextView) findViewById(R.id.gnomeProfile_height);
        TextView gnomeHair = (TextView) findViewById(R.id.gnomeProfile_hair);
        ImageView gnomeImg = (ImageView) findViewById(R.id.img);
        ListView professions = (ListView) findViewById(R.id.profList);

        ProfessionAdapter padapter = new ProfessionAdapter(this, (ArrayList<String>) gnome.getProfessions());
        professions.setAdapter(padapter);
        UIUtils.setListViewHeightBasedOnItems(professions);
        padapter.notifyDataSetChanged();

        gnomeName.setText(gnome.getName());
        gnomeAge.setText(gnomeAge.getText()+Integer.toString(gnome.getAge()));
        gnomeWeight.setText(gnomeWeight.getText()+Double.toString(gnome.getWeight()));
        gnomeHeight.setText(gnomeHeight.getText()+Double.toString(gnome.getHeight()));
        gnomeHair.setText(gnomeHair.getText()+gnome.getHair_color());


        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.displayImage(gnome.getThumbnail(), gnomeImg);

        ListView friends = (ListView) findViewById(R.id.gnomeFriendsListView);
        FriendsAdapter fadapter = new FriendsAdapter(this, (ArrayList<String>) gnome.getFriends());
        friends.setAdapter(fadapter);
        UIUtils.setListViewHeightBasedOnItems(friends);



    }


}
