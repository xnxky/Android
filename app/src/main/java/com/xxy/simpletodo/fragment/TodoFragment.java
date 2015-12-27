package com.xxy.simpletodo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.xxy.simpletodo.activity.EditItemActivity;
import com.xxy.simpletodo.R;
import com.xxy.simpletodo.table.Item;

import org.joda.time.LocalDate;

import java.util.Iterator;

/**
 * Created by xxiao on 12/26/15.
 */
public class TodoFragment extends TabFragment {

  private final int REQUEST_CODE = 20;
  private Iterator<Item> editIterator;
  private int editItemIndex;

  public TodoFragment() {
    super();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isDone", false);
    this.setArguments(bundle);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    setupEditItemListener();
    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.todo_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add_item_menu:
        modifyItem(getDefaultItem(), items.size());
        return true;
      case R.id.edit_item_menu:
        editIterator = items.iterator();
        editItemIndex = -1;
        editItem();
        return true;
      case R.id.mark_item_menu:
        updateDoneItem();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private boolean editItem() {
    Item nextItem = null;
    while(editIterator.hasNext()) {
      editItemIndex ++;
      nextItem = editIterator.next();
      if(nextItem.isChecked) {
        break;
      } else {
        nextItem = null;
      }
    }
    if(nextItem != null) {
      modifyItem(nextItem, editItemIndex);
      return false;
    } else {
      editIterator = null;
      return true;
    }
  }

  private void modifyItem(Item targetItem, int index) {
    Intent intent = new Intent(getActivity(), EditItemActivity.class);
    intent.putExtra("item", targetItem);
    intent.putExtra("itemIndex", index);
    startActivityForResult(intent, REQUEST_CODE);
  }

  private void updateDoneItem() {
    Iterator<Item> iterator = items.iterator();
    while(iterator.hasNext()) {
      Item curItem = iterator.next();
      if(curItem.isChecked) {
        curItem.isDone = true;
        curItem.setCompDate(
            new LocalDate().toString(Item.DATE_FORMAT));
        curItem.save();
        iterator.remove();
      }
    }
    itemAdapter.notifyDataSetChanged();
  }

  private Item getDefaultItem() {
    return new Item(
           "Name",
           "Content",
           Item.Priority.MEDIUM,
           new LocalDate().toString(Item.DATE_FORMAT),
           false);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode==REQUEST_CODE && resultCode==RESULT_OK) {
      int itemIndex = data.getExtras().getInt("itemIndex");
      Item targetItem = (Item)data.getExtras().getSerializable("item");
      if(itemIndex == items.size()) {
        items.add(targetItem);
      } else {
        Item oldItem = items.get(itemIndex);
        oldItem.update(targetItem);
        targetItem = oldItem;
      }
      targetItem.save();
      if(editIterator==null || editItem()) {
        itemAdapter.notifyDataSetChanged();
      }
    }
  }

  private void setupEditItemListener() {
    lvItems.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent,
                                  View view, int position, long id) {
            modifyItem(items.get(position), position);
          }
        }
    );
  }

}
