package test.axxa.gnometown;
import android.util.*;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * Created by Marcus on 06/11/2017.
 */

public class TownManager {
    public Town town = new Town();

    public void readPopulation(InputStream is ) throws UnsupportedEncodingException, IOException{
        town = readJsonStream(is);
    }

    public Town readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readTown(reader);
        } finally {
            reader.close();
        }
    }

    public Town readTown(JsonReader reader) throws  IOException{
        Town town = new Town();
        reader.beginObject();
        while(reader.hasNext()){
            town.setName(reader.nextName());
            town.setPopulation(readGnomesArray(reader));
        }
        reader.endObject();
        return town;
    }

    public List<Gnome> readGnomesArray(JsonReader reader) throws IOException {
        List<Gnome> population = new ArrayList<Gnome>();

        reader.beginArray();
        while (reader.hasNext()) {
            population.add(readGnome(reader));
        }
        reader.endArray();
        return population;
    }

    public List<String> readGnomeProfessions(JsonReader reader) throws IOException{
        List<String> professions = new ArrayList<String>();

        reader.beginArray();
        while(reader.hasNext()){
            professions.add(reader.nextString());
        }
        reader.endArray();
        return professions;
    }

    public List<String> readGnomeFriends(JsonReader reader) throws IOException{
        List<String> friends = new ArrayList<String>();

        reader.beginArray();
        while(reader.hasNext()){
            friends.add(reader.nextString());
        }
        reader.endArray();
        return friends;
    }

    public Gnome readGnome(JsonReader reader) throws  IOException{
        Gnome gnome = new Gnome();

        int id;
        String gnome_name;
        int age;
        double weight;
        double height;
        String hair_color;
        ArrayList<String> professions;
        ArrayList<Gnome> friends;

        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("id")) {
                gnome.setId(reader.nextInt());
            } else if (name.equals("name")) {
                gnome.setName(reader.nextString());
            } else if (name.equals("thumbnail")) {
                gnome.setThumbnail(reader.nextString());
            } else if (name.equals("age")) {
                gnome.setAge(reader.nextInt());
            } else if (name.equals("weight")) {
                gnome.setWeight(reader.nextDouble());
            } else if (name.equals("height")) {
                gnome.setHeight(reader.nextDouble());
            }else if (name.equals("hair_color")) {
                gnome.setHair_color(reader.nextString());
            }else if (name.equals("professions")) {
                gnome.setProfessions(readGnomeProfessions(reader));
            }else if (name.equals("friends")) {
                gnome.setFriends(readGnomeFriends(reader));
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return gnome;
    }

}
