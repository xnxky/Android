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

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.layout.LabeledTextView;
import com.xxy.simpletodo.table.Item;

import java.util.Iterator;

/**
 * Created by xxiao on 12/26/15.
 */
public class DoneFragment extends TabFragment {

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

  //interesting, if I do it in one while loop,
  //none of the dialogs won't show until the
  //iteration is over
  private void viewItem() {
    while(iterator!=null && iterator.hasNext()) {
      Item nextItem = iterator.next();
      if(nextItem.isChecked){
        viewItem(nextItem);
        break;
      }
    }
  }

  private void viewItem(int index) {
    Item item = getItem(index);
    viewItem(item);
  }

  private void viewItem(Item item) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    final View view = inflater.inflate(R.layout.view_item, null);
    setDisplayItem(view, item);
    Dialog dialog = new AlertDialog.
        Builder(getActivity()).
        setView(view).
        setPositiveButton(
            "Back",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                viewItem();
              }
            }
        ).
        setOnKeyListener(
            new DialogInterface.OnKeyListener() {
              @Override
              public boolean onKey(
                  DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK &&
                    event.getRepeatCount()==0) {
                  iterator = null;
                }
                return false;
              }
            }
        ).create();
    dialog.show();
  }

  protected void setDisplayItem(View view, Item targetItem) {
    LabeledTextView etName = (LabeledTextView)view.findViewById(R.id.etItemName);
    etName.setLabelAndContent(
        curResource.getString(R.string.task_name_label),
        targetItem.name);

    LabeledTextView tvDate = (LabeledTextView)view.findViewById(R.id.datePicker);
    tvDate.setLabelAndContent(
        curResource.getString(R.string.task_comp_date_label),
        targetItem.getDate()
    );

    LabeledTextView tvPriority = (LabeledTextView)view.findViewById(R.id.etItemPriority);
    tvPriority.setLabelAndContent(
        curResource.getString(R.string.task_priority_label),
        targetItem.priority.name()
    );

    LabeledTextView etConent = (LabeledTextView)view.findViewById(R.id.etItemContent);
    etConent.setLabelAndContent(
        curResource.getString(R.string.task_note_label),
        targetItem.content
    );
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.done_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.view_item_menu:
        iterator = items.iterator();
        viewItem();
        return true;
      case R.id.undone_item_menu:
        bulkUnmarkItems();
        return true;
      default:
        return super.onOptionsItemSelected(item);
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
