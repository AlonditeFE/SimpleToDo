package com.example.simpletodo;

import org.apache.commons.io.FileUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

        List<String> items;

        Button btnAdd;
        EditText etItem;
        RecyclerView rvItems;
        ItemsAdapter itemsAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvitems);

        //etItem.setText("Running into too many problems...");

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
                @Override
                public void onItemLongClicked(int position) {
                //delete item
                        items.remove(position);
                //notify deletion
                itemsAdapter.notifyItemRemoved(position);
                        Toast.makeText(getApplicationContext(), "Item was removed my guy! (and so was my sanity)", Toast.LENGTH_SHORT).show();
                        saveItems();
                }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        String todoItem = etItem.getText().toString();
                        //Add item to model
                        items.add(todoItem);
                        //notify that the item is inserted
                        itemsAdapter.notifyItemInserted(items.size()-1);
                        etItem.setText("");
                        Toast.makeText(getApplicationContext(), "Item was added my man!", Toast.LENGTH_SHORT).show();
                        saveItems();
                }
        });
        }

        private File getDataFile(){
                return new File(getFilesDir(), "data.txt");
        }
        //load items
        private void loadItems(){
                try{
                items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
                Log.e("MainActivity", "Error reading items", e);
                items = new ArrayList<>();
        }
        }

        //save items
        public void saveItems() {
                try {
                        FileUtils.writeLines(getDataFile(), items);
                } catch (IOException e) {
                        Log.e("MainActivity", "Error writing items", e);
                }
        }
}