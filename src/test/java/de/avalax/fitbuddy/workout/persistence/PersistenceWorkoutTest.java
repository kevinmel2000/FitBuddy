package de.avalax.fitbuddy.workout.persistence;

import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersistenceWorkoutTest {

    private DataLayer dataLayer;
    private Workout persistenceWorkout;
    private Workout workout;
    private int exercisePosition;

    @Before
    public void setUp() {
        exercisePosition = 0;
        dataLayer = mock(DataLayer.class);
        workout = mock(Workout.class);
        when(dataLayer.load()).thenReturn(workout);
        persistenceWorkout = new PersistenceWorkout(dataLayer);
    }

    @Test
    public void persistenceWorkout_shouldCallCreatePersistenceWorkout() throws Exception {
        verify(dataLayer).load();
    }

    @Test
    public void getExerciseCount_shouldCallGetExerciseCountFromWorkout() throws Exception {
        persistenceWorkout.getExerciseCount();

        verify(workout).getExerciseCount();
    }

    @Test
    public void getExercise_shouldCallGetExerciseFromWorkout() throws Exception {
        persistenceWorkout.getExercise(exercisePosition);

        verify(workout).getExercise(exercisePosition);
    }

    @Test
    public void getCurrentSet_shouldCallGetCurrentSetFromWorkout() throws Exception {
        persistenceWorkout.getCurrentSet(exercisePosition);

        verify(workout).getCurrentSet(exercisePosition);
    }

    @Test
    public void getReps_shouldCallGetRepsFromWorkout() throws Exception {
        persistenceWorkout.getReps(exercisePosition);

        verify(workout).getReps(exercisePosition);
    }

    @Test
    public void setReps_shouldCallSetRepsFromWorkout() throws Exception {
        int repsCount = 12;
        persistenceWorkout.setReps(exercisePosition, repsCount);

        verify(workout).setReps(exercisePosition, repsCount);
    }

    @Test
    public void setTendency_shouldCallSetTendencyFromWorkout() throws Exception {
        persistenceWorkout.setTendency(exercisePosition, Tendency.MINUS);

        verify(workout).setTendency(exercisePosition, Tendency.MINUS);
    }
}
