# Pre-work - *Reminder*

**Reminder** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Xiangyang Xiao**

Time spent: **20** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [X] Add support for completion due dates for todo items (and display within listview item)
* [X] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [X] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] List anything else that you can get done to improve the app functionality!

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://github.com/xnxky/Android/blob/orig/simpleToDo.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

I spent a lot of time to make the theme right, like actionbar, for AppCompatActivity, 
android.suport.v7.app. Many info I found in web does not apply to it any more. for
instance, I tried to set background color by creating a new theme and customizing its
color, but apprantly, no matter what color I give, it's always black.

## License

    Copyright [2016] [Xiangyang Xiao]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

## additional info
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

