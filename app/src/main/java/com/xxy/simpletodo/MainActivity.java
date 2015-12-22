package com.xxy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.google.common.collect.ImmutableList;
import com.xxy.simpletodo.tables.Item;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private List<Item> items;
  private ArrayAdapter<Item> itemsAdapter;
  private ListView lvItems;

  private final int REQUEST_CODE = 20;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    lvItems = (ListView)findViewById(R.id.lvItems);
    for(String content : ImmutableList.of(
            "clean up the house",
            "apply for the a credit card")
            ) {
      Item newItem = new Item(content);
      newItem.save();
    }
    items = new Select().
                from(Item.class).
                execute();
    itemsAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, items);
    lvItems.setAdapter(itemsAdapter);
    setupListViewListener();
    setupEditItemListener();
  }

  public void addItem(View v) {
    EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
    String newItemText = etNewItem.getText().toString();
    Item newItem = new Item(newItemText);
    newItem.save();
    items.add(newItem);
    itemsAdapter.notifyDataSetChanged();;
    etNewItem.setText("");
  }

  public void setupListViewListener() {
    lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
              @Override
              public boolean onItemLongClick(AdapterView<?> adapter,
                                             View item, int pos, long id) {
                items.get(pos).delete();
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
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
                intent.putExtra("itemText", items.get(pos).content);
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
      Item targetItem = items.get(itemIndex);
      targetItem.content =  itemContent;
      targetItem.save();
      itemsAdapter.notifyDataSetChanged();
    }
  }

}
