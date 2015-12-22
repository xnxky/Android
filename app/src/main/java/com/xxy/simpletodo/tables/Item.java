package com.xxy.simpletodo.tables;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by xiangyang_xiao on 12/21/15.
 */
@Table(name = "Items")
public class Item extends Model {
  @Column(name = "Content")
  public String content;

  @Override
  public String toString() {
    return content;
  }

  public Item() {
    super();
  }

  public Item(String content) {
    super();
    this.content = content;
  }
}
