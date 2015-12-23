package com.xxy.simpletodo.tables;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.LocalDate;

import java.io.Serializable;

/**
 * Created by xiangyang_xiao on 12/21/15.
 */
@Table(name = "Items")
public class Item extends Model
                  implements Serializable {

  public enum Priority {
    HIGH, MEDIUM, LOW
  }

  public enum Status {
    PREPARE, PROCESS, DONE
  }

  @Column(name = "name")
  public String name;

  @Column(name = "Content")
  public String content;

  @Column(name = "priority")
  public Priority priority;

  @Column(name = "dueDate")
  public LocalDate dueDate;

  @Column(name = "status")
  public Status status;

  public Item() {
    super();
  }

  public Item(String name,
              String content,
              Priority priority,
              LocalDate dueDate,
              Status status) {
    super();
    this.name = name;
    this.content = content;
    this.priority = priority;
    this.dueDate = dueDate;
    this.status = status;
  }

}
