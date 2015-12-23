package com.xxy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.xxy.simpletodo.adapter.ItemAdapter;
import com.xxy.simpletodo.tables.Item;

import org.joda.time.LocalDate;

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
    items = new Select().
                from(Item.class).
                execute();
    itemsAdapter = new ItemAdapter(this, items);
    lvItems.setAdapter(itemsAdapter);
    setupListViewListener();
    setupEditItemListener();
  }

  public void addItem(View v) {
    Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
    intent.putExtra("item", getDefaultItem());
    intent.putExtra("itemIndex", items.size());
    startActivityForResult(intent, REQUEST_CODE);
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
                intent.putExtra("item", items.get(pos));
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
      Item targetItem = (Item)data.getExtras().getSerializable("item");
      if(itemIndex == items.size()) {
        items.add(targetItem);
      } else {
        items.set(itemIndex, targetItem);
      }
      targetItem.save();
      itemsAdapter.notifyDataSetChanged();
    }
  }

  private Item getDefaultItem() {
    return new Item(
           "Please add the name of the item",
           "Please edit the content ",
           Item.Priority.HIGH,
           new LocalDate(),
           Item.Status.PREPARE);
  }

}
