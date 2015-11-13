package com.flying.xiaopo.evernotepicker;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 一个自定义的TimeBar，通过将子视图合并成一个单一的自定义视图进行管理，提供设定时间的暴露接口
 * Created by Flying SnowBean on 2015/11/11.
 */
public class TimeBar extends ViewGroup implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private final String TAG = TimeBar.class.getSimpleName();

    private ImageView mImageView;
    private TextView mTvTime;
    private TextView mTvIcon;
    private SeekBar mSeekBar;

    private int mSeekBarWidth = 0;
    private int maxHeight = 0;

    private final static int STATE_DAY = 0;
    private final static int STATE_NIGHT = 1;

    private int mState = STATE_DAY;

    private int mHour;
    private int mMinute;

    private int mTextColorDay;
    private int mTextColorNight;


    public TimeBar(Context context) {
        super(context);
    }

    public TimeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TimeBar(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.time_bar, this, true);
        mImageView = (ImageView) findViewById(R.id.iv_day_night);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvIcon = (TextView) findViewById(R.id.tv_icon);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setMax(24);

        mTvTime.setClickable(true);
        mTvTime.setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeBar, defStyleAttr, 0);

        final float textSize = a.getDimension(R.styleable.TimeBar_time_textSize, 16f);
        mTvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        mTextColorDay = a.getColor(R.styleable.TimeBar_time_textColor_day, Color.parseColor("#fab613"));
        mTextColorNight = a.getColor(R.styleable.TimeBar_time_textColor_night, Color.parseColor("#86b3bb"));
        mTvTime.setTextColor(mTextColorDay);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightUsed = 0;
        int widthUsed = 0;

        measureChildWithMargins(mImageView, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
        heightUsed += mImageView.getMeasuredHeight();
        if (mImageView.getMeasuredWidth() > widthUsed) widthUsed = mImageView.getMeasuredWidth();

        measureChildWithMargins(mTvTime, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
        heightUsed += mTvTime.getMeasuredHeight();
        if (mTvTime.getMeasuredWidth() > widthUsed) widthUsed = mTvTime.getMeasuredWidth();

        measureChildWithMargins(mTvIcon, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
        if (mTvIcon.getMeasuredWidth() > widthUsed) widthUsed = mTvIcon.getMeasuredWidth();

        measureChildWithMargins(mSeekBar, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
        heightUsed += mSeekBar.getMeasuredHeight();
        if (mSeekBar.getMeasuredWidth() > widthUsed) widthUsed = mSeekBar.getMeasuredWidth();

        setMeasuredDimension(widthUsed, heightUsed);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int preHeight = 0;
        int preWidth = 0;

        int w = (mTvTime.getMeasuredWidth()) / 2;

        preHeight += mImageView.getMeasuredHeight();

        mTvTime.layout(mSeekBar.getMeasuredWidth() / 2 - w - getPaddingLeft(), preHeight, mTvTime.getMeasuredWidth() + mSeekBar.getMeasuredWidth() / 2 - w - getPaddingLeft(), preHeight + mTvTime.getMeasuredHeight());
        preWidth += mTvTime.getMeasuredWidth();

        mTvIcon.layout(preWidth - mTvIcon.getMeasuredWidth() / 4 + mSeekBar.getMeasuredWidth() / 2 - w - getPaddingLeft(), preHeight + mTvIcon.getMeasuredWidth() / 4, preWidth + mTvIcon.getMeasuredWidth() / 2 + mSeekBar.getMeasuredWidth() / 2 - w - getPaddingLeft(), preHeight + mTvIcon.getMeasuredHeight() - mTvIcon.getMeasuredWidth() / 4);
        preHeight += mTvIcon.getMeasuredHeight();

        mImageView.layout(getPaddingLeft(), preHeight - mImageView.getMeasuredHeight(), mImageView.getMeasuredWidth() + getPaddingLeft(), preHeight);

        mSeekBar.layout(0, preHeight, mSeekBar.getMeasuredWidth(), preHeight + mSeekBar.getMeasuredHeight());

        maxHeight = preHeight - mImageView.getMeasuredHeight();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSeekBarWidth = mSeekBar.getWidth() - mSeekBar.getPaddingLeft() - mSeekBar.getPaddingRight();
        mSeekBar.setProgress(0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int max = seekBar.getMax();

        mImageView.setX(mSeekBarWidth * progress / mSeekBar.getMax() + mSeekBar.getPaddingLeft() / 3);
        mImageView.setY(maxHeight * (progress - max / 2) * (progress - max / 2) / (max * max / 4));

        if (mState == STATE_DAY) {
            mTvTime.setText(calculateTime(360, progress));
        } else if (mState == STATE_NIGHT) {
            mTvTime.setText(calculateTime(1080, progress));
        }

    }

    private String calculateTime(int minutes, int progress) {
        minutes = (minutes + progress * 30) % 1440;

        //更新属性
        mHour = minutes / 60;
        mMinute = minutes % 60;

        String hour = String.valueOf(minutes / 60);
        String min = String.valueOf(minutes % 60);
        if (hour.length() == 1) hour = "0" + hour;
        if (min.length() == 1) min = "0" + min;
        return String.format("%s:%s", hour, min);
    }

    private void changeState(int state) {
        mState = state;
        if (mState == STATE_DAY) {
            mImageView.setBackgroundResource(R.drawable.datepicker_handle_day);
            mTvIcon.setBackgroundResource(R.drawable.spinner_time_yellow);
            mSeekBar.setThumb(getResources().getDrawable(R.drawable.time_handle));
            mTvTime.setTextColor(mTextColorDay);
        }
        if (mState == STATE_NIGHT) {
            mImageView.setBackgroundResource(R.drawable.datepicker_handle_night);
            mTvIcon.setBackgroundResource(R.drawable.spinner_time_blue);
            mSeekBar.setThumb(getResources().getDrawable(R.drawable.time_handle_night));
            mTvTime.setTextColor(mTextColorNight);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mSeekBarWidth = mSeekBar.getWidth() - mSeekBar.getPaddingLeft() - mSeekBar.getPaddingRight();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getProgress() == seekBar.getMax()) {
            if (mState == STATE_DAY) {
                mTvTime.setText(calculateTime(360, 0));
                changeState(STATE_NIGHT);
                seekBar.setProgress(0);
            } else if (mState == STATE_NIGHT) {
                mTvTime.setText(calculateTime(1080, 0));
                changeState(STATE_DAY);
                seekBar.setProgress(0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), this, mHour, mMinute, true);
        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //更新属性
        mHour = hourOfDay;
        mMinute = minute;

        int minutes = hourOfDay * 60 + minute;

        if ((hourOfDay > 6 && hourOfDay < 18) || (hourOfDay == 6 && minute >= 0)) {
            changeState(STATE_DAY);
            mSeekBar.setProgress(mSeekBar.getMax() * (minutes - 360) / 720);
        } else {
            changeState(STATE_NIGHT);
            if (hourOfDay <= 6) {
                mSeekBar.setProgress(mSeekBar.getMax() / 2 * minutes / 360 + 12);
            } else if (hourOfDay >= 18) {
                mSeekBar.setProgress(mSeekBar.getMax() / 2 * minutes / 360 - 36);
            }
        }

        mTvTime.setText(calculateTime(minutes, 0));
    }

    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }

    public String getTime() {
        String hour = String.valueOf(mHour);
        String min = String.valueOf(mMinute);
        if (hour.length() == 1) hour = "0" + hour;
        if (min.length() == 1) min = "0" + min;
        return String.format("%s:%s", hour, min);
    }
}
