package de.avalax.fitbuddy.application.edit.workout;

import android.util.Log;
import android.view.View;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.*;

import java.util.List;

public class ManageWorkout {

    private WorkoutRepository workoutRepository;

    private ExerciseRepository exerciseRepository;

    private SetRepository setRepository;

    private WorkoutService workoutService;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Workout workout;

    private Workout deletedWorkout;

    private Exercise deletedExercise;

    private Integer deletedExerciseIndex;

    public ManageWorkout(WorkoutSession workoutSession, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, SetRepository setRepository, WorkoutService workoutService) {
        this.workoutSession = workoutSession;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
        this.workoutService = workoutService;
    }

    private void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public int unsavedChangesVisibility() {
        return unsavedChanges ? View.VISIBLE : View.GONE;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutId id) throws WorkoutNotFoundException {
        this.workout = workoutRepository.load(id);
    }

    public void switchWorkout() {
        workoutSession.switchWorkout(workout);
        setUnsavedChanges(false);
    }

    public List<WorkoutListEntry> getWorkoutList() {
        return workoutRepository.getWorkoutList();
    }

    public Workout createWorkout() {
        workout = new BasicWorkout();
        workoutRepository.save(workout);
        setUnsavedChanges(false);
        return workout;
    }

    public void createWorkoutFromJson(String json) throws WorkoutParseException {
        Workout workoutFromJson = workoutService.workoutFromJson(json);
        if (workoutFromJson != null) {
            workout = workoutFromJson;
            workoutRepository.save(workoutFromJson);
            setUnsavedChanges(false);
        }
    }

    public void deleteWorkout() {
        if (workout == null) {
            return;
        }
        workoutRepository.delete(workout.getWorkoutId());
        deletedExercise = null;
        setUnsavedChanges(workout);
        workout = null;
    }

    private void setUnsavedChanges(Workout workout) {
        deletedWorkout = workout;
        setUnsavedChanges(true);
    }

    private void setUnsavedChanges(int index, Exercise exercise) {
        this.deletedExerciseIndex = index;
        this.deletedExercise = exercise;
        setUnsavedChanges(true);
    }

    public void undoDeleteExercise() {
        workout.addExercise(deletedExerciseIndex, deletedExercise);
        exerciseRepository.save(workout.getWorkoutId(), deletedExerciseIndex, deletedExercise);
        deletedExerciseIndex = null;
        deletedExercise = null;
        setUnsavedChanges(false);
    }

    public void undoDeleteWorkout() {
        workout = deletedWorkout;
        workoutRepository.save(deletedWorkout);
        deletedWorkout = null;
        setUnsavedChanges(false);
    }

    public void deleteExercise(Exercise exercise, int position) {
        exerciseRepository.delete(exercise.getExerciseId());
        workout.deleteExercise(exercise);
        setUnsavedChanges(position, exercise);
        deletedWorkout = null;
    }

    public void saveExercise(Exercise exercise, int position) {
        workout.replaceExercise(exercise);
        exerciseRepository.save(workout.getWorkoutId(), position, exercise);
        setUnsavedChanges(false);
    }

    public void createExercise() {
        Exercise exercise = workout.createExercise();
        exerciseRepository.save(workout.getWorkoutId(), workout.countOfExercises() - 1, exercise);
        setUnsavedChanges(false);
    }

    public void createExerciseBefore(int position) {
        workout.createExercise(position);
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public void createExerciseAfter(int position) {
        workout.createExercise(position + 1);
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public boolean hasDeletedWorkout() {
        return deletedWorkout != null;
    }

    public boolean hasDeletedExercise() {
        return deletedExercise != null;
    }

    public void changeName(String name) {
        workout.setName(name);
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public void replaceSets(Exercise exercise, List<Set> setToAdd) {
        for (int i = 0; i < exercise.countOfSets(); i++) {
            try {
                Set set = exercise.setAtPosition(i);
                setRepository.delete(set.getSetId());
                exercise.removeSet(set);
            } catch (SetNotAvailableException e) {
                Log.d("Can't delete set", e.getMessage(), e);
            }
        }
        for (Set set : setToAdd) {
            setRepository.save(exercise.getExerciseId(), set);
            exercise.addSet(set);
        }
        setUnsavedChanges(false);
    }
}
