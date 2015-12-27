package com.xxy.simpletodo.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.xxy.simpletodo.R;
import com.xxy.simpletodo.adapter.ItemAdapter;
import com.xxy.simpletodo.table.Item;

import java.util.Iterator;
import java.util.List;

/**
 * Created by xxiao on 12/26/15.
 */
public class TabFragment extends Fragment {

  protected static final int RESULT_OK = Activity.RESULT_OK;

  protected List<Item> items;
  protected ListView lvItems;
  protected ItemAdapter itemAdapter;

  private SingleDeleteAction singleDeleteAction =
      new SingleDeleteAction();
  private BulkDeleteAction bulkDeleteAction =
      new BulkDeleteAction();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Log.i("xxy", "onCreateView");
    View tabView = inflater.inflate(R.layout.tab_fragment, container, false);
    lvItems = (ListView) tabView.findViewById(R.id.fragmentItems);
    setupListViewListener();
    return tabView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i("xxy", "onActivityCreated");
    fetchItemsFromDb();
    itemAdapter = new ItemAdapter(getActivity(), items);
    lvItems.setAdapter(itemAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    Log.i("xxy", "onStart");
  }

  @Override
  public void onPause() {
    Log.i("xxy", "onPause");
    super.onPause();
  }

  @Override
  public void onStop() {
    Log.i("xxy", "onStop");
    super.onStop();
  }

  @Override
  public void onDestroyView() {
    Log.i("xxy", "onDestroyView");
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    Log.i("xxy", "onDestroy");
    super.onDestroy();
  }

  public void setupListViewListener() {
    lvItems.setOnItemLongClickListener(
        new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> adapter,
                                         View item, int pos, long id) {
            createDeleteAlertDialog(singleDeleteAction, pos);
            return true;
          }
        }
    );
  }

  private void fetchItemsFromDb() {
    boolean isDone = getArguments().getBoolean("isDone");
    items = new Select().
      from(Item.class).
      where("isDone = ?", isDone).
      execute();

    for(Item item : items) {
      item.isChecked = false;
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.delete_item_menu:
        createDeleteAlertDialog(bulkDeleteAction, -1);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private interface DeleteAction {
    void deleteItem(int pos);
  }

  private class SingleDeleteAction implements DeleteAction {
    @Override
    public void deleteItem(int pos) {
      items.get(pos).delete();
      items.remove(pos);
      itemAdapter.notifyDataSetChanged();
    }
  }

  private class BulkDeleteAction implements DeleteAction {
    @Override
    public void deleteItem(int pos) {
      Iterator<Item> iterator = items.iterator();
      while(iterator.hasNext()) {
        Item curItem = iterator.next();
        if(curItem.isChecked) {
          curItem.delete();
          iterator.remove();
        }
      }
      itemAdapter.notifyDataSetChanged();
    }
  }

  private void createDeleteAlertDialog(
      final DeleteAction deleteAction,
      final int itemIndex) {

    Dialog dialog = new AlertDialog.
                        Builder(getActivity()).
                        setTitle("Alert").
                        setMessage("Are you sure you want to delete?").
                        setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                deleteAction.deleteItem(itemIndex);
                              }
                            }
                        ).
                        setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                              }
                            }
                        ).create();
    dialog.show();
  }

}
