package com.xxy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;

import com.xxy.simpletodo.tables.Item;

import org.joda.time.LocalDate;

public class EditItemActivity extends AppCompatActivity {

  private int itemIndex;
  private Item targetItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_item);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    itemIndex = getIntent().getIntExtra("itemIndex", 0);
    targetItem = (Item)getIntent().getSerializableExtra("item");

    EditText etName = (EditText) findViewById(R.id.etItemName);
    etName.setText(targetItem.name);
    Selection.setSelection(etName.getText(), etName.length());
    EditText etConent = (EditText) findViewById(R.id.etItemContent);
    etConent.setText(targetItem.content);
    EditText etPriority = (EditText) findViewById(R.id.etItemPriority);
    etPriority.setText(targetItem.priority.name());
    EditText etDueDate = (EditText) findViewById(R.id.etItemDueDate);
    etDueDate.setText(targetItem.dueDate.toString("MM/dd/yyyy"));
    EditText etStatus = (EditText) findViewById(R.id.etItemStatus);
    etStatus.setText(targetItem.status.name());

  }

  public void saveItem(View v) {
    EditText etName = (EditText) findViewById(R.id.etItemName);
    targetItem.name = etName.getText().toString();
    EditText etConent = (EditText) findViewById(R.id.etItemContent);
    targetItem.content = etConent.getText().toString();
    EditText etPriority = (EditText) findViewById(R.id.etItemPriority);
    targetItem.priority = Item.Priority.valueOf(etPriority.getText().toString());
    EditText etDueDate = (EditText) findViewById(R.id.etItemDueDate);
    targetItem.dueDate = new LocalDate(etDueDate.getText().toString());
    EditText etStatus = (EditText) findViewById(R.id.etItemStatus);
    targetItem.status = Item.Status.valueOf(etStatus.getText().toString());

    Intent intent = new Intent();
    intent.putExtra("itemIndex", itemIndex);
    intent.putExtra("itemText", targetItem);

    setResult(RESULT_OK, intent);
    finish();
  }

}
