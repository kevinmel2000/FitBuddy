package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSetRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SQLiteFinishedExerciseRepositoryTest {
    private FinishedExerciseRepository finishedExerciseRepository;
    private FinishedWorkoutId finishedWorkoutId;
    private Exercise exercise;

    private void createWorkout(FitbuddySQLiteOpenHelper sqLiteOpenHelper) {
        SetRepository setRepository = new SQLiteSetRepository(sqLiteOpenHelper);
        ExerciseRepository exerciseRepository = new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);
        WorkoutRepository workoutRepository = new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
        FinishedWorkoutRepository finishedWorkoutRepository = new SQLiteFinishedWorkoutRepository(
                sqLiteOpenHelper, finishedExerciseRepository, workoutRepository);
        Workout workout = new BasicWorkout();
        workoutRepository.save(workout);
        finishedWorkoutId = finishedWorkoutRepository.saveWorkout(workout);
    }

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteSetRepositoryTest", 1, context, R.raw.fitbuddy_db);
        FinishedSetRepository finishedSetRepository = new SQLiteFinishedSetRepository(sqLiteOpenHelper);
        finishedExerciseRepository = new SQLiteFinishedExerciseRepository(sqLiteOpenHelper, finishedSetRepository);
        createWorkout(sqLiteOpenHelper);
        exercise = new BasicExercise();
    }

    @Test
    public void saveExercise_shouldInsertExerciseInformation() throws Exception {
        exercise.setName("new exercise name");
        finishedExerciseRepository.save(finishedWorkoutId, exercise);

        List<FinishedExercise> finishedExercises = finishedExerciseRepository.allExercisesBelongsTo(finishedWorkoutId);

        assertThat(finishedExercises, hasSize(1));
        assertThat(finishedExercises.get(0).getName(), equalTo("new exercise name"));
    }

    @Test
    public void saveExercise_shouldInsertExerciseWithAllSets() throws Exception {
        Set set1 = exercise.getSets().createSet();
        set1.setMaxReps(15);
        set1.setReps(12);
        set1.setWeight(42L);
        Set set2 = exercise.getSets().createSet();
        set2.setMaxReps(20);
        set2.setReps(10);
        set2.setWeight(21L);
        finishedExerciseRepository.save(finishedWorkoutId, exercise);

        List<FinishedExercise> finishedExercises = finishedExerciseRepository.allExercisesBelongsTo(finishedWorkoutId);

        assertThat(finishedExercises, hasSize(1));
        assertThat(finishedExercises.get(0).getSets(), hasSize(2));
        assertThat(finishedExercises.get(0).getSets().get(0).getWeight(), equalTo(set1.getWeight()));
        assertThat(finishedExercises.get(0).getSets().get(0).getReps(), equalTo(set1.getReps()));
        assertThat(finishedExercises.get(0).getSets().get(0).getMaxReps(), equalTo(set1.getMaxReps()));
        assertThat(finishedExercises.get(0).getSets().get(1).getWeight(), equalTo(set2.getWeight()));
        assertThat(finishedExercises.get(0).getSets().get(1).getReps(), equalTo(set2.getReps()));
        assertThat(finishedExercises.get(0).getSets().get(1).getMaxReps(), equalTo(set2.getMaxReps()));
    }
}