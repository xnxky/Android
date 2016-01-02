package com.xxy.simpletodo.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.xxy.simpletodo.R;

/**
 * Created by xxiao on 1/1/16.
 */
public class LabeledEditText extends LabeledView<EditText> {

  public LabeledEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void inflate() {
    LayoutInflater.from(context)
                  .inflate(R.layout.labeled_edit_text, this, true);
    label = (TextView) findViewById(R.id.text_label);
    content = (EditText) findViewById(R.id.edit_text);
  }

  public String getContentString() {
    return content.getText().toString();
  }

  public void setContent(String content) {
    this.content.setText(content);
    this.content.setSelection(content.length());
  }
}
