package com.quanlykho.feature.statistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.navigation.NavigationView;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.SuppliesQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.pdfexport.CreatePdfActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesByDate;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.DateCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SuppliesChartActivity extends BaseChartActivity implements SeekBar.OnSeekBarChangeListener, OnChartValueSelectedListener {


	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	private AutoCompleteTextView warehouseNameDropdown, suppliesNameDropdown;
	private BarChart barChart;
	private SeekBar seekBarX, seekBarY;
	private TextView tvX, tvY;
	private ArrayList<Supplies> suppliesArrayList = new ArrayList<Supplies>();
	private ArrayList<String> suppliesNameList = new ArrayList<String>();
	private ArrayList<Warehouse> warehouseArrayList = new ArrayList<Warehouse>();
	private ArrayList<String> warehouseNameList = new ArrayList<String>();
	private ArrayList<SuppliesByDate> suppliesByDateArrayList = new ArrayList<SuppliesByDate>();
	private ArrayAdapter warehouseNameListAdapter, suppliesNameListAdapter;

	private DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
	private DAO.SuppliesQuery suppliesQuery = new SuppliesQuery();

	private String currentWarehouseName = "";
	private String currentSuppliesName = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplies_chart);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("Biểu đồ vật tư");
		setControl();
		setEvent();
		//đọc dữ liệu
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
				warehouseNameListAdapter = new ArrayAdapter(SuppliesChartActivity.this, R.layout.dropdown_item, warehouseNameList);
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
				if (suppliesArrayList.size() > 0) {
					suppliesNameList = (ArrayList<String>) suppliesArrayList.stream()
							.map(Supplies::getSuppliesName).collect(Collectors.toList());
				}
				if (suppliesNameList.size() >= 5) {
					suppliesNameDropdown.setDropDownHeight(720);
				}
				suppliesNameListAdapter = new ArrayAdapter(SuppliesChartActivity.this, R.layout.dropdown_item, suppliesNameList);
				suppliesNameDropdown.setAdapter(suppliesNameListAdapter);
			}

			@Override
			public void onFailure(String message) {

			}
		});

		seekBarY.setOnSeekBarChangeListener(this);
		seekBarX.setOnSeekBarChangeListener(this);
		barChart.setDrawBarShadow(false);
		barChart.setDrawValueAboveBar(false);
		barChart.getDescription().setEnabled(false);

		// if more than 60 entries are displayed in the chart, no values will be
		// drawn
		barChart.setMaxVisibleValueCount(60);

		// scaling can now only be done on x- and y-axis separately
		barChart.setPinchZoom(false);

		// draw shadows for each bar that show the maximum value
		// chart.setDrawBarShadow(true);

		barChart.setDrawGridBackground(false);
