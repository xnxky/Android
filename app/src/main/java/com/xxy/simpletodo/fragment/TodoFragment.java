package com.xxy.simpletodo.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import com.xxy.simpletodo.dialogFragment.EditDialogFragment;
import com.xxy.simpletodo.layout.LabeledDatePicker;
import com.xxy.simpletodo.layout.LabeledEditText;
import com.xxy.simpletodo.layout.LabeledSpinner;
import com.xxy.simpletodo.listener.DialogListener;
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
  private final EditDialogListener editDialogListener;

  @Override
  public String getTitle() {
    return "ToDo";
  }

  @Override
  public int getTabImageId() {
    return R.drawable.todo_tab;
  }

  public TodoFragment() {
    super();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isDone", false);
    this.setArguments(bundle);
    refreshIndexList = new ArrayList<>();
    editDialogListener = new EditDialogListener();
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
    EditDialogFragment editDialogFragment =
        EditDialogFragment.newInstance(editDialogListener);
    editDialogFragment.setIndex(index);
    FragmentManager fm = getActivity().getSupportFragmentManager();
    editDialogFragment.show(fm, "edit");

  }

  private void handleLastModify() {
    if(editIterator == null || editItem()) {
      for(int refreshIdx : refreshIndexList) {
        Item refreshItem = getItem(refreshIdx);
        items.remove(refreshIdx);
        Item.insert(items, refreshItem);
      }
      if(!refreshIndexList.isEmpty())
        itemAdapter.notifyDataSetChanged();
      refreshIndexList.clear();
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
        theOtherFragment.addItem(curItem);
      }
    }
    itemAdapter.notifyDataSetChanged();
    theOtherFragment.updateItems();
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

  private class EditDialogListener implements DialogListener {
    @Override
    public void onPositiveButtonPressed(DialogInterface dialog, View view, int index) {
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
        itemAdapter.notifyDataSetChanged();
      }
      handleLastModify();
    }

    @Override
    public void onNegativeButtonPressed(DialogInterface dialog) {
      handleLastModify();
    }

    @Override
    public void onBackKeyClicked(int keyCode, KeyEvent event) {
      if(keyCode == KeyEvent.KEYCODE_BACK &&
          event.getRepeatCount() == 0) {
        editIterator = null;
        handleLastModify();
      }
    }

    @Override
    public void initialize(View view, int index) {
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
  }

}
