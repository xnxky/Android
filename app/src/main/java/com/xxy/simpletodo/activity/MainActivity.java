package com.xxy.simpletodo.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.adapter.ToDoFragmentPagerAdapter;
import com.xxy.simpletodo.fragment.DoneFragment;
import com.xxy.simpletodo.fragment.TabFragment;
import com.xxy.simpletodo.fragment.TodoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private TabLayout tabLayout;
  private ViewPager viewPager;

  private static final String TAB_INDEX = "TAB_INDEX";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ActionBar tabBar = getSupportActionBar();
    Drawable titleBackground = ContextCompat.getDrawable(
        getApplicationContext(), R.drawable.title_background);

    if(titleBackground != null) {
      tabBar.setBackgroundDrawable(titleBackground);
    }
    tabBar.setDisplayHomeAsUpEnabled(true);

    viewPager = (ViewPager) findViewById(R.id.fragment_container);
    List<TabFragment> fragmentList =
        new ArrayList<>();
    TabFragment todoFragment = new TodoFragment();
    TabFragment doneFragment = new DoneFragment();
    todoFragment.setTheOtherFragment(doneFragment);
    doneFragment.setTheOtherFragment(todoFragment);
    fragmentList.add(todoFragment);
    fragmentList.add(doneFragment);
    ToDoFragmentPagerAdapter pagerAdapter =
        new ToDoFragmentPagerAdapter(
            getSupportFragmentManager(),
            fragmentList,
            getApplicationContext()
        );
    viewPager.setAdapter(pagerAdapter);

    tabLayout = (TabLayout)findViewById(R.id.tabs_layout);
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.setTabMode(TabLayout.MODE_FIXED);

    for(int pos = 0; pos < tabLayout.getTabCount(); pos++) {
      Tab tab = tabLayout.getTabAt(pos);
      tab.setCustomView(pagerAdapter.getTabView(pos));
    }

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(TAB_INDEX,tabLayout.getSelectedTabPosition());
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    viewPager.setCurrentItem(savedInstanceState.getInt(TAB_INDEX));
  }

}
