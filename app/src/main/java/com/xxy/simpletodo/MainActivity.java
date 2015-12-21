package com.xxy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private ArrayList<String> items;
  private ArrayAdapter<String> itemsAdapter;
  private ListView lvItems;

  private final int REQUEST_CODE = 20;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    lvItems = (ListView)findViewById(R.id.lvItems);
    readItems();
    itemsAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, items);
    lvItems.setAdapter(itemsAdapter);
    items.add("clean up the house");
    items.add("apply for the a credit card");
    setupListViewListener();
    setupEditItemListener();
  }

  public void addItem(View v) {
    EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
    String newItemText = etNewItem.getText().toString();
    itemsAdapter.add(newItemText);
    etNewItem.setText("");
    writeItems();
  }

  public void setupListViewListener() {
    lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
              @Override
              public boolean onItemLongClick(AdapterView<?> adapter,
                                             View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
              }
            });
  }

  public void setupEditItemListener() {
    lvItems.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> adapter,
                                         View item, int pos, long id) {

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("itemText", items.get(pos));
                intent.putExtra("itemIndex", pos);

                startActivityForResult(intent, REQUEST_CODE);
              }
            }
    );
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode==REQUEST_CODE && resultCode==RESULT_OK) {
      int itemIndex = data.getExtras().getInt("itemIndex");
      String itemContent = data.getExtras().getString("itemText");
      items.set(itemIndex, itemContent);
      itemsAdapter.notifyDataSetChanged();
      writeItems();
    }
  }

  private void readItems() {
    File filesDir = getFilesDir();
    File toDoFile = new File(filesDir, "todo");
    try {
      items = new ArrayList<>(FileUtils.readLines(toDoFile));
    } catch (IOException e) {
      items = new ArrayList<>();
    }
  }

  private void writeItems() {
    File filesDir = getFilesDir();
    File toDoFile = new File(filesDir, "todo");
    try {
      FileUtils.writeLines(toDoFile, items);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
