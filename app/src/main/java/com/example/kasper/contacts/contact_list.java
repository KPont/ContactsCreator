package com.example.kasper.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class contact_list extends AppCompatActivity {

    private ListView lv;
    //private List<Contact> allcontacts= new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ArrayList<String> allContacts = new ArrayList<String>();
        lv = (ListView) findViewById(R.id.contacList);


        ContentResolver cr = this.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {

            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String name = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        String phoneNumber = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String StringContact = name + "\n" + phoneNumber + "\n";



                        Cursor pCur2 = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);


                            if(pCur2 != null) {

                                while (pCur2.moveToNext()) {

                                    String address = pCur2
                                            .getString(pCur2
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                                    StringContact += address+"\n";
                                    break;
                                }
                            pCur2.close();
                        }
                        StringContact +="\n";
                        allContacts.add(StringContact);
                        break;

                    }
                    pCur.close();


                }

            } while (cursor.moveToNext()) ;
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                allContacts );

        lv.setAdapter(arrayAdapter);
    }

}
