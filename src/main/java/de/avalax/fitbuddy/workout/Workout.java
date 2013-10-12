package de.avalax.fitbuddy.workout;

import com.google.inject.ImplementedBy;
import de.avalax.fitbuddy.workout.persistence.PersistenceWorkout;

@ImplementedBy(PersistenceWorkout.class)
public interface Workout {
    Exercise getExercise(int position);
    int getExerciseCount();
    int getReps(int position);
    void setReps(int position, int reps);
    void setTendency(int position, Tendency tendency);
    Set getCurrentSet(int exercisePosition);
}
