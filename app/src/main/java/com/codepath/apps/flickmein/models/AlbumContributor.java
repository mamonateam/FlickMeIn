package com.codepath.apps.flickmein.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "contributors")
public class AlbumContributor extends Model implements Serializable {

    private static final long serialVersionUID = 8982342234L;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    public AlbumContributor() { super(); }

    public AlbumContributor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String[] getTags() {
        return (new String[]{"fmiuser"+this.name, "fmicolor"+this.color});
    }

    @Override
    public String toString() {
        return "AlbumContributor{" +
                "name='" + name + '\'' +
                ", color=" + color +
                '}';
    }
}
