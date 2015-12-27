package com.xxy.simpletodo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.common.collect.ImmutableList;
import com.xxy.simpletodo.R;
import com.xxy.simpletodo.fragment.DoneFragment;
import com.xxy.simpletodo.fragment.TabFragment;
import com.xxy.simpletodo.fragment.TodoFragment;
import com.xxy.simpletodo.listener.TodoTabListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  //private List<Item> items;
  //private ArrayAdapter<Item> itemsAdapter;
  //private ListView lvItems;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ActionBar tabBar = getSupportActionBar();
    tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    List<String> tabTextList = ImmutableList.of(
        "ToDo",
        "Complete"
    );

    List<TabFragment> tabFragments = ImmutableList.of(
        new TodoFragment(),
        new DoneFragment()
    );

    for(int i=0; i<tabTextList.size(); i++) {
      ActionBar.Tab todoTab = tabBar.
                              newTab().
                              setText(tabTextList.get(i)).
                              setTabListener(
                                  new TodoTabListener(
                                      tabFragments.get(i)
                                  ));
      tabBar.addTab(todoTab);
    }
    //setupListViewListener();
    //setupEditItemListener();
  }

/*
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
*/

}
