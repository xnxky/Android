package com.xxy.simpletodo.listener;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by xxiao on 1/8/16.
 */
public interface DialogListener {
  void onPositiveButtonPressed(DialogInterface dialog, View view, int index);
  void onNegativeButtonPressed(DialogInterface dialog);
  void onBackKeyClicked(int keyCode, KeyEvent event);

  void initialize(View view, int index);
}
