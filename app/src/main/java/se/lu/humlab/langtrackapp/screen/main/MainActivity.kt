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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.TempSurvey
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.databinding.ActivityMainBinding
import se.lu.humlab.langtrackapp.interfaces.OnBoolPopupReturnListener
import se.lu.humlab.langtrackapp.interfaces.OnSurveyRowClickedListener
import se.lu.humlab.langtrackapp.popup.OneChoicePopup
import se.lu.humlab.langtrackapp.screen.login.LoginActivity
import se.lu.humlab.langtrackapp.screen.survey.SurveyActivity
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBind : ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    lateinit var mAuth: FirebaseAuth
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recycler: RecyclerView
    lateinit var adapter: SurveyAdapter
    var surveyList = mutableListOf<Survey>()


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

        recycler = mBind.surveyRecycler
        linearLayoutManager = LinearLayoutManager(this)
        recycler.layoutManager = linearLayoutManager
        adapter = SurveyAdapter()
        recycler.adapter = adapter
        val itemDecorator = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_divider)!!)
        mBind.surveyRecycler.addItemDecoration(itemDecorator)
        adapter.setOnRowClickedListener(object: OnSurveyRowClickedListener {
            override fun rowClicked(item: Survey) {
                SurveyContainerActivity.start(this@MainActivity, item)
            }
        })

        mBind.testButton.setOnClickListener {
            SurveyActivity.start(this)
        }
        mBind.mainLogOutButton.setOnClickListener {
            showLogOutPopup()
        }

        //temp
        surveyList.add(TempSurvey.getTempSurvey("1"))
        surveyList.add(TempSurvey.getTempSurvey("2"))
        surveyList.add(TempSurvey.getTempSurvey("3"))
        surveyList.add(TempSurvey.getTempSurvey("4"))
        surveyList.add(TempSurvey.getTempSurvey("5"))
        surveyList.add(TempSurvey.getTempSurvey("6"))
        surveyList.add(TempSurvey.getTempSurvey("7"))
        adapter.setTasks(surveyList)
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
