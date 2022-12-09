package cs333fa22.hfad.demosqlite;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static  final String DB_NAME = "employees.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(DBContract.EmployeeEntry.CREATE_EMP_TABLE_CMD);
        db.execSQL(DBContract.EmployeeEntry.CREATE_EMP_TABLE_CMD);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DBContract.EmployeeEntry.DROP_EMP_TABLE_CMD);
        onCreate(db);
    }

    public void saveEmployee(String name, String design, long dobInMs){

        String insertString = String.format("INSERT INTO %s (%s, %s, %s) " +
                                            "Values('%s', %d, '%s')",
                                            DBContract.EmployeeEntry.TABLE_NAME,
                                            DBContract.EmployeeEntry.COLUMN_NAME,
                                            DBContract.EmployeeEntry.COLUMN_DOB,
                                            DBContract.EmployeeEntry.COLUMN_DESIGNATION,
                                            name,
                                            dobInMs,
                                            design);

        System.out.println("SAVING: " + insertString);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertString);

        db.close();
    }

    public ArrayList<Employee> fetchAllEmployees(){
        ArrayList<Employee> allEmps = new ArrayList<>();
        String selectAllString = "SELECT * FROM " + DBContract.EmployeeEntry.TABLE_NAME;
        System.out.println("FETCHING all: " + selectAllString);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectAllString, null);

        int idPos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_ID);
        int namePos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_NAME);
        int desigPos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_DESIGNATION);
        int dobPos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_DOB);

        while(cursor.moveToNext()) {
            long id = cursor.getLong(idPos);
            long dob = cursor.getLong(dobPos);
            String name = cursor.getString(namePos);
            String desig = cursor.getString(desigPos);

            allEmps.add(new Employee(id, name, dob, desig));
        }
        cursor.close();
        db.close();
        return allEmps;
    }

    public void updateEmployee(Employee emp){
        String updateString = String.format("UPDATE %s \n" +
                "SET %s = '%s',\n" +
                "    %s = '%s',\n" +
                "    %s = %d\n" +
                "WHERE %s = %d;",
                DBContract.EmployeeEntry.TABLE_NAME,
                DBContract.EmployeeEntry.COLUMN_NAME,
                emp.getName(),
                DBContract.EmployeeEntry.COLUMN_DESIGNATION,
                emp.getDesignation(),
                DBContract.EmployeeEntry.COLUMN_DOB,
                emp.getDob(),
                DBContract.EmployeeEntry.COLUMN_ID,
                emp.getId());

        System.out.println("UPDATING: " + updateString);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateString);
    }
}
