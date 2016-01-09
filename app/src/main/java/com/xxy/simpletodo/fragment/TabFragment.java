package com.xxy.simpletodo.fragment;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.google.common.collect.ImmutableList;
import com.xxy.simpletodo.R;
import com.xxy.simpletodo.adapter.ItemAdapter;
import com.xxy.simpletodo.dialogFragment.DeleteDialogFragment;
import com.xxy.simpletodo.listener.DialogListener;
import com.xxy.simpletodo.table.Item;

import org.joda.time.LocalDate;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xxiao on 12/26/15.
 */
public class TabFragment extends Fragment {

  protected List<Item> items;
  protected ListView lvItems;
  protected ItemAdapter itemAdapter;
  protected Resources curResource;

  private SingleDeleteAction singleDeleteAction =
      new SingleDeleteAction();
  private BulkDeleteAction bulkDeleteAction =
      new BulkDeleteAction();

  private View tabView;
  private final DeleteDialogListener deleteDialogListener;

  public TabFragment() {
    super();
    deleteDialogListener =
        new DeleteDialogListener();
  }

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
    tabView = inflater.inflate(R.layout.tab_fragment, container, false);
    List<Integer> boldViewList = ImmutableList.of(
        R.id.selectAllText,
        R.id.priorityLabel,
        R.id.highPriority,
        R.id.mediumPriority,
        R.id.lowPriority);
    for(int viewId : boldViewList) {
      TextView oneView = (TextView)tabView.findViewById(viewId);
      oneView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      oneView.getPaint().setFakeBoldText(true);
    }
    lvItems = (ListView) tabView.findViewById(R.id.fragmentItems);
    setupSelectAllCheckBoxListener(tabView);
    setupListViewListener();

    return tabView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i("xxy", "onActivityCreated");
    fetchItemsFromDb();
    FragmentActivity curActivity = getActivity();
    curResource = curActivity.getResources();
    itemAdapter = new ItemAdapter(
        curActivity,
        items,
        curResource.getColor(R.color.high),
        curResource.getColor(R.color.medium),
        curResource.getColor(R.color.low)
    );

    lvItems.setAdapter(itemAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    Log.i("xxy", "onStart");
    //TODO: to understand what happened here:
    //Interesting, do this in onActivityCreated() will not uncheck
    //checkbox_selector in ui even if monitoring indicates the code is executed
    //Maybe the checkbox_selector found there is actually a different one?
    /*
    CheckBox selectAllCheckBox = (CheckBox)tabView.findViewById(R.id.selectAllCheckBox);
    TextView selectAllText = (TextView)tabView.findViewById(R.id.selectAllText);
    selectAllCheckBox.setChecked(false);
    selectAllText.setText(R.string.select_all);
    */
    CheckBox selectAllCheckBox = (CheckBox)tabView.findViewById(R.id.selectAllCheckBox);
    TextView textView = (TextView)tabView.findViewById(R.id.selectAllText);
    int textId = selectAllCheckBox.isChecked() ?
        R.string.unselect_all : R.string.select_all;
    textView.setText(textId);
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.i("xxy", "onResume");
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

  public void setupSelectAllCheckBoxListener(View tabView) {
    final CheckBox selectAllCheckBox = (CheckBox)tabView.findViewById(R.id.selectAllCheckBox);
    final TextView textView = (TextView)tabView.findViewById(R.id.selectAllText);
    selectAllCheckBox.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            boolean allCheckStatus = selectAllCheckBox.isChecked();
            if (allCheckStatus) {
              textView.setText(R.string.unselect_all);
            } else {
              textView.setText(R.string.select_all);
            }
            for (Item item : items) {
              if (item.checkbox != null) {
                item.checkbox.setChecked(allCheckStatus);
              }
              item.isChecked = allCheckStatus;
            }
            itemAdapter.notifyDataSetChanged();
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
    Comparator<Item> comparator = isDone?
        Item.doneComparator : Item.toDoComparator;
    Collections.sort(items, comparator);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main_menu, menu);

    if(menu != null) {
      if(menu.getClass().getSimpleName().equals("MenuBuilder")) {
        try {
          Method m = menu.
              getClass().
              getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
          m.setAccessible(true);
          m.invoke(menu, true);
        } catch (Exception e) {
        }
      }
    }
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

  public interface DeleteAction {
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

  protected Item getItem(int index) {
    Item targetItem;
    if(index == items.size()) {
      targetItem = getDefaultItem();
    } else {
      targetItem = items.get(index);
    }
    return targetItem;
  }

  private Item getDefaultItem() {
    return new Item(
           "",
           "",
           Item.Priority.MEDIUM,
           new LocalDate().toString(Item.DATE_FORMAT),
           false);
  }

  private void createDeleteAlertDialog(
      final DeleteAction deleteAction,
      final int itemIndex) {

    deleteDialogListener.setAction(deleteAction);
    DeleteDialogFragment deleteDialogFragment =
        DeleteDialogFragment.newInstance(deleteDialogListener);
    deleteDialogFragment.setIndex(itemIndex);
    FragmentManager fm = getActivity().getSupportFragmentManager();
    deleteDialogFragment.show(fm, "delete");
  }

  private class DeleteDialogListener implements DialogListener {
    private DeleteAction deleteAction;

    public void setAction(DeleteAction deleteAction) {
      this.deleteAction = deleteAction;
    }

    public void onPositiveButtonPressed(
        DialogInterface dialog, View view, int index) {
      deleteAction.deleteItem(index);
    }

    public void onNegativeButtonPressed(DialogInterface dialog) {
      dialog.dismiss();
    }

    public void onBackKeyClicked(int keyCode, KeyEvent event) {}

    public void initialize(View view, int index) {}
  }

}
