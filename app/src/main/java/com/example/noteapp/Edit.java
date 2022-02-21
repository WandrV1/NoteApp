package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Edit extends AppCompatActivity {

    Toolbar toolbar;
    EditText noteTitle;
    EditText noteContent;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        id = i.getLongExtra("id",0);
        Database database = new Database(this);
        Note note = database.getNoteById(id);

        final String title = note.getTitle();
        String content = note.getContent();
        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getSupportActionBar().setTitle(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        noteTitle.setText(title);
        noteContent.setText(content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (noteTitle.getText().length() != 0) {
                Note note = new Note(id, noteTitle.getText().toString(), noteContent.getText().toString());
                Database database = new Database(getApplicationContext());
                database.updateNote(note);
                goToMain();
                Toast.makeText(this, "Note edited", Toast.LENGTH_SHORT).show();
            } else {
                noteTitle.setError("Title cannot be blank");
            }
        } else if (item.getItemId() == R.id.delete) {
            Database database = new Database(getApplicationContext());
            database.deleteNoteById(id);
            goToMain();
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}