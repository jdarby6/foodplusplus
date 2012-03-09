package eecs.dietary.assistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraView extends Activity {
	private static final String TAG = "CameraDemo";
	Preview preview; 
	Button buttonClick; 
	Uri mImageCaptureUri;
	AlertDialog ad;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_view);
		ad =  new AlertDialog.Builder(this).create();

		preview = new Preview(this); 
		((FrameLayout) findViewById(R.id.preview)).addView(preview); 

		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) { 
			    Camera.Parameters parameters = preview.camera.getParameters();
			    List<String> focusModes = parameters.getSupportedFocusModes();
			    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
			      {
			          Log.d("camera", "focus_mode_auto");
			          preview.camera.autoFocus(new Camera.AutoFocusCallback() {
		                    public void onAutoFocus(boolean success, Camera camera) {
		                        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		                    }
		                });
			      }
			      else
			      {
			          Log.d("camera", "focus_mode_macro");
			          preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			      }
			    
			    //Matrix mtx = new Matrix();
			 
			}
		});

		Log.d(TAG, "onCreate'd");
	}

	
	// Called when shutter is opened
	ShutterCallback shutterCallback = new ShutterCallback() { 
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	// Handles data for raw picture
	PictureCallback rawCallback = new PictureCallback() { 
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	// Handles data for jpeg picture
	PictureCallback jpegCallback = new PictureCallback() { 
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				// Write to SD Card
				/*String outPutString = String.format("/sdcard/%d.jpg",
						System.currentTimeMillis());
				outStream = new FileOutputStream(outPutString); 
				outStream.write(data);
				outStream.close();
				mImageCaptureUri = Uri.fromFile(new File(outPutString));*/
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
				
				  BitmapFactory.Options options = new BitmapFactory.Options();
				    options.inSampleSize = 2; //adjusts down-scaling
				    options.inPreferQualityOverSpeed = true; //possibly will slow down
				    options.inPurgeable = true; //might save us memory by re-allocating/de-allocating
				//    options.inDensity = 30;
				    //options.inScaled = true; by default already
				    options.inTargetDensity = 300;
				   // options.
				    
				    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length,options);
				    Matrix mtx = new Matrix();
				    mtx.preRotate(90);
				    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, false);
				    
				    
				    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
				    
				    DietaryAssistantActivity._OCR.ReadBitmapImage(bitmap);
				    
				   
				    
				    
				//	AlertDialog ad = new AlertDialog.Builder().create();
				    ad.setMessage(DietaryAssistantActivity._OCR.readText);
					ad.show();
				
				
			} /*catch (FileNotFoundException e) { 
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/ finally {
			}
			
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

}



