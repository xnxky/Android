package com.xxy.simpletodo.table;

import android.widget.CheckBox;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by xiangyang_xiao 12/21/15.
 */
@Table(name = "Items")
public class Item extends Model
                  implements Serializable {

  public final static String DATE_FORMAT = "MM/dd/yy";

  public enum Priority {
    HIGH, MEDIUM, LOW
  }

  @Column(name = "name")
  public String name;

  @Column(name = "Content")
  public String content;

  @Column(name = "priority")
  public Priority priority;

  //Format DATE_FORMAT
  @Column(name = "dueDate")
  private String dueDate;

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  //Format DATE_FORMAT
  @Column(name = "compDate")
  private String compDate;

  public void setCompDate(String compDate) {
    this.compDate = compDate;
  }

  @Column(name = "isDone")
  public boolean isDone;

  public boolean isChecked = false;

  public transient CheckBox checkbox;

  public String getDate() {
    if(isDone) {
      return compDate;
    } else {
      return dueDate;
    }
  }

  public Item() {
    super();
  }

  public Item(String name,
              String content,
              Priority priority,
              String dueDate,
              boolean isDone) {
    super();
    updateFields(name,
                 content,
                 priority,
                 dueDate,
                 null,
                 isDone);
  }

  public void update(Item item) {
    updateFields(item.name,
                 item.content,
                 item.priority,
                 item.dueDate,
                 item.compDate,
                 item.isDone);
  }

  public void updateFields(String name,
              String content,
              Priority priority,
              String dueDate,
              String compDate,
              boolean isDone) {
    this.name = name;
    this.content = content;
    this.priority = priority;
    this.dueDate = dueDate;
    this.compDate = compDate;
    this.isDone = isDone;
  }

}
