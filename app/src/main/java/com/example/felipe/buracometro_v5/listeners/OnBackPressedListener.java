package com.example.felipe.buracometro_v5.listeners;

/**
 * Created by felipemota on 31/01/2018.
 */

public interface OnBackPressedListener {

    /**
     * Callback, which is called if the Back Button is pressed.
     * Fragments that extend MainFragment can/should override this Method.
     *
     * @return true if the App can be closed, false otherwise
     */
    boolean onBackPressed();

}
