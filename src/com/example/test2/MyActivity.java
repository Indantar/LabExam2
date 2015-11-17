package com.example.test2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.xmlpull.v1.XmlPullParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    SQLiteDatabase db;
    TextView txtMsg;
    Button btnGoParser;
    Button btnPrintDB;
    Button btnDelete;
    Button btnCh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        txtMsg = (TextView) findViewById(R.id.textView);
        btnPrintDB = (Button) findViewById(R.id.printDB);
        btnGoParser = (Button) findViewById(R.id.btnReadXml);
        btnDelete = (Button) findViewById(R.id.button);
        btnCh = (Button) findViewById(R.id.button2);
        btnGoParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoParser.setEnabled(false);
                // do slow XML reading in a separated thread
                Integer xmlResFile = R.xml.employee;
                new backgroundAsyncTask().execute(xmlResFile);
            }
        });
        btnPrintDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openDatabase();
                try {
                    String sql = "select * from tableD" ;
                    Cursor c = db.rawQuery(sql, null);
                    txtMsg.append("\n-showTable: tableD " + showCursor(c) );

                } catch (Exception e) {
                    txtMsg.append("\nError showTable: " + e.getMessage());

                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDatabase();
                dropTable();
            }
        });
        btnCh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDatabase();
                useUpdateMethod();
            }
        });
    }
    private void useUpdateMethod() {
        try {
            // using the 'update' method to change name of selected friend
            String[] whereArgs = { "1" };

            ContentValues updValues = new ContentValues();
            updValues.put("FNAME", "Dawid");

            int recAffected = db.update("tableD", updValues,
                    "recID = ? ", whereArgs);

            txtMsg.append("\n-useUpdateMethod - Rec Affected " + recAffected);

        } catch (Exception e) {
            txtMsg.append("\n-useUpdateMethod - Error: " + e.getMessage() );
        }
    }
    private void dropTable() {
        // (clean start) action query to drop table

        try {
            db.execSQL("DROP TABLE IF EXISTS tableD;");
            txtMsg.append("\n-dropTable - dropped!!");
        } catch (Exception e) {
            txtMsg.append("\nError dropTable: " + e.getMessage());
            finish();
        }
    }
    private String getColumnType(Cursor cursor, int i) {
        try {
            //peek at a row holding valid data
            cursor.moveToFirst();
            int result = cursor.getType(i);
            String[] types = {":NULL", ":INT", ":FLOAT", ":STR", ":BLOB", ":UNK" };
            //backtrack - reset cursor's top
            cursor.moveToPosition(-1);
            return types[result];
        } catch (Exception e) {
            return " ";
        }
    }
    private String showCursor( Cursor cursor) {
        // show SCHEMA (column names & types)
        cursor.moveToPosition(-1); //reset cursor's top
        String cursorData = "\nCursor: [";

        try {
            // get column names
            String[] colName = cursor.getColumnNames();
            for(int i=0; i<colName.length; i++){
                String dataType = getColumnType(cursor, i);
                cursorData += colName[i] + dataType;

                if (i<colName.length-1){
                    cursorData+= ", ";
                }
            }
        } catch (Exception e) {
            Log.e( "<<SCHEMA>>" , e.getMessage() );
        }
        cursorData += "]";

        // now get the rows
        cursor.moveToPosition(-1); //reset cursor's top
        while (cursor.moveToNext()) {
            String cursorRow = "\n[";
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                cursorRow += cursor.getString(i);
                if (i<cursor.getColumnCount()-1)
                    cursorRow +=  ", ";
            }
            cursorData += cursorRow + "]";
        }
        return cursorData + "\n";
    }
    private void useDeleteMethod() {
        // using the 'delete' method to remove a group of friends
        // whose id# is between 2 and 7

        try {
            String[] whereArgs = { "1","2","3","4","5","6","7" };

            int recAffected = db.delete("tableD", "recID = ?", whereArgs);

            txtMsg.append("\n-useDeleteMethod - Rec affected " + recAffected);

        } catch (Exception e) {
            txtMsg.append("\n-useDeleteMethod - Error: " + e.getMessage());
        }
    }
    private void openDatabase() {
        try {
            // path to private memory:
            //String SDcardPath = "data/data/example.test2";
            // -----------------------------------------------------------
            // this provides the path name to the SD card
            String SDcardPath = Environment.getExternalStorageDirectory().getPath();

            String myDbPath = SDcardPath + "/database2.db";
            txtMsg.append("\n-openDatabase - DB Path: " + myDbPath);

            db = SQLiteDatabase.openDatabase(myDbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            txtMsg.append("\n-openDatabase - DB was opened");
        } catch (SQLiteException e) {
            txtMsg.append("\nError openDatabase: " + e.getMessage());
            finish();
        }
    }// createDatabase

    private void insertSomeDbData(String[][] e) {
        // create table: table
        db.beginTransaction();
        try {
            //create table
            db.execSQL("create table tableD ("
                    + " recID integer PRIMARY KEY autoincrement, "
                    + " FNAME text, " + " MINIT text," + " LNAME text, " + " SSN text,"
                    + " BDATE text, " + " ADDRESS text," + "SEX text, "+ "SALARY text,"
                    + " SUPERSSN text," + "DNO text);");
            // commit your changes
            db.setTransactionSuccessful();

            txtMsg.append("\n-insertSomeDbData - Table was created");

        } catch (SQLException e1) {
            txtMsg.append("\nError insertSomeDbData: " + e1.getMessage());
            finish();
        } finally {
            db.endTransaction();
        }
        db.beginTransaction();
        try {
            for(int x = 0; x < 8;x++)
            {
                ContentValues val = new ContentValues();
                val.put("FNAME", e[x][1]);
                val.put("MINIT", e[x][2]);
                val.put("LNAME", e[x][3]);
                val.put("SSN", e[x][4]);
                val.put("BDATE", e[x][5]);
                val.put("ADDRESS", e[x][6]);
                val.put("SEX", e[x][7]);
                val.put("SALARY", e[x][8]);
                val.put("SUPERSSN", e[x][9]);
                val.put("DNO", e[x][10]);
                db.insert("tableD",null,val);
            }
            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
            db.setTransactionSuccessful();
        } catch (SQLiteException e1) {
            txtMsg.append("\nError insertSomeDbData: " + e1.getMessage());
            finish();
        } finally {
            db.endTransaction();
        }
    }

    public class backgroundAsyncTask extends AsyncTask<Integer, Void, String[][]> {
        ProgressDialog dialog = new ProgressDialog(MyActivity.this);

        @Override
        protected void onPostExecute(String[][] result) {
            super.onPostExecute(result);
            dialog.dismiss();
            //txtMsg.setText(result.toString());
            openDatabase();
            insertSomeDbData(result);
            db.close();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String[][] doInBackground(Integer... params) {
            int xmlResFile = params[0];
            XmlPullParser parser = getResources().getXml(xmlResFile);
            int i = 0;
            String[][] e = new String[8][11];
            String nodeText = "";
            String nodeName = "";
            try {
                int eventType = -1;
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    eventType = parser.next();

                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        //stringBuilder.append("\nSTART_DOCUMENT");

                    } else if (eventType == XmlPullParser.END_DOCUMENT) {
                        //stringBuilder.append("\nEND_DOCUMENT");

                    } else if (eventType == XmlPullParser.START_TAG) {
                        nodeName = parser.getName();
                        //stringBuilder.append("\nSTART_TAG: " + nodeName);
                        //stringBuilder.append(getAttributes(parser));
                        if (nodeName == "EMPLOYEE") {
                            i++;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        nodeName = parser.getName();
                        switch (nodeName) {
                            case "FNAME":
                                e[i][1] = nodeText;
                                break;
                            case "MINIT":
                                e[i][2] = nodeText;
                                break;
                            case "LNAME":
                                e[i][3] = nodeText;
                                break;
                            case "SSN":
                                e[i][4] = nodeText;
                                break;
                            case "BDATE":
                                e[i][5] = nodeText;
                                break;
                            case "ADDRESS":
                                e[i][6] = nodeText;
                                break;
                            case "SEX":
                                e[i][7] = nodeText;
                                break;
                            case "SALARY":
                                e[i][8] = nodeText;
                                break;
                            case "SUPERSSN":
                                e[i][9] = nodeText;
                                break;
                            case "DNO":
                                e[i][10] = nodeText;
                                break;
                        }
                        //stringBuilder.append("\nEND_TAG:   " + nodeName );

                    } else if (eventType == XmlPullParser.TEXT) {
                        nodeText = parser.getText();
                        //stringBuilder.append("\n    TEXT: " + nodeText);
                    }
                }
            } catch (Exception e1) {
                Log.e("<<PARSING ERROR>>", e1.getMessage());

            }

            return e;
        }// doInBackground

        private String getAttributes(XmlPullParser parser) {
            StringBuilder stringBuilder = new StringBuilder();
            // trying to detect inner attributes nested inside a node tag
            String name = parser.getName();
            if (name != null) {
                int size = parser.getAttributeCount();

                for (int i = 0; i < size; i++) {
                    String attrName = parser.getAttributeName(i);
                    String attrValue = parser.getAttributeValue(i);
                    stringBuilder.append("\n    Attrib <key,value>= "
                            + attrName + ", " + attrValue);
                }
            }// innerElements
            return stringBuilder.toString();
        }
    }
}
// backroundAsyncTask

