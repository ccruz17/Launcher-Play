package cruga.team.framework.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cruga.team.clases.App;


public class CircleMenu extends ViewGroup {

    private ArrayList<App> _apps;
    private int _num_items;
    // Event listeners
    private OnItemClickListener onItemClickListener = null;
    private OnItemSelectedListener onItemSelectedListener = null;
    private OnRotationFinishedListener onRotationFinishedListener = null;

    // Sizes of the ViewGroup
    private int circleWidth, circleHeight;
    private int radius = 0;

    private int childWidth = 0;
    private int childHeight = 0;

    // Touch detection
    private GestureDetector gestureDetector;
    // Detecting inverse rotations
    private boolean[] quadrantTouched;
    // Settings of the ViewGroup
    private int speed = 25;
    private float angle = 270;
    private FirstChildLocation firstChildPosition = FirstChildLocation.North;
    private boolean isRotating = true;

    private ItemView tappedView = null;
    private int selected_item = 0;
    private int selected_idx = -1;
    private String selected_direction = "";

    private String last_direction = "";
    private int last_item = -1;
    private int last_idx = -1;




    // Rotation animator
    private ObjectAnimator animator;

    private int iconSize = 50;//dp
    private int textColor = Color.BLACK;
    private int textSize = 12;//sp

    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Initializes the ViewGroup and modifies it's default behavior by the
     * passed attributes
     *
     * @param attrs the attributes used to modify default settings
     */
    protected void init(AttributeSet attrs) {
        gestureDetector = new GestureDetector(getContext(),
                new MyGestureListener());
        quadrantTouched = new boolean[]{false, false, false, false, false};
        // Needed for the ViewGroup to be drawn
        setWillNotDraw(false);

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle % 360;
        setChildAngles();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setFirstChildPosition(FirstChildLocation position) {
        this.firstChildPosition = position;
    }

    public void setRotating(boolean rotating) {
        isRotating = rotating;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /*public void setItems(@DrawableRes int[] itemIcons) {
        setItems(null, itemIcons);
    }*/

    public void updateItems(ArrayList<App> apps, int num_items) {

    }

    public void setItems(ArrayList<App> apps, int num_items) {
        this._apps = apps;
        this._num_items = num_items;
        removeAllViews();

        for (int i = 0; i < num_items; i++) {
            App currentApp = apps.get(i);

            ItemView itemView = new ItemView(getContext());
            itemView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            itemView.setPosition(i);
            itemView.setIdx(currentApp.idx);

            Bitmap iconBitmap = ((BitmapDrawable)currentApp.icono).getBitmap();
            itemView.setIcon(iconBitmap, iconSize);

            if (apps != null) {
                itemView.setText(currentApp.label);
                itemView.setTextVisible(true);
                itemView.setTextColor(textColor);
                itemView.setTextSize(textSize);
            } else {
                itemView.setTextVisible(false);
            }
            addView(itemView);
        }
    }

    /**
     * Returns the currently selected menu
     *
     * @return the view which is currently the closest to the first item
     * position
     */
    public ItemView getSelectedItem() {
        return (selected_item >= 0) ? (ItemView) getChildAt(selected_item) : null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // The sizes of the ViewGroup
        circleHeight = getHeight();
        circleWidth = getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxChildWidth = 0;
        int maxChildHeight = 0;

        // Measure once to find the maximum child size.
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
        }

        // Measure again for each child to be exactly the same size.
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxChildWidth,
                MeasureSpec.EXACTLY);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxChildHeight,
                MeasureSpec.EXACTLY);

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