//

		XAxis xAxis = barChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setDrawGridLines(false);
		xAxis.setGranularity(1f); // only intervals of 1 day
		xAxis.setLabelCount(7);


		YAxis leftAxis = barChart.getAxisLeft();
		leftAxis.setLabelCount(8, false);
		leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		leftAxis.setSpaceTop(15f);
		leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

		YAxis rightAxis = barChart.getAxisRight();
		rightAxis.setDrawGridLines(false);
		rightAxis.setLabelCount(8, false);
		rightAxis.setSpaceTop(15f);
		rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

		Legend l = barChart.getLegend();
		l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
		l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
		l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
		l.setDrawInside(false);
		l.setForm(Legend.LegendForm.SQUARE);
		l.setFormSize(9f);
		l.setTextSize(11f);
		l.setXEntrySpace(4f);

		// setting data
		seekBarY.setProgress(50);
		seekBarX.setProgress(12);

	}

	private void setData(int count, float range, ArrayList<SuppliesByDate> suppliesByDateArrayList) throws ParseException {

		ArrayList<BarEntry> values = new ArrayList<>();
		ArrayList<String> xAxisValues = new ArrayList<String>();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat nf = new SimpleDateFormat("dd/MM/yyyy");
		Date firstDate, lastDate;
		long diff = 0;
		try {
			if (suppliesByDateArrayList.size() > 0) {
				firstDate = f.parse(suppliesByDateArrayList.get(0).getDate());
				Log.d("Date", "first date:" + firstDate.toString());
				lastDate = f.parse(suppliesByDateArrayList.get(suppliesByDateArrayList.size() - 1).getDate());
				Log.d("Date", "last date:" + lastDate.toString());
				diff = DateCalculator.getDifferenceDays(firstDate, lastDate);
				Log.d("Date", "diff:" + diff);
				xAxisValues.removeAll(xAxisValues);
				xAxisValues.addAll(DateCalculator.listDate(firstDate, lastDate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.d("Date", "xaxis:" + xAxisValues.toString());

		HashMap<String,Integer> hashMap = new HashMap<String,Integer>();

		Log.d("Date", "list::" + suppliesByDateArrayList.size());

		for (SuppliesByDate e: suppliesByDateArrayList) {
			Log.d("Date", "vao vong lap");
			Date date1 = f.parse(e.getDate());
			Log.d("Date", "date1:" + date1);
			String date2 = nf.format(date1);
			Log.d("Date", "date2:" + date2);
			hashMap.put(date2, e.getSuppliesAmount());
			Log.d("Date", "hash:" + date2);
		}

		int temp = 0;

		for (int i = 0; i < xAxisValues.size(); i++) {
			if (hashMap.get(xAxisValues.get(i)) != null) {
				values.add(new BarEntry(i, hashMap.get(xAxisValues.get(i))));
			} else {
				values.add(new BarEntry(i, 0));
			}
		}
		XAxis xAxis = barChart.getXAxis();
		Log.d("Chart", "list string:" + xAxisValues.size());
		xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
		BarDataSet set1;

		if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
			set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
			set1.setValues(values);
			barChart.getData().notifyDataChanged();
			barChart.notifyDataSetChanged();

		} else {
			set1 = new BarDataSet(values, "Số lượng vật tư theo ngày");
			set1.setDrawIcons(false);

			int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
			int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
			int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
			int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
			int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
			int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
			int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
			int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
			int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
			int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

			List<GradientColor> gradientFills = new ArrayList<>();
			gradientFills.add(new GradientColor(startColor1, endColor1));
			gradientFills.add(new GradientColor(startColor2, endColor2));
			gradientFills.add(new GradientColor(startColor3, endColor3));
			gradientFills.add(new GradientColor(startColor4, endColor4));
			gradientFills.add(new GradientColor(startColor5, endColor5));

			set1.setGradientColors(gradientFills);

			ArrayList<IBarDataSet> dataSets = new ArrayList<>();
			dataSets.add(set1);

			BarData data = new BarData(dataSets);
			data.setValueTextSize(10f);
			data.setBarWidth(0.9f);
			barChart.setData(data);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				drawerLayout.openDrawer(Gravity.LEFT);
				break;
			case R.id.actionToggleValues: {
				for (IDataSet set : barChart.getData().getDataSets())
					set.setDrawValues(!set.isDrawValuesEnabled());

				barChart.invalidate();
				break;
			}
			case R.id.actionToggleAutoScaleMinMax: {
				barChart.setAutoScaleMinMaxEnabled(!barChart.isAutoScaleMinMaxEnabled());
				barChart.notifyDataSetChanged();
				break;
			}
			case R.id.actionToggleBarBorders: {
				for (IBarDataSet set : barChart.getData().getDataSets())
					((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

				barChart.invalidate();
				break;
			}
			case R.id.animateX: {
				barChart.animateX(2000);
				break;
			}
			case R.id.animateY: {
				barChart.animateY(2000);
				break;
			}
			case R.id.animateXY: {
				barChart.animateXY(2000, 2000);
				break;
			}
			case R.id.actionSave: {
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
					saveToGallery();
				} else {
					requestStoragePermission(barChart);
				}
				break;
			}
		}
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

		tvX.setText(String.valueOf(seekBarX.getProgress()));
		tvY.setText(String.valueOf(seekBarY.getProgress()));

		try {
			setData(seekBarX.getProgress(), seekBarY.getProgress(), suppliesByDateArrayList);
		} catch (ParseException exception) {
			exception.printStackTrace();
		}
		barChart.setFitBars(true);
		barChart.invalidate();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	private final RectF mOnValueSelectedRectF = new RectF();

	@Override
	protected void saveToGallery() {
		saveToGallery(barChart, "Biểu đồ nhập vật tư theo ngày");
	}

	@Override
	public void onValueSelected(Entry e, Highlight h) {

		if (e == null)
			return;

		RectF bounds = mOnValueSelectedRectF;
		barChart.getBarBounds((BarEntry) e, bounds);

		MPPointF position = barChart.getPosition(e, barChart.getData().getDataSetByIndex(h.getDataSetIndex())
				.getAxisDependency());

		Log.i("bounds", bounds.toString());
		Log.i("position", position.toString());

		MPPointF.recycleInstance(position);
	}

	@Override
	public void onNothingSelected() {
	}


	private void setControl() {
		warehouseNameDropdown = findViewById(R.id.warehouseNameDropdown_ChartActivity);
		suppliesNameDropdown = findViewById(R.id.suppliesNameDropdown);
		barChart = findViewById(R.id.supplieschart);
		tvX = findViewById(R.id.tvXMax);
		tvY = findViewById(R.id.tvYMax);
		seekBarX = findViewById(R.id.seekBar1);
		seekBarY = findViewById(R.id.seekBar2);
		drawerLayout = findViewById(R.id.activity_chart_supplies);
		navigationView = findViewById(R.id.navigationView);

	}

	private void setEvent() {
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(SuppliesChartActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(SuppliesChartActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(SuppliesChartActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(SuppliesChartActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(SuppliesChartActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(SuppliesChartActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(SuppliesChartActivity.this, ExportHistoryActivity.class));
						break;
					case R.id.nav_statistics:
						startActivity( new Intent(SuppliesChartActivity.this, SuppliesChartActivity.class));
						break;
					case R.id.nav_pdf:
						startActivity( new Intent(SuppliesChartActivity.this, CreatePdfActivity.class));
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
				currentWarehouseName = warehouseArrayList.get(position).getWarehouseName();
				if (currentSuppliesName.length() > 0) {
					warehouseQuery.readAllSuppliesWithReceiptDateByWarehouseName(currentWarehouseName, currentSuppliesName, new QueryResponse<List<SuppliesByDate>>() {
						@Override
						public void onSuccess(List<SuppliesByDate> data) {
							if (suppliesByDateArrayList.size() > 0) {
								suppliesByDateArrayList.removeAll(suppliesByDateArrayList);
							}
							suppliesByDateArrayList.addAll(data);
							try {
								setData(seekBarX.getProgress(), seekBarY.getProgress(), suppliesByDateArrayList);
							} catch (ParseException exception) {
								exception.printStackTrace();
							}
							barChart.setFitBars(true);
							barChart.invalidate();
						}

						@Override
						public void onFailure(String message) {
						}
					});
				}
			}
		});
		suppliesNameDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentSuppliesName = suppliesArrayList.get(position).getSuppliesName();
				if (currentWarehouseName.length() > 0) {
					warehouseQuery.readAllSuppliesWithReceiptDateByWarehouseName(currentWarehouseName, currentSuppliesName, new QueryResponse<List<SuppliesByDate>>() {
						@Override
						public void onSuccess(List<SuppliesByDate> data) {
							if (suppliesByDateArrayList.size() > 0) {
								suppliesByDateArrayList.removeAll(suppliesByDateArrayList);
							}
							suppliesByDateArrayList.addAll(data);
							try {
								setData(seekBarX.getProgress(), seekBarY.getProgress(), suppliesByDateArrayList);
							} catch (ParseException exception) {
								exception.printStackTrace();
							}
							barChart.setFitBars(true);
							barChart.invalidate();
						}

						@Override
						public void onFailure(String message) {
						}
					});
				}
			}
		});

	}

}