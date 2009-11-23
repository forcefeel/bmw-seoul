package com.eastsunset.bmw;

import android.content.Context;
import android.os.Vibrator;

public class VibratorControl
{
        private Vibrator vibrator;
       
        public VibratorControl(Context context)
        {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
       
        public void start()
        {
                vibrator.vibrate(2000); 
           
        }
}