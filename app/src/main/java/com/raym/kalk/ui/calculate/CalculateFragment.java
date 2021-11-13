package com.raym.kalk.ui.calculate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.raym.kalk.R;
import com.raym.kalk.activities.ResultActivity;
import com.raym.kalk.models.Calculator;
import com.raym.kalk.models.Course;
import com.raym.kalk.models.KalkDataManager;

import java.util.ArrayList;
import java.util.Objects;

public class CalculateFragment extends Fragment {
    private EditText mCourseGradeEditText;
    private EditText mCourseChoiceEditText;
    public Calculator mCalculator = new Calculator();
    private String mCourseCode;
    private int mCreditLoad;
    private int mTotalCreditUnit;
    private int mTotalCreditLoad;
    private int mGrade;
    private int mGradeEquivalent;
    private float mResult;
    private int mCreditUnit;
    public String mFinalResult;
    final static String EMPTY_PLACE = "";
    ArrayList<Course> arrayListOfCourses;
    private Button mNextCourseButton;
    private Button mPreviousButton;
    private Button mDoneButton;
    private String mSingleCourse;
    private InputMethodManager mInputMethodManager;
    final static String EMPTY = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calculate, container, false);

        mDoneButton = root.findViewById(R.id.done_button);
        mPreviousButton = root.findViewById(R.id.previous_course_button);
        mNextCourseButton = root.findViewById(R.id.next_course_button);
        mCourseChoiceEditText = root.findViewById(R.id.edit_text_course_choice);
        mCourseGradeEditText = root.findViewById(R.id.edit_text_grade);

        arrayListOfCourses = (ArrayList<Course>) KalkDataManager.getInstance().getCourseArrayList();

        mNextCourseButton.setOnClickListener(view -> {
            //to compare this data, we should get the array list first then we get a single course
            //make it a string to efficiently compare
            mCourseCode = mCourseChoiceEditText.getText().toString();
            String courseCode = mCourseCode.toUpperCase();
            for ( int i = 0; i < arrayListOfCourses.size(); i++) {
                mSingleCourse = KalkDataManager.getInstance().getCourseArrayList().get(i).getCourseCode();
                if (mSingleCourse.equals(courseCode) && KalkDataManager.getInstance().getCourseArrayList().get(i) != null) {
                    mCreditUnit = KalkDataManager.getInstance().getCourseArrayList().get(i).getCreditUnit();
                    Toast.makeText(getContext(), "credit unit: " + mCreditUnit, Toast.LENGTH_SHORT).show();
                    mCourseChoiceEditText.setText(EMPTY_PLACE);
                    mGrade = Integer.parseInt(mCourseGradeEditText.getText().toString());
                    mCourseGradeEditText.setText(EMPTY_PLACE);
                    mGradeEquivalent = checkGradeEquivalent(mGrade);

                    mTotalCreditUnit = mCalculator.calculateTotalCreditUnits(mCreditUnit);
                    mCreditLoad = mCalculator.calculateCreditLoad(mCreditUnit, mGradeEquivalent);
                    mTotalCreditLoad = mCalculator.calculateTotalCreditLoad(mCreditLoad);
                }else{
                    hideKeyBoard();
                    mCourseGradeEditText.setText(EMPTY);
                    mCourseChoiceEditText.setText(EMPTY);
                    mCourseChoiceEditText.setFocusable(View.FOCUSABLE);
                    Snackbar.make(root,"That course is not registered or does not exist, Try again.", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
        //when the user clicks on the previous button
        mPreviousButton.setOnClickListener(view -> Toast.makeText(getContext(), "Nothing to do yet", Toast.LENGTH_SHORT).show());
        //nothing really wrong with this lines of code
        //when the user clicks on the done button
        mDoneButton.setOnClickListener(view -> {
            //calculate
            mResult = mCalculator.calculateGP(mTotalCreditLoad, mTotalCreditUnit);
            mFinalResult = String.valueOf(mResult);
            Intent calculationActivityIntent = new Intent(getContext(), ResultActivity.class);
            calculationActivityIntent.putExtra(Intent.EXTRA_TEXT, mFinalResult);
            startActivity(calculationActivityIntent);

            //final answer is...
            Toast.makeText(getContext(),"GPA: " + mFinalResult, Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    private void hideKeyBoard() {
        mInputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getView()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private int checkGradeEquivalent(int grade) {
        if (grade >= 75) {
            //for A; greater than 75
            mGradeEquivalent = 5;
        } else if (grade > 59) {
            //for B; greater than 60, but less than 75
            mGradeEquivalent = 4;
        } else if (grade > 50) {
            //for C; greater than 50, but less than 60
            mGradeEquivalent = 3;
        } else if (grade > 44) {
            //for D; greater than 45 and less than 50
            mGradeEquivalent = 2;
        } else if (grade > 39) {
            //for E; greater than 39
            mGradeEquivalent = 1;
        } else {
            //for F
            mGradeEquivalent = 0;
        }
        return mGradeEquivalent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}