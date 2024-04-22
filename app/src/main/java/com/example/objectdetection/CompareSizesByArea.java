package com.example.objectdetection;

import android.util.Size;
import java.util.Comparator;


public class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size size1, Size size2) {
            long area1 = size1.getWidth() * size1.getHeight();
            long area2 = size2.getWidth() * size2.getHeight();
            if (area1 < area2) {
                return -1;
            } else if (area1 > area2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

