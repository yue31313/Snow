package com.example.showviewtest.snowview;

import java.util.ArrayList;
import com.example.showviewtest.R;
import com.example.showviewtest.entity.SnowEntity;
import com.nineoldandroids.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.*;

public class SnowView extends View {

	private Context context;
	/** 动画驱动器 from android open project */
	private ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
	/** "雪花位图" */
	Bitmap snowBitmap;
	/** 当前"雪花"总数 */
	int numSnows = 0;
	/** 当前"雪花"集合 */
	ArrayList<SnowEntity> snows = new ArrayList<SnowEntity>();
	/** 上一次记录的时间 */
	long prevTime;
	/** Matrix */
	Matrix m = new Matrix();
	/** Paint */
	Paint paint = null;
	/**内置传感器*/
	private Sensor moveBallSensor;
	private MoveBallSensorListener moveBallSensorListener;
	//view size
	private int viewHeight;
	private int viewWidth;
	//bitmpa size
	private int bitmapWidth;
	private int bitmapHeight;

	public SnowView(Context context, AttributeSet attributeSet, int flag) {
		// TODO Auto-generated constructor stub
		super(context, attributeSet, flag);
		this.context = context;
		init();
	}

	public SnowView(Context context, AttributeSet attributeSet) {
		// TODO Auto-generated constructor stub
		super(context, attributeSet);
		this.context = context;
		init();
	}

	public SnowView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		snowBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.snow);
		bitmapWidth=snowBitmap.getWidth();
		bitmapHeight=snowBitmap.getHeight();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		/** 动画监听器 */
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				// TODO Auto-generated method stub
				long nowTime = System.currentTimeMillis();
				float sec = (float) (nowTime - prevTime) / 1000f;
				prevTime = nowTime;
				for (int i = 0; i < snows.size(); i++) {
					SnowEntity cacheSnow = snows.get(i);
					// 根据时间和速度计算移动距离，更新Y坐标
					cacheSnow.y += (cacheSnow.speed * sec);
					// 如果Y坐标大于本视图高度，从头播放
					if (cacheSnow.y > getHeight()) {
						//重新生成x
						cacheSnow.resetX();
						//重置y
						cacheSnow.y = 0 - cacheSnow.height;
					}
					// 根据旋转速度和事件更新旋转的角度
					cacheSnow.rotation = cacheSnow.rotation
							+ (cacheSnow.rotationSpeed * sec);
				}
				invalidate();
			}
		});
		// 循环播放
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setDuration(3000);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Reset list of droidflakes, then restart it with 8 flakes
		snows.clear(); // 重置雪花集合
		numSnows = 0; // 重置雪花个数
		addSnows(16); // 默认添加16个雪花
		// Cancel animator in case it was already running
		animator.cancel(); // 关闭动画驱动
		// Set up fps tracking and start the animation
		prevTime = 0; // 重置时间
		animator.start(); // 启动动画驱动
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth=this.getMeasuredWidth();
		viewHeight=this.getMeasuredHeight();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		// 绘制背景
		canvas.drawColor(Color.BLACK);

		// 绘制雪花
		for (int i = 0; i < snows.size(); ++i) {
			SnowEntity cacheSnow = snows.get(i);
			m.setTranslate(-cacheSnow.width / 2, -cacheSnow.height / 2);
			m.postRotate(cacheSnow.rotation);
			m.postTranslate(cacheSnow.width / 2 + cacheSnow.x, cacheSnow.height
					/ 2 + cacheSnow.y);
			canvas.drawBitmap(cacheSnow.bitmap, m, null);
		}
	}
	
	/**内置传感器监听器*/
	private class MoveBallSensorListener implements SensorEventListener
	{

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			float x=event.values[SensorManager.DATA_X];
			float y=event.values[SensorManager.DATA_Y];
			float z=event.values[SensorManager.DATA_Z];
			//设置屏幕向上的时候可用
			if(z>5)
			{
				//有效范围
				if((x>=-5||x<=5)&&(y>=-5||y<=5))
				{
					//碰撞解决(x方向)
					for(int i = 0; i < snows.size(); ++i)
					{
						SnowEntity cacheSnow = snows.get(i);
						cacheSnow.x+=-x;
						//x方向碰撞
//						if(cacheSnow.x>viewWidth-cacheSnow.width)
//						{
//							cacheSnow.x=viewWidth-cacheSnow.width;
//						}
//						if(cacheSnow.x<0)
//						{
//							cacheSnow.x=0;
//						}
					}
					invalidate();
				}
			}
		}
	}
	
	/**
	 * 添加"雪花"
	 */
	public void addSnows(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			snows.add(SnowEntity.createShow(getWidth(), snowBitmap));
		}
		setNumSnows(numSnows + quantity);
	}

	/**
	 * 减去"雪花"
	 */
	public void subtractSnows(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			int index = numSnows - i - 1;
			snows.remove(index);
		}
		setNumSnows(numSnows - quantity);
	}

	/** 获取"雪花的当前个数" */
	public int getNumSnows() {
		return numSnows;
	}

	/** 设置"雪花"的当前个数 */
	public void setNumSnows(int quantity) {
		numSnows = quantity;
	}
	
	/**注册传感器*/
	public void regSensor(SensorManager sensorManager)
	{
		//生成加速度传感器
		moveBallSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		moveBallSensorListener=new MoveBallSensorListener();
		//注册监听器
		sensorManager.registerListener(moveBallSensorListener, moveBallSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	/**注销传感器*/
	public void unregSensor(SensorManager sensorManager)
	{
		sensorManager.unregisterListener(moveBallSensorListener, moveBallSensor);
		moveBallSensor=null;
		moveBallSensorListener=null;
	}

	public void pause() {
		// Make sure the animator's not spinning in the background when the
		// activity is paused.
		animator.cancel();
	}

	public void resume() {
		animator.start();
	}

}
