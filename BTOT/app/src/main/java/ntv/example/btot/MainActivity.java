package ntv.example.btot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ntv.example.btot.DBConfig.DBHelper;
import ntv.example.btot.R;
import ntv.example.btot.DBConfig.MyDatabase;
import ntv.example.btot.Model.SanPham;

public class MainActivity extends AppCompatActivity {
    ListView itemlist;
    TextView warning;
    private MyDatabase db;
    private DBHelper db1;
    List<SanPham> sp  =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemlist = findViewById(R.id.list_item);
        db = new MyDatabase(this);
        sp = db.getSanPham();
        String ma = "SP01";
        String ten = "IPHONE X";
        db1 = new DBHelper(this);
//db1.insertData(ma, ten,15000);
        ArrayAdapter<SanPham> aa = new ArrayAdapter<SanPham>(this,
                android.R.layout.simple_list_item_1, sp);
        itemlist.setAdapter(aa);
        Log.d("test data",sp.get(1).toString());

        itemlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SanPham selectedItem = (SanPham)itemlist.getItemAtPosition(i);
                Log.d("test select", selectedItem.toString());
                showEditDialog(selectedItem.getMaSP(), selectedItem.getTenSP(), String.valueOf(selectedItem.getDonGia()));
                return false;
            }
        });

    }

    private void showEditDialog(String ma, String ten, String gia) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Information");

        // Inflate the custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_info, null);
        builder.setView(dialogView);

        // Find views in the custom layout
        // Set initial text of editInfoEditText
        final EditText name = dialogView.findViewById(R.id.tenSPed);
        name.setText(ten);
        final EditText masp = dialogView.findViewById(R.id.maSped);
        masp.setText(ma);
        final EditText dg = dialogView.findViewById(R.id.donGiaed);
        dg.setText(gia);
        List<String> dsma = new ArrayList<>();
        for (SanPham p : db.getSanPham()) {
            dsma.add(p.getMaSP());
        }
        warning = dialogView.findViewById(R.id.warning);
        masp.addTextChangedListener( new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String maMoi = masp.getText().toString();
                if(dsma.contains(maMoi)){
                    warning.setVisibility(View.VISIBLE);
                    builder.setPositiveButton("Tro ve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(null, null);
                }else{
                    builder.setPositiveButton("Cap nhat", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String updatedname = name.getText().toString();
                            String updatedma = masp.getText().toString();
                            String updatedgia = dg.getText().toString();
                            if(!updatedma.isEmpty() && !updatedname.isEmpty() && !updatedgia.isEmpty()){

                                capNhatThongTinSanPham(updatedma, updatedname, Float.parseFloat(updatedgia));}
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


            // Set click listener for the positive butto

        // Set click listener for the negative button
        builder.setNegativeButton("Xoa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    xoaSanPham(ma);
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void capNhatThongTinSanPham(String masp, String ten, Float gia) {

            boolean isUpdated = db.CapNhatSanPham(masp,ten,gia);
            if (isUpdated) {
                Toast.makeText(this, "Cập nhật san pham thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không thể cập nhật san pham", Toast.LENGTH_SHORT).show();
            }

    }
    private void xoaSanPham(String masp){
        boolean isdeleted = db.xoaSanPham(masp);
        if (isdeleted) {
            Toast.makeText(this, "Xoa san pham thành công", Toast.LENGTH_SHORT).show();
            // Remove the item from the sp list
            for (SanPham item : sp) {
                if (item.getMaSP().equals(masp)) {
                    sp.remove(item);
                    break;
                }
            }
            // Notify the adapter that the data has changed
            ((ArrayAdapter<SanPham>) itemlist.getAdapter()).notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Không thể xoa", Toast.LENGTH_SHORT).show();
        }
    }
}