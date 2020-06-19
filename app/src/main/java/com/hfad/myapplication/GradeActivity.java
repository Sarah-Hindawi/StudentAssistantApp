package com.hfad.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class GradeActivity extends AppCompatActivity {
    private ViewGroup grades, weights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.theme == 0) getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grades = (ViewGroup) findViewById(R.id.grades);
        weights = (ViewGroup) findViewById(R.id.weights);

    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.mainmenu, m);
        return super.onCreateOptionsMenu(m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        onClickAdd();
        return super.onOptionsItemSelected(mi);
    }

    private void onClickAdd() {
        EditText newGrade = new EditText(this);
        EditText newWeight = new EditText(this);

        newGrade.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newWeight.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newGrade.setHint(getResources().getString(R.string.gradeHint));
        newWeight.setHint(getResources().getString(R.string.weightHint));
        grades.addView(newGrade);
        weights.addView(newWeight);
    }


    public void onCalculateClick(View view) {

        int size = grades.getChildCount();
        double[] gradesArray = new double[size];
        double[] weightsArray = new double[size];


        for (int i = 1; i < size; i++) {
            try {
                grades.getChildAt(i);

                EditText gradeText = (EditText) grades.getChildAt(i);
                double gradeNum = Double.parseDouble(gradeText.getText().toString());
                gradesArray[i] = gradeNum;

                EditText weightText = (EditText) weights.getChildAt(i);
                double weightNum = (Double.parseDouble(weightText.getText().toString())) / 100;
                weightsArray[i] = weightNum;

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        double finalGrade = 0;

        for (int i = 0; i < size; i++) {
            finalGrade += gradesArray[i] * weightsArray[i];
        }

        TextView grade = (TextView) findViewById(R.id.actualGrade);
        grade.setText(getResources().getString(R.string.Yourgrade, finalGrade));
    }
}
