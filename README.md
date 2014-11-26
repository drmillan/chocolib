chocolib
========

Android UI components

## UI Components

### ExText
Extended EditText with custom font support
### ExButton
Extended Button with custom font support
### ExLabel
Extended TextView with custom font support

Truetype font must be placed under assets/fonts

Widget usage sample with font: Pacifico:

assets/fonts/Pacifico.ttf

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:custom="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center">

<com.chocodev.chocolib.widget.ExLabel
        custom:fontName="Pacifico"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceMedium"
        android:text="Circle"
        android:textSize="30sp"
        android:layout_marginRight="20dp"
        android:layout_marginLeft="20dp"
        android:gravity="center"
        android:id="@+id/textView"
        android:layout_gravity="center_horizontal" />

</LinearLayout>

```

![Alt text](/doc/exlabel.png)