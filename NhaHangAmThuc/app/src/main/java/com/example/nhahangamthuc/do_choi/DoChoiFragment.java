package com.example.nhahangamthuc.do_choi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhahangamthuc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoChoiFragment extends Fragment {

    private RecyclerView recyclerView;
    private DoChoiAdapter adapter;
    private List<DoChoi> doChoiList;
    Context context;
    private ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference reference;

    public DoChoiFragment() {
        // Required empty public constructor
    }

    public static DoChoiFragment newInstance() {
        return new DoChoiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_do_choi, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_do_choi);
        view.findViewById(R.id.button_them_do_choi).setOnClickListener(v -> {
            themDoChoi();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        progressDialog = new ProgressDialog(context);

        super.onViewCreated(view, savedInstanceState);

        doChoiList = new ArrayList<>();

        setData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        adapter = new DoChoiAdapter(doChoiList, new DoChoiAdapter.IItemClick() {
            @Override
            public void onUpdateClick(DoChoi doChoi) {
                suaDoChoi(doChoi);
            }

            @Override
            public void onDeleteClick(DoChoi doChoi) {
                xoaDoChoi(doChoi);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void setData() {
        progressDialog.show();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("list_do_choi");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doChoiList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoChoi doChoi = dataSnapshot.getValue(DoChoi.class);
                    doChoiList.add(doChoi);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "???? x???y ra l???i khi l???y danh s??ch ????? ch??i!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void themDoChoi() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);
        dialog.show();

        dialog.findViewById(R.id.btn_them).setOnClickListener(v -> {
            EditText editTextTen = dialog.findViewById(R.id.edt_ten_do_choi);
            EditText editTextSoLuong = dialog.findViewById(R.id.edt_so_luong);
            int id = doChoiList.get(doChoiList.size()-1).getId()+1;
            DoChoi doChoi = new DoChoi(id, Integer.parseInt(editTextSoLuong.getText().toString()), editTextTen.getText().toString());
            reference.child(String.valueOf(id)).setValue(doChoi, (error, ref) -> {
                dialog.dismiss();
                Toast.makeText(context, "Th??m ????? ch??i th??nh c??ng!", Toast.LENGTH_SHORT).show();
            });
        });

        dialog.findViewById(R.id.btn_huy).setOnClickListener(v -> dialog.dismiss());
    }

    private void suaDoChoi(DoChoi doChoi) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);
        TextView title = dialog.findViewById(R.id.text_view_title);
        title.setText("C???p nh???t th??ng tin ????? ch??i");

        EditText editTextTen = dialog.findViewById(R.id.edt_ten_do_choi);
        editTextTen.setText(doChoi.getTen());
        EditText editTextSoLuong = dialog.findViewById(R.id.edt_so_luong);
        editTextSoLuong.setText(String.valueOf(doChoi.getSoLuong()));
        dialog.show();

        Button buttonSua = dialog.findViewById(R.id.btn_them);
        buttonSua.setText("C???p nh???t");
        buttonSua.setOnClickListener(v -> {
            doChoi.setTen(editTextTen.getText().toString());
            doChoi.setSoLuong(Integer.parseInt(editTextSoLuong.getText().toString()));
            reference.child(String.valueOf(doChoi.getId())).updateChildren(doChoi.toMap(), (error, ref) -> {
                dialog.dismiss();
                Toast.makeText(context, "C???p nh???t th??ng tin ????? ch??i th??nh c??ng!", Toast.LENGTH_SHORT).show();
            });
        });

        dialog.findViewById(R.id.btn_huy).setOnClickListener(v -> dialog.dismiss());
    }

    private void xoaDoChoi(DoChoi doChoi) {
        new AlertDialog.Builder(context).setTitle("X??a ????? ch??i")
                .setMessage("B???n c?? ch???c ch???n mu???n x??a ????? ch??i n??y kh??ng?")
                .setPositiveButton("X??a", (dialog, which) -> reference.child(String.valueOf(doChoi.getId())).removeValue((error, ref) ->
                        Toast.makeText(context, "X??a ????? ch??i th??nh c??ng!", Toast.LENGTH_SHORT).show()))
                .setNegativeButton("H???y", null).show();
    }
}