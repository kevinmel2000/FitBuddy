package de.avalax.fitbuddy.app;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import de.avalax.fitbuddy.app.edit.WorkoutAdapter;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;

public class ManageWorkoutActivity extends ListActivity implements ActionBar.OnNavigationListener {
    @Inject
    protected WorkoutSession workoutSession;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FitbuddyApplication) getApplication()).inject(this);
        initActionBar();
        initListView();
    }

    private void initListView() {
        //TODO: select from dropdown
        Workout workout = workoutSession.getWorkout();
        setListAdapter(WorkoutAdapter.newInstance(getApplication(), R.layout.row, workout));
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getWorkouts());

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        //TODO: select current workout
        actionBar.setSelectedNavigationItem(0);
    }

    private String[] getWorkouts() {
        //TODO: fill dropdown with workouts
        return new String[] {
                "Workout A",
                "Workout B",
                "Workout C"};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //TODO: dynamic data from workouts
        inflater.inflate(R.menu.manage_workout_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        //TODO: onNavigationItemSelected
        return true;
    }
}