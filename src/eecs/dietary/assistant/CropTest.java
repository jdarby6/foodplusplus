package eecs.dietary.assistant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class CropTest extends Activity {
	Panel myView;
	private Bitmap userImage;
	private static final int CLEAR_ID = Menu.FIRST;
	private static final int SUBMIT_ID = Menu.FIRST+1;
	private int displayHeight;
	private int displayWidth;
	private Point topLeft = new Point(-1, -1);
	private Point bottomRight = new Point(-1, -1);
	private Bitmap original;
	private float drawHeight, drawWidth;
	private int origWidth, origHeight;
	private int dispH, dispW;
	
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent calling = this.getIntent();
        String path = calling.getStringExtra("UserImage");
        Log.d("touch", path);
        Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
       
        //options.inSampleSize = 2;
        options.inScaled = false;
        
        //original = BitmapFactory.decodeFile(path, options);
        if(original != null) {
        	original = null;
        }
        original = decodeFile(path);
        
        Display display = getWindowManager().getDefaultDisplay();
        int width = original.getWidth(); origWidth = width;
        int height = original.getHeight(); origHeight = height;
        int newWidth = display.getWidth();
        int newHeight = display.getHeight();
        
        //Point widthPoint = new Point();
        //display.getSize(widthPoint);
        
        displayHeight = newHeight;
        displayWidth = newWidth;
        
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        
        if(scaleWidth >= scaleHeight) scaleWidth = scaleHeight;
        else scaleHeight = scaleWidth;
        
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        
        userImage = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        myView = new Panel(this);
        setContentView(myView);
        myView.requestFocus();
        
    }
    
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, CLEAR_ID, 0, "Clear");
        menu.add(0, SUBMIT_ID, 0, "Submit");
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CLEAR_ID:
                myView.clear();
                return true;
            case SUBMIT_ID:
                myView.submit();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    class Coord {
        double x;
        double y;
    };
    
    class Panel extends View {
    	
    	int curX;
		int curY;
		float startX, startY;
		Canvas traceCan;
		Paint myPaint;
		Path path;
		private ArrayList<Path> _graphics = new ArrayList<Path>();
		
    	public Panel (Context context) {
    		super(context);
    		myPaint = new Paint();  
    		myPaint.setDither(true);  
    		myPaint.setColor(Color.RED);  
    		myPaint.setStyle(Paint.Style.STROKE);  
    		myPaint.setStrokeJoin(Paint.Join.ROUND);  
    		myPaint.setStrokeCap(Paint.Cap.ROUND);  
    		myPaint.setStrokeWidth(3);
    	}
    	
    	public void submit() {
            // TODO Auto-generated method stub
           
            File FILE_PATH = Environment.getExternalStorageDirectory();
            FILE_PATH = new File(FILE_PATH + "/DietaryAssistant/tmp/");
            Log.d("touch", FILE_PATH.toString() );
            FILE_PATH.mkdirs();
            File cropFile = new File(FILE_PATH, "cropped.png");
            Log.d("touch", cropFile.getAbsolutePath());
            try {
                FileOutputStream fout = new FileOutputStream(cropFile);
                PrintStream p = new PrintStream(fout);
                
                int leftX, rightX;
                if(topLeft.x < bottomRight.x)
                {
                	leftX = topLeft.x; rightX = bottomRight.x;
                }
                else
                {
                	leftX = bottomRight.x; rightX = topLeft.x;
                }
                
                int topY, bottomY;
                if(topLeft.y < bottomRight.y)
                {
                	topY = topLeft.y; bottomY = bottomRight.y;
                }
                else
                {
                	topY = bottomRight.y; bottomY = topLeft.y;
                }
                
                topLeft.x = leftX; topLeft.y = topY;
                bottomRight.x = rightX; bottomRight.y = bottomY;
                
                int x = topLeft.x, y = topLeft.y, width = (bottomRight.x-topLeft.x), height = (bottomRight.y-topLeft.y);
                
                Log.d("touch", "x: " + x + " y: " + y + " width: " + width + " height: " + height);
                
                Log.d("touch", "dispH: " + dispH + "dispW: " + dispW);
                
                Log.d("touch", "origWidth: " + origWidth + " origHeight: " + origHeight + " displayWidth: " + displayWidth + " displayHeight: " + displayHeight);
                
                width = (int)((double)width * ((double)origWidth / (double)displayWidth));
                height = (int)((double)height * ((double)origHeight / (double)displayHeight));
                int newX = (int)((double)x * ((double)origWidth / (double)displayWidth));
                int newY = (int)((double)y * ((double)origHeight / (double)displayHeight));
                
                
                
                Log.d("touch", "x: " + newX + " y: " + newY + " width: " + width + " height: " + height);
                
                Bitmap bmp = Bitmap.createBitmap(original, newX, newY, width, height);
                //original.crea
                //bmp = bmp.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
                
    			//Convert it to grayscale...
    			bmp = toGrayscale(bmp);
    			
    			//Adjust contrast...
    			bmp = createContrast(bmp, 20);
    			
    			
               
                try {
                    FileOutputStream out = new FileOutputStream(cropFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                p.close();
                bmp.recycle();
                original.recycle();
                userImage.recycle();
               
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
            setResult(0);
            finish();
        }
    	
    	@Override
    	public void onDraw(Canvas canvas) {
    		traceCan = canvas;
    		drawHeight = (displayHeight-userImage.getHeight())/2;
    		drawWidth = (displayWidth-userImage.getWidth())/2;
    		Log.d("touch", "" + drawHeight + " " + drawWidth + " " + displayHeight + " " + displayWidth);
    		canvas.drawBitmap(userImage, drawWidth, drawHeight, null);
    		dispH = canvas.getHeight();
    		dispW = canvas.getWidth();
    		for (Path path : _graphics) {  
    		    canvas.drawPath(path, myPaint);  
    		  }
    	}
    	
    	public void clear() {
    		
    		if (traceCan != null) {
                _graphics.clear();
                invalidate();
            }
    		topLeft.x = -1;
    		bottomRight.x = -1;
    		
    	}
    	
    	@Override public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            
            if(action == MotionEvent.ACTION_DOWN) {
            	clear();
            	
            	path = new Path();
            	
            	curX = (int)event.getX();
            	curY = (int)event.getY();
            	
            	Log.d("touch", "x: " + curX + " y: " + curY);
            	
            	if(topLeft.x == -1)
            	{
            		Log.d("touch", "Grabbing topLeft");
            		topLeft.x = curX;
            		topLeft.y = curY;
            	}
            	invalidate();
            }
            else if(action == MotionEvent.ACTION_MOVE) {
            	int newX = (int)event.getX(); int newY = (int)event.getY();
            	if(((newX >= curX+3) || (newX <= curX-3)) || ((newY >= curY+3)||(newY <= curY-3))) {
            		path.reset();
            		Log.d("touch", "Moving, draw the rect already c'mon");
            		bottomRight.x = curX;
            		bottomRight.y = curY;
            		curX = newX;
            		curY = newY;
            		path.addRect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, Path.Direction.CW);
            		_graphics.clear();
            		_graphics.add(path);
            		invalidate();
            	}
            }
            
            return true;
    	}
    	    	  	
    }
    
    private Bitmap decodeFile(String f){
        Bitmap b = null;
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            while(!checkBitmapFitsInMemory(o.outWidth, o.outHeight, 2))
            {
            	scale *= 2;
            	o.inSampleSize = scale;
            	BitmapFactory.decodeStream(fis, null, o);
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
            o2.inScaled = false;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
        }
        return b;
    }
    
    public static boolean checkBitmapFitsInMemory(long bmpwidth,long bmpheight, int bmpdensity ){
        long reqsize=bmpwidth*bmpheight*bmpdensity;
        long allocNativeHeap = Debug.getNativeHeapAllocatedSize();


        final long heapPad=(long) Math.max(4*1024*1024,Runtime.getRuntime().maxMemory()*0.1);
        if ((reqsize + allocNativeHeap + heapPad) >= Runtime.getRuntime().maxMemory())
        {
            return false;
        }
        return true;

    }
    
	public Bitmap toGrayscale(Bitmap bmpOriginal)
	{        
	    int width, height;
	    height = bmpOriginal.getHeight();
	    width = bmpOriginal.getWidth();    

	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    Canvas c = new Canvas(bmpGrayscale);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bmpOriginal, 0, 0, paint);
	    return bmpGrayscale;
	}
	
	public Bitmap createContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }
}
