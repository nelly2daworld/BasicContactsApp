package com.example.android.contactsapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_FILE_NAME = "Contacts_SharedPrefs";
    TextView name;
    TextView number;
    EditText editNameText;
    EditText editNumText;
    String nameText;
    int numText;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        editNameText = findViewById(R.id.name_edit_text);
        editNumText = findViewById(R.id.number_edit_text);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> onSaveButtonPressed());

        Button loadButton = findViewById(R.id.load_button);
        loadButton.setOnClickListener(v -> onLoadButtonPressed());

        database = new ContactSqliteHelper(this).getWritableDatabase();
    }

    private void onSaveButtonPressed() {
        ContentValues values = new ContentValues();
        nameText = editNameText.getText().toString();
        numText = Integer.parseInt(String.valueOf(editNumText.getText()));
        try (Cursor cursor = database.query(
                ContactDbSchema.TABLE_NAME,
                null,
                ContactDbSchema.Cols.NAME + " = ?",
                new String[]{editNameText.getText().toString()},
                null, null, null)) {
            // Make the cursor point to the first row in the result set.

            cursor.moveToFirst();
            //return whether cursor is point it ot the position after the last row
            if (cursor.isAfterLast()) {
                values.put(ContactDbSchema.Cols.NAME, nameText);
                //No records exist. Need to insert a new one.
                database.insert(ContactDbSchema.TABLE_NAME, ContactDbSchema.Cols.NAME, values);
                Toast.makeText(getBaseContext(),"Inserted Name",Toast.LENGTH_SHORT).show();

                values.clear();//clear the vals

                values.put(ContactDbSchema.Cols.NUMBER, numText);
                database.insert(ContactDbSchema.TABLE_NAME, ContactDbSchema.Cols.NUMBER, values);
                Toast.makeText(getBaseContext(),"Inserted Number",Toast.LENGTH_SHORT).show();

                values.clear();
            } else {
//                ContentValues args = new ContentValues();

                values.put(ContactDbSchema.Cols.NUMBER, numText);
                database.update(ContactDbSchema.TABLE_NAME, values, null, null);
                Toast.makeText(getBaseContext(),"Number Updated",Toast.LENGTH_SHORT).show();
                values.clear();

                //A record already exists. Need to update it with the new number.
            }
        }
    }

    private void onLoadButtonPressed() {
//        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
        try (Cursor cursor = database.query(ContactDbSchema.TABLE_NAME,
                null,
                ContactDbSchema.Cols.NAME + " = ?",
                new String[]{editNameText.getText().toString()},
                null, null, null)) {
            cursor.moveToFirst();
            if (cursor.isAfterLast()) {
                //The contact was not found.
            }
            int colIndex = cursor.getColumnIndex(ContactDbSchema.Cols.NUMBER);
            if (colIndex < 0) {
                //The column was not found in the cursor
            }
            String numberStr = cursor.getString(colIndex);
        }
    }

    public class ContactDbSchema {
        public static final String TABLE_NAME = "contacts";

        public final class Cols {
            public static final String NAME = "name";
            public static final String NUMBER = "number";
        }
    }

    //helper class to create a database if one doesn't exist already exist
    class ContactSqliteHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "TechExchange_Contacts";
        private static final int VERSION = 1;

        ContactSqliteHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + ContactDbSchema.TABLE_NAME +
                    "( _id integer primary key autoincrement, " +
                    ContactDbSchema.Cols.NAME + ", " +
                    ContactDbSchema.Cols.NUMBER + ")");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}


//        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        //Write num to sharedPreF using name as key
//        TextView name = findViewById(R.id.name_edit_text);
//        TextView number = findViewById(R.id.number_edit_text);
//
//        SharedPreferences pref = getSharedPreferences("Contacts", MODE_PRIVATE);
//        pref.edit()
//                .putString(name.getText()
//                        .toString(), number.getText()
//                        .toString())
//                .apply();


//        public Cursor query (String table,
//                             String[] columns,
//                             String selection,
//                             String[] selectionArgs,
//                             String groupBy,
//                             String having,
//                             String orderBy)

//        Cursor cursor = database.query(
//                ContactDbSchema.TABLE_NAME,
//                null,
//                ContactDbSchema.Cols.NAME + " = ?",
//                 new String[]{nameText},
//                null, null, null);
//
//        )