package ntv.example.bt2.DBConfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Khai bao tag
    private static String TAG = "DataBaseHelper";
    //Khai bao ten va version db.
    public static final String DB_NAME = "bt2.db";
    public static final int DB_VERSION = 1;
    //khai bao quyen ghi va doc data tu database;
    private SQLiteDatabase mDefaultWritableDatabase;
    private String databasePath;
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.mDefaultWritableDatabase = sqLiteDatabase;

        String sql1 = "CREATE TABLE PhongBan (" +
                "maPhongban text PRIMARY KEY NOT NULL," +
                "tenPhongBan text NOT NULL,"+
                "soPhongBan int default null,"+
                "created_at DATETIME DEFAULT NULL,"
                + "updated_at DATETIME DEFAULT NULL,"
                + "deleted_at DATETIME DEFAULT NULL)";
        sqLiteDatabase.execSQL(sql1);

        String sql2 = "CREATE TABLE NhanVien (" +
                "maNhanVien text PRIMARY KEY NOT NULL," +
                "tenNhanVien text NOT NULL,"+
                "tuoiNhanVien int default null,"+
                "maPB int not null,"+
                "created_at DATETIME DEFAULT NULL,"
                + "updated_at DATETIME DEFAULT NULL,"
                + "deleted_at DATETIME DEFAULT NULL,"
                + "FOREIGN KEY (maPB) REFERENCES PhongBan (maPhongBan))";
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        this.mDefaultWritableDatabase = sqLiteDatabase;
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PhongBan");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NhanVien");
    }
    public void createDatabase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
//            copyDataBase();
        }
    }
    //Kiem tra thu db co ton tai chua
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
            Log.e(TAG, "Database is exist.");
        } catch (Exception e) {
            Log.e(TAG, "Database does not exist.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }
    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db;
        if(mDefaultWritableDatabase != null){
            db = mDefaultWritableDatabase;
        }else{
            db = super.getWritableDatabase();
        }
        return db;
    }
    @Override
    public synchronized void close(){
        if(mDefaultWritableDatabase!=null){
            mDefaultWritableDatabase.close();
        }
        super.close();
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    public void insertData(String id, String tensp, String mapb, int tuoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNhanVien", id);
        values.put("tenNhanVien", tensp);
        values.put("tuoiNhanVien", tuoi);
        values.put("maPB", mapb);
        db.insert("NhanVien", null, values);
        db.close();
    }

}
