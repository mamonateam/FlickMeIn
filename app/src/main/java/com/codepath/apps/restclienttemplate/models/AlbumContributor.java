package com.codepath.apps.restclienttemplate.models;

import android.graphics.Color;

/**
 * Created by jesusft on 2/19/15.
 */
public class AlbumContributor {

    private String name;
    private int color;

    public AlbumContributor(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String[] getTags() {
        return (new String[]{"@"+this.name, "#"+this.color});
    }

    @Override
    public String toString() {
        return "AlbumContributor{" +
                "name='" + name + '\'' +
                ", color=" + color +
                '}';
    }
}
