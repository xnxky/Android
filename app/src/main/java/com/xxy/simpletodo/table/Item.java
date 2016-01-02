package com.xxy.simpletodo.table;

import android.widget.CheckBox;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xiangyang_xiao 12/21/15.
 */
@Table(name = "Items")
public class Item extends Model
                  implements Serializable {

  public final static String DATE_FORMAT = "MM/dd/yyyy";

  public enum Priority {
    HIGH, MEDIUM, LOW
  }

  private final static List<String> priorityList
      = new ArrayList<>();

  static {
    for(Item.Priority onePriority : Item.Priority.values()) {
      priorityList.add(onePriority.name());
    }
  }

  public static List<String> getPriorityList() {
    return priorityList;
  }

  public static Comparator<Item> toDoComparator =
      new Comparator<Item>() {
        @Override
        public int compare(Item lhs, Item rhs) {
          return toDoCompare(lhs, rhs);
        }
      };

  private static int toDoCompare(Item first, Item second) {
    int comparePriorityResult = comparePriority(first, second);
    if(comparePriorityResult != 0) return comparePriorityResult;
    return compareDate(
        first.getDateValue(),
        second.getDateValue()
    );
  }

  private static int comparePriority(
      Item first, Item second) {
    int firstOrdinal = first.priority.ordinal();
    int secOrdinal = second.priority.ordinal();
    if(firstOrdinal != secOrdinal) {
      return firstOrdinal - secOrdinal;
    }
    return 0;
  }

  private static int compareDate(int[] date1, int[] date2) {
    for(int idx=0; idx<3; idx++) {
      if(date1[idx] != date2[idx]) {
        return date1[idx] - date2[idx];
      }
    }
    return 0;
  }

  public static Comparator<Item> doneComparator =
      new Comparator<Item>() {
        @Override
        public int compare(Item lhs, Item rhs) {
          int compareDateResult = -compareDate(
              lhs.getDateValue(),
              rhs.getDateValue()
          );
          if(compareDateResult != 0) return compareDateResult;
          return comparePriority(lhs, rhs);
        }
      };

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

  public static String getDateString(
      int year, int month, int day
  ) {
    String yearString = String.valueOf(year);
    String monthString = month < 9 ?
        "0" + String.valueOf(month+1) : String.valueOf(month+1);
    String dayString = day < 10 ?
        "0" + String.valueOf(day) : String.valueOf(day);
    return monthString+"/"+dayString+"/"+yearString;
  }

  public int[] getDateValue() {
    return getDateValue(getDate());
  }

  public static int[] getDateValue(String date) {
    String[] dateArray = date.split("/");
    int year = Integer.valueOf(dateArray[2]);
    int month = Integer.valueOf(dateArray[0])-1;
    int day = Integer.valueOf(dateArray[1]);
    return new int[]{year, month, day};
  }

  public static void insert(List<Item> items, Item newItem) {
    Comparator<Item> comparator = newItem.isDone?
        doneComparator : toDoComparator;
    int firstIdx = 0;
    int lastIdx = items.size()-1;
    while(firstIdx <= lastIdx) {
      int midIdx = (firstIdx+lastIdx)/2;
      int compareResult =
          comparator.compare(items.get(midIdx), newItem);
      if(compareResult <= 0) {
        firstIdx = midIdx + 1;
      } else if(compareResult > 0) {
        lastIdx = midIdx - 1;
      }
    }
    items.add(firstIdx, newItem);
  }

}
