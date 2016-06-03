# StorageView
android storageview

this is a view which shows the system storage situation（including the percentage） and represent with a rectangle

![image](https://github.com/peterforme/StorageView/blob/master/StorageApp/MyApplication/screenshots/0603_22_40_01.png)

usage:

you  need to declare the view on the xml, like this:

<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"

    tools:context="com.fkdd.storageapp.MainActivity"
    tools:showIn="@layout/activity_main">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!" />

    <com.fkdd.storageapp.StorageView
        android:id="@+id/storageView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        />

</RelativeLayout>


in the class , use it like this

storageView = (StorageView)findViewById(R.id.storageView);
storageView.setUsedRate(percent);
storageView.setLeftText(usedStr);
storageView.setRightText(availableStr);
storageView.setTotalText(totalStr);
storageView.setRefresh();

