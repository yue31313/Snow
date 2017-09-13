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
	/** ���������� from android open project */
	private ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
	/** "ѩ��λͼ" */
	Bitmap snowBitmap;
	/** ��ǰ"ѩ��"���� */
	int numSnows = 0;
	/** ��ǰ"ѩ��"���� */
	ArrayList<SnowEntity> snows = new ArrayList<SnowEntity>();
	/** ��һ�μ�¼��ʱ�� */
	long prevTime;
	/** Matrix */
	Matrix m = new Matrix();
	/** Paint */
	Paint paint = null;
	/**���ô�����*/
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
		/** ���������� */
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				// TODO Auto-generated method stub
				long nowTime = System.currentTimeMillis();
				float sec = (float) (nowTime - prevTime) / 1000f;
				prevTime = nowTime;
				for (int i = 0; i < snows.size(); i++) {
					SnowEntity cacheSnow = snows.get(i);
					// ����ʱ����ٶȼ����ƶ����룬����Y����
					cacheSnow.y += (cacheSnow.speed * sec);
					// ���Y������ڱ���ͼ�߶ȣ���ͷ����
					if (cacheSnow.y > getHeight()) {
						//��������x
						cacheSnow.resetX();
						//����y
						cacheSnow.y = 0 - cacheSnow.height;
					}
					// ������ת�ٶȺ��¼�������ת�ĽǶ�
					cacheSnow.rotation = cacheSnow.rotation
							+ (cacheSnow.rotationSpeed * sec);
				}
				invalidate();
			}
		});
		// ѭ������
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setDuration(3000);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Reset list of droidflakes, then restart it with 8 flakes
		snows.clear(); // ����ѩ������
		numSnows = 0; // ����ѩ������
		addSnows(16); // Ĭ�����16��ѩ��
		// Cancel animator in case it was already running
		animator.cancel(); // �رն�������
		// Set up fps tracking and start the animation
		prevTime = 0; // ����ʱ��
		animator.start(); // ������������
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

		// ���Ʊ���
		canvas.drawColor(Color.BLACK);

		// ����ѩ��
		for (int i = 0; i < snows.size(); ++i) {
			SnowEntity cacheSnow = snows.get(i);
			m.setTranslate(-cacheSnow.width / 2, -cacheSnow.height / 2);
			m.postRotate(cacheSnow.rotation);
			m.postTranslate(cacheSnow.width / 2 + cacheSnow.x, cacheSnow.height
					/ 2 + cacheSnow.y);
			canvas.drawBitmap(cacheSnow.bitmap, m, null);
		}
	}
	
	/**���ô�����������*/
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
			//������Ļ���ϵ�ʱ�����
			if(z>5)
			{
				//��Ч��Χ
				if((x>=-5||x<=5)&&(y>=-5||y<=5))
				{
					//��ײ���(x����)
					for(int i = 0; i < snows.size(); ++i)
					{
						SnowEntity cacheSnow = snows.get(i);
						cacheSnow.x+=-x;
						//x������ײ
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
	 * ���"ѩ��"
	 */
	public void addSnows(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			snows.add(SnowEntity.createShow(getWidth(), snowBitmap));
		}
		setNumSnows(numSnows + quantity);
	}

	/**
	 * ��ȥ"ѩ��"
	 */
	public void subtractSnows(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			int index = numSnows - i - 1;
			snows.remove(index);
		}
		setNumSnows(numSnows - quantity);
	}

	/** ��ȡ"ѩ���ĵ�ǰ����" */
	public int getNumSnows() {
		return numSnows;
	}

	/** ����"ѩ��"�ĵ�ǰ���� */
	public void setNumSnows(int quantity) {
		numSnows = quantity;
	}
	
	/**ע�ᴫ����*/
	public void regSensor(SensorManager sensorManager)
	{
		//���ɼ��ٶȴ�����
		moveBallSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		moveBallSensorListener=new MoveBallSensorListener();
		//ע�������
		sensorManager.registerListener(moveBallSensorListener, moveBallSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	/**ע��������*/
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
