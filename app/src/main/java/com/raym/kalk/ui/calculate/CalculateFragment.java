package com.raym.kalk.ui.calculate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import com.raym.kalk.R;
import com.raym.kalk.activities.ResultActivity;
import com.raym.kalk.models.Calculator;
import com.raym.kalk.models.Course;
import com.raym.kalk.models.KalkDataManager;

import java.util.ArrayList;

public class CalculateFragment extends Fragment {

    public CalculateFragment() {

    }

    private EditText mCourseGradeEditText;
    public final Calculator mCalculator = new Calculator();
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calculate, container, false);

        Button mDoneButton = root.findViewById(R.id.done_button);
        Button mNextCourseButton = root.findViewById(R.id.next_course_button);
        mCourseGradeEditText = root.findViewById(R.id.edit_text_grade);
        AppCompatSpinner mAppCompatSpinner = root.findViewById(R.id.spinner_course_choice);

        arrayListOfCourses = (ArrayList<Course>) KalkDataManager.getInstance().getCourseArrayList();

        ArrayAdapter<Course> courseArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arrayListOfCourses);
        courseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAppCompatSpinner.setAdapter(courseArrayAdapter);
        mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCourseCode = arrayListOfCourses.get(i).getCourseCode();
                mCreditUnit = arrayListOfCourses.get(i).getCreditUnit();
                Toast.makeText(getContext(), "selected: " + mCourseCode + "{" + mCreditUnit + " credit unit(s)}", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mNextCourseButton.setOnClickListener(view -> {
            //collect the input from the spinner and get its credit unit, and collect the grade
            if (mCourseGradeEditText.getText().toString().equals("") || mCourseGradeEditText == null) {
                Toast.makeText(getContext(), "Cannot Solve with empty value for grade"
                        , Toast.LENGTH_SHORT).show();
                mCourseGradeEditText.requestFocus();
            } else {
                mGrade = Integer.parseInt(mCourseGradeEditText.getText().toString());
                mCourseGradeEditText.setText(EMPTY_PLACE);
                mCourseGradeEditText.requestFocus();
                //check grade equivalent
                mGradeEquivalent = checkGradeEquivalent(mGrade);
                //start actual calculations
                mTotalCreditUnit = mCalculator.calculateTotalCreditUnits(mCreditUnit);
                mCreditLoad = mCalculator.calculateCreditLoad(mCreditUnit, mGradeEquivalent);
                mTotalCreditLoad = mCalculator.calculateTotalCreditLoad(mCreditLoad);
            }
        });
        //nothing really wrong with these lines of code
        //when the user clicks on the done button
        mDoneButton.setOnClickListener(view -> {
            //calculate
            if (mCourseGradeEditText.getText().toString().equals("") || mCourseGradeEditText == null) {
                Toast.makeText(getContext(), "Cannot Solve with empty value for grade"
                        , Toast.LENGTH_SHORT).show();
                mCourseGradeEditText.requestFocus();
            } else {
                mResult = mCalculator.calculateGP(mTotalCreditLoad, mTotalCreditUnit);
                mFinalResult = String.valueOf(mResult);
                Intent calculationActivityIntent = new Intent(getContext(), ResultActivity.class);
                calculationActivityIntent.putExtra(Intent.EXTRA_TEXT, mFinalResult);
                startActivity(calculationActivityIntent);
            }
        });
        return root;
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

}