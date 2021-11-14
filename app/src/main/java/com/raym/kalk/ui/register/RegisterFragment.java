package com.raym.kalk.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.raym.kalk.R;
import com.raym.kalk.models.Course;
import com.raym.kalk.models.KalkDataManager;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    public RegisterFragment(){

    }

    private EditText mCourseCodeEditText;
    private EditText mCreditUnitEditText;
    private Course mSingleCourse;
    private String mCourseCode;
    private int mCreditUnit;
    private final ArrayList<Course> mCoursesArrayList = new ArrayList<>();
    static final String EMPTY = "";
    private AppCompatSpinner mAppCompatSpinner;
    private int mPosition;
    private ArrayAdapter<Course> mCourseArrayAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate our layout
        View root = inflater.inflate(R.layout.fragment_register_course, container, false);

        //variable declaration
        mCourseCodeEditText = root.findViewById(R.id.editTxt_course_code);
        mCreditUnitEditText = root.findViewById(R.id.editTxt_credit_unit);
        Button mRemoveCourseButton = root.findViewById(R.id.remove_course);
        Button mAddCourseButton = root.findViewById(R.id.button_add_course);
        mAppCompatSpinner = root.findViewById(R.id.spinner_remove_course);

        //make the edit text request focus
        mCourseCodeEditText.requestFocus();

        //setup our spinner
        mCourseArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, KalkDataManager.getInstance().getCourseArrayList());
        mCourseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAppCompatSpinner.setAdapter(mCourseArrayAdapter);

        //when the user clicks on the add course button
        mAddCourseButton.setOnClickListener(view -> {
            //get the course code and credit unit from the user and convert them to a string
            //and int respectively assign them to their respective variables and clear their fields
            /*for course code*/
            if (mCourseCodeEditText.getText().toString().equals("") || mCreditUnitEditText.getText().toString().equals("")) {
                hideKeyBoard();
                Snackbar.make(root, "Cannot Input Empty Values for Either Course code or Credit Unit, DUH!!", Snackbar.LENGTH_SHORT).show();
            } else if (mCourseCodeEditText.getText().toString().length() < 3 || Integer.parseInt(mCreditUnitEditText.getText().toString()) < 0) {
                hideKeyBoard();
                Snackbar.make(root, "Please input the Correct items for the either fields", Snackbar.LENGTH_SHORT).show();
            } else {
                //collect the data inputted and take the cursor to the first edit text
                mCourseCode = mCourseCodeEditText.getText().toString().trim();
                mCreditUnit = Integer.parseInt(mCreditUnitEditText.getText().toString());
                //gets the string
                String courseCode = mCourseCode.toUpperCase();
                int cu = mCreditUnit;
                //and clears the field
                mCourseCodeEditText.setText(EMPTY);
                mCreditUnitEditText.setText(EMPTY);
                //make the edit text request focus again
                mCourseCodeEditText.requestFocus();
                //pass it to the single object of the Course class
                mSingleCourse = new Course(courseCode, cu);
                //set those variables into our course class
                mSingleCourse.setCourseCode(courseCode);
                mSingleCourse.setCreditUnit(cu);
                //finally, add that single set course in our arraylist of courses
                mCoursesArrayList.add(mSingleCourse);
                //pass that list to our data manager
                KalkDataManager.getInstance().setCourseArrayList(mCoursesArrayList);
                //when done display a success SnackBar
                Snackbar.make(root, "Added: " + mSingleCourse.getCourseCode() + "{ " + mSingleCourse.getCreditUnit()
                        + " credit unit(s)}", BaseTransientBottomBar.LENGTH_SHORT).show();
                //setup our spinner
                mCourseArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, KalkDataManager.getInstance().getCourseArrayList());
                mCourseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAppCompatSpinner.setAdapter(mCourseArrayAdapter);
                mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        mPosition = i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //do nothing
                    }
                });
            }
        });

        mRemoveCourseButton.setOnClickListener(view -> {
            if (mCourseArrayAdapter != null) {
                mCoursesArrayList.remove(mPosition);
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(root, "How the hell do you intend to remove... NOTHING, lol", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    public void hideKeyBoard() {
        InputMethodManager mInputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getView()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}