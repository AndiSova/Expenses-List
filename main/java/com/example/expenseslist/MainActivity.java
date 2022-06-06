package com.example.expenseslist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private ChildEventListener childEventListener;

    private ArrayList<Expense> expenseList;

    private ExpenseAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;

        switch (item.getItemId())
        {
            case R.id.search_item:
                intent = new Intent(this, SearchActivity.class );
                startActivity(intent);
                return true;
            case R.id.add_item:
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.stats_item:
                intent = new Intent(this, StatisticsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Expenses");

        expenseList = new ArrayList<Expense>();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrayAdapter.add(dataSnapshot.getValue(Expense.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addChildEventListener(childEventListener);

        arrayAdapter = new ExpenseAdapter(this, expenseList);
        ListView results = (ListView) findViewById(R.id.listViewResults);
        results.setAdapter(arrayAdapter);


        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedItem = (Expense) parent.getItemAtPosition(position);

                Bundle b = new Bundle();
                b.putString("uid", selectedItem.getUid());
                b.putString("item", selectedItem.getItem());
                b.putString("date", selectedItem.getDate());
                b.putString("price", selectedItem.getPrice());
                b.putString("quantity", selectedItem.getQuantity());

                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                intent.putExtras(b);
                startActivity(intent);


            }
        });

        results.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                final int position = i;
                final AdapterView<?> p = parent;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int j) {
                                        Expense selectedItem = (Expense) p.getItemAtPosition(position);

                                        myRef.child(selectedItem.getUid()).removeValue();

                                        expenseList.remove(selectedItem);

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}
