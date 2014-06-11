package com.example.pulltorefreshlistviewdemo2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PullToRefreshListView extends ListView
	implements OnScrollListener{
	// 状态
	private static final int TAP_TO_REFRESH = 1;
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3; //释放刷新
	private static final int REFRESHING = 4;
	
	// 数据条数
	private int itemRowCount = 0;
	// 分页条数
	private int pageSize = 0;
	private OnRefreshListener mOnRefreshListener;
	
	// 监听对ListView的滑动动作
	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;
	
	// 顶部刷新时出现的控件
	private RelativeLayout mRefreshView;
	private TextView mRefreshViewText;
	private ImageView mRefreshViewImage;
	private ProgressBar mRefreshViewProgress;
	private TextView mRefreshViewLastUpdated;

	private int mCurrentScrollState;// 当前滑动状态
	private int mRefreshState;// 当前刷新状态
	
	private RotateAnimation mFlipAnimation;
	private RotateAnimation mReverseFlipAnimation;

	private int mRefreshViewHeight;  //getMeasuredHeight()是实际View的大小，与屏幕无关，
	private int mRefreshOriginalTopPadding;
	private int mLastMotionY;

	private boolean mBounceHack;
	
	public PullToRefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
	 * 初始化控件和动画
	 * 
	 * @param context
	 */
	private void init(Context context) {
		// TODO Auto-generated method stub
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());//定义了动画的变化速度
		mFlipAnimation.setDuration(250);  //表动画持续250ms
		mFlipAnimation.setFillAfter(true);	//表示动画结束后停留在动画的最后位置
		
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);
		
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mRefreshView = (RelativeLayout) mInflater.inflate(
				R.layout.pull_to_refresh_header, this, false);
		mRefreshViewText = (TextView) mRefreshView
				.findViewById(R.id.pull_to_refresh_text);
		mRefreshViewImage = (ImageView) mRefreshView
				.findViewById(R.id.pull_to_refresh_image);
		mRefreshViewProgress = (ProgressBar) mRefreshView
				.findViewById(R.id.pull_to_refresh_progress);
		mRefreshViewLastUpdated = (TextView) mRefreshView
				.findViewById(R.id.pull_to_refresh_updated_at);
		
		mRefreshViewImage.setMinimumHeight(50);
		mRefreshView.setOnClickListener(new OnClickRefreshListener());
		mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();
		
		mRefreshState = TAP_TO_REFRESH;
		
		// 为ListView头部添加view
		addHeaderView(mRefreshView);
		
		super.setOnScrollListener(this);
		
		measureView(mRefreshView);

		mRefreshViewHeight = mRefreshView.getMeasuredHeight();  //getMeasuredHeight()是实际View的大小，与屏幕无关，
	}
	//如果你在自己的view中Override了这个方法。那么我们最关注的是它什么时候调用？
	//从开发文档中我们可以看出，onAttachedToWindow是在第一次onDraw前调用的。
	//也就是我们写的View在没有绘制出来时调用的，但只会调用一次。
	@Override
	protected void onAttachedToWindow() {
		setSelection(1);   //希望打开一个listview的时候能够自动设置显示的位置， setSelection(int pos)可以设置显示的位置
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

		setSelection(1);
	}
	
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}
	
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
			mRefreshViewLastUpdated.setText(lastUpdated);
		} else {
			mRefreshViewLastUpdated.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 触摸
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int y=(int)event.getY();
		mBounceHack = false;
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP : // 按下抬起
				if (!isVerticalScrollBarEnabled()) {
					setVerticalScrollBarEnabled(true); //作用是隐藏listView的滚动条，
				}
				if (getFirstVisiblePosition() == 0  //获得第一个可见的位置
						&& mRefreshState != REFRESHING) {
					if ((mRefreshView.getBottom() >= mRefreshViewHeight || mRefreshView
							.getTop() >= 0)   //mRefreshView超屏了
							&& mRefreshState == RELEASE_TO_REFRESH) {  
						mRefreshState = REFRESHING;
						prepareForRefresh();// 准备刷新
						onRefresh();// 刷新
					} else if (mRefreshView.getBottom() < mRefreshViewHeight
							|| mRefreshView.getTop() <= 0) {
						resetHeader();// 中止刷新
						setSelection(1);
					}
				}
				break;
			case MotionEvent.ACTION_DOWN : // 屏幕按下
				mLastMotionY = y;// 获得按下y轴位置
				break;
			case MotionEvent.ACTION_MOVE ://滑动
				// 计算边距
				applyHeaderPadding(event);
				break;
		}
		return super.onTouchEvent(event);
	}
	private void applyHeaderPadding(MotionEvent event) {
		// TODO Auto-generated method stub
		int pointerCount = event.getHistorySize();
		for (int p = 0; p < pointerCount; p++) {
			if (mRefreshState == RELEASE_TO_REFRESH) {
				if (isVerticalFadingEdgeEnabled()) {
					setVerticalScrollBarEnabled(false);
				}
				
				int historicalY = (int) event.getHistoricalY(p);
				int topPadding = (int) (((historicalY - mLastMotionY) - mRefreshViewHeight) / 1.7);
				
				mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
						topPadding, mRefreshView.getPaddingRight(),
						mRefreshView.getPaddingBottom());
			}
		}
	}

	private void resetHeaderPadding() {
		mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
				mRefreshOriginalTopPadding, mRefreshView.getPaddingRight(),
				mRefreshView.getPaddingBottom());
	}
	
	//终止刷新
	private void resetHeader() {
		if (mRefreshState != TAP_TO_REFRESH) {
			mRefreshState = TAP_TO_REFRESH;
			resetHeaderPadding();
			mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);
			mRefreshViewImage
					.setImageResource(R.drawable.ic_pulltorefresh_arrow);// 换成箭头
			mRefreshViewImage.clearAnimation();// 清除动画
			mRefreshViewImage.setVisibility(View.GONE);// 隐藏图标
			mRefreshViewProgress.setVisibility(View.GONE);// 隐藏进度条
		}
	}
	
	@SuppressWarnings("deprecation")
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount){
		// TODO Auto-generated method stub
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL  //手指在屏幕上滑动
				&& mRefreshState != REFRESHING) {   //更新状态不是正在更新
			if (firstVisibleItem == 0) {
				mRefreshViewImage.setVisibility(View.VISIBLE);
				if ((mRefreshView.getBottom() >= mRefreshViewHeight + 20 || mRefreshView
						.getTop() >= 0) && mRefreshState != RELEASE_TO_REFRESH) {
					mRefreshViewText
					.setText(R.string.pull_to_refresh_release_label);
					mRefreshViewImage.clearAnimation();
					mRefreshViewImage.startAnimation(mFlipAnimation);
					mRefreshState = RELEASE_TO_REFRESH;
				}else if(mRefreshView.getBottom() < mRefreshViewHeight + 20
						&& mRefreshState != PULL_TO_REFRESH)
				{
					mRefreshViewText
					.setText(R.string.pull_to_refresh_pull_label);
					if (mRefreshState != TAP_TO_REFRESH) {
						mRefreshViewImage.clearAnimation();
						mRefreshViewImage.startAnimation(mReverseFlipAnimation);
					}
					mRefreshState = PULL_TO_REFRESH;
				}
			}else{
				mRefreshViewImage.setVisibility(View.GONE);
				resetHeader();
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING  // 用户之前触摸滑动，现在正在滑行，直到停止
				&& firstVisibleItem == 0 && mRefreshState != REFRESHING) {
			setSelection(1);
			mBounceHack = true;
		} else if (mBounceHack && mCurrentScrollState == SCROLL_STATE_FLING) {
			setSelection(1);
		}

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		mCurrentScrollState = scrollState;
		if (mCurrentScrollState == SCROLL_STATE_IDLE) {   //视图没有滑动
			mBounceHack = false;
		}
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}
	
	public void prepareForRefresh() {
		resetHeaderPadding();
		mRefreshViewImage.setVisibility(View.GONE);
		mRefreshViewImage.setImageDrawable(null);
		mRefreshViewProgress.setVisibility(View.VISIBLE);
		mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);
		mRefreshState = REFRESHING;
	}
	
	public void onRefresh() {
		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}
	
	public void onRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onRefreshComplete();
	}
	
	public void onRefreshComplete() {
		// TODO Auto-generated method stub
		resetHeader();
		int bottomPosition = mRefreshView.getBottom();
		Log.i("bottom", "pull:rows:" + String.valueOf(itemRowCount)
				+ ",pageSize:" + String.valueOf(pageSize));
		if (bottomPosition > 0) {
			invalidateViews();
			setSelection(1);// 选择第二项
		}
		// 若数据行数小于本次分页行数，则隐藏顶部和底部控件
		if (pageSize > 0 && itemRowCount < pageSize) {
			removeHeaderView(mRefreshView);
		}
	}

	private class OnClickRefreshListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mRefreshState != REFRESHING) {
				prepareForRefresh();
				onRefresh();
			}
		}
	}
	public void SetDataRow(int row) {
		itemRowCount = row;
	}
	public void SetPageSize(int size) {
		pageSize = size;
	}
	public interface OnRefreshListener {
		public void onRefresh();
	}
}
