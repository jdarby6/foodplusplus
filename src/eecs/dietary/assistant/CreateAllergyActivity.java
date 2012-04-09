package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

//First screen in create allergy sequence
//Displays text box input for allergy name 

public class CreateAllergyActivity extends Activity {
	static final int OK = 0;
	static final int ALLERGY_EXISTS = 1;
	static final int BLANK_FIELD = 2;
	
	private Button discard;
	private Button create; 
	private EditText et;
	private GridView gv;
	private List<String> allergIcons; 
	private int selectedIconIndex = 0;
	
	public static int CALL_CREATEACTIVITY2 = 38238414;
	public static int DISCARD = 391931;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.createallergyscreen1);
		
		/*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);	*/
		
		allergIcons = new ArrayList<String>();
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		allergIcons.add("hehehe");
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		gv = (GridView) this.findViewById(R.id.gridAllergies);
		gv.setAdapter(new ImageAdapterAllergyIcons(this,allergIcons));
		gv.setFocusable(true);
		gv.setClickable(true);
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
						selectedIconIndex = arg2;
						gv.invalidateViews(); //may not be the most efficient way to do the click
											  //highlighting but think it should be good enough
			
			}
		});
		
		
		et = (EditText) this.findViewById(R.id.editTextAllergyCreate);	
		et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		
	
		this.discard = (Button) this.findViewById(R.id.buttonDiscardAllergy1);
		this.create = (Button) this.findViewById(R.id.buttonCreateAllergy1);

		this.create.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				
				//call function to see if the input allergy name in edittext is ok
				int error_flag = OK;

				if(et.getText().toString().trim().isEmpty()) error_flag = BLANK_FIELD;
				else {
					Cursor cursor = DietaryAssistantActivity._Ingredients.dbHelper.checkIfAllergyExists(et.getText().toString());
					if(cursor.getCount() > 0) error_flag = ALLERGY_EXISTS;
				}
				
				if(error_flag == OK) {
					Intent i = new Intent();
					i.setClass(CreateAllergyActivity.this, CreateAllergyActivity2.class);
					i.putExtra("allergyname", et.getText().toString());
					i.putExtra("iconIndex",selectedIconIndex);
					startActivityForResult(i, CALL_CREATEACTIVITY2);
				}
				else if(error_flag == ALLERGY_EXISTS) {
					Animation shake = AnimationUtils.loadAnimation(getBaseContext(),R.anim.shake);
					et.startAnimation(shake);
					CreateAllergyActivity.this.showDialog(ALLERGY_EXISTS);
				}
				else if(error_flag == BLANK_FIELD) {
					Animation shake = AnimationUtils.loadAnimation(getBaseContext(),R.anim.shake);
					et.startAnimation(shake);
					CreateAllergyActivity.this.showDialog(BLANK_FIELD);
				}
			}
		});
		this.discard.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				setResult(DISCARD);
				finish();
				
			}
		});
	}
		
		
	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		if(requestcode == CALL_CREATEACTIVITY2) {
			if(resultcode == CreateAllergyActivityReviewAllergy.SAVED_BUTTON) {
				setResult(CreateAllergyActivityReviewAllergy.SAVED_BUTTON);
				finish();
			}
		}	
	}	
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
		AlertDialog.Builder builder;
		
	    if (id == ALLERGY_EXISTS) {
			builder = new AlertDialog.Builder(this);
			builder.setMessage("An allergy with this name already exists.")
			       .setTitle("Error")
			       .setCancelable(false)
			       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			dialog = builder.create();
		} 
	    else if (id == BLANK_FIELD) {
			builder = new AlertDialog.Builder(this);
			builder.setMessage("You must type in a name for this allergy before continuing.")
			       .setTitle("Error")
			       .setCancelable(false)
			       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			dialog = builder.create();
		} 
	    else {
			dialog = null;
		}
	    return dialog;
	}
	
	
	
	
	public class ImageAdapterAllergyIcons extends BaseAdapter {
	      private Context _context;
	      private final List<String> _allergies;
	      
	      
	      
	      public ImageAdapterAllergyIcons(Context _MyContext, List<String> allergies)
	      {
	         _context = _MyContext;
	         _allergies = allergies;
	      }
	      
	      public View getView(int position, View convertView, ViewGroup parent) 
	      {
	    	  LayoutInflater inflater = (LayoutInflater) _context
	    				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	 
	    			View gridItem;
	    			String allergy = (String) getItem(position);
	    			
	    				
	    			if(convertView == null) {
	    			
	    				gridItem = new View(_context);  
	    				gridItem = inflater.inflate(R.layout.ingredienticon, null);
	    			}
	    			else {
	    				gridItem = convertView;
	    			}

	    			ImageView imageView = (ImageView) gridItem
    						.findViewById(R.id.grid_item_image); 
    				DietaryAssistantActivity._Ingredients.setImageIcon(imageView,position);
    				
	    			if(position == selectedIconIndex) {
	    				gridItem.setBackgroundColor(Color.rgb(0xE7, 0x7A, 0x26));
	    			}
	    			else {
	    				gridItem.setBackgroundColor(Color.TRANSPARENT);
	    			}
	    			
	    			return gridItem;
	      }

	     
		public Object getItem(int arg0) {
	    	  return _allergies.get(arg0);
	      }

	      public long getItemId(int arg0) {
	         // TODO Auto-generated method stub
	         return 0;
	      }

		public int getCount() {
			return _allergies.size();
		}
		
	   }
	
	
	
}