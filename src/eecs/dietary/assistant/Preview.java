package eecs.dietary.assistant;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class Preview extends SurfaceView implements SurfaceHolder.Callback { 
  private static final String TAG = "Preview";

  SurfaceHolder mHolder;  
  public Camera camera; 

  Preview(Context context) {
    super(context);

    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed.
    mHolder = getHolder();  
    mHolder.addCallback(this);  
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
  }

  // Called once the holder is ready
  public void surfaceCreated(SurfaceHolder holder) {  
    // The Surface has been created, acquire the camera and tell it where
    // to draw.
	  if(camera == null)
		  camera = Camera.open(); 
    try {
      camera.setPreviewDisplay(holder); 

      camera.setPreviewCallback(new PreviewCallback() { 
        // Called for each frame previewed
        public void onPreviewFrame(byte[] data, Camera camera) {  
          Log.d(TAG, "onPreviewFrame called at: " + System.currentTimeMillis());
          Preview.this.invalidate();  
        }
      });
    } catch (IOException e) { 
      e.printStackTrace();
    }
  }

  // Called when the holder is destroyed
  public void surfaceDestroyed(SurfaceHolder holder) {  
	  if (camera != null) {
		  	camera.stopPreview();
	        camera.setPreviewCallback(null);
	        camera.release();
	        camera = null;
	    }
  }

  // Called when holder has changed
  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	  
	  Camera.Parameters parameters = camera.getParameters();
	  List<String> focusModes = parameters.getSupportedFocusModes();
	  if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
	  {
		  Log.d("camera", "focus_mode_auto");
	      parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
	  }
	  else
	  {
		  Log.d("camera", "focus_mode_macro");
		  parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
	  }
	  camera.setDisplayOrientation(90);
	  camera.setParameters(parameters);
	  camera.startPreview();
  }

}