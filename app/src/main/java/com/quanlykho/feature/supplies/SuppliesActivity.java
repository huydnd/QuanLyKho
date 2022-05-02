package com.quanlykho.feature.supplies;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.SuppliesQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.Supplies;
import com.quanlykho.util.BitmapHelper;
import com.quanlykho.util.MovableFloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuppliesActivity extends AppCompatActivity implements SuppliesListAdapter.CustomButtonListener {

	private DrawerLayout drawerLayout;
	private NavigationView navigationView;



	ListView suppliesListView;
	ArrayList<Supplies> suppliesArrayList= new ArrayList<Supplies>();
	SuppliesListAdapter suppliesListAdapter;
	MovableFloatingActionButton addButton;
	//For Popup add
	private Button addImageButton_addSuppliesPopup,addButton_addSuppliesPopup, cancelButton_addSuppliesPopup;
	private EditText nameSuppliesEditText_addSuppliesPopup, unitSuppliesEditText_addSuppliesPopup;
	private ImageView imageView_addSuppliesPopup;
	//For popup edit
	private Button editImageButton_editSuppliesPopup, editButton_editSuppliesPopup, cancelButton_editSuppliesPopup;
	private EditText nameSuppliesEditText_editSuppliesPopup, unitSuppliesEditText_editSuppliesPopup;
	private ImageView imageView_editSuppliesPopup;

	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	private DAO.SuppliesQuery suppliesQuery = new SuppliesQuery();

	private int suppliesIndex = -1;
	private String path;

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_IMAGE_GALLERY = 3;
	private Bitmap mResultsBitmap;

	private boolean currentView=true;

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplies);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("DANH SÁCH VẬT TƯ");
		setControl();
		setEvent();

		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
				Toast.makeText(SuppliesActivity.this,"So luong vat tu = "+suppliesArrayList.size(),Toast.LENGTH_SHORT).show();

			}
			@Override
			public void onFailure(String message) {

			}
		});
		suppliesListAdapter = new SuppliesListAdapter(this, R.layout.item_supplies,suppliesArrayList);
		suppliesListAdapter.setCustomButtonListener(this);
		suppliesListView.setAdapter(suppliesListAdapter);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			case android.R.id.home:
				drawerLayout.openDrawer(Gravity.LEFT);
				break;

			default:break;
		}
		return false;
	}

	private void setEvent(){
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				createSuppliesPopup(SuppliesActivity.this);
			}
		});
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(SuppliesActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(SuppliesActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(SuppliesActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(SuppliesActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(SuppliesActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(SuppliesActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(SuppliesActivity.this, ExportHistoryActivity.class));
						break;
					case R.id.nav_quit:
						finishAndRemoveTask();
						break;
					default:
						break;
				}
				return true;
			}
		});

	}

	private void setControl(){
		addButton = findViewById(R.id.floatingActionButton);
		suppliesListView = findViewById(R.id.suppliesListView);
		drawerLayout = findViewById(R.id.activity_supplies_drawer);
		navigationView = findViewById(R.id.navigationView);
	}

	void createSuppliesPopup(Context context){
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View addSuppliesPopup = inflater.inflate(R.layout.popup_supplies_add,null);
		//setControl
		nameSuppliesEditText_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesNameEditText);
		unitSuppliesEditText_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesUnitEditText);
		addImageButton_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesImageAddButton);
		imageView_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesImageView);
		addButton_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesAddButton);
		cancelButton_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesCancelButton);
		//createAndShow
		imageView_addSuppliesPopup.setImageResource(R.drawable.default_image);
		dialogBuilder.setView(addSuppliesPopup);
		dialog = dialogBuilder.create();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.TOP;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(wlp);
		currentView=true;
		dialog.show();

		//setListener
		addImageButton_addSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ContextCompat.checkSelfPermission(SuppliesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					requestPermissions(
							new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
							REQUEST_IMAGE_CAPTURE
					);
					startImageUploadOptions();
				} else {
					startImageUploadOptions();
				}
			}
		});
		addButton_addSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String suppliesName = nameSuppliesEditText_addSuppliesPopup.getText().toString();
				String suppliesUnit = unitSuppliesEditText_addSuppliesPopup.getText().toString();
