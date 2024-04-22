package com.example.objectdetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BitmapUtil {
    public static Bitmap decodeSampledBitmapFromByte(Context context, byte[] bytes, int reqWidth, int reqHeight) {
        // Calculate the maximum size of the image that can be loaded into memory
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int maxImageSize = maxMemory / 8;

        // Decode the byte array into a Bitmap object with the specified dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        int scale = 1;
        while ((options.outWidth / scale) * (options.outHeight / scale) > maxImageSize) {
            scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Rotate the Bitmap if necessary
        int orientation = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            ExifInterface exif = new ExifInterface(new ByteArrayInputStream(bytes));
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        // Scale the Bitmap to the desired dimensions
        if (bitmap.getWidth() > reqWidth || bitmap.getHeight() > reqHeight) {
            float scaleWidth = (float) reqWidth / bitmap.getWidth();
            float scaleHeight = (float) reqHeight / bitmap.getHeight();
            float scaleRatio = Math.min(scaleWidth, scaleHeight);
            int newWidth = Math.round(bitmap.getWidth() * scaleRatio);
            int newHeight = Math.round(bitmap.getHeight() * scaleRatio);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            bitmap.recycle();
            bitmap = scaledBitmap;
        }

        return bitmap;
    }
}
