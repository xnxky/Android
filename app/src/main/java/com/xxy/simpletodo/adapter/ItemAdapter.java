package com.xxy.simpletodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.tables.Item;

import java.util.List;

/**
 * Created by xiangyang_xiao on 12/22/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

  public ItemAdapter(Context context, List<Item> items) {
    super(context, 0, items);
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
    TextView itemContent = (TextView) convertView.findViewById(R.id.itemContent);
    TextView itemPriority = (TextView) convertView.findViewById(R.id.itemPriority);
    TextView itemDueDate = (TextView) convertView.findViewById(R.id.itemDueDate);
    TextView itemStatus = (TextView) convertView.findViewById(R.id.itemStatus);

    itemName.setText(item.name);
    itemContent.setText(item.content);
    itemPriority.setText(item.priority.name());
    itemDueDate.setText(item.dueDate.toString("MM/dd/yyyy"));
    itemStatus.setText(item.status.name());

    return convertView;
  }
}
