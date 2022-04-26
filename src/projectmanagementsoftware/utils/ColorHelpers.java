/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.utils;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author david
 */
public class ColorHelpers {
    public static final float GOLDEN_RATIO = 1.61803398875f;
    private static float currentHue = new Random().nextFloat();
    
    public static Color nextColor(float saturation, float value) {
        currentHue += 1 / GOLDEN_RATIO;
        currentHue %= 1;
        
        return hsv(currentHue, saturation, value);
    }
    
    public static Color hsv(float hue, float saturation, float value) {
        int hi = (int) (hue * 6);
        float f = hue * 6 - hi;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);
        
        switch (hi) {
            case 0:
                return new Color((int) (value * 256), (int) (t * 256), (int) (p * 256));
            
            case 1:
                return new Color((int) (q * 256), (int) (value * 256), (int) (p * 256));
                
            case 2:
                return new Color((int) (p * 256), (int) (value * 256), (int) (t * 256));
            
            case 3:
                return new Color((int) (p * 256), (int) (q * 256), (int) (value * 256));
                
            case 4:
                return new Color((int) (t * 256), (int) (p * 256), (int) (value * 256));
            
            case 5:
                return new Color((int) (value * 256), (int) (p * 256), (int) (q * 256));
                
            default:
                return new Color(0, 0, 0);
        }
    }
}
