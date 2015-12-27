package com.xxy.simpletodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.table.Item;

/**
 * Created by xxiao on 12/27/15.
 */
public class ViewItemActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    setContentView(R.layout.activity_view_item);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Item curItem = (Item)getIntent().getSerializableExtra("item");

    TextView tvName = (TextView) findViewById(R.id.tvItemName);
    tvName.setText(curItem.name);

    TextView tvContent = (TextView) findViewById(R.id.tvItemContent);
    tvContent.setText(curItem.content);

    TextView tvPriority = (TextView) findViewById(R.id.tvItemPriority);
    tvPriority.setText(curItem.priority.name());

    TextView tvDate = (TextView) findViewById(R.id.tvItemCompleteDate);
    tvDate.setText(curItem.getDate());
  }

  public void goBack(View v) {
    Intent intent = new Intent();
    setResult(RESULT_OK, intent);
    finish();
  }

}
