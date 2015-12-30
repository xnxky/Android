package com.xxy.simpletodo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.activity.ViewItemActivity;
import com.xxy.simpletodo.table.Item;

import java.util.Iterator;

/**
 * Created by xxiao on 12/26/15.
 */
public class DoneFragment extends TabFragment {

  private final static int REQUEST_CODE = 21;
  private final static int REQUEST_OK = Activity.RESULT_OK;

  private Iterator<Item> iterator;


  public DoneFragment() {
    super();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isDone", true);
    this.setArguments(bundle);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View view = super.onCreateView(
        inflater, container, savedInstanceState);
    setupViewItemListener();
    return view;
  }

  private void setupViewItemListener() {
    lvItems.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(
              AdapterView<?> parent, View view, int position, long id) {
            viewItem(position);
          }
        }
    );
  }

  private void viewItem(int index) {
    Intent indent = new Intent(getActivity(), ViewItemActivity.class);
    indent.putExtra("item", items.get(index));
    startActivity(indent);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.done_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.view_item_menu:
        iterator = items.iterator();
        bulkViewItems();
        return true;
      case R.id.undone_item_menu:
        bulkUnmarkItems();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void bulkViewItems() {
    Item nextItem = null;
    while(iterator.hasNext()) {
      nextItem = iterator.next();
      if(nextItem.isChecked){
        break;
      } else {
        nextItem = null;
      }
    }
    if(nextItem != null) {
      Intent intent = new Intent(getActivity(), ViewItemActivity.class);
      intent.putExtra("item", nextItem);
      startActivityForResult(intent, REQUEST_CODE);
    } else {
      iterator = null;
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if(requestCode==REQUEST_CODE && resultCode==REQUEST_OK) {
      if(iterator!=null) {
        bulkViewItems();
      }
    }
  }

  private void bulkUnmarkItems() {
    Iterator<Item> iterator = items.iterator();
    while(iterator.hasNext()) {
      Item curItem = iterator.next();
      if(curItem.isChecked) {
        curItem.isChecked = false;
        curItem.isDone = false;
        curItem.save();
        iterator.remove();
      }
    }
    itemAdapter.notifyDataSetChanged();
  }

}
