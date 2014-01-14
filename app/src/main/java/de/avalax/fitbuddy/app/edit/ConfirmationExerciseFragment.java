package de.avalax.fitbuddy.app.edit;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.avalax.fitbuddy.app.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class ConfirmationExerciseFragment extends RoboFragment {
    private static final String EDITABLE_EXERCISE = "editableExercise";
    @InjectView(R.id.exerciseNameEditText)
    private EditText exerciseNameEditText;
    @InjectView(R.id.buttonCancel)
    private Button buttonCancel;
    @InjectResource(R.string.new_exercise_name)
    private String newExerciseName;
    @InjectResource(R.string.action_delete)
    private String buttonDeleteExerciseText;
    private EditableExercise editableExercise;
    private ViewPager viewPager;

    public static ConfirmationExerciseFragment newInstance(EditableExercise editableExercise) {
        ConfirmationExerciseFragment confirmationExerciseFragment = new ConfirmationExerciseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EDITABLE_EXERCISE, editableExercise);
        confirmationExerciseFragment.setArguments(args);
        return confirmationExerciseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        editableExercise = (EditableExercise) getArguments().getSerializable(EDITABLE_EXERCISE);
        return inflater.inflate(R.layout.fragment_edit_confirmation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        if (!newExerciseName.equals(editableExercise.getName())) {
            exerciseNameEditText.setText(editableExercise.getName());
        }
        if (editableExercise instanceof ExistingEditableExercise) {
            buttonCancel.setText(buttonDeleteExerciseText);
        }
        exerciseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editableExercise.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewPager.getAdapter().notifyDataSetChanged();
                viewPager.invalidate();
            }
        });
    }
}
