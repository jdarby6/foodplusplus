package eecs.dietary.assistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_view);

		preview = new Preview(this); 
		((FrameLayout) findViewById(R.id.preview)).addView(preview); 

		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) { 
				preview.camera.autoFocus(new Camera.AutoFocusCallback() {
					public void onAutoFocus(boolean success, Camera camera) {
						camera.takePicture(shutterCallback, rawCallback, jpegCallback);
					}
				});
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
				String outPutString = String.format("/sdcard/%d.jpg",
						System.currentTimeMillis());
				outStream = new FileOutputStream(outPutString); 
				outStream.write(data);
				outStream.close();
				mImageCaptureUri = Uri.fromFile(new File(outPutString));
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) { 
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

}



