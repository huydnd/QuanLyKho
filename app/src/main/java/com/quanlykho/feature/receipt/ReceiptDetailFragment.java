package com.quanlykho.feature.receipt;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.SuppliesQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptDetailFragment extends Fragment {

	private static final String ARG_WAREHOUSE_NAME = "warehouseName";
	//Fragment
	private ImageButton addDetail,delDetail;
	private ArrayList<SuppliesDetail> suppliesDetailArrayList=new ArrayList<SuppliesDetail>();
	private ReceiptDetailListAdapter receiptDetailListAdapter;
	private ListView detailListView;
	private View previousSelectedItem;
	//POPUP
	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	private Button addButton_addDetailPopup,cancelButton_addDetailPopup;
	private AutoCompleteTextView suppliesName;
	private TextInputEditText suppliesUnit,suppliesAmount;
	private ArrayAdapter suppliesArrayAdapter;
	private ArrayList<String> suppliesNameList;
	private DAO.SuppliesQuery suppliesQuery = new SuppliesQuery();
	private DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();

	// TODO: Rename and change types of parameters
	private String warehouseName;
	private int detailIndex=-1;

	private ReceiptViewModel viewModel;

//	OnFragmentInteractionListener onFragmentInteractionListener= (OnFragmentInteractionListener) getActivity();


	public ReceiptDetailFragment() {
		// Required empty public constructor
	}
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param warehouseName Parameter 1.
	 * @return A new instance of fragment ReceiptDetailFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ReceiptDetailFragment newInstance(String warehouseName) {
		ReceiptDetailFragment fragment = new ReceiptDetailFragment();
		Bundle args = new Bundle();
		args.putString(ARG_WAREHOUSE_NAME, warehouseName);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			warehouseName = getArguments().getString(ARG_WAREHOUSE_NAME);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_receipt_detail, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//Set control
		addDetail = view.findViewById(R.id.addSuppliesButton);
		delDetail = view.findViewById(R.id.delSuppliesButton);
		detailListView = view.findViewById(R.id.receipt_detail_listview);
		viewModel  = new ViewModelProvider(requireActivity()).get(ReceiptViewModel.class);

		//Set event

		addDetail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				createDetailPopup(getContext());
			}
		});

		delDetail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(detailIndex==-1){
					return;
				}
				suppliesDetailArrayList.remove(detailIndex);
				receiptDetailListAdapter.notifyDataSetChanged();
			}
		});
		detailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				detailIndex=i;
//				if (previousSelectedItem!=null) {
//					previousSelectedItem.setBackgroundColor(Color.WHITE);
//				}
//				previousSelectedItem  = view;
//				view.setBackgroundColor(Color.main_color);
			}
		});

		//Create list view
		receiptDetailListAdapter = new ReceiptDetailListAdapter(this.getContext(),R.layout.item_receipt_detail,suppliesDetailArrayList);
		detailListView.setAdapter(receiptDetailListAdapter);
	}


	public void createDetailPopup(Context context){
		ArrayList<Supplies> suppliesArrayList=new ArrayList<Supplies>();
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View addDetailPopup = inflater.inflate(R.layout.popup_receipt_detail,null);
		//setControl
		suppliesName = addDetailPopup.findViewById(R.id.receipt_detail_supplies_name_add_popup);
		suppliesAmount = addDetailPopup.findViewById(R.id.receipt_detail_supplies_amount_add_popup);
		suppliesUnit = addDetailPopup.findViewById(R.id.receipt_detail_supplies_unit_add_popup);
		addButton_addDetailPopup = addDetailPopup.findViewById(R.id.receipt_detail_supplies_add_button);
		cancelButton_addDetailPopup = addDetailPopup.findViewById(R.id.receipt_detail_supplies_cancel_button);
		//createAndShow
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
				suppliesNameList= (ArrayList<String>) suppliesArrayList.stream()
						.map(Supplies::getSuppliesName).collect(Collectors.toList());
			}
			@Override
			public void onFailure(String message) {
			}
		});
		if(suppliesArrayList.size()==0){
			Toast.makeText(this.getContext(),"Vui lòng thêm vật tư vào danh sách",Toast.LENGTH_SHORT).show();
			return;
		}
		suppliesArrayAdapter = new ArrayAdapter(this.getContext(),R.layout.dropdown_item,suppliesNameList);
		SuppliesDetail currenSuppliesDetail=new SuppliesDetail();
		currenSuppliesDetail.setName("");
		currenSuppliesDetail.setUnit("");
		suppliesName.setAdapter(suppliesArrayAdapter);
		dialogBuilder.setView(addDetailPopup);
		dialog = dialogBuilder.create();
		dialog.show();


		//setListener
		addButton_addDetailPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(currenSuppliesDetail.getName().equals("")||currenSuppliesDetail.getUnit().equals("")){
					Toast.makeText(dialog.getContext(), "Chưa chọn loại vật tư",Toast.LENGTH_SHORT).show();
					return;
				}else if(suppliesAmount.getText().toString().equals("")) {
					Toast.makeText(dialog.getContext(), "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
					return;
				}else{
					currenSuppliesDetail.setAmount(Integer.parseInt(suppliesAmount.getText().toString()));
				}
				suppliesDetailArrayList.add(currenSuppliesDetail);
				viewModel.setData(suppliesDetailArrayList);
				receiptDetailListAdapter.notifyDataSetChanged();
				dialog.dismiss();

			}
		});

		cancelButton_addDetailPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

		suppliesName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				suppliesUnit.setText(suppliesArrayList.get(i).getSuppliesUnit());
				currenSuppliesDetail.setName(suppliesArrayList.get(i).getSuppliesName());
				currenSuppliesDetail.setUnit(suppliesArrayList.get(i).getSuppliesUnit());
			}
		});

	}

}