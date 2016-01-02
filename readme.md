This is a basic reminder of tasks to do and tasks completed

It has a menu bar with actions, and 2 tabs: "TODO" and "COMPLETE" For each
category of task. The actions available in menu bar are different when 
different tabs are selected.

For TODO tab, we have "+" icon for adding a new task, pen icon for editing 
tasks that have been checked, in the overflow, there are "Delete" and 
"Mark As Complete", which will delete and mark as complete the tasks that have
been checked. 

For COMPLETE tab, we have "eye" icon for viewing tasks that have been checked, a
trash can icon for deleting tasks that have been checked and in the overflow area,
"Mark As TODO" to remark the complete tasks as todo and move them back to tab area.

In the content area of each tab, we have a title line that indicates the color For
each prioirty (Red for HIGH, Orange for MEDIUM, and Green for LOW) and the "select all"
checkbox to select all the tasks in this tab. Then it's a listview of all the tasks in 
the current tab. We can click on each task to edit(in TODO tab) or view the task(In 
COMPLETE tab) and we can long click on each task to delete each task after confirming 
"yes" on the alert dialog.

The edit UI in TODO tab is an alert dialog with editable fields (EditText, Spinner and 
DatePicker), but the view UI in COMPLETE tab is only TextView in alert dialog. When a
todo task(s) are marked as complete, their date will be updated to the current date As
the completion date; When they are remarked as todo, their original todo due date are 
set back for the date. 

Also, when we bulk edit or view tasks, the back button can be clicked to cancel the rest
of tasks, while, in the eidt situation, saved modified tasks will be updated with new info.

Here is a demo of all the mentioned functionalities: ![image](https://github.com/xnxky/Android/blob/orig/simpleToDo.gif)
