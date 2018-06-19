

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ICSDELIVERYSYSTEM";
    private static final String TABLE_JOB = "IcsDeliveryTable";

    // Couter Table
    private static final String Id = "Id";
    private static final String Title = "Title";
    private static final String ZoneId = "ZoneId";
    private static final String AccountCode = "AccountCode";
    private static final String ServiceNumber = "ServiceNumber";
    private static final String Type = "Type";
    private static final String Amount = "Amount";
    private static final String Delivered = "Delivered";
    private static final String PaymentMode = "PaymentMode";
    private static final String Signature = "Signature";
    private static final String Comments = "Comments";
    private static final String ChequeNumber = "ChequeNumber";
    private static final String CollectCash = "CollectCash";
    private static final String IsFinish = "IsFinish";
    private static final String Date = "Date";
    private static final String Time = "Time";


    private static final String CREATE_TABLE_JOBS = "CREATE TABLE "
            + TABLE_JOB + "(" +
            Id + " INTEGER PRIMARY KEY," +
            Title + " TEXT," +
            ZoneId + " TEXT," +
            AccountCode + " TEXT," +
            ServiceNumber + " TEXT," +
            Type + " INTEGER," +
            Amount + " TEXT," +
            Delivered + " INTEGER," +
            PaymentMode + " INTEGER," +
            Signature + " TEXT," +
            Comments + " TEXT," +
            ChequeNumber + " TEXT," +
            CollectCash + " TEXT," +
            IsFinish + " INTEGER," +
            Date + " TEXT," +
            Time + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_JOBS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB);

        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB);
        database.execSQL(CREATE_TABLE_JOBS);
        database.close();
    }

    public void addJob(ModelJob modelJob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Title, modelJob.getTitle());
        values.put(ZoneId, modelJob.getZoneId());
        values.put(AccountCode, modelJob.getAccountCode());
        values.put(ServiceNumber, modelJob.getServiceNumber());
        values.put(Type, modelJob.getType());
        values.put(Amount, modelJob.getAmount());
        values.put(Delivered, modelJob.getDelivered());
        values.put(PaymentMode, modelJob.getPaymentMode());
        values.put(Signature, modelJob.getSignature());
        values.put(Comments, modelJob.getComments());
        values.put(ChequeNumber, modelJob.getChequeNumber());
        values.put(CollectCash, modelJob.getCollectCash());
        values.put(IsFinish, modelJob.getIsFinish());
        values.put(Date, Utility.getCurrentDate());
        values.put(Time, Utility.getCurrentTime());

        db.insert(TABLE_JOB, null, values);
        db.close();
    }

    public void deleteJob(int JobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOB, Id + " = ?",
                new String[]{String.valueOf(JobId)});
    }

    public List<ModelJob> getJobList() {
        List<ModelJob> jobList = new ArrayList<ModelJob>();
        String selectQuery = "SELECT  * FROM " + TABLE_JOB + " WHERE " + IsFinish + " in (0)";
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ModelJob jobModel = new ModelJob();
                jobModel.setId(c.getInt((c.getColumnIndex(Id))));
                jobModel.setTitle(c.getString((c.getColumnIndex(Title))));
                jobModel.setZoneId(c.getString((c.getColumnIndex(ZoneId))));
                jobModel.setAccountCode(c.getString((c.getColumnIndex(AccountCode))));
                jobModel.setServiceNumber(c.getString((c.getColumnIndex(ServiceNumber))));
                jobModel.setType(c.getInt((c.getColumnIndex(Type))));
                jobModel.setAmount(c.getString((c.getColumnIndex(Amount))));
                jobModel.setDelivered(c.getInt((c.getColumnIndex(Delivered))));
                jobModel.setPaymentMode(c.getInt((c.getColumnIndex(PaymentMode))));
                jobModel.setSignature(c.getString((c.getColumnIndex(Signature))));
                jobModel.setComments(c.getString((c.getColumnIndex(Comments))));
                jobModel.setChequeNumber(c.getString((c.getColumnIndex(ChequeNumber))));
                Log.e("--getJobList: ", "---" + c.getString((c.getColumnIndex(CollectCash))));
                jobModel.setCollectCash(c.getString((c.getColumnIndex(CollectCash))));
                jobModel.setIsFinish(c.getInt((c.getColumnIndex(IsFinish))));
                jobModel.setDate(c.getString((c.getColumnIndex(Date))));
                jobModel.setTime(c.getString((c.getColumnIndex(Time))));
                jobList.add(jobModel);
            } while (c.moveToNext());
        }
        Log.e("~~getJobList: ", new Gson().toJson(jobList));
        return jobList;
    }


    public void updatejobList(ModelJob modelJob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("~~updatejobList: ", new Gson().toJson(modelJob));
        values.put(Title, modelJob.getTitle());
        values.put(ZoneId, modelJob.getZoneId());
        values.put(AccountCode, modelJob.getAccountCode());
        values.put(ServiceNumber, modelJob.getServiceNumber());
        values.put(Type, modelJob.getType());
        values.put(Amount, modelJob.getAmount());
        values.put(Delivered, modelJob.getDelivered());
        values.put(PaymentMode, modelJob.getPaymentMode());
        values.put(Signature, modelJob.getSignature());
        values.put(Comments, modelJob.getComments());
        values.put(CollectCash, modelJob.getCollectCash());
        values.put(ChequeNumber, modelJob.getChequeNumber());
        values.put(IsFinish, modelJob.getIsFinish());
        values.put(Date, Utility.getCurrentDate());
        values.put(Time, Utility.getCurrentTime());
        db.update(TABLE_JOB, values, Id + "= ?",
                new String[]{String.valueOf(modelJob.getId())});
        db.close();
    }

    public ModelJob getJobDetails(int JobID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JOB, new String[]{Id, Title, ZoneId, AccountCode,
                        ServiceNumber, Type, Amount, Delivered, PaymentMode, Signature, Comments,
                        ChequeNumber, CollectCash, IsFinish, Date, Time}, Id + "=?",
                new String[]{String.valueOf(JobID)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        ModelJob jobModel = new ModelJob();
        jobModel.setId(cursor.getInt(0));
        jobModel.setTitle(cursor.getString(1));
        jobModel.setZoneId(cursor.getString(2));
        jobModel.setAccountCode(cursor.getString(3));
        jobModel.setServiceNumber(cursor.getString(4));
        jobModel.setType(cursor.getInt(5));
        jobModel.setAmount(cursor.getString(6));
        jobModel.setDelivered(cursor.getInt(7));
        jobModel.setPaymentMode(cursor.getInt(8));
        jobModel.setSignature(cursor.getString(9));
        jobModel.setComments(cursor.getString(10));
        jobModel.setChequeNumber(cursor.getString(11));
        jobModel.setCollectCash(cursor.getString(12));
        jobModel.setIsFinish(cursor.getInt(13));
        jobModel.setDate(cursor.getString(14));
        jobModel.setTime(cursor.getString(15));
        return jobModel;
    }

}
