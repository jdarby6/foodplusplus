package eecs.dietary.assistant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class CameraView extends Activity {

	private String _imagepath = Environment.getExternalStorageDirectory().toString() + "/ocr.png";

	public static int RETURN_FROM_CAMERA = 99;
	public static int PICK_FROM_FILE = 3;
	public static int RETURN_FROM_CROP = 57392923;
	public static int RETURN_FROM_OCR = 555749321;
	public static int RETURN_FROM_NEW_CROP = 54324587;

	private Uri outputFileUri;
	public int pixeldensity = 400;
	public int subsamplefactor = 1;
	public boolean preferquality = true;

	AlertDialog ad; 

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//		setContentView(R.layout.list_item); //trash view

		//		ad = new AlertDialog.Builder(this).create();

		File file = new File(_imagepath);
		outputFileUri = Uri.fromFile(file);		
		//outputFileUri.



		String[] addPhoto=new String[]{ "Camera" , "Gallery" };
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle(getResources().getString(R.string.method));

		dialog.setItems(addPhoto,new DialogInterface.OnClickListener(){
	//		@Override -- caused errors for me when compiling (jason)
			public void onClick(DialogInterface dialog, int id) {

				if(id == 0){
					// Call camera Intent
					final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

					startActivityForResult(intent, RETURN_FROM_CAMERA);
					dialog.dismiss();
				}
				if(id == 1){
					//call gallery
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
					dialog.dismiss();
				}
			}
		});

		dialog.show();
	}





	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);


		if(requestcode == RETURN_FROM_CAMERA) {
			//User has now accepted the photo and pressed OK 
			if (resultcode == RESULT_OK) {
				//doCrop();
				Intent sendImage = new Intent(this, CropTest.class);
				sendImage.putExtra("UserImage", _imagepath);
				startActivityForResult(sendImage, RETURN_FROM_NEW_CROP);
			}
		}
		else if(requestcode == PICK_FROM_FILE) {
			//User has selected an image from the phone's gallery to be cropped
			if (resultcode == RESULT_OK) {
				//String sourceImagePath = getPath(data.getData());
				File FILE_PATH = Environment.getExternalStorageDirectory();
	            FILE_PATH = new File(FILE_PATH + "/DietaryAssistant/tmp/");
	            String final_path = FILE_PATH.toString() + "/cropped.png";
				copyGalleryPhotoLoc(final_path);

				doCrop();
			}
		}
		else if(requestcode == RETURN_FROM_CROP) {  	
			//Image has been cropped, and needs to be passed to the OCR methods
			if (resultcode == RESULT_OK) {
				Bitmap bitmap = prepareCroppedImage(data);
				startOCR(bitmap);
			}

		}
		else if(requestcode == RETURN_FROM_OCR) {
			setResult(0);
			finish();
		}
		else if(requestcode == RETURN_FROM_NEW_CROP) {
			File FILE_PATH = Environment.getExternalStorageDirectory();
            FILE_PATH = new File(FILE_PATH + "/DietaryAssistant/tmp/");
            String final_path = FILE_PATH.toString() + "/cropped.png";
            Log.d("mycamera", final_path);
			
			BitmapFactory.Options options = new BitmapFactory.Options();

			options.inSampleSize = subsamplefactor;
			options.inPreferQualityOverSpeed = preferquality;
			options.inTargetDensity = pixeldensity;
			Bitmap bitmap = BitmapFactory.decodeFile(final_path, options); 	
			
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

			} catch (IOException e) {
				ad.setMessage("io exception");
				ad.show();
			}

			startOCR(bitmap);
		}
		else
		{
			Log.d("mycamera", "Error: Unrecognized requestcode: '" + requestcode + "'");
			//ad.setMessage("Error: Unrecognized requestcode: '" + requestcode + "'");
			//ad.show();
			super.finish();
		}
	}





	@Override
	protected void onDestroy() {
		super.onDestroy();

	}





	public void doCrop() {
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

			//			intent.putExtra("outputX", 256);
			//			intent.putExtra("outputY", 256);
			intent.putExtra("scale", false);
			intent.putExtra("return-data", true);
			intent.putExtra("output", outputFileUri);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

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





	public Bitmap prepareCroppedImage(Intent data) {
		try {
			Bitmap temp = (Bitmap) data.getExtras().get("data");

			OutputStream outstream;
			outstream = getContentResolver().openOutputStream(outputFileUri);
			temp.compress(Bitmap.CompressFormat.PNG, 100, outstream); //can also be .JPEG
			outstream.close();
		}
		catch(FileNotFoundException e) {}
		catch (IOException e) {} 


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

		} catch (IOException e) {
			ad.setMessage("io exception");
			ad.show();
		}

		return bitmap;
	}





	public void startOCR(Bitmap bitmap) {
		Log.d("mycamera", "Starting OCR");
		if(bitmap == null) Log.d("mycamera", "bimap is null. shit.");			
		else
		{
		DietaryAssistantActivity._OCR.ReadBitmapImage(bitmap);
		//		int conf = DietaryAssistantActivity._OCR._tessapi.meanConfidence();
		//		DietaryAssistantActivity._OCRReader.IngredientDictionary = DietaryAssistantActivity._Ingredients.returnAll();
		//		DietaryAssistantActivity._OCRReader.IngredientsFound = DietaryAssistantActivity._OCRReader.RetrieveIngredients(DietaryAssistantActivity._OCR.readText);

		///ad.setMessage(Integer.toString(conf) + "//" + DietaryAssistantActivity._OCR.readText);
		///ad.show();

		Intent i = new Intent(this, OCRFeedback.class);
		startActivityForResult(i, RETURN_FROM_OCR);
		}
	}





	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		startManagingCursor(cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}





	public void copyGalleryPhotoLoc(String sourceImagePath) {
		try {

			File source = new File(sourceImagePath);
			File destination = new File(_imagepath);
			if (source.exists()) {
				FileChannel src = new FileInputStream(source).getChannel();
				FileChannel dst = new FileOutputStream(destination).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}
			else {
				Toast.makeText(this, "Can not find gallery image source", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {}
	}
}