//				Bitmap bitmap = BitmapFactory.decodeFile(path);
//				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//				bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
//				byte[] bytesImage = byteArrayOutputStream.toByteArray();
				BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView_addSuppliesPopup.getDrawable());
				Bitmap bitmap = bitmapDrawable .getBitmap();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] imageInByte = stream.toByteArray();
				createSupplies(context,suppliesName,suppliesUnit,imageInByte);
				dialog.dismiss();
			}
		});

		cancelButton_addSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});


	}

	void createSupplies(Context context,String name, String unit,byte[] image){
		if(name.length()==0){
			Toast.makeText(context, "Tên vật tư không được trống", Toast.LENGTH_SHORT).show();
			return;
		}
		if(unit.length()==0){
			Toast.makeText(context, "Đơn vị không được trống", Toast.LENGTH_SHORT).show();
			return;
		}

		ArrayList<Supplies> e = new ArrayList<>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				e.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});
		for(Supplies w : e) {
			if (w.getSuppliesName().equals(name)) {
				Toast.makeText(context, "Thông tin vật tư đã tồn tại", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		Supplies supplies= new Supplies(name,unit,image);
		suppliesQuery.createSupplies(context,supplies, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				suppliesArrayList.removeAll(suppliesArrayList);
				suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
					@Override
					public void onSuccess(List<Supplies> data) {
						suppliesArrayList.addAll(data);
					}
					@Override
					public void onFailure(String message) {
					}
				});
				suppliesListAdapter.notifyDataSetChanged();
//				ArrayList<String> warehouseNameList= (ArrayList<String>) warehouseArrayList.stream()
//						.map(Warehouse::getWarehouseName).collect(Collectors.toList());
				onResume();
			}
			@Override
			public void onFailure(String message) {
			}
		});

	}

	//image Handler
	private void startImageUploadOptions() {
		final CharSequence[] options = {"Chụp từ Camera", "Chọn từ thư viện", "Hủy"};
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SuppliesActivity.this);
		builder.setTitle("Chọn ảnh cho sản phẩm mới");
		builder.setIcon(R.drawable.ic_baseline_camera_alt_48);
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Chụp từ Camera")) {
					launchCamera(SuppliesActivity.this);
				} else if (options[item].equals("Chọn từ thư viện")) {
					launchGalleryImagePicker();
				} else if (options[item].equals("Hủy")) {
					dialog.dismiss();
				}
			}
		});
		Dialog imageDialog=builder.create();
		Window window = imageDialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.BOTTOM;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(wlp);
		imageDialog.show();
	}
	private void launchCamera(Context context) {
		// Create the capture image intent
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		//if (takePictureIntent.resolveActivity() != null) {
		// Create the temporary File where the photo should go
		File photoFile = null;
		try {
			photoFile = BitmapHelper.createTempImageFile(context);
		} catch (IOException ex) {
			// Error occurred while creating the File
			ex.printStackTrace();
		}
		// Continue only if the File was successfully created
		if (photoFile != null) {
			// Get the path of the temporary file
			this.path = photoFile.getAbsolutePath();
			// Get the content URI for the image file
			Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
					"com.quanlykho",
					photoFile);

			// Add the URI so the camera can store the image
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

			// Launch the camera activity
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
		//}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
				case REQUEST_IMAGE_CAPTURE:
					processAndSetImage();
					break;
				case REQUEST_IMAGE_GALLERY:
					if (resultCode == RESULT_OK && data != null) {
						getImageFromGallery(data);
					}
					break;
			}
		}
	}

	private void processAndSetImage() {
		mResultsBitmap = BitmapHelper.resamplePic(this, this.path);
//		mResultsBitmap= BitmapHelper.resizeImage(mResultsBitmap,200,false);
		if(currentView) {
			imageView_addSuppliesPopup.setImageBitmap(mResultsBitmap);
		}else {
			imageView_editSuppliesPopup.setImageBitmap(mResultsBitmap);
		}
	}

	private void getImageFromGallery(Intent data) {
		this.path = getPathFromCameraData(data,this);
		if(checkImageSizeLimit(path)){
			if(currentView) {
				imageView_addSuppliesPopup.setImageBitmap(BitmapFactory.decodeFile(this.path));
			}else {
				imageView_editSuppliesPopup.setImageBitmap(BitmapFactory.decodeFile(this.path));
			}
		}
		else{
			Toast.makeText(this, "File quá lớn. Tối đa chỉ 1 MB", Toast.LENGTH_LONG).show();
		}
	}

	private static String getPathFromCameraData(Intent data, Context context) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = {MediaStore.Images.Media.DATA};
		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		Log.d("hihi", "getPathFromCameraData: " + picturePath);
		cursor.close();
