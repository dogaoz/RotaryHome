package com.dogaozkaraca.rotaryhome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Color;

public class DominantColor {

String colour;


public DominantColor(Bitmap image) throws Exception {

     int height = image.getHeight();
     int width = image.getWidth();

     Map m = new HashMap();

        for(int i=0; i < width ; i++){

            for(int j=0; j < height ; j++){

                int rgb = image.getPixel(i, j);
                int[] rgbArr = getRGBArr(rgb);                

                if (!isGray(rgbArr)) {   

                        Integer counter = (Integer) m.get(rgb);   
                        if (counter == null)
                            counter = 0;
                        counter++;                                
                        m.put(rgb, counter);       

                }                
            }
        }        

     //   String colourHex = getMostCommonColour(m);
    }



    public static int getMostCommonColour(Bitmap bitmap) {

if (bitmap == null)
    throw new NullPointerException();

int width = bitmap.getWidth();
int height = bitmap.getHeight(); // strike --for only getting top side of wallpaper --strike
int size = width * height;
int pixels[] = new int[size];

Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

bitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

final List<HashMap<Integer, Integer>> colorMap = new ArrayList<HashMap<Integer, Integer>>();
colorMap.add(new HashMap<Integer, Integer>());
colorMap.add(new HashMap<Integer, Integer>());
colorMap.add(new HashMap<Integer, Integer>());

int color = 0;
int r = 0;
int g = 0;
int b = 0;
Integer rC, gC, bC;
for (int i = 0; i < pixels.length; i++) {
    color = pixels[i];

    r = Color.red(color);
    g = Color.green(color);
    b = Color.blue(color);

    rC = colorMap.get(0).get(r);
    if (rC == null)
        rC = 0;
    colorMap.get(0).put(r, ++rC);

    gC = colorMap.get(1).get(g);
    if (gC == null)
        gC = 0;
    colorMap.get(1).put(g, ++gC);

    bC = colorMap.get(2).get(b);
    if (bC == null)
        bC = 0;
    colorMap.get(2).put(b, ++bC);
}

int[] rgb = new int[3];
for (int i = 0; i < 3; i++) {
    int max = 0;
    int val = 0;
    for (Map.Entry<Integer, Integer> entry : colorMap.get(i).entrySet()) {
        if (entry.getValue() > max) {
            max = entry.getValue();
            val = entry.getKey();
        }
    }
    rgb[i] = val;
}

int dominantColor = Color.rgb(rgb[0], rgb[1], rgb[2]);

return dominantColor;       
    }    


    public static int[] getRGBArr(int pixel) {

        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        return new int[]{red,green,blue};

  }

    public static boolean isGray(int[] rgbArr) {

        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];

        int tolerance = 10;

        if (rgDiff > tolerance || rgDiff < -tolerance) 
            if (rbDiff > tolerance || rbDiff < -tolerance) { 

                return false;

            }                

        return true;
    }


public String returnColour() {

    if (colour.length() == 6) {
        return colour.replaceAll("\\s", "");
    } else {
        return "error";
    }
}

}
