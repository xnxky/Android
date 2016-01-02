package com.xxy.simpletodo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.layout.LabeledDatePicker;
import com.xxy.simpletodo.layout.LabeledEditText;
import com.xxy.simpletodo.layout.LabeledSpinner;
import com.xxy.simpletodo.table.Item;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xxiao on 12/26/15.
 */
public class TodoFragment extends TabFragment {

  private Iterator<Item> editIterator;
  private ArrayAdapter<String> priorityAdapter;
  private int editItemIndex;
  private List<Integer> refreshIndexList;

  public TodoFragment() {
    super();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isDone", false);
    this.setArguments(bundle);
    refreshIndexList = new ArrayList<>();
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
    inflater.inflate(R.menu.todo_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add_item_menu:
        modifyItem(items.size());
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
      modifyItem(editItemIndex);
      return false;
    } else {
      editIterator = null;
      return true;
    }
  }

  private void modifyItem(final int index) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    final View view = inflater.inflate(R.layout.edit_item, null);
    setDisplayItem(view, index);
    Dialog dialog = new AlertDialog.
                    Builder(getActivity()).
                    setView(view).
                    setPositiveButton(
                        "Save",
                        new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                            saveModifiedItem(view, index);
                          }
                        }
                    ).
                    setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                            cancelModifiedItem();
                          }
                        }
                    ).
                    setOnKeyListener(
                        new DialogInterface.OnKeyListener() {
                          @Override
                          public boolean onKey(
                              DialogInterface dialog, int keyCode, KeyEvent event) {
                            if(keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getRepeatCount() == 0) {
                              if(! refreshIndexList.isEmpty()) {
                                for(int idx : refreshIndexList) {
                                  Item targetItem = getItem(idx);
                                  items.remove(idx);
                                  Item.insert(items, targetItem);
                                  itemAdapter.notifyDataSetChanged();
                                }
                                refreshIndexList.clear();
                              }
                            }
                            return false;
                          }
                        }).create();
    dialog.show();
  }

  protected void setDisplayItem(View view, int index) {
    Item targetItem = getItem(index);
    LabeledEditText etName = (LabeledEditText)view.findViewById(R.id.etItemName);
    etName.setLabelAndContent(
        curResource.getString(R.string.task_name_label), targetItem.name);
    LabeledEditText etContent = (LabeledEditText)view.findViewById(R.id.etItemContent);
    etContent.setLabelAndContent(
        curResource.getString(R.string.task_note_label), targetItem.content);
    LabeledSpinner spinner = (LabeledSpinner)view.findViewById(R.id.etItemPriority);
    spinner.setLabel(curResource.getString(R.string.task_priority_label));
    spinner.setContent(targetItem.priority.name(), getPriorityAdapter());
    LabeledDatePicker datePicker = (LabeledDatePicker)view.findViewById(R.id.datePicker);
    datePicker.setLabel(curResource.getString(R.string.task_due_date_label));
    datePicker.setContent(targetItem.getDate());
  }

  private void saveModifiedItem(View view, int index) {
    Item targetItem = getItem(index);
    LabeledEditText etName = (LabeledEditText)view.findViewById(R.id.etItemName);
    targetItem.name = etName.getContentString();
    LabeledEditText etConent = (LabeledEditText)view.findViewById(R.id.etItemContent);
    targetItem.content = etConent.getContentString();
    LabeledSpinner spinner = (LabeledSpinner)view.findViewById(R.id.etItemPriority);
    targetItem.priority = Item.Priority.valueOf(spinner.getContentString());
    LabeledDatePicker datePicker = (LabeledDatePicker)view.findViewById(R.id.datePicker);
    targetItem.setDueDate(datePicker.getContentString());
    targetItem.save();

    if(index != items.size()) {
      refreshIndexList.add(index);
    } else {
      Item.insert(items, targetItem);
    }

    if(editIterator == null || editItem()) {
      for(int refreshIdx : refreshIndexList) {
        Item refreshItem = getItem(refreshIdx);
        items.remove(refreshIdx);
        Item.insert(items, refreshItem);
      }
      refreshIndexList.clear();
      itemAdapter.notifyDataSetChanged();
    }
  }

  private void cancelModifiedItem() {
    if(editIterator == null || editItem()) {
      itemAdapter.notifyDataSetChanged();
    }
  }

  private void updateDoneItem() {
    Iterator<Item> iterator = items.iterator();
    while(iterator.hasNext()) {
      Item curItem = iterator.next();
      if(curItem.isChecked) {
        curItem.isChecked = false;
        curItem.isDone = true;
        curItem.setCompDate(
            new LocalDate().toString(Item.DATE_FORMAT));
        curItem.save();
        iterator.remove();
      }
    }
    itemAdapter.notifyDataSetChanged();
  }

  private void setupEditItemListener() {
    lvItems.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent,
                                  View view, int position, long id) {
            editIterator = null;
            modifyItem(position);
          }
        }
    );
  }

  public ArrayAdapter<String> getPriorityAdapter() {
    if(priorityAdapter == null) {
      priorityAdapter = new ArrayAdapter<>(
          getActivity(),
          android.R.layout.simple_spinner_item,
          Item.getPriorityList()
      );
      priorityAdapter.setDropDownViewResource(
          android.R.layout.simple_list_item_single_choice
      );
    }
    return priorityAdapter;
  }

}
