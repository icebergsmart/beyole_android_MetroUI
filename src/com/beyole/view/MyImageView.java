package com.beyole.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MyImageView extends ImageView {
	private static final int SCALE_REDUCE_INIT = 0;
	private static final int SCALE_ADD_INIT = 6;
	private static final int SCALLING = 1;
	// 控件的高度
	private int mHeight;
	// 控件的宽度
	private int mWidth;
	// 1/2控件的宽度
	private int mCenterWidth;
	// 1/2控件的高度
	private int mCenterHeight;
	// 缩放比例
	private float mScaleType=0.95f;

	// 缩放是否结束
	private boolean isFinished = true;

	public MyImageView(Context context) {
		this(context, null);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
			mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
			mCenterWidth = mWidth / 2;
			mCenterHeight = mHeight / 2;
			// 消除图片资源的锯齿
			// 要转换为BitmapDrawable才能对图片资源进行操作
			Drawable drawable = getDrawable();
			BitmapDrawable bd = (BitmapDrawable) drawable;
			bd.setAntiAlias(true);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 按下时对view进行缩小
			myScaleHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
			break;
		case MotionEvent.ACTION_UP:
			// 抬起时对view进行放大
			myScaleHandler.sendEmptyMessage(SCALE_ADD_INIT);
			break;
		}
		return true;
	}

	/**
	 * 控制缩放的handler
	 */
	private Handler myScaleHandler = new Handler() {
		// 用来进行对图片资源进行缩放的matrix
		private Matrix matrix = new Matrix();
		private int count = 0;// 计数器
		private float s;
		private boolean isClicked;

		public void handleMessage(android.os.Message msg) {
			matrix.set(getImageMatrix());
			switch (msg.what) {
			case SCALE_ADD_INIT:
				if (!isFinished) {
					myScaleHandler.sendEmptyMessage(SCALE_ADD_INIT);
				} else {
					isFinished = false;
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(1.0f / mScaleType));
					beginScale(matrix, s);
					myScaleHandler.sendEmptyMessage(SCALLING);
				}
				break;
			case SCALE_REDUCE_INIT:
				// 如果缩放没有结束，则继续缩放
				if (!isFinished) {
					myScaleHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
				} else {
					isFinished = false;
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(mScaleType));
					beginScale(matrix, s);
					myScaleHandler.sendEmptyMessage(SCALLING);
				}
				break;
			case SCALLING:
				Log.i("test", "缩放梯度为:"+s);
				beginScale(matrix, s);
				if (count < 4) {
					myScaleHandler.sendEmptyMessage(SCALLING);
				} else {
					isFinished = true;
					if (MyImageView.this.mOnViewClickListener != null && !isClicked) {
						isClicked = true;
						MyImageView.this.mOnViewClickListener.onViewClick(MyImageView.this);
					} else {
						isClicked = false;
					}
				}
				count++;
				break;
			}
		};
	};
	protected synchronized void beginScale(Matrix matrix, float s) {
		matrix.postScale(s, s, mCenterWidth, mCenterHeight);
		setImageMatrix(matrix);
	}

	private OnViewClickListener mOnViewClickListener;


	public void setOnClickIntent(OnViewClickListener clickListener) {
		this.mOnViewClickListener = clickListener;
	}

	public interface OnViewClickListener {
		public void onViewClick(MyImageView view);
	}

}
