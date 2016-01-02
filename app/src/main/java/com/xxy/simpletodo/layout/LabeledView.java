package com.xxy.simpletodo.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by xxiao on 1/1/16.
 */
public abstract class LabeledView<T extends View >
    extends LinearLayout {

  protected TextView label;
  protected T content;
  protected final Context context;

  abstract public void inflate();
  abstract public void setContent(String content);
  abstract public String getContentString();

  public LabeledView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOrientation(HORIZONTAL);
    this.context = context;
    inflate();
  }

  public boolean setFocus() {
    return content.requestFocus();
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  public void setLabelAndContent(
      String label, String content
  ) {
    setLabel(label);
    setContent(content);
  }

}
