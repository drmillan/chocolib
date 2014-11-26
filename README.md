chocolib
========

Android UI components

## UI Components

Truetype font must be placed under assets/fonts

### ExText
Extended EditText with custom font support
### ExButton
Extended Button with custom font support
### ExLabel
Extended TextView with custom font support


## UI sample

Widget usage sample with font: Pacifico:
assets/fonts/Pacifico.ttf
assets/fonts/CaviarDreams.ttf
assets/fonts/AlexBrush-Regular.ttf

```xml

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    <com.chocodev.chocolib.widget.ExLabel
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        app:fontName="Pacifico"
        android:textSize="30sp"
        android:gravity="center"
        android:layout_margin="10dp"
        android:text="ExLabel"/>
    <com.chocodev.chocolib.widget.ExButton
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        app:fontName="AlexBrush-Regular"
        android:textSize="40sp"
        android:gravity="center"
        android:layout_margin="10dp"
        android:text="ExButton"/>
    <com.chocodev.chocolib.widget.ExText
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        app:fontName="CaviarDreams"
        android:textSize="30sp"
        android:gravity="center"
        android:layout_margin="10dp"
        android:text="ExText"/>
</LinearLayout>


```

![Alt text](/doc/widget_demo.png)