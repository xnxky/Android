package com.xxy.simpletodo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by xiangyang_xiao on 12/21/15.
 */
@Table(name = "Categories")
public class Category extends Model {
  @Column(name = "Name")
  public String name;
}
