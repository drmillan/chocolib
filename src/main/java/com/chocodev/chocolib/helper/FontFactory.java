package com.chocodev.chocolib.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by DRM on 26/07/13.
 */
public class FontFactory {
    private static FontFactory instance=new FontFactory();
    private Map<String,Typeface> fontCache=new TreeMap<String, Typeface>();
    private FontFactory()
    {

    }
    public static FontFactory getInstance()
    {
        return instance;
    }

    public synchronized Typeface getTypeface(Context context,String fontName) {

        if(!fontCache.containsKey(fontName))
        {
            AssetManager manager=context.getAssets();

            String fontNameWithExtension = new String();
            try {
                String fileList[] = manager.list("fonts");
                if(fileList!=null) {
                    for (String fileName : fileList) {
                        if (fontName.equals(fileName.substring(0, fileName.lastIndexOf('.')))) {
                            // File found
                            fontNameWithExtension = fileName;
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Typeface tf;
            if (fontNameWithExtension.length() > 0) {
                tf=Typeface.createFromAsset(manager, "fonts/" + fontNameWithExtension);
            } else {
                // Font not found
                tf = Typeface.DEFAULT;
            }
            fontCache.put(fontName,tf);
        }
        Typeface typeFace=fontCache.get(fontName);
        return typeFace;
    }
}
