package com.xxy.simpletodo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.common.collect.ImmutableList;
import com.xxy.simpletodo.R;
import com.xxy.simpletodo.fragment.DoneFragment;
import com.xxy.simpletodo.fragment.TabFragment;
import com.xxy.simpletodo.fragment.TodoFragment;
import com.xxy.simpletodo.listener.TodoTabListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    View view = findViewById(R.id.fragment_container);
    //view.getBackground().setAlpha(200);

    final ActionBar tabBar = getSupportActionBar();
    tabBar.setBackgroundDrawable(
        getResources().getDrawable(R.drawable.title_background));
    tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    List<String> tabTextList = ImmutableList.of(
        "ToDo",
        "Complete"
    );

    List<TabFragment> tabFragments = ImmutableList.of(
        new TodoFragment(),
        new DoneFragment()
    );

    for(int i=0; i<tabTextList.size(); i++) {
      ActionBar.Tab todoTab = tabBar.
                              newTab().
                              setText(tabTextList.get(i)).
                              setTabListener(
                                  new TodoTabListener(
                                      tabFragments.get(i)
                                  ));
      tabBar.addTab(todoTab);
    }
  }

}
