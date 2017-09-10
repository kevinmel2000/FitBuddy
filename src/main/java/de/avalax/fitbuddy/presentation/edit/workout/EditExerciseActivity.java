package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.BasicSets;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.Sets;

public class EditExerciseActivity extends AppCompatActivity {

    private static final int EDIT_SET = 3;
    private EditText nameEditText;
    private Sets sets;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_exercise);
        setSupportActionBar(toolbar);
        nameEditText = findViewById(R.id.edit_text_exercise_name);
        sets = new BasicSets(new ArrayList<>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_exercise, menu);

        return true;
    }

    public void onAddSetButtonClick(View view) {
        Intent intent = new Intent(this, EditSetActivity.class);
        startActivityForResult(intent, EDIT_SET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_SET && resultCode == Activity.RESULT_OK) {
            int reps = data.getIntExtra("reps", 0);
            double weight = data.getDoubleExtra("weight", 0);
            Set set = sets.createSet();
            set.setMaxReps(reps);
            set.setWeight(weight);

            EditExerciseFragment workoutListFragment = (EditExerciseFragment)
                    getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
            workoutListFragment.addSet(set);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_exercise) {
            Intent intent = new Intent();
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("sets", sets);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }
}