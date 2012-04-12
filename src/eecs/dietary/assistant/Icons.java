package eecs.dietary.assistant;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.ImageView;

class Icons {
	DataBaseHelper dbHelper;

	Icons(Context context) {
		dbHelper = new DataBaseHelper(context, "icon_indices"); //copy local database "icon_indices" to work with this app

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

		InsertAllergyIcon("milk", 46);
		InsertAllergyIcon("soy", 20);
		InsertAllergyIcon("wheat", 29);
		InsertAllergyIcon("peanuts", 34);
		InsertAllergyIcon("shellfish", 50);
	}

	//Return index of this allergy's icon, if it exists.
	//Currently defaults to 40 if not found... just arbitrarily
	public int GetIconIndex(String allergy) {

		Cursor c = dbHelper.findAllergyIcon(allergy);
		if(c.getCount() == 0) return -1;
		else {
			c.moveToNext();
			return c.getInt(0);
		}


	}

	//Give allergy "allergy" the icon with index "iconindex" in database
	public void InsertAllergyIcon(String allergy, int iconindex) {
		dbHelper.InsertAllergyIcon(allergy, iconindex);
	}

	public void RemoveAllergyIcon(String allergy) {
		dbHelper.RemoveAllergyIcon(allergy);
	}


	public void setImageIcon(ImageView iv, int allergyIconIndex) {

		if(iv != null) {
			switch(allergyIconIndex) {
			case 46:		    		 
				iv.setImageResource(R.drawable.untitled46);
				break;
			case 20:
				iv.setImageResource(R.drawable.untitled20);
				break;
			case 29:
				iv.setImageResource(R.drawable.untitled29);
				break;
			case 34:
				iv.setImageResource(R.drawable.untitled34);
				break;
			case 50:
				iv.setImageResource(R.drawable.untitled50);
				break;
			case 4:
				iv.setImageResource(R.drawable.untitled14);
				break;
			case 3:
				iv.setImageResource(R.drawable.untitled15);
				break;
			case 2:
				iv.setImageResource(R.drawable.untitled16);
				break;
			case 7:
				iv.setImageResource(R.drawable.untitled17);
				break;
			case 6:
				iv.setImageResource(R.drawable.untitled18);
				break;
			case 8:
				iv.setImageResource(R.drawable.untitled19);
				break;
			case 5:
				iv.setImageResource(R.drawable.untitled20);
				break;
			case 9:
				iv.setImageResource(R.drawable.untitled21);
				break;
			case 13:
				iv.setImageResource(R.drawable.untitled22);
				break;
			case 12:
				iv.setImageResource(R.drawable.untitled23);
				break;
			case 11:
				iv.setImageResource(R.drawable.untitled24);
				break;
			case 10:
				iv.setImageResource(R.drawable.untitled25);
				break;
			case 14:
				iv.setImageResource(R.drawable.untitled26);
				break;
			case 15:
				iv.setImageResource(R.drawable.untitled27);
				break;
			case 16:
				iv.setImageResource(R.drawable.untitled28);
				break;
			case 17:
				iv.setImageResource(R.drawable.untitled29);
				break;
			case 18:
				iv.setImageResource(R.drawable.untitled30);
				break;
			case 19:
				iv.setImageResource(R.drawable.untitled31);
				break;
			case 21:
				iv.setImageResource(R.drawable.untitled33);
				break;
			case 22:
				iv.setImageResource(R.drawable.untitled34);
				break;
			case 23:
				iv.setImageResource(R.drawable.untitled35);
				break;
			case 24:
				iv.setImageResource(R.drawable.untitled36);
				break;
			case 25:
				iv.setImageResource(R.drawable.untitled37);
				break;
			case 26:
				iv.setImageResource(R.drawable.untitled38);
				break;
			default:
				iv.setImageResource(R.drawable.untitled40);
				break;
			}
		}  

	}
}