package com.codepath.apps.restclienttemplate.models;

public class AlbumContributor {

    private String name;
    private String color;

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
