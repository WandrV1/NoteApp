package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Detail extends AppCompatActivity {

    long id;
    Database database;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        id = i.getLongExtra("id", 0);
        database = new Database(this);
        note = database.getNoteById(id);
        getSupportActionBar().setTitle(note.getTitle());
        TextView details = findViewById(R.id.noteDesc);
        details.setText(note.getContent());
        details.setMovementMethod(new ScrollingMovementMethod());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent i = new Intent(this, Edit.class);
            i.putExtra("id", note.getId());
            startActivity(i);
        } else if (item.getItemId() == R.id.delete) {
            database.deleteNoteById(id);
            Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            goToMain();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
