package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Database database = new Database(this);
        notes = database.getAll();
        recyclerView = findViewById(R.id.notesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Toast.makeText(this, "Add new note", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, AddNote.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.joke) {
            generateJoke();
        }
        if (item.getItemId() == R.id.map) {
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateJoke() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://v2.jokeapi.dev/joke/Programming?type=single", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String joke = response.getString("joke");
                            Note note = new Note("Random joke", joke);
                            Database database = new Database(MainActivity.this);
                            long noteId = database.saveNote(note);
                            if (noteId != -1)
                            {
                                Intent i = new Intent(MainActivity.this, Detail.class);
                                i.putExtra("id", noteId);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}