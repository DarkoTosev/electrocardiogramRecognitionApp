package com.darko.ecg_second;

public class Item implements Comparable<Item>{
    private String name;
    private String data;
    private String path;
    private String image;

    public Item(String n, String d, String p, String img)
    {
        name = n;
        data = d;
        path = p;
        image = img;
    }

    public String getName()
    {
        return name;
    }
    public String getData()
    {
        return data;
    }
    public String getPath()
    {
        return path;
    }
    public String getImage() {
        return image;
    }

    public int compareTo(Item o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}