package com.hfad.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NoteContentsActivity extends AppCompatActivity {

    private static final String CHOSEN = "chosen";
    private int noteId;
    private SQLiteDatabase db;
    private String contents;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.theme == 0) getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_contents);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteId = (Integer) getIntent().getExtras().get(CHOSEN);

        SQLiteOpenHelper oh = new SQL(this);
        db = oh.getReadableDatabase();
        Cursor c = db.query("NOTES", new String[]{"TITLE", "CONTENTS"},
                "_id=?", new String[]{Integer.toString(noteId)}, null,
                null, null);
        c.moveToFirst();

        Log.d("here", Integer.toString(noteId));

        String n = c.getString(0);
        contents = c.getString(1);
        c.close();
        db.close();

        TextView title = (TextView) findViewById(R.id.tv1);
        title.setText(n);

        TextView content = (TextView) findViewById(R.id.tv2);
        content.setText(contents);
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.notesmenu, m);
        MenuItem mi = m.findItem(R.id.share);
        shareActionProvider = (androidx.appcompat.widget.ShareActionProvider) MenuItemCompat.getActionProvider(mi);
        setShareAction();
        return super.onCreateOptionsMenu(m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (mi.getItemId() == R.id.edit) {
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra("id", noteId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, NotesActivity.class);
            intent.putExtra("id", noteId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(mi);
    }

    private void setShareAction() {
        Intent in = new Intent(Intent.ACTION_SEND);
        in.setType("text/plain");
        in.putExtra(Intent.EXTRA_TEXT, contents);
        shareActionProvider.setShareIntent(in);
    }
}