        setMeasuredDimension(resolveSize(maxChildWidth, widthMeasureSpec),
                resolveSize(maxChildHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutWidth = r - l;
        int layoutHeight = b - t;

        // Laying out the child views
        final int childCount = getChildCount();
        int left, top;
        radius = (layoutWidth <= layoutHeight) ? layoutWidth / 3
                : layoutHeight / 3;

        childWidth = (int) (radius / 1.5);
        childHeight = (int) (radius / 1.5);

        float angleDelay = 360.0f / getChildCount();

        for (int i = 0; i < childCount; i++) {
            final ItemView child = (ItemView) getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            if (angle > 360) {
                angle -= 360;
            } else {
                if (angle < 0) {
                    angle += 360;
                }
            }

            child.setAngle(angle);
            child.setPosition(i);

            left = Math.round((float) (((layoutWidth / 2) - childWidth / 2) + radius
                    * Math.cos(Math.toRadians(angle))));
            top = Math.round((float) (((layoutHeight / 2) - childHeight / 2) + radius
                    * Math.sin(Math.toRadians(angle))));

            child.layout(left, top, left + childWidth, top + childHeight);
            angle += angleDelay;
        }
    }

    /**
     * Rotates the given view to the firstChildPosition
     *
     * @param view the view to be rotated
     */
    private void rotateViewToCenter(ItemView view) {
        Log.v(VIEW_LOG_TAG, "rotateViewToCenter");
        if (isRotating) {
            float destAngle = (firstChildPosition.getValue() - view.getAngle());

            if (destAngle < 0) {
                destAngle += 360;
            }

            if (destAngle > 180) {
                destAngle = -1 * (360 - destAngle);
            }

            animateTo(angle + destAngle, 7500 / speed);
        }
    }

    private void rotateButtons(float degrees) {
        angle += degrees;
        setChildAngles();
    }

    private void animateTo(float endDegree, long duration) {
        if (animator != null && animator.isRunning()
                || Math.abs(angle - endDegree) < 1) {
            return;
        }

        animator = ObjectAnimator.ofFloat(this, "angle", angle,
                endDegree);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            private boolean wasCanceled = false;

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (wasCanceled) {
                    return;
                }

                if (onRotationFinishedListener != null) {
                    onRotationFinishedListener.onRotationFinished(getSelectedItem());
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                wasCanceled = true;
            }
        });
        animator.start();
    }

    private void stopAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            animator = null;
        }
    }

    private void setChildAngles() {
        int left, top, childCount = getChildCount();
        float angleDelay = 360.0f / childCount;
        float localAngle = angle;
        for (int i = 0; i < childCount; i++) {

            if (localAngle > 360) {
                localAngle -= 360;
            } else {
                if (localAngle < 0) {
                    localAngle += 360;
                }
            }

            ItemView child = (ItemView) getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            left = Math.round((float) (((circleWidth / 2) - childWidth / 2) + radius
                    * Math.cos(Math.toRadians(localAngle))));
            top = Math.round((float) (((circleHeight / 2) - childHeight / 2) + radius
                    * Math.sin(Math.toRadians(localAngle))));

            child.setAngle(localAngle);

            float distance = Math.abs(localAngle - firstChildPosition.getValue());
            float halfAngleDelay = angleDelay / 2;
            boolean isFirstItem = distance < halfAngleDelay
                    || distance > (360 - halfAngleDelay);

            if (isFirstItem && selected_item != child.getPosition()) {
                selected_item = child.getPosition();
                selected_idx = child.getIdx();
                if (onItemSelectedListener != null && isRotating) {
                    onItemSelectedListener.onItemSelected(child);
                }
                Log.i("myLog", "----------------------------");
                Log.i("myLog", "Selected IDX: " + selected_idx);


                if(last_idx == -1) { //first time
                    if(_num_items-1 == (selected_item)) {//left to right - show last app
                        App current = _apps.get(_apps.size() - 1);
                        selected_idx = current.idx;
                        selected_direction = "atras";
                        Log.i("CCG_Ruta", "A");
                    }else { //right to left - show next app
                        child = (ItemView) getChildAt(0);
                        App current = _apps.get(_num_items); // _num_items = 7 by default then is the next app  for example ap0, ap1, ap2, ap3, ap4, ap5, ap6 ->nest is ap7 and _num_items is 7
                        selected_idx = current.idx;
                        selected_direction = "adelante";
                        Log.i("CCG_Ruta", "B");
                    }
                }else {
                    if(last_item == _num_items-1) { // 6 = 6
                        if(selected_item == 0) {

                            App tmpNextApp = _apps.get(_apps.size()-1); //last app
                            selected_idx = last_idx == tmpNextApp.idx ? 0 : last_idx + 1;
                            child = (ItemView) getChildAt(last_item);
                            selected_direction = "adelante";
                            Log.i("CCG_RUTA", "C");

                        }else {

                            selected_idx = last_idx - 1;
                            App tmpNextApp = _apps.get(_apps.size()-1); //last app

                            selected_idx = selected_idx == -1 ? tmpNextApp.idx : selected_idx;
                            selected_direction = "atras";
                            Log.i("CCG_RUTA", "D");

                        }
                    }else if(last_item == 0) { //0 == 0
                        if(selected_item == 6) {

                            selected_idx = last_idx - 1;
                            App tmpNextApp = _apps.get(_apps.size()-1); //last app

                            selected_idx = selected_idx == -1 ? tmpNextApp.idx : selected_idx;
                            selected_direction = "atras";
                            Log.i("CCG_RUTA", "E");
                        }else {

                            App tmpNextApp = _apps.get(_apps.size()-1); //last app

                            selected_idx = last_idx == tmpNextApp.idx ? 0 : last_idx+1;
                            child = (ItemView) getChildAt(last_item);
                            selected_direction = "adelante";
                            Log.i("CCG_RUTA", "F");

                        }
                    }else {
                        if(last_item > selected_item) {

                            selected_idx = last_idx - 1;
                            App tmpNextApp = _apps.get(_apps.size()-1); //last app

                            selected_idx = selected_idx == -1 ? tmpNextApp.idx : selected_idx;
                            selected_direction = "atras";
                            Log.i("CCG_RUTA", "G");
                        }else {


                            App tmpNextApp = _apps.get(_apps.size()-1); //last app

                            selected_idx = last_idx == tmpNextApp.idx ? 0 : last_idx + 1;
                            child = (ItemView) getChildAt(last_item);
                            selected_direction = "adelante";
                            Log.i("CCG_RUTA", "H");
                        }
                    }
                }

                App nextApp = _apps.get(selected_idx);
                child.setText(nextApp.label);
                Bitmap iconBitmap = ((BitmapDrawable)nextApp.icono).getBitmap();
                child.setIcon(iconBitmap, iconSize);
                child.setIdx(nextApp.idx);


                /*
                if(last_direction == "") {
                    App nextApp = _apps.get(newIndex);
                    child.setText(nextApp.label);
                    Bitmap iconBitmap = ((BitmapDrawable)nextApp.icono).getBitmap();
                    child.setIcon(iconBitmap, iconSize);
                    child.setIdx(nextApp.idx);
                    Log.i("myLog", "inicio");
                    last_inverse = false;

                } else if(last_direction == direction) {
                    Log.i("myLog", "Misma direcion");
                    if(last_inverse) {
                        if(last_direction == "mas"){
                            int last_item_calculate_inverse = 0;
                            if(last_item_selected == 0) {
                                last_item_calculate_inverse = _num_items-(was_inverse);//por inverse simple
                                Log.i("myLog", "XXXX");
                            }else {
                                last_item_calculate_inverse = last_item_selected - (was_inverse+1); //pot inverse simple
                                Log.i("myLog", "YYYY");
                            }

                            Log.i("myLog", "LastItemInverse: " + last_item_calculate_inverse);
                            Log.i("myLog", "WasInverse: " + was_inverse);


                            ItemView childTmp = (ItemView) getChildAt(last_item_calculate_inverse);
                            int tempNewIndex = childTmp.getIdx();

                            App nextApp = _apps.get(tempNewIndex+was_inverse);
                            child.setText(nextApp.label);
                            Bitmap iconBitmap = ((BitmapDrawable)nextApp.icono).getBitmap();
                            child.setIcon(iconBitmap, iconSize);
                            child.setIdx(nextApp.idx);
                            newIndex = nextApp.idx;
                        }else {

                        }
                        if(was_inverse >= 1) {
                            was_inverse--;
                        }
                        last_inverse = false;
                    }else {
                        App nextApp = _apps.get(newIndex);
                        child.setText(nextApp.label);
                        Bitmap iconBitmap = ((BitmapDrawable)nextApp.icono).getBitmap();
                        child.setIcon(iconBitmap, iconSize);
                        child.setIdx(nextApp.idx);
                        was_inverse = 1;
                    }

                } else {
                    if(!last_inverse){
                        was_inverse++;
                    }
                    last_inverse = true;
                    if(last_direction == "menos") {
                        Log.i("myLogDirection", "mas");
                        int last_item_calculate_inverse = 0;
                        if(last_item_selected == 0) {
                            last_item_calculate_inverse = 6;
                        }else {
                            last_item_calculate_inverse = last_item_selected -1;
                        }

                        ItemView childTmp = (ItemView) getChildAt(last_item_calculate_inverse);
                        int tempNewIndex = childTmp.getIdx();

                        App nextApp = _apps.get(tempNewIndex+1);
                        child.setText(nextApp.label);
                        Bitmap iconBitmap = ((BitmapDrawable)nextApp.icono).getBitmap();
                        child.setIcon(iconBitmap, iconSize);
                        child.setIdx(nextApp.idx);
                    } else {
                        Log.i("myLogDirection", "menos");
                        int last_item_calculate_inverse = 0;
                        if(last_item_selected == 0) {
                            last_item_calculate_inverse = 6;
                        }else {
                            last_item_calculate_inverse = last_item_selected - was_inverse;
                        }
                        if(last_idx ==0) {
                            App nextApp = _apps.get(newIndex);
                            child.setText(nextApp.label);
                            Bitmap iconBitmap = ((BitmapDrawable)nextApp.icono).getBitmap();
                            child.setIcon(iconBitmap, iconSize);
                            child.setIdx(nextApp.idx);
                        }else {
                            Log.i("myLog", "LastCalculate: " +last_item_calculate_inverse);
                            ItemView childTmp = (ItemView) getChildAt(last_item_calculate_inverse);
                            int tempNewIndex = childTmp.getIdx();

                            App nextApp = _apps.get(tempNewIndex);
                            child.setText(nextApp.label);
                            Bitmap iconBitmap = ((BitmapDrawable)nextApp.icono).getBitmap();
                            child.setIcon(iconBitmap, iconSize);
                            child.setIdx(nextApp.idx);
                            newIndex = tempNewIndex;
                        }

                    }
                }
                */


                Log.i("myLog", "Pos: "+ selected_item);
                Log.i("myLog", "Last Pos: "+ last_item);
                Log.i("myLog", "Idx: "+ selected_idx);
                Log.i("myLog", "Last Idx: "+ last_idx);
                Log.i("myLog", "Dir: " + selected_direction);
                Log.i("myLog", "LasDir: " + last_direction);

                last_item = selected_item;
                last_idx = selected_idx;
                last_direction = selected_direction;


            }

            child.layout(left, top, left + childWidth, top + childHeight);
            localAngle += angleDelay;
        }
    }

    /**
     * @return The angle of the unit circle with the image views center
     */
    private double getPositionAngle(double xTouch, double yTouch) {
        double x = xTouch - (circleWidth / 2d);
        double y = circleHeight - yTouch - (circleHeight / 2d);

        switch (getPositionQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
            case 3:
                return 180 - (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                // ignore, does not happen
                return 0;
        }
    }

    /**
     * @return The quadrant of the position
     */
    private static int getPositionQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    // Touch helpers
    private double touchStartAngle;
    private boolean didMove = false;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isEnabled()) {
            gestureDetector.onTouchEvent(event);
            if (isRotating) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // reset the touched quadrants
                        for (int i = 0; i < quadrantTouched.length; i++) {
                            quadrantTouched[i] = false;
                        }

                        stopAnimation();
                        touchStartAngle = getPositionAngle(event.getX(),
                                event.getY());
                        didMove = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        double currentAngle = getPositionAngle(event.getX(),
                                event.getY());
                        rotateButtons((float) (touchStartAngle - currentAngle));
                        touchStartAngle = currentAngle;
                        didMove = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (didMove) {
                            rotateViewToCenter((ItemView) getChildAt(selected_item));
                        }
                        break;
                }
            }

            // set the touched quadrant to true
            quadrantTouched[getPositionQuadrant(event.getX()
                    - (circleWidth / 2), circleHeight - event.getY()
                    - (circleHeight / 2))] = true;
            return true;
        }
        return false;
    }

    private class MyGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (!isRotating) {
                return false;
            }
            // get the quadrant of the start and the end of the fling
            int q1 = getPositionQuadrant(e1.getX() - (circleWidth / 2),
                    circleHeight - e1.getY() - (circleHeight / 2));
            int q2 = getPositionQuadrant(e2.getX() - (circleWidth / 2),
                    circleHeight - e2.getY() - (circleHeight / 2));

            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math
                    .abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math
                    .abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {
                // the inverted rotations
                animateTo(getCenteredAngle(angle - (velocityX + velocityY) / 25),
                        25000 / speed);
            } else {
                // the normal rotation
                animateTo(getCenteredAngle(angle + (velocityX + velocityY) / 25),
                        25000 / speed);
            }

            return true;
        }

        private float getCenteredAngle(float angle) {
            float angleDelay = 360 / getChildCount();
            float localAngle = angle % 360;

            if (localAngle < 0) {
                localAngle = 360 + localAngle;
            }

            for (float i = firstChildPosition.getValue(); i < firstChildPosition.getValue() + 360; i += angleDelay) {
                float locI = i % 360;
                float diff = localAngle - locI;
                if (Math.abs(diff) < angleDelay / 2) {
                    angle -= diff;
                    break;
                }
            }

            return angle;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int tappedViewsPosition = pointToPosition(e.getX(), e.getY());
            if (tappedViewsPosition >= 0) {
                tappedView = (ItemView) getChildAt(tappedViewsPosition);
                tappedView.setPressed(true);
            }

            if (tappedView != null) {
                if (selected_item == tappedViewsPosition) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(tappedView);
                    }
                } else {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(tappedView);
                    }
                    /*rotateViewToCenter(tappedView);
                    if (!isRotating) {
                        if (onItemSelectedListener != null) {
                            onItemSelectedListener.onItemSelected(tappedView);
                        }

                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(tappedView);
                        }
                    }*/
                }
                return true;
            }
            return super.onSingleTapUp(e);
        }

        private int pointToPosition(float x, float y) {
            for (int i = 0; i < getChildCount(); i++) {
                View item = getChildAt(i);
                if (item.getLeft() < x && item.getRight() > x
                        & item.getTop() < y && item.getBottom() > y) {
                    return i;
                }
            }
            return -1;
        }
    }

    public enum FirstChildLocation {
        East(0), South(90), West(180), North(270);

        private float value;

        FirstChildLocation(float position) {
            this.value = position;
        }

        public float getValue() {
            return this.value;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(ItemView view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(ItemView view);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnRotationFinishedListener {
        void onRotationFinished(ItemView view);
    }

    public void setOnRotationFinishedListener(OnRotationFinishedListener onRotationFinishedListener) {
        this.onRotationFinishedListener = onRotationFinishedListener;
    }

    /**
     * Custom text and icon for the {@link #CircleMenu} class.
     */
    public static class ItemView extends LinearLayout {
        private TextView textView;
        private ImageView imageView;
        // Angle is used for the positioning on the circle
        private float angle = 0;
        // Position represents the index of this view in the view groups children array
        private int position = 0;
        // The text of the view
        private String text;

        private int idx = 0;

        /**
         * Return the angle of the view.
         *
         * @return Returns the angle of the view in degrees.
         */
        public float getAngle() {
            return angle;
        }

        /**
         * Set the angle of the view.
         *
         * @param angle The angle to be set for the view.
         */
        public void setAngle(float angle) {
            this.angle = angle;
        }

        /**
         * Return the position of the view.
         *
         * @return Returns the position of the view.
         */
        public int getPosition() {
            return position;
        }

        /**
         * Set the position of the view.
         *
         * @param position The position to be set for the view.
         */
        public void setPosition(int position) {
            this.position = position;
        }

        /**
         * Return the idx of the view.
         *
         * @return Returns the idx of the view.
         */
        public int getIdx() {
            return idx;
        }

        /**
         * Set the position of the view.
         *
         * @param idx The position to be set for the view.
         */
        public void setIdx(int idx) {
            this.idx = idx;
        }


        /**
         * Return the text of the view.
         *
         * @return Returns the text of the view.
         */
        public String getText() {
            return text;
        }

        /**
         * Set the text of the view.
         *
         * @param text The text to be set for the view.
         */
        public void setText(String text) {
            this.text = text;
            textView.setText(text);
        }

        public void setIcon(Bitmap icon, int iconSize) {
            iconSize = Utils.toPx(getContext(), iconSize);
            imageView.setImageBitmap(Utils.scale(icon, iconSize, iconSize));
        }

        public void setIcon(@DrawableRes int icon, int iconSize) {
            setIcon(BitmapFactory.decodeResource(getResources(), icon), iconSize);
        }

        public void setTextVisible(boolean iconVisible) {
            textView.setVisibility(iconVisible ? VISIBLE : GONE);
        }

        public void setTextColor(@ColorInt int color) {
            textView.setTextColor(color);
        }

        public void setTextSize(int size) {
            textView.setTextSize(size);
        }

        public ItemView(Context context) {
            super(context);
            int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
            setLayoutParams(new ViewGroup.LayoutParams(wrapContent, wrapContent));
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            imageView = new ImageView(context);
            addView(imageView);
            addView(textView);
        }

    }

    public static class Utils {

        /**
         * dp转换为px
         *
         * @param context
         * @param dpValue
         * @return
         */
        public static int toPx(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            int pxValue = (int) (dpValue * scale + 0.5f);
            return pxValue;
        }

        /**
         * 缩放图片，会变形
         *
         * @param bitmap
         * @param newWidth
         * @param newHeight
         * @return
         */
        public static Bitmap scale(Bitmap bitmap, int newWidth, int newHeight) {
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }

    }

}
