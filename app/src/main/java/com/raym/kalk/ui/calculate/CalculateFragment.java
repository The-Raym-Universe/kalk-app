package com.raym.kalk.ui.calculate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.raym.kalk.R;
import com.raym.kalk.databinding.FragmentCalculateBinding;
import com.raym.kalk.models.Calculator;
import com.raym.kalk.models.Course;
import com.raym.kalk.models.KalkDataManager;

import java.util.ArrayList;

public class CalculateFragment extends Fragment {

    private CalculateViewModel calculateViewModel;
    private FragmentCalculateBinding binding;
    //added
    private EditText mCourseGrade;
    private EditText mCourseChoice;
    public Calculator mCalculator = new Calculator();
    private String mCourseCode;
    private int mCreditLoad;
    private int mTotalCreditUnit;
    private int mTotalCreditLoad;
    private int mGrade;
    private int mGradeEquivalent;
    private float mResult;
    private final int mCreditUnit = 3;
    public String mFinalResult;
    static String EMPTY_PLACE = "";
    ArrayList<Course> arrayListOfCourses;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calculate, container, false);

        //calculate

        Button doneButton = root.findViewById(R.id.done_button);
        Button previousButton = root.findViewById(R.id.previous_course_button);
        Button nextCourseButton = root.findViewById(R.id.next_course_button);
        mCourseChoice = root.findViewById(R.id.edit_text_course_choice);
        mCourseGrade = root.findViewById(R.id.edit_text_grade);

        nextCourseButton.setOnClickListener(view -> {
            //to compare this data, we should get the array list first then we get a single course
            //make it a string to efficiently compare
            arrayListOfCourses = (ArrayList<Course>) KalkDataManager.getInstance().getCourseArrayList();
            String singleCourse = KalkDataManager.getInstance().getCourse().toString();

            String courseCode = mCourseCode.toUpperCase();
            mCourseCode = mCourseChoice.getText().toString();

            for (Course single_course :
                    arrayListOfCourses) {
                if (singleCourse.equals(courseCode)) {

                }
            }
            mCourseChoice.setText(EMPTY_PLACE);
            mGrade = Integer.parseInt(mCourseGrade.getText().toString());
            mCourseGrade.setText(EMPTY_PLACE);
            mGradeEquivalent = checkGradeEquivalent(mGrade);
            mCourseChoice.setFocusable(true);

            mTotalCreditUnit = mCalculator.calculateTotalCreditUnits(mCreditUnit);
            mCreditLoad = mCalculator.calculateCreditLoad(mCreditUnit, mGradeEquivalent);
            mTotalCreditLoad = mCalculator.calculateTotalCreditLoad(mCreditLoad);
        });
        //when the user clicks on the previous button
        previousButton.setOnClickListener(view -> Toast.makeText(getContext(), "Nothing to do yet", Toast.LENGTH_SHORT).show());
        //nothing really wrong with this lines of code
        //when the user clicks on the done button
        doneButton.setOnClickListener(view -> {
            mResult = mCalculator.calculateGP(mTotalCreditLoad, mTotalCreditUnit);
            mFinalResult = String.valueOf(mResult);
//            Intent calculationActivityIntent = new Intent(getContext(), ResultActivity.class);
//            calculationActivityIntent.putExtra(Intent.EXTRA_TEXT, mFinalResult);
//            startActivity(calculationActivityIntent);

            //final answer is...
            Toast.makeText(getContext(),"GPA: " + mFinalResult, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}