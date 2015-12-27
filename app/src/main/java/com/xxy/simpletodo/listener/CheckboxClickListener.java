package com.xxy.simpletodo.listener;

import android.view.View;
import android.widget.CheckBox;

import com.xxy.simpletodo.table.Item;

/**
 * Created by xxiao on 12/27/15.
 */
public class CheckboxClickListener implements View.OnClickListener {

  private Item item;
  private CheckBox checkbox;

  public CheckboxClickListener(Item item, CheckBox checkbox) {
    this.item = item;
    this.checkbox = checkbox;
  }

  @Override
  public void onClick(View v) {
    item.isChecked = checkbox.isChecked();
  }
}
