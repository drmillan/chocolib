package com.chocodev.chocolib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


/**
 * Created by drm on 24/06/15.
 */
public class ImageCaptureHelper {
    public static final int ACTION_REQUEST_GALLERY = 9999;
    public static final int ACTION_REQUEST_CAMERA = 8888;
    public String onActivityResult(Context context,Uri cameraImageUri,int requestCode, int resultCode, Intent data) throws IOException{
        return onActivityResult(context,cameraImageUri,requestCode,resultCode,data,-1,-1);

    }
    public String onActivityResult(Context context,Uri cameraImageUri,int requestCode, int resultCode, Intent data,int maxImageSize,int maxFileSize) throws IOException{
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == ACTION_REQUEST_GALLERY){
                if (data.getData()!=null){
                    Uri imageUri = data.getData();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        String appPath="";
                        if(imageUri.toString().startsWith("content")) {
                            appPath=copyToAppData(context,context.getContentResolver().openInputStream(imageUri));
                        }
                        else {
                            String sourcePath = getRealPath(context, imageUri);
                            appPath = copyToAppData(context,sourcePath, false);
                            BitmapFactory.decodeFile(appPath, options);
                        }
                        processBitmap(appPath, options,maxImageSize,maxFileSize);
                        return appPath;
                }
            } else if (requestCode == ACTION_REQUEST_CAMERA){
                context.getContentResolver().notifyChange(cameraImageUri, null);
                    // Mover el archivo de la carpeta imágenes a la carpeta privada
                    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String dataDir = context.getApplicationInfo().dataDir;
                    String imageFileName = new File(cameraImageUri.getPath()).getName();
                    File src = new File(storageDir + "/" + imageFileName);
                    File dest = new File(dataDir + "/" + imageFileName);
                    FileUtils.moveFile(src, dest);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    String encodedPath = dest.getAbsolutePath();
                    String appPath=copyToAppData(context,encodedPath, true);
                    BitmapFactory.decodeFile(appPath, options);
                    processBitmap(appPath, options,maxImageSize,maxFileSize);
                    return appPath;
            }
        }
        return "";
    }
    private String copyToAppData(Context context,String sourcePath, boolean isCameraPicture) throws IOException {
        File file=new File(sourcePath);
        ContextWrapper c = new ContextWrapper(context);
        String destFile=c.getFilesDir().getAbsolutePath()+"/"+ UUID.randomUUID().toString()+"_"+file.getName();
        FileUtils.copyFile(file, new File(destFile));
        // Después de copiar el archivo, si es una fotografía tomada desde la cámara, borramos el archivo fuente para que no ocupe espacio
        if (isCameraPicture){
            FileUtils.deleteQuietly(file);
        }
        return destFile;

    }

    private String copyToAppData(Context context,InputStream sourceStream) throws IOException{
        ContextWrapper c = new ContextWrapper(context);
        String destFile=c.getFilesDir().getAbsolutePath()+"/"+ UUID.randomUUID().toString();
        FileUtils.copyInputStreamToFile(sourceStream, new File(destFile));
        return destFile;

    }

    protected String getRealPath(Context context, Uri imageUri){
        String fullPath;
        if (Build.VERSION.SDK_INT < 11){
            fullPath = RealPathUtil.getRealPathFromURI_BelowAPI11(context, imageUri);
        }
        else if (Build.VERSION.SDK_INT < 19){
            fullPath = RealPathUtil.getRealPathFromURI_API11to18(context, imageUri);
        }
        else {
            fullPath = RealPathUtil.getRealPathFromURI_API19(context, imageUri);
        }
        return fullPath;
    }

    protected void processBitmap(String pathToBitmap, BitmapFactory.Options options,int maxImageSize,int maxFileSize) throws IOException {
        if(maxImageSize==-1 && maxFileSize==-1)
        {
            return;
        }
        // Ajustamos el tamaño del bitmap
        int scale = 1;
        while (options.outWidth/scale > maxImageSize || options.outHeight/scale > maxImageSize){
            scale++;
        }

        BitmapFactory.Options optionsForScale = new BitmapFactory.Options();
        optionsForScale.inSampleSize = scale;
        Bitmap scaled = BitmapFactory.decodeFile(pathToBitmap, optionsForScale);

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, scaled.getWidth(), scaled.getHeight()), new RectF(0, 0, maxImageSize, maxImageSize), Matrix.ScaleToFit.CENTER);

        if(maxFileSize!=-1) {
            // Ajustamos la calidad del JPG
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int size;
            int quality = 100;
            Bitmap scaledFit;

            do {
                baos.reset();
                scaledFit = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), m, true);
                scaledFit.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                size = baos.size();
                quality -= 10;
                if (size > maxFileSize) {
                    scaledFit.recycle();
                }
            } while (size > maxFileSize);
            if (scaled != scaledFit) {
                scaled.recycle();
            }
            baos.flush();
            baos.close();
            // Guardamos el bitmap escalado y eliminamos el anterior, que es más grande y ocupa más espacio

            // Eliminamos el .jpg creado de tomar la fotografía que es grande
            FileUtils.deleteQuietly(new File(pathToBitmap));

            // Guardamos scaledFit, un bitmap con el tamaño correcto, en el .jpg que se enviará al servidor en base64
            File scaledBitmapFile = new File(pathToBitmap);
            FileOutputStream out = new FileOutputStream(scaledBitmapFile);
            scaledFit.compress(Bitmap.CompressFormat.JPEG, quality + 10, out);
            out.flush();
            out.close();
            // Como ya hemos guardado el nuevo archivo y tenemos la ruta, no necesitamos el bitmap, lo reciclamos
            scaledFit.recycle();
        }
        else
        {
            Bitmap scaledFit = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), m, true);
            File scaledBitmapFile = new File(pathToBitmap);
            FileOutputStream out = new FileOutputStream(scaledBitmapFile);
            scaledFit.compress(Bitmap.CompressFormat.PNG,100, out);
            out.flush();
            out.close();
            scaledFit.recycle();
        }
    }

}
