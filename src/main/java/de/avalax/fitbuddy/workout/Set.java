package de.avalax.fitbuddy.workout;

public interface Set {
    double getWeight();
    void setRepetitions(int repetitions);
    int getRepetitions();

		void setMaxRepetitions(int maxRepetitions);

		int getMaxRepetition();
}