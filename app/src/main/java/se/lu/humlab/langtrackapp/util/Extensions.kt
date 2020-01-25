package se.lu.humlab.langtrackapp.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import se.lu.humlab.langtrackapp.R

fun AppCompatActivity.loadFragment(fragment: Fragment){
    supportFragmentManager
        .beginTransaction()
        .replace(
            R.id.surveyContainer,
            fragment
        )
        .commit()
}