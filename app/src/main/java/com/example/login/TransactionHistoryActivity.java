package com.example.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransactionHistoryActivity extends AppCompatActivity {

    RecyclerView rvTransaction;
    SearchView svTransaction;
    Spinner spinnerSearches;
    LinkedList<Transaction> transactions;
    ArrayList<String> searches;
    int searchPosition;
    TransactionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_history);

        rvTransaction = findViewById(R.id.rvTransaction);
        svTransaction = findViewById(R.id.svTransaction);
        spinnerSearches = findViewById(R.id.spinnerSearches);

        transactions = loadData();
        loadSearches();

        adapter = new TransactionAdapter(transactions, searchPosition);
        rvTransaction.setAdapter(adapter);

        ArrayAdapter<String> searchesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searches);
        searchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearches.setAdapter(searchesAdapter);

        // setting default value for the spinnerSearches
        spinnerSearches.setSelection(0);

        // setting the on click item listener for the selected spinner item
        spinnerSearches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchPosition = position;
                svTransaction.setQueryHint("Search By " + searches.get(position));


                // setting the adapter for the recyclerview again after searchPosition is updated
                adapter = new TransactionAdapter(transactions, searchPosition);
                rvTransaction.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rvTransaction.setLayoutManager(new LinearLayoutManager(this));


        svTransaction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void loadSearches() {
        searches = new ArrayList<>();

        searches.add("category");
        searches.add("amount");
        searches.add("date");
    }

    private LinkedList<Transaction> loadData() {
        transactions = new LinkedList<>();

        // Get the Firebase Database reference
        DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference("transactions");

        // Add a listener to read data from Firebase
        transactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transactions.clear();  // Clear the current list to avoid duplication

                // Iterate through the transactions in the database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = snapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }

                // Notify the adapter of data changes to update the RecyclerView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransactionHistoryActivity.this, "Failed to load transactions.", Toast.LENGTH_SHORT).show();
            }
        });

        return transactions;
    }

    public void switchAnalysisActivity(View view) {
        Intent intent = new Intent(this, AnalysisActivity.class);
        startActivity(intent);
    }
}