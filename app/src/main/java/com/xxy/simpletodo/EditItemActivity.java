package com.xxy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

  private int itemIndex;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_item);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    itemIndex = getIntent().getIntExtra("itemIndex", 0);
    String itemText = getIntent().getStringExtra("itemText");
    EditText et = (EditText) findViewById(R.id.itemContent);
    et.setText(itemText);
    Selection.setSelection(et.getText(), et.length());
  }

  public void saveItem(View v) {
    EditText et = (EditText) findViewById(R.id.itemContent);
    String itemText = et.getText().toString();

    Intent intent = new Intent();
    intent.putExtra("itemIndex", itemIndex);
    intent.putExtra("itemText", itemText);

    setResult(RESULT_OK, intent);
    finish();
  }

}
