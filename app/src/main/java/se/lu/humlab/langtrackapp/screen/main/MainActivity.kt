package se.lu.humlab.langtrackapp.screen.main
/*
* tempinloggning
* email: stephan.bjorck@humlab.lu.se
* lösenord: 123456
* */

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.ActivityMainBinding
import se.lu.humlab.langtrackapp.interfaces.OnBoolPopupReturnListener
import se.lu.humlab.langtrackapp.popup.OneChoicePopup
import se.lu.humlab.langtrackapp.screen.login.LoginActivity
import se.lu.humlab.langtrackapp.screen.survey.SurveyActivity
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBind : ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()
        mAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProviders.of(this,
            MainViewModelFactory(this)
        ).get(MainViewModel::class.java)
        mBind.viewModel = viewModel
        mBind.testButton.setOnClickListener {
            SurveyActivity.start(this)
        }
        mBind.testButton2.setOnClickListener {
            SurveyContainerActivity.start(this)
        }
        mBind.mainLogOutButton.setOnClickListener {
            showLogOutPopup()
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser == null){
            LoginActivity.start(this)
        }else{
            //Set listeners
        }
    }

    private fun showLogOutPopup(){
        val alertFm = supportFragmentManager.beginTransaction()
        val width = (main_layout.measuredWidth * 0.75).toInt()
        val oneChoicePopup = OneChoicePopup.show(
            width = width,
            title = "Logga ut",
            infoText = "Vill du logga ut?\n${mAuth.currentUser?.email}",
            okButtonText = "Logga ut",
            placecenter = true,
            cancelable = true
        )
        oneChoicePopup.setCompleteListener(object : OnBoolPopupReturnListener {
            override fun popupReturn(value: Boolean) {
                if (value){
                    mAuth.signOut()
                    LoginActivity.start(this@MainActivity)
                }
            }
        })
        oneChoicePopup.show(alertFm, "oneChoicePopup")
    }

    companion object {
        fun start(context: Context){
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
