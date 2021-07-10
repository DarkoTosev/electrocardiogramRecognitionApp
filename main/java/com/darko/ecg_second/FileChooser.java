package com.darko.ecg_second;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;

public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = Environment.getExternalStorageDirectory();
        fill(currentDir);
    }

    private void fill(File f)
    {
        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<Item>dir = new ArrayList<Item>();
        List<Item>fls = new ArrayList<Item>();

        try{
            for(File ff: dirs)
            {
                if(ff.isDirectory()){
                    File[] fbuf = ff.listFiles();
                    String num_item = "";
                    if(fbuf != null)
                        num_item = String.valueOf(fbuf.length) + " items";
                    else num_item = "0 item";

                    dir.add(new Item(ff.getName(), num_item, ff.getAbsolutePath(), "directory_icon"));
                }
                else fls.add(new Item(ff.getName(), ff.length() + " Byte", ff.getAbsolutePath(), "file_icon"));
            }
        } catch(Exception e) {

        }

        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0, new Item("..", "Parent Directory", f.getParent(), "directory_up"));
        adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view, dir);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Item o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon") || o.getImage().equalsIgnoreCase("directory_up")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else onFileClick(o);
    }

    private void onFileClick(Item o)
    {
        Intent intent = new Intent();
        intent.putExtra("GetPath", currentDir.toString());
        intent.putExtra("GetFileName", o.getName());
        intent.putExtra("GetAbsolutePath", o.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }
}