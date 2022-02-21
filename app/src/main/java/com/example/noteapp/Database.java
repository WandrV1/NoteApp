package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotesDB";
    private static final String DATABASE_TABLE = "Notes";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DATABASE_TABLE +
                               " (" + ID + " INTEGER PRIMARY KEY, " +
                               "" +TITLE + " TEXT, " + CONTENT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i >= i1) {
            return;
        }
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public long saveNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(CONTENT, note.getContent());

        long id = database.insert(DATABASE_TABLE, null, values);
        Log.d("Inserted", "ID -> " + id);
        return id;
    }

    public Note getNoteById(long id){
        SQLiteDatabase database = this.getReadableDatabase();
        String[] query = new String[] {ID, TITLE, CONTENT};
        Cursor cursor = database.query(DATABASE_TABLE, query, ID+"=?",
                                       new String[]{String.valueOf(id)},
                                      null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new Note(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
    }

    public List<Note> getAll() {
        SQLiteDatabase database = this.getReadableDatabase();
        List <Note> notes = new ArrayList<>();
        String query = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + ID + " DESC";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        return notes;
    }

    public long updateNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(CONTENT, note.getContent());

        return database.update(DATABASE_TABLE, values,
                              ID + "=?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNoteById(long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

}
