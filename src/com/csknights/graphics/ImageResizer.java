package com.csknights.graphics;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageResizer {

	private Matrix matrix;

	public void init(int oldWidth, int oldHeight, float newWidth, float newHeight) {
        float scaleWidth = newWidth / oldWidth;
        float scaleHeight = newHeight / oldHeight;
        matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);		
	}

    public Bitmap resize(Bitmap bitmap) {
    	if (matrix == null) {
    		return bitmap;
    	}
    	
    	int width = bitmap.getWidth();
        int height = bitmap.getHeight();

    	Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                					width, height, matrix, true);
    	return resizedBitmap;
    }
}
