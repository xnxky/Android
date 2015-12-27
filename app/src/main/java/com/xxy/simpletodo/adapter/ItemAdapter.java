package com.xxy.simpletodo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.listener.CheckboxClickListener;
import com.xxy.simpletodo.table.Item;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangyang_xiao on 12/22/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

  private static final int TASK_NAME_MAX_LENGTH = 33;
  private static final String eclipse = "...";

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
    //TextView itemContent = (TextView) convertView.findViewById(R.id.itemContent);
    //TextView itemPriority = (TextView) convertView.findViewById(R.id.itemPriority);
    TextView itemDueDate = (TextView) convertView.findViewById(R.id.itemDueDate);
    //TextView itemStatus = (TextView) convertView.findViewById(R.id.itemStatus);

    itemName.setText(getDisplayTaskName(item.name));
    itemName.setTextColor(getColor(item));

    //itemContent.setText(item.content);
    //itemPriority.setText(item.priority.name());
    itemDueDate.setText(item.getDate());
    //itemStatus.setText(item.status.name().substring(0,1));

    handleCheckbox(convertView, item);


    return convertView;
  }

  private String getDisplayTaskName(String taskName) {
    List<String> words = new ArrayList<>();
    int curLength = eclipse.length();
    for(String word : taskName.split("\\s")) {
      if(word.length() == 0) continue;

      if(word.length() + 1 + curLength > TASK_NAME_MAX_LENGTH) {
        if(words.size() == 0) {
          words.add(word.substring(0, TASK_NAME_MAX_LENGTH-eclipse.length()));
        }
        words.add(eclipse);
        break;
      }
      words.add(word);
      curLength += 1 + word.length();
    }
    return StringUtils.join(words, " ");
  }

  private int getColor(Item item) {
    int color;
    switch (item.priority) {
      case HIGH:
        color = Color.RED;
        break;
      case MEDIUM:
        color = Color.YELLOW;
        break;
      case LOW:
      default:
        color = Color.GREEN;
    }
    return color;
  }

  private void handleCheckbox(View convertView, Item item) {
    CheckBox checkbox = (CheckBox)convertView.
                                  findViewById(R.id.checkbox);
    checkbox.setChecked(item.isChecked);
    checkbox.setOnClickListener(
        new CheckboxClickListener(item, checkbox));
  }

}
