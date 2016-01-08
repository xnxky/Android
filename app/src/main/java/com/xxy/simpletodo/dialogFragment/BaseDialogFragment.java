package com.xxy.simpletodo.dialogFragment;

import android.support.v4.app.DialogFragment;

import com.xxy.simpletodo.listener.DialogListener;

/**
 * Created by xxiao on 1/8/16.
 */
public class BaseDialogFragment extends DialogFragment {

  protected int index;
  protected DialogListener dialogListener;

  protected void setListener(
      DialogListener dialogListener
  ) {
    this.dialogListener = dialogListener;
  }

  public void setIndex(int index) {
    this.index = index;
  }
}
