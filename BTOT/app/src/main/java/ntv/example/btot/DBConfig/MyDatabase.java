package ntv.example.btot.DBConfig;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ntv.example.btot.Model.SanPham;

public class MyDatabase extends SQLiteOpenHelper {

    public MyDatabase(Context context) {
        super(context,  "btot.db", null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    @SuppressLint("Range")
    public List<SanPham> getSanPham() {
        List<SanPham> dsSanPham = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM SanPhams";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String ma = cursor.getString(cursor.getColumnIndex("maSanPham"));
                String ten = cursor.getString(cursor.getColumnIndex("tenSanPham"));
                Float gia = cursor.getFloat(cursor.getColumnIndex("donGia"));
                SanPham post = new SanPham(ma, ten, gia);
                dsSanPham.add(post);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dsSanPham;
    }

    public boolean CapNhatSanPham( String masp, String tensp, Float gia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("maSanPham", masp);
        values.put("tenSanPham", tensp);
        values.put("donGia", gia);

        int rowsAffected = db.update("SanPhams", values, null, null);
        db.close();

        return rowsAffected > 0;
    }
    public boolean xoaSanPham(String masp) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("SanPhams", "maSanPham = ?", new String[]{String.valueOf(masp)});
        return result > 0;
    }
}