//        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		return picturePath;
	}

	private void launchGalleryImagePicker() {
		Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
	}

	boolean checkImageSizeLimit(String path){
		File file = new File(path);
		int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
		if(file_size < 1000)// smaller than 1mb, 24kb is saved for better future :D
			return true;
		else
			return false;
	}



	public void editSuppliesPopup(Context context,int index){
		Supplies supplies = suppliesArrayList.get(index);

		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View editSuppliesPopup = inflater.inflate(R.layout.popup_supplies_edit,null);
		//setControl
		nameSuppliesEditText_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesNameEditText);
		unitSuppliesEditText_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesUnitEditText);
		editImageButton_editSuppliesPopup =editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesImageEditButton);
		imageView_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesImageView);
		editButton_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesEditButton);
		cancelButton_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesCancelButton);
		//createAndShow
		nameSuppliesEditText_editSuppliesPopup.setText(supplies.getSuppliesName());
		unitSuppliesEditText_editSuppliesPopup.setText(supplies.getSuppliesUnit());
		byte[] bitmapdata = supplies.getSuppliesImage();
		Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
		bitmap = BitmapHelper.resizeImage(bitmap,200,false);
		imageView_editSuppliesPopup.setImageBitmap(bitmap);
		dialogBuilder.setView(editSuppliesPopup);
		dialog = dialogBuilder.create();
		currentView=false;
		dialog.show();

		//setListener
		editImageButton_editSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ContextCompat.checkSelfPermission(SuppliesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					requestPermissions(
							new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
							REQUEST_IMAGE_CAPTURE
					);
					startImageUploadOptions();
				} else {
					startImageUploadOptions();
				}
			}
		});
		editButton_editSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name= nameSuppliesEditText_editSuppliesPopup.getText().toString();
				String unit= unitSuppliesEditText_editSuppliesPopup.getText().toString();
				Bitmap bitmap = ((BitmapDrawable) imageView_editSuppliesPopup.getDrawable()).getBitmap();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
				byte[] imageInByte = baos.toByteArray();
				if(name.length()==0){
					Toast.makeText(context, "Tên vật tư không được trống", Toast.LENGTH_SHORT).show();
					return;
				}
				if(unit.length()==0){
					Toast.makeText(context, "Đơn vị không được trống", Toast.LENGTH_SHORT).show();
					return;
				}
				supplies.setSuppliesName(name);
				supplies.setSuppliesUnit(unit);
				supplies.setSuppliesImage(imageInByte);
				editSupplies(context,supplies);
				dialog.dismiss();
			}
		});

		cancelButton_editSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				dialog.dismiss();
			}
		});
	}

	void editSupplies(Context context,Supplies supplies){
		ArrayList<Supplies> e = new ArrayList<>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				e.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});
		for(Supplies w : e) {
			if ((w.getSuppliesId()!=supplies.getSuppliesId())&&(w.getSuppliesName().equals(supplies.getSuppliesName()))) {
				Toast.makeText(context, "Thông tin vật tư đã tồn tại", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		suppliesQuery.updateSupplies(supplies, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				suppliesArrayList.removeAll(suppliesArrayList);
				suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
					@Override
					public void onSuccess(List<Supplies> data) {
						suppliesArrayList.addAll(data);
					}
					@Override
					public void onFailure(String message) {
					}
				});
				suppliesListAdapter.notifyDataSetChanged();
				onResume();
			}
			@Override
			public void onFailure(String message) {
			}
		});

	}



	void delSupplies(String name) {
		suppliesQuery.deleteSuppliesByName(name, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				suppliesArrayList.removeAll(suppliesArrayList);
				suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
					@Override
					public void onSuccess(List<Supplies> data) {
						suppliesArrayList.addAll(data);
					}

					@Override
					public void onFailure(String message) {
					}
				});
				suppliesListAdapter.notifyDataSetChanged();
				onResume();
			}

			@Override
			public void onFailure(String message) {
			}
		});
	}

	@Override
	public void onEditButtonClickListener(int position) {
		editSuppliesPopup(SuppliesActivity.this,position);
	}

	@Override
	public void onDelButtonClickListenner(int position) {
		delSupplies(suppliesArrayList.get(position).getSuppliesName());
	}


}