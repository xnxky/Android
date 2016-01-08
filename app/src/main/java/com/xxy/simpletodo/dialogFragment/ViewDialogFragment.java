package com.xxy.simpletodo.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.listener.DialogListener;

/**
 * Created by xxiao on 1/8/16.
 */
public class ViewDialogFragment extends BaseDialogFragment {

  public ViewDialogFragment() {}

  public static ViewDialogFragment newIntance(
      DialogListener dialogListener
  ) {
    ViewDialogFragment viewDialogFragment =
        new ViewDialogFragment();
    viewDialogFragment.setListener(dialogListener);
    return viewDialogFragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstance) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    final View view = inflater.inflate(R.layout.view_item, null);
    dialogListener.initialize(view, index);
    Dialog dialog = new AlertDialog.
        Builder(getActivity()).
        setView(view).
        setPositiveButton(
            "Back",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialogListener.onPositiveButtonPressed(dialog, view, index);
              }
            }
        ).
        setOnKeyListener(
            new DialogInterface.OnKeyListener() {
              @Override
              public boolean onKey(
                  DialogInterface dialog, int keyCode, KeyEvent event
              ) {
                dialogListener.onBackKeyClicked(keyCode, event);
                return false;
              }
            }
        ).create();
    return dialog;
  }

}
