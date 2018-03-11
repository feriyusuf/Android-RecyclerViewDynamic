package com.feriyusuf.recycleview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.feriyusuf.recycleview.model.Contact;
import com.feriyusuf.recycleview.recycleradapter.ContactAdapter;
import com.feriyusuf.recycleview.util.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<Contact> contacts;
    private ContactAdapter contactAdapter;

    //Karena datanya dummy
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<>();
        random = new Random();

        //set dummy data
        for (int i = 0; i < 10; i++){
            Contact contact = new Contact();
            contact.setPhone(phoneNumberGenerating());
            contact.setEmail("technoboy"+ i + "promail.com");
            contacts.add(contact);
        }

        //find view by id and attaching adapter for the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_contacts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(recyclerView, contacts, this);
        recyclerView.setAdapter(contactAdapter);

        contactAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(contacts.size() <= 20){
                    contacts.add(null);
                    contactAdapter.notifyItemInserted(contacts.size() -1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contacts.remove(contacts.size() - 1);
                            contactAdapter.notifyItemRemoved(contacts.size());

                            //Generating more data
                            int index = contacts.size();
                            int end = index + 10;

                            for(int i = index; i < end; i++){
                                Contact contact = new Contact();
                                contact.setPhone(phoneNumberGenerating());
                                contact.setEmail("technoboy"+ i + "mailpro.com");
                                contacts.add(contact);
                            }
                            contactAdapter.notifyDataSetChanged();
                            contactAdapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(MainActivity.this, "Loading Data Complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String phoneNumberGenerating() {
        int low = 100000000;
        int high = 999999999;

        int randomNumber = random.nextInt(high - low) + low;
        return "0" + randomNumber;
    }
}
