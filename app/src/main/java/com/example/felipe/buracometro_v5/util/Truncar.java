package com.example.felipe.buracometro_v5.util;

import java.math.BigDecimal;

public class Truncar {
    public double truncate(Double valor, int precisao) {
        BigDecimal bd = BigDecimal.valueOf(valor);
        bd = bd.setScale(precisao, BigDecimal.ROUND_DOWN);

        return bd.doubleValue();
    }
}
