/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.configuracoes;

import java.awt.Color;

/**
 *
 * @author rhuan
 */
public enum ColorSystem {
    
    DEFAULT(new Color(72,126,176)),
    
    TURQUOISE(new Color(26, 188, 156)),
    EMERALD(new Color(46, 204, 113)),
    PETERRIVER(new Color(52, 152, 219)),
    
    GREENSEA(new Color(22, 160, 133)),
    NEPHRITS(new Color(39, 174, 96)),
    BELIZEHOLE(new Color(41, 128, 185)),
    
    MIDNIGTHBLUE(new Color(44, 62, 80)),
    MIDNIGTH(new Color(43, 77, 112)),
    MIDNIGTHLIGHT(new Color(59, 101, 143));
    
    
    
    
    
    private Color color;

    private ColorSystem(Color color) {
        this.color = color;
    }

    public static Color DEFAULT() {
        return DEFAULT.color;
    }

    public static ColorSystem TURQUOISE() {
        return TURQUOISE;
    }

    public static ColorSystem EMERALD() {
        return EMERALD;
    }

    public static ColorSystem PETERRIVER() {
        return PETERRIVER;
    }

    public static ColorSystem GREENSEA() {
        return GREENSEA;
    }

    public static ColorSystem NEPHRITS() {
        return NEPHRITS;
    }

    public static ColorSystem BELIZEHOLE() {
        return BELIZEHOLE;
    }

    public static ColorSystem MIDNIGTHBLUE() {
        return MIDNIGTHBLUE;
    }

    public static ColorSystem MIDNIGTH() {
        return MIDNIGTH;
    }

    public static ColorSystem MIDNIGTHLIGHT() {
        return MIDNIGTHLIGHT;
    }
    
    
    
}
