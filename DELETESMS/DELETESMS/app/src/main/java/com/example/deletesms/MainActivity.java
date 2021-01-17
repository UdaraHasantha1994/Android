package com.example.deletesms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button DelSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DelSMS = findViewById(R.id.delete_btn);
        DelSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //In here, I have sent a SMS from +94712467989 in the body I have written DELETE.
                // I want to delete this SMS from the
                //device when I clicked this button.
                Delete_SMS(getApplicationContext(),"DELETE","+94712467989");

            }
        });
    }


    private void Delete_SMSs(Context context, String message, String number){
        try {
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = context.getContentResolver().query(
                    uriSms,
                    new String[] { "_id", "thread_id", "address", "person",
                            "date", "body" }, "read=0", null, null);
            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);
                    String date = c.getString(3);
                    Log.e("log>>>",
                            "0--->" + c.getString(0) + "1---->" + c.getString(1)
                                    + "2---->" + c.getString(2) + "3--->"
                                    + c.getString(3) + "4----->" + c.getString(4)
                                    + "5---->" + c.getString(5));
                    Log.e("log>>>", "date" + c.getString(0));
                    ContentValues values = new ContentValues();
                    values.put("read", false);
                    getContentResolver().update(Uri.parse("content://sms/"),
                            values, "_id=" + id, null);

                    if (message.equals(body) && address.equals(number)) {
                        // mLogger.logInfo("Deleting SMS with id: " + threadId);
                        context.getContentResolver().delete(
                                Uri.parse("content://sms/" + id), "date=?",
                                new String[] { c.getString(4) });
                        Log.e("log>>>", "Delete success.........");
                        Toast.makeText(context,"The SMS has been deleted",Toast.LENGTH_LONG).show();
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("log>>>", e.toString());
        }
    }
}