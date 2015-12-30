package com.xxy.simpletodo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.table.Item;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xxiao on 12/26/15.
 */
public class TodoFragment extends TabFragment {

  private Iterator<Item> editIterator;
  private int editItemIndex;
  private final List<String> priorityList;

  public TodoFragment() {
    super();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isDone", false);
    this.setArguments(bundle);

    priorityList = new ArrayList<>();
    for(Item.Priority onePriority : Item.Priority.values()) {
      priorityList.add(onePriority.name());
    }
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
    Spinner prioritySpinner = (Spinner)view.findViewById(
        R.id.etItemPriority);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(
        getActivity(),
        android.R.layout.simple_spinner_item,
        priorityList
    );

    adapter.setDropDownViewResource(
        android.R.layout.simple_list_item_single_choice);

    prioritySpinner.setAdapter(adapter);

    displayItem(view, index);
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
                    ).create();
    dialog.show();
  }

  private void displayItem(View view, int index) {
    Item targetItem = getItem(index);
    EditText etName = (EditText)view.findViewById(R.id.etItemName);
    etName.setText(targetItem.name);
    EditText etConent = (EditText)view.findViewById(R.id.etItemContent);
    etConent.setText(targetItem.content);
    Spinner prioritySpinner = (Spinner)view.findViewById(R.id.etItemPriority);
    setPrioritySpinnerValue(prioritySpinner, targetItem);
    EditText etDueDate = (EditText)view.findViewById(R.id.etItemDueDate);
    etDueDate.setText(targetItem.getDate());
    DatePicker datePicker = (DatePicker)view.findViewById(R.id.datePicker);
    updateDateDisplay(datePicker);
  }

  private void updateDateDisplay(DatePicker datepicker) {
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    datepicker.init(year, month, day, null);
  }

  private void setPrioritySpinnerValue(Spinner spinner, Item item) {
    ArrayAdapter<String> adapter = (ArrayAdapter)spinner.getAdapter();
    int position = adapter.getPosition(item.priority.name());
    spinner.setSelection(position);
  }

  private void saveModifiedItem(View view, int index) {
    Item targetItem = getItem(index);
    if(index == items.size()) items.add(targetItem);
    EditText etName = (EditText)view.findViewById(R.id.etItemName);
    targetItem.name = etName.getText().toString();
    EditText etConent = (EditText)view.findViewById(R.id.etItemContent);
    targetItem.content = etConent.getText().toString();
    Spinner prioritySpinner = (Spinner)view.findViewById(R.id.etItemPriority);
    targetItem.priority = Item.Priority.valueOf((String)prioritySpinner.getSelectedItem());
    EditText etDueDate = (EditText)view.findViewById(R.id.etItemDueDate);
    targetItem.setDueDate(etDueDate.getText().toString());
    targetItem.save();

    if(editIterator == null || editItem()) {
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

  private Item getDefaultItem() {
    return new Item(
           "Name",
           "Content",
           Item.Priority.MEDIUM,
           new LocalDate().toString(Item.DATE_FORMAT),
           false);
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

  private Item getItem(int index) {
    Item targetItem;
    if(index == items.size()) {
      targetItem = getDefaultItem();
    } else {
      targetItem = items.get(index);
    }
    return targetItem;
  }

}
