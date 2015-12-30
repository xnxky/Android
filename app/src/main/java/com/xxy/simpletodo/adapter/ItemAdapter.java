package com.xxy.simpletodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.listener.CheckboxClickListener;
import com.xxy.simpletodo.table.Item;

import java.util.List;

/**
 * Created by xiangyang_xiao on 12/22/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

  private static final int TASK_NAME_MAX_LENGTH = 33;
  private static final String eclipse = "...";
  private final int highColor;
  private final int medColor;
  private final int lowColor;

  public ItemAdapter(
      Context context,
      List<Item> items,
      int highColor,
      int medColor,
      int lowColor) {
    super(context, 0, items);
    this.highColor = highColor;
    this.medColor = medColor;
    this.lowColor = lowColor;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Item item = getItem(position);

    if (convertView == null) {
      convertView = LayoutInflater.
                    from(getContext()).
                    inflate(R.layout.display_item, parent, false);
    }

    TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
    TextView itemDueDate = (TextView) convertView.findViewById(R.id.itemDueDate);
    itemName.setText(item.name);
    itemName.setTextColor(getColor(item));
    itemDueDate.setText(item.getDate());
    handleCheckbox(convertView, item);
    return convertView;
  }


  private int getColor(Item item) {
    int color;
    switch (item.priority) {
      case HIGH:
        color = highColor;
        break;
      case MEDIUM:
        color = medColor;
        break;
      case LOW:
      default:
        color = lowColor;
    }
    return color;
  }

  private void handleCheckbox(View convertView, Item item) {
    CheckBox checkbox = (CheckBox)convertView.
                                  findViewById(R.id.checkbox);
    item.checkbox = checkbox;
    checkbox.setChecked(item.isChecked);
    checkbox.setOnClickListener(
        new CheckboxClickListener(item, checkbox));
  }

}
