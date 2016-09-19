package cruga.team.listeners;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by christian on 23/08/16.
 */
public class TouchDonationsListener implements View.OnTouchListener {

    Handler handler = new Handler();

    int numberOfTaps = 0;
    long lastTapTimeMs = 0;
    long touchDownMs = 0;
    Context mContext;
    LinearLayout linearPremium;



    public TouchDonationsListener(Context context, LinearLayout linearPremium) {
        mContext = context;
        this.linearPremium = linearPremium;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownMs = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacksAndMessages(null);

                if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                    //it was not a tap

                    numberOfTaps = 0;
                    lastTapTimeMs = 0;
                    break;
                }

                if (numberOfTaps > 0
                        && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                    numberOfTaps += 1;
                } else {
                    numberOfTaps = 1;
                }

                lastTapTimeMs = System.currentTimeMillis();

                if (numberOfTaps == 5) {
                    linearPremium.setVisibility(View.VISIBLE);
                } else if (numberOfTaps == 2) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, ViewConfiguration.getDoubleTapTimeout());
                }
        }
        return true;
    }
}
