package eecs.dietary.assistant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class CameraView extends Activity {

	public static int RETURN_FROM_CAMERA = -1;
	public static int RETURN_FROM_CROP = 4;
	public static int RETURN_FROM_OCR = 555749321;
	public int pixeldensity = 300;
	public int subsamplefactor = 2;

	private Uri outputFileUri;

	public boolean preferquality = true;
	private String _imagepath = Environment.getExternalStorageDirectory().toString() + "/ocr.jpg";
	AlertDialog ad; 

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_item); //trash view

		ad = new AlertDialog.Builder(this).create();

		File file = new File(_imagepath);
		outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);

	}

	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);


		if(resultcode == RETURN_FROM_CAMERA && requestcode != RETURN_FROM_CROP) {
			//User has now accepted the photo and pressed OK 
			doCrop();
		}
		else if(requestcode == RETURN_FROM_CROP) {  	

			/*File f = new File(_imagepath);            

			if (f.exists()) f.delete();*/

			BitmapFactory.Options options = new BitmapFactory.Options();

			options.inSampleSize = subsamplefactor;
			options.inPreferQualityOverSpeed = preferquality;
			options.inTargetDensity = pixeldensity;
			Bitmap bitmap = BitmapFactory.decodeFile(_imagepath, options);

			try {

				ExifInterface exif = new ExifInterface(_imagepath);
				int exifOrientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);

				int rotate = 0;

				switch (exifOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
				}

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);

				// Convert to ARGB_8888, required by tess
				bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

				DietaryAssistantActivity._OCR.ReadBitmapImage(bitmap);
				int conf = DietaryAssistantActivity._OCR._tessapi.meanConfidence();
				DietaryAssistantActivity._OCRReader.IngredientDictionary = DietaryAssistantActivity._Ingredients.returnAll();
				DietaryAssistantActivity._OCRReader.IngredientsFound = DietaryAssistantActivity._OCRReader.RetrieveIngredients(DietaryAssistantActivity._OCR.readText);

				///ad.setMessage(Integer.toString(conf) + "//" + DietaryAssistantActivity._OCR.readText);
				///ad.show();

				Intent i = new Intent(this, OCRFeedback.class);
				startActivityForResult(i,RETURN_FROM_OCR);

			} catch (IOException e) {
				ad.setMessage("io exception");
				ad.show();
			}
		}
		else if(requestcode==RETURN_FROM_OCR) {

			File file = new File(_imagepath);
			outputFileUri = Uri.fromFile(file);

			final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

			startActivityForResult(intent, 0);
		}
		else {
			super.finish();
		}
	}

	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

		int size = list.size();

		if (size == 0) {	        
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} 
		else {
			intent.setData(outputFileUri);

//			intent.putExtra("outputX", 200);
//			intent.putExtra("outputY", 200);
//			intent.putExtra("aspectX", 1);
//			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Intent i 		= new Intent(intent);
				ResolveInfo res	= list.get(0);

				i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

				startActivityForResult(i, RETURN_FROM_CROP);
			} 
			else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
					co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
					co.appIntent= new Intent(intent);

					co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");
				builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
					public void onClick( DialogInterface dialog, int item ) {
						startActivityForResult( cropOptions.get(item).appIntent, RETURN_FROM_CROP);
					}
				});

				builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
					public void onCancel( DialogInterface dialog ) {

						if (outputFileUri != null ) {
							getContentResolver().delete(outputFileUri, null, null );
							outputFileUri = null;
						}
					}
				} );

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}
}
