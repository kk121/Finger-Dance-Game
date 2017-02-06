package com.krishna.fingerdance.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.krishna.fingerdance.model.Player;


/**
 * Created by krishna on 06/02/17.
 */

public class TileView extends View {
    private OnTouchDownListener touchDownListener;
    private OnTouchUpListener touchUpListener;
    private int player = Player.PLAYER_1;
    int tileRow = 0;
    int tileColumn = 0;
    private boolean color;

    public TileView(Context context, int row, int column) {
        super(context);
        tileRow = row;
        tileColumn = column;
    }

    public TileView(Context context, AttributeSet attrs) {
        this(context, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (color) {
            if (player == Player.PLAYER_1) {
                canvas.drawColor(Color.BLUE);
            } else {
                canvas.drawColor(Color.RED);
            }
        } else {
            canvas.drawColor(Color.parseColor("#FFECB3"));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
                if (touchDownListener != null)
                    touchDownListener.onTouchDown(this, player);
                return true;
            case MotionEvent.ACTION_UP:
                setColor(false);
                invalidate();
                if (touchUpListener != null)
                    touchUpListener.onTouchUp(this, player);
                return true;
        }
        return false;
    }


    /* getters and setters */
    public int getTileRow() {
        return tileRow;
    }

    public int getTileColumn() {
        return tileColumn;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setOnTouchDownListener(OnTouchDownListener touchDownListener) {
        this.touchDownListener = touchDownListener;
    }

    public void setOnTouchUpListener(OnTouchUpListener touchUpListener) {
        this.touchUpListener = touchUpListener;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    /* Interfaces for event communication */
    public interface OnTouchDownListener {
        void onTouchDown(TileView tileView, int player);
    }

    public interface OnTouchUpListener {
        void onTouchUp(TileView tileView, int player);
    }
}
