package de.avalax.fitbuddy.core.workout.basic;


import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Tendency;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.exceptions.ExerciseNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class BasicWorkoutTest {

    private Workout workout;
    private LinkedList<Exercise> exercises;
    private int exerciseIndex;

    @Before
    public void setUp() throws Exception {
        exerciseIndex = 0;
        exercises = new LinkedList<Exercise>();
        workout = new BasicWorkout(exercises);
    }

    @Test
    public void getExerciseCount_shouldReturnCountOfExercises() throws Exception {
        exercises.add(mock(Exercise.class));
        exercises.add(mock(Exercise.class));
        exercises.add(mock(Exercise.class));

        assertThat(workout.getExerciseCount(), equalTo(exercises.size()));
    }

    @Test
    public void getExercise_shouldReturnFistExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        assertThat(workout.getExercise(0), equalTo(exercise));
    }

    @Test
    public void getExercise_shouldReturnSecondExercise() throws Exception {
        int exercisePosition = 1;
        Exercise exercise = mock(Exercise.class);

        exercises.add(mock(Exercise.class));
        exercises.add(exercise);

        assertThat(workout.getExercise(exercisePosition), equalTo(exercise));
    }

    @Test
    public void getCurrentSet_shouldReturnCurrentSet() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        Set set = mock(Set.class);

        when(exercise.getCurrentSet()).thenReturn(set);

        assertThat(workout.getCurrentSet(exerciseIndex), equalTo(set));
    }

    @Test
    public void setReps_shouldSetRepsTo12() throws Exception {

        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        workout.setReps(exerciseIndex, 12);

        verify(exercise).setReps(12);
    }

    @Test
    public void getReps_shouldReturnRepsFromExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        when(exercise.getReps()).thenReturn(12);

        assertThat(workout.getReps(exerciseIndex), equalTo(12));
    }

    @Test
    public void getName_shouldReturnNameFromExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        when(exercise.getName()).thenReturn("NameOfExercise");

        assertThat(workout.getName(exerciseIndex), equalTo("NameOfExercise"));
    }

    @Test
    public void setTendency_shouldSetTendencyInExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.setTendency(exerciseIndex, Tendency.NEUTRAL);

        verify(exercise).setTendency(Tendency.NEUTRAL);
    }

    @Test
    public void incrementSet_shouldIncrementSetFromExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.incrementSet(exerciseIndex);

        verify(exercise).incrementCurrentSet();

    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void getExercise_shouldThrowExerciseNotAvailableExceptionWhenPositionIsGreaterThanExerciseCount() throws Exception {
        int nextExercisePosition = 1;
        exercises.add(mock(Exercise.class));

        workout.getExercise(nextExercisePosition);
    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void getExercise_shouldThrowExceptionWhenSmallerThanExerciseCount() throws Exception {
        int previousExercise = -1;
        exercises.add(mock(Exercise.class));

        workout.getExercise(previousExercise);
    }

    @Test
    public void getProgress_shouldReturnZeroProgress() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        when(exercise.getMaxSets()).thenReturn(3);
        when(exercise.getSetNumber()).thenReturn(1);
        when(exercise.getReps()).thenReturn(0);

        assertThat(workout.getProgress(exerciseIndex), equalTo(0.0f));
    }

    @Test
    public void getProgress_shouldReturnExerciseCount() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));
        exercises.add(exercise);
        when(exercise.getMaxSets()).thenReturn(3);
        when(exercise.getSetNumber()).thenReturn(3);
        when(exercise.getReps()).thenReturn(1);

        assertThat(workout.getProgress(1), equalTo((float) workout.getExerciseCount()));
    }

    @Test
    public void getProgress_shouldReturn1point5() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));
        exercises.add(exercise);
        when(exercise.getMaxSets()).thenReturn(3);
        when(exercise.getSetNumber()).thenReturn(2);
        when(exercise.getReps()).thenReturn(1);

        float countAndProgress = workout.getExerciseCount() - 1 + (2 / 3.0f);
        assertThat(workout.getProgress(1), equalTo(countAndProgress));
    }

    @Test
    public void addExerciseBefore_shouldAddExerciseBeforePosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExerciseBefore(exerciseIndex,exercise);

        assertThat(exercises.size(),is(2));
        assertThat(workout.getExercise(exerciseIndex), equalTo(exercise));
    }

    @Test
    public void addExerciseBefore_shouldAddExerciseAfterPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExerciseAfter(exerciseIndex,exercise);

        assertThat(exercises.size(),is(2));
        assertThat(workout.getExercise(exerciseIndex +1), equalTo(exercise));
    }

    @Test
    public void setExercise_shouldAddExerciseAtPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);

        workout.setExercise(exerciseIndex, exercise);

        assertThat(exercises.size(),is(1));
        assertThat(workout.getExercise(exerciseIndex), equalTo(exercise));
    }

    @Test
    public void setExercise_shouldReplaceExerciseAtPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.setExercise(exerciseIndex, exercise);

        assertThat(exercises.size(),is(1));
        assertThat(workout.getExercise(exerciseIndex), equalTo(exercise));
    }

    @Test
    public void removeExercise_shouldRemoveExerciseAtPosition() throws Exception {
        exercises.add(mock(Exercise.class));

        workout.removeExercise(exerciseIndex);

        assertThat(exercises.size(), is(0));
    }

    @Test
    public void removeExercise_shouldRemoveLastOfTwoExercisesAtPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.removeExercise(1);

        assertThat(exercises.size(), is(1));
        assertThat(exercises.get(0),equalTo(exercise));
    }

    @Test
    public void removeExercise_shouldRemoveFirstOfTwoExercisesAtPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));
        exercises.add(exercise);

        workout.removeExercise(0);

        assertThat(exercises.size(), is(1));
        assertThat(exercises.get(0),equalTo(exercise));
    }

    @Test
    public void removeExercise_shouldDoNothingWhenIndexIsOutOfBounce() throws Exception {
        workout.removeExercise(0);
    }
}