package com.example.showviewtest.entity;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * 雪花描述类
 * */
public class SnowEntity {

	//显示的坐标
	public float x;
	public float y;
	//显示的大小
	public int width;
	public int height;
	//父组件宽度
	private float parentViewWidth=0;
	/**旋转的角度*/
    public float rotation;
    /**下落的速度*/
    public float speed;
    /**旋转的速度*/
    public float rotationSpeed;
    /**显示的位图*/
    public Bitmap bitmap;
    /**缓存位图*/
    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();
	
    /**
     * 创建雪花
     * */
    public static SnowEntity createShow(float parentViewWidth,Bitmap showBitmap)
    {
    	
    	SnowEntity snow=new SnowEntity();
    	snow.parentViewWidth=parentViewWidth;
    	
    	//随机生成宽度
    	snow.width=(int)(5+(float)Math.random()*50);//宽度设置为5-55之间
    	float hwRatio=showBitmap.getHeight() / showBitmap.getWidth();//转化系数
    	snow.height=(int)(snow.width*hwRatio);
    	
    	//随机生成一个位置
    	snow.x=(float)Math.random()*(parentViewWidth-snow.width);
    	snow.y=0-(snow.height+(float)Math.random()*snow.height);
    	
    	//随机生成下落速度
    	snow.speed = 50 + (float) Math.random() * 150;//速度50-200之内随机数
    	
    	//随机生成旋转角度
    	snow.rotation = (float) Math.random() * 180 - 90;//其实角度为-90 - 90的随机数
    	
    	//随机生成旋转角度速度
    	snow.rotationSpeed = (float) Math.random() * 90 - 45;//旋转角度速度为-45 - 45的随机数
    	
    	//生成位图
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
