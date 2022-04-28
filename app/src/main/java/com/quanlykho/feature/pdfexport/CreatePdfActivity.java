package com.quanlykho.feature.pdfexport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.SuppliesQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.statistic.SuppliesChartActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.GmailSender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CreatePdfActivity extends AppCompatActivity {


	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	private Warehouse warehouse;
	private ArrayList<Warehouse> warehouseArrayList = new ArrayList<Warehouse>();
	private ArrayList<String> warehouseNameList = new ArrayList<String>();

	private HashMap<String, byte[]> hashMap = new HashMap<String, byte[]>();
	private ArrayList<Supplies> suppliesArrayList = new ArrayList<Supplies>();
	private ArrayList<SuppliesDetail> suppliesDetailArrayList = new ArrayList<SuppliesDetail>();
	private DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
	private DAO.SuppliesQuery suppliesQuery = new SuppliesQuery();

	private ArrayAdapter warehouseNameListAdapter;
	private AutoCompleteTextView warehouseNameDropdown;
	private TextInputEditText emailTextEdit;
	private Button genaratePdfButton,sendMailButton;
	private File file;

	private int warehouseIndex = -1;

	private static final int PERMISSION_REQUEST_CODE = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_pdf);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("PDF");
		setControl();
		setEvent();

		warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
			@Override
			public void onSuccess(List<Warehouse> data) {
				warehouseArrayList.addAll(data);
				if (warehouseArrayList.size() > 0) {
					warehouseNameList = (ArrayList<String>) warehouseArrayList.stream()
							.map(Warehouse::getWarehouseName).collect(Collectors.toList());
				}
				if (warehouseNameList.size() >= 5) {
					warehouseNameDropdown.setDropDownHeight(720);
				}
				warehouseNameListAdapter = new ArrayAdapter(CreatePdfActivity.this, R.layout.dropdown_item, warehouseNameList);
				warehouseNameDropdown.setAdapter(warehouseNameListAdapter);
			}

			@Override
			public void onFailure(String message) {
			}
		});
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
				for (Supplies e : data) {
					hashMap.put(e.getSuppliesName(), e.getSuppliesImage());
				}
			}

			@Override
			public void onFailure(String message) {

			}
		});

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

	private void setControl() {
		this.genaratePdfButton = findViewById(R.id.generatePdfButton);
		this.warehouseNameDropdown = findViewById(R.id.pdf_warehouseNameDropdown);
		this.sendMailButton = findViewById(R.id.sendMailButton);
		this.emailTextEdit = findViewById(R.id.pdf_mailAddressTextEdit);
		drawerLayout = findViewById(R.id.activity_createpdf_drawer);
		navigationView = findViewById(R.id.navigationView);
	}

	private void setEvent() {
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(CreatePdfActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(CreatePdfActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(CreatePdfActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(CreatePdfActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(CreatePdfActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(CreatePdfActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(CreatePdfActivity.this, ExportHistoryActivity.class));
						break;
					case R.id.nav_statistics:
						startActivity( new Intent(CreatePdfActivity.this, SuppliesChartActivity.class));
						break;
					case R.id.nav_pdf:
						startActivity( new Intent(CreatePdfActivity.this, CreatePdfActivity.class));
						break;
					case R.id.nav_quit:
						finishAffinity();
						break;
					default:
						break;
				}
				return true;
			}
		});
		warehouseNameDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				warehouseIndex = position;
				warehouse = warehouseArrayList.get(position);
				warehouseQuery.readAllDetailByWarehouseName(warehouseNameList.get(position), new QueryResponse<List<SuppliesDetail>>() {
					@Override
					public void onSuccess(List<SuppliesDetail> data) {
						suppliesDetailArrayList.removeAll(suppliesDetailArrayList);
						suppliesDetailArrayList.addAll(data);
					}

					@Override
					public void onFailure(String message) {

					}
				});
			}
		});

		genaratePdfButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (warehouseIndex != -1) {
					if (checkPermission()) {
						generatePDF("");
					} else {
						requestPermission();
					}
				} else {
					Toast.makeText(CreatePdfActivity.this, "Vui lòng chọn kho", Toast.LENGTH_SHORT);
				}
			}
		});
		sendMailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Objects.requireNonNull(emailTextEdit.getText()).toString().length()==0){
					Toast.makeText(CreatePdfActivity.this,"Địa chỉ email trống",Toast.LENGTH_SHORT).show();
				}else {
					generatePDF(emailTextEdit.getText().toString());
				}
			}
		});
	}

	private void generatePDF(String mailAddress) {
		//    PDF
		int leftX = 20, rightX = 220;
		int pageNumber = 1;
		int width = 360;
		int height = 720;
		int imageX = 20, tenX = 120, donviX = 200, soLuongX = 300, currentY = 240;
		PdfDocument myPdfDocument = new PdfDocument();
		Paint myPaint = new Paint();
		PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(width, height, pageNumber).create();
		PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
		Canvas canvas = myPage.getCanvas();

		Bitmap icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.warehouse_logo);
		Bitmap scaleIcon = Bitmap.createScaledBitmap(icon, 100, 90, false);
		canvas.drawBitmap(scaleIcon, leftX, 20, myPaint);
		myPaint.setTextSize(24);
		myPaint.setColor(getResources().getColor(R.color.main_color));
		canvas.drawText("Danh sách vật tư", 150, 68, myPaint);


		myPaint.setColor(Color.RED);
		canvas.drawLine(0, 120, 360, 120, myPaint);

		myPaint.setTextSize(12);
		myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
		myPaint.setColor(Color.BLACK);
		canvas.drawText("Mã kho: " + warehouse.getWarehouseId(), leftX, 140, myPaint);
		canvas.drawText("Tên kho: " + warehouse.getWarehouseName(), leftX, 170, myPaint);
		canvas.drawText("Địa chỉ: " + warehouse.getWarehouseAddress(), leftX, 200, myPaint);

		myPaint.setColor(Color.RED);
		canvas.drawLine(0, 210, 360, 210, myPaint);

		myPaint.setColor(Color.BLACK);
		myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		canvas.drawText("Ảnh", 20, 240, myPaint);
		canvas.drawText("VẬT TƯ", 120, 240, myPaint);
		canvas.drawText("ĐƠN VỊ", 200, 240, myPaint);
		canvas.drawText("SỐ LƯỢNG", 280, 240, myPaint);

		SuppliesDetail temp;
		for (int i = 0; i < suppliesDetailArrayList.size(); i++) {
			temp = suppliesDetailArrayList.get(i);
			myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
			Bitmap bitmap = BitmapFactory.decodeByteArray(hashMap.get(temp.getName()), 0, hashMap.get(temp.getName()).length);
			Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, 64, 64, false);
			canvas.drawBitmap(scaleBitmap, imageX, currentY +10, myPaint);

			myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
			canvas.drawText(temp.getName(), tenX, currentY + 40, myPaint);
			canvas.drawText(temp.getUnit(), donviX, currentY + 40, myPaint);
			canvas.drawText(String.valueOf(temp.getAmount()), soLuongX, currentY + 40, myPaint);
			myPaint.setColor(Color.BLACK);
			myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
			canvas.drawLine(leftX, currentY+80, 320, currentY+80, myPaint);
			currentY += 80;
			if (currentY > 650) {
				myPdfDocument.finishPage(myPage);
				currentY = 20;
				pageNumber += 1;
				myPageInfo = new PdfDocument.PageInfo.Builder(width, height, pageNumber).create();
				myPage = myPdfDocument.startPage(myPageInfo);
				canvas = myPage.getCanvas();
			}
		}
		myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
		myPaint.setColor(Color.BLACK);
		SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		canvas.drawText("Ngày xuất: " +format.format(Date.valueOf(String.valueOf(LocalDate.now()))), leftX, currentY+30, myPaint);


		myPdfDocument.finishPage(myPage);
		String fileName = "danhsachkho_" + warehouse.getWarehouseName() + ".pdf";
		file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

		if (file.exists()) {
			file.delete();
		}
		try {
			System.out.println("Run into this");
			myPdfDocument.writeTo(new FileOutputStream(file));
			if(mailAddress.length()!=0) {
			String subject = "Báo cáo tình trạng kho " + warehouse.getWarehouseName() + " ngày "+ format.format(Date.valueOf(String.valueOf(LocalDate.now())));
			GmailSender gmailSender = new GmailSender(CreatePdfActivity.this, mailAddress, subject, file);
			gmailSender.execute();
			}else {
				Toast.makeText(CreatePdfActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(CreatePdfActivity.this, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
		}
		myPdfDocument.close();
	}

	private boolean checkPermission() {
		// checking of permissions.
		int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
		int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
		return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermission() {
		// requesting permissions if not provided.
		ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0) {

				// after requesting permissions we are showing
				// users a toast message of permission granted.
				boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
				boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

				if (writeStorage && readStorage) {
					Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}
	}

}