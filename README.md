# TimeBar
A TimeBar which imitates evernote.
just a simple flat custom view
#Screenshot
![](https://github.com/wuapnjie/TimeBar/blob/master/screenshots/timebar.gif)
#Usage
in your layout
```xml
<com.flying.xiaopo.evernotepicker.TimeBar
		android:id="@+id/timeBar"
		android:layout_gravity="center"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"/>
```
in your java file
```java
    int hour = mTimeBar.getHour();
    int minute = mTimeBar.getMinute();
    String time = mTimeBar.getTime();
```
