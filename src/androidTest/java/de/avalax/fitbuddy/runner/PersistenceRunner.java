package de.avalax.fitbuddy.runner;

import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder;
import de.avalax.fitbuddy.domain.model.workout.Workout;

public class PersistenceRunner {
    private FitbuddyActivityTestRule activityRule;

    public PersistenceRunner(FitbuddyActivityTestRule activityRule) {
        this.activityRule = activityRule;
    }

    public Workout addWorkout(BasicWorkoutBuilder builder) {
        Workout workout = builder.build();
        activityRule.editWorkoutApplicationService.saveWorkout(workout);
        return workout;
    }

    public void finishWorkout(Workout workout) throws ResourceException {
        activityRule.workoutApplicationService.switchWorkout(workout);
        activityRule.workoutApplicationService.finishCurrentWorkout();
    }

    public void deleteWorkouts() throws ResourceException {
        List<Workout> workouts = activityRule.editWorkoutApplicationService.loadAllWorkouts();
        for (Workout workout : workouts) {
            activityRule.editWorkoutApplicationService.deleteWorkout(workout);
        }

        if (activityRule.workoutApplicationService.hasActiveWorkout()) {
            activityRule.workoutApplicationService.finishCurrentWorkout();
        }
    }

    public void deleteFinishedWorkouts() {
        List<FinishedWorkout> workouts = activityRule.finishedWorkoutApplicationService.loadAllFinishedWorkouts();
        for (FinishedWorkout finishedWorkout : workouts) {
            activityRule.finishedWorkoutApplicationService.delete(finishedWorkout);
        }
    }
}