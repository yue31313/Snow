package com.example.showviewtest.entity;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * ѩ��������
 * */
public class SnowEntity {

	//��ʾ������
	public float x;
	public float y;
	//��ʾ�Ĵ�С
	public int width;
	public int height;
	//��������
	private float parentViewWidth=0;
	/**��ת�ĽǶ�*/
    public float rotation;
    /**������ٶ�*/
    public float speed;
    /**��ת���ٶ�*/
    public float rotationSpeed;
    /**��ʾ��λͼ*/
    public Bitmap bitmap;
    /**����λͼ*/
    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();
	
    /**
     * ����ѩ��
     * */
    public static SnowEntity createShow(float parentViewWidth,Bitmap showBitmap)
    {
    	
    	SnowEntity snow=new SnowEntity();
    	snow.parentViewWidth=parentViewWidth;
    	
    	//������ɿ��
    	snow.width=(int)(5+(float)Math.random()*50);//�������Ϊ5-55֮��
    	float hwRatio=showBitmap.getHeight() / showBitmap.getWidth();//ת��ϵ��
    	snow.height=(int)(snow.width*hwRatio);
    	
    	//�������һ��λ��
    	snow.x=(float)Math.random()*(parentViewWidth-snow.width);
    	snow.y=0-(snow.height+(float)Math.random()*snow.height);
    	
    	//������������ٶ�
    	snow.speed = 50 + (float) Math.random() * 150;//�ٶ�50-200֮�������
    	
    	//���������ת�Ƕ�
    	snow.rotation = (float) Math.random() * 180 - 90;//��ʵ�Ƕ�Ϊ-90 - 90�������
    	
    	//���������ת�Ƕ��ٶ�
    	snow.rotationSpeed = (float) Math.random() * 90 - 45;//��ת�Ƕ��ٶ�Ϊ-45 - 45�������
    	
    	//����λͼ
    	snow.bitmap=bitmapMap.get(snow.height);
    	if(snow.bitmap == null)
    	{
    		snow.bitmap = Bitmap.createScaledBitmap(showBitmap,
                    (int)snow.width, (int)snow.height, true);
            bitmapMap.put(snow.width, snow.bitmap);
    	}
    	
    	return snow;
    }
    
    public void resetX()
    {
    	this.x=(float)Math.random()*(parentViewWidth-this.width);
    }
    
}
