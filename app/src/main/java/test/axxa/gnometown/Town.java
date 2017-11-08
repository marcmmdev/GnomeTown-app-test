package test.axxa.gnometown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcus on 06/11/2017.
 */

public class Town {
    private String name;
    private List<Gnome> population;

    public Town() {
    }

    public Town(List<Gnome> population) {
        this.population = population;
    }

    public List<Gnome> getPopulation() {
        return population;
    }

    public void setPopulation(List<Gnome> population) {
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
