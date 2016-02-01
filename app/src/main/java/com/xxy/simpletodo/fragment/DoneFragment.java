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

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.dialogFragment.ViewDialogFragment;
import com.xxy.simpletodo.layout.LabeledTextView;
import com.xxy.simpletodo.listener.DialogListener;
import com.xxy.simpletodo.table.Item;

import java.util.Iterator;

/**
 * Created by xxiao on 12/26/15.
 */
public class DoneFragment extends TabFragment {

  private Iterator<Item> iterator;
  private int viewItemIndex;
  private final ViewDialogListener viewDialogListener;

  @Override
  public String getTitle() {
    return "Complete";
  }

  @Override
  public int getTabImageId() {
    return R.drawable.done_tab;
  }

  public DoneFragment() {
    super();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isDone", true);
    this.setArguments(bundle);
    viewDialogListener = new ViewDialogListener();
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
      viewItemIndex ++;
      Item nextItem = iterator.next();
      if(nextItem.isChecked){
        viewItem(viewItemIndex);
        break;
      }
    }
  }

  private void viewItem(int index) {
    ViewDialogFragment dialogFragment =
        ViewDialogFragment.newIntance(viewDialogListener);
    dialogFragment.setIndex(index);
    FragmentManager fm = getActivity().getSupportFragmentManager();
    dialogFragment.show(fm, "view");
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
        viewItemIndex = -1;
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
        theOtherFragment.addItem(curItem);
      }
    }
    itemAdapter.notifyDataSetChanged();
    theOtherFragment.updateItems();
  }

  private class ViewDialogListener implements DialogListener {
    @Override
    public void onPositiveButtonPressed(
        DialogInterface dialog, View view, int index) {
      dialog.dismiss();
      viewItem();
    }

    @Override
    public void onNegativeButtonPressed(DialogInterface dialog) {
    }

    @Override
    public void onBackKeyClicked(int keyCode, KeyEvent event) {
      if(keyCode==KeyEvent.KEYCODE_BACK &&
          event.getRepeatCount()==0) {
        iterator = null;
      }
    }

    @Override
    public void initialize(View view, int index) {
      Item targetItem = getItem(index);
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
  }

}
