package com.xxy.simpletodo.listener;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.xxy.simpletodo.R;

/**
 * Created by xxiao on 12/26/15.
 */
public class TodoTabListener implements ActionBar.TabListener {

  private Fragment fragment;

  public TodoTabListener(Fragment fragment) {
    this.fragment = fragment;
  }

  @Override
  public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
    if(fragment != null) {
      fragmentTransaction.replace(R.id.fragment_container, fragment);
    }
  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
    if(fragment != null) {
      fragmentTransaction.remove(fragment);
    }
  }

}
