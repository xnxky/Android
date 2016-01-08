package com.xxy.simpletodo.dialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.xxy.simpletodo.listener.DialogListener;

/**
 * Created by xxiao on 1/8/16.
 */
public class DeleteDialogFragment extends BaseDialogFragment {

  public DeleteDialogFragment() {}

  public static DeleteDialogFragment newInstance(
      DialogListener dialogListener
  ) {
    DeleteDialogFragment deleteDialogFragment =
        new DeleteDialogFragment();
    deleteDialogFragment.setListener(dialogListener);
    return deleteDialogFragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstance) {
    Dialog dialog = new AlertDialog.
        Builder(getActivity()).
        setTitle("Alert").
        setMessage("Are you sure you want to delete?").
        setPositiveButton(
            "Yes",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialogListener.onPositiveButtonPressed(null, null, index);
              }
            }
        ).
        setNegativeButton(
            "No",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int index) {
                dialogListener.onNegativeButtonPressed(dialog);
              }
            }
        ).create();
    return dialog;
  }
}
