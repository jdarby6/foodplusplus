package eecs.dietary.assistant;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

public class BarcodeView extends Activity {
	private DataBaseHelper dbHelper;
	
	
	public static int CALL_CAMERA = 93831345;
	
	

	
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);	
		this.setContentView(R.layout.camera_view);
		dbHelper = new DataBaseHelper(this, "upc_db"); //copy local database "upc_db" to work with this app
		try {
			dbHelper.createDataBase();

		} catch (IOException ioe) {
			throw new Error("Unable to create database");

		}

		try {
			dbHelper.openDataBase();

		} catch(SQLException sqle){	
			throw sqle;

		}

		Intent intent = new Intent("com.google.zxing.client.android.SCAN"); //zxing barcode scanner intent
		intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
		startActivityForResult(intent, 0);

	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				// Handle successful scan

//				//temporary message to demonstrate that the barcode was read successfully
//				Toast.makeText(this, "Got this number from barcode: " + contents, Toast.LENGTH_LONG).show();
				
				Cursor cursor = dbHelper.findUPC(contents);
				//brand, name, description, container, size, uom, ingredients
				if(cursor.getCount() > 0) {
					Intent i = new Intent(this, BarcodeFeedback.class);

					cursor.moveToNext();
					i.putExtra("brand", cursor.getString(0));
					i.putExtra("name", cursor.getString(1));
					i.putExtra("description", cursor.getString(2));
					i.putExtra("container", cursor.getString(3));
					i.putExtra("size", cursor.getDouble(4));
					i.putExtra("uom", cursor.getString(5));
					i.putExtra("ingredients", cursor.getString(6));
					
					startActivity(i);
				}
				else {
					//Toast.makeText(this, "Could not find UPC code '" + contents + "'", Toast.LENGTH_LONG).show();
						//Dialog d;
					  AlertDialog.Builder builder = new AlertDialog.Builder(this);
					  builder.setMessage("Could not find code '" + contents + "'. Try camera?");
					  builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							setResult(CALL_CAMERA);
							finish();
						}
					});
					  builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							setResult(0);
							finish();
						}
					});
					  
					builder.show();
					
					
					
					
				}
				
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
			//finish();
		}
	}

}