package se.lu.humlab.langtrackapp.screen.main
/*

Stephan Björck
Humanistlaboratoriet
Lunds Universitet
stephan.bjorck@humlab.lu.se



* tempinloggning

* email: deltagare1a2b3c@humlablu.com
* användarnamn: deltagare1a2b3c
* lösenord: 123456

* email: test1@humlablu.com
* användarnamn: test1
* lösenord: 123456
* */

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.data.model.User
import se.lu.humlab.langtrackapp.databinding.ActivityMainBinding
import se.lu.humlab.langtrackapp.interfaces.OnBoolPopupReturnListener
import se.lu.humlab.langtrackapp.interfaces.OnSurveyRowClickedListener
import se.lu.humlab.langtrackapp.popup.OneChoicePopup
import se.lu.humlab.langtrackapp.screen.login.LoginActivity
import se.lu.humlab.langtrackapp.screen.overview.OverviewActivity
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBind : ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    lateinit var mAuth: FirebaseAuth
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SurveyAdapter


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

        mBind.surveyRecycler.addItemDecoration(MyItemDecorator(4,28))
        adapter.setOnRowClickedListener(object: OnSurveyRowClickedListener {
            override fun rowClicked(item: Survey) {
                if (item.responded){//TODO: check if survey is active
                    OverviewActivity.start(this@MainActivity, item)
                }else{
                    SurveyContainerActivity.start(this@MainActivity, item)
                }
            }
        })

        mBind.mainLogOutButton.setOnClickListener {
            showLogOutPopup()
        }

        viewModel.getUserLiveData().observeForever {
            mBind.currentUser = it
        }

        adapter.setTasks(viewModel.surveyList)

        viewModel.surveyListLiveData.observeForever {
            adapter.setTasks(it)
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser == null){
            LoginActivity.start(this)
        }else{
            //Get surveys
            //test
            viewModel.getSurveys()
            val userEmail = mAuth.currentUser!!.email
            val userName = userEmail?.substringBefore('@')
            viewModel.setCurrentUser(User("",userName ?: "", userEmail ?: ""))
        }
    }

    private fun showLogOutPopup(){
        val alertFm = supportFragmentManager.beginTransaction()
        val width = (main_layout.measuredWidth * 0.75).toInt()
        val oneChoicePopup = OneChoicePopup.show(
            width = width,
            title = "Logga ut",
            infoText = "Vill du logga ut?\n${viewModel.getCurrentUser().userName}",
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
class MyItemDecorator(private val horizontal: Int, private val vertical: Int): RecyclerView.ItemDecoration(){

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = horizontal
        outRect.left = horizontal
        if (parent.getChildLayoutPosition(view) == 0){
            outRect.top = vertical
        }
        outRect.bottom = vertical
    }

}
