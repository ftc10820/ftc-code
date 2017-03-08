package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

/**
 * Created by the Falconeers 10820
 */

public class ImageLineCode {
    private ImageView imageView;
    private boolean mHex = false;
    private boolean uHex = false;
    private boolean dHex = false;
    private final int COLOR = 14737632;

    private String getHex(int pixel) {
        int r = Color.red(pixel);
        int b = Color.blue(pixel);
        int g = Color.green(pixel);
        String hex = "";
        hex += (Integer.toHexString(r));
        hex += (Integer.toHexString(g));
        hex += (Integer.toHexString(b));
        return hex;
    }

    private int getInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public boolean getValues() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        int mPixel = bitmap.getPixel(imageWidth / 2, imageHeight / 2);
        int uPixel = bitmap.getPixel(imageWidth / 2, (int) ((double) imageHeight / 1.5));
        int dPixel = bitmap.getPixel(imageWidth / 2, (int) ((double) imageHeight / 2.5));
//        int mRedValue = Color.red(mPixel);
//        int mBlueValue = Color.blue(mPixel);
//        int mGreenValue = Color.green(mPixel);
//        int dRedValue = Color.red(dPixel);
//        int dBlueValue = Color.blue(dPixel);
//        int dGreenValue = Color.green(dPixel);
//        int uRedValue = Color.red(uPixel);
//        int uBlueValue = Color.blue(uPixel);
//        int uGreenValue = Color.green(uPixel);

//        int pixel = 0;
        for (int i = 0; i < 3; i++) {

            switch (i) {
                case 0:
//                    pixel = uPixel;

                    if (getInt(getHex(uPixel)) > COLOR) uHex = true;

                    break;
                case 1:
//                    pixel = mPixel;
                    if (getInt(getHex(mPixel)) > COLOR) mHex = true;

                    break;
                case 2:
//                    pixel = dPixel;
                    if (getInt(getHex(dPixel)) > COLOR) dHex = true;

                    break;
            }

            if (uHex && dHex && mHex) {
                return true;
            }

        }
        return false;
    }
}
