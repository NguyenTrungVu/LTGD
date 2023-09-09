package ntv.example.bt2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import ntv.example.bt2.DBConfig.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    String[] mapb = {"NV01", "NV02", "NV03", "NV04", "NV05", "NV06", "NV07", "NV08", "NV09"};
    String[] tenpb = {"Trung Vu", "A Thoat", "Hoai Anh", "Quoc Chien", "Trung Viet", "Van Khai", "Minh Duong", "Hai Dang", "Tuan Kiet"};
    int[] sopb = {21,20,24,26,21,23,20,22,30};
    String[] pkmapb = {"CNTT", "CNTT", "KT", "KT", "TCNH","KT","CNTT","TCNH", "TCNH"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(MainActivity.this);
//        db.createDatabase();
        for (int i =0; i<mapb.length; i++){
            db.insertData(mapb[i], tenpb[i],  pkmapb[i],sopb[i]);
            Log.d("test insert", "insert done");
        }
    }
}