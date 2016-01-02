package com.xxy.simpletodo.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.xxy.simpletodo.R;

/**
 * Created by xxiao on 1/1/16.
 */
public class LabeledSpinner extends LabeledView<Spinner> {

  ArrayAdapter<String> adapter;

  public LabeledSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void inflate() {
    LayoutInflater.from(context)
                  .inflate(R.layout.labeled_spinner, this, true);
    label = (TextView) findViewById(R.id.text_label);
    content = (Spinner) findViewById(R.id.spinner_priority);
  }

  public String getContentString() {
    return (String)content.getSelectedItem();
  }

  public void setAdapter(ArrayAdapter<String> adapter) {
    this.adapter = adapter;
  }

  public void setContent(String priorityString){
    if(adapter != null) {
      int position = adapter.getPosition(priorityString);
      content.setSelection(position);
    } else {
      Log.e("No adapter", "Spinner adapter is not set yet");
    }
  }

  public void setContent(
      String priorityString, ArrayAdapter<String> adapter) {
    if(this.adapter == null) {
      this.adapter = adapter;
      content.setAdapter(this.adapter);
    }
    setContent(priorityString);
  }

}
