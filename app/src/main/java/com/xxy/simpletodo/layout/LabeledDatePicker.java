package com.xxy.simpletodo.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.TextView;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.table.Item;

/**
 * Created by xxiao on 1/1/16.
 */
public class LabeledDatePicker extends LabeledView<DatePicker> {

  public LabeledDatePicker(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void inflate() {
    LayoutInflater.from(context)
                  .inflate(R.layout.labeled_datepicker, this, true);
    label = (TextView) findViewById(R.id.text_label);
    content = (DatePicker) findViewById(R.id.date_picker);
  }

  public String getContentString() {
    return Item.getDateString(
        content.getYear(),
        content.getMonth(),
        content.getDayOfMonth()
    );
  }

  public void setContent(String dateString) {
    //array of length 3: year, month, day
    int[] dateInfo = Item.getDateValue(dateString);
    content.init(
        dateInfo[0],
        dateInfo[1],
        dateInfo[2],
        null
    );
  }
}


