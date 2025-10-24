package com.udasecurity.imageservice;

import java.awt.image.BufferedImage;

public class ImageService {

    /**
     * Simulates cat detection logic.
     * In a real app, you would use ML or image processing here.
     * For now, this just randomly decides or checks a placeholder condition.
     */
    public boolean imageContainsCat(BufferedImage image, float confidenceThreshold) {
        // Simulated result: assume cat detected if width > height (for testing)
        boolean detected = image.getWidth() > image.getHeight();
        System.out.println("Image analyzed. Cat detected: " + detected + " (Threshold: " + confidenceThreshold + ")");
        return detected;
    }
}
