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
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.left_drawer_menu.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Assignment
import se.lu.humlab.langtrackapp.data.model.User
import se.lu.humlab.langtrackapp.databinding.ActivityMainBinding
import se.lu.humlab.langtrackapp.interfaces.OnBoolPopupReturnListener
import se.lu.humlab.langtrackapp.interfaces.OnSurveyRowClickedListener
import se.lu.humlab.langtrackapp.popup.ExpiredSurveyPopup
import se.lu.humlab.langtrackapp.popup.OneChoicePopup
import se.lu.humlab.langtrackapp.screen.about.AboutActivity
import se.lu.humlab.langtrackapp.screen.contact.ContactActivity
import se.lu.humlab.langtrackapp.screen.instructions.InstructionsActivity
import se.lu.humlab.langtrackapp.screen.login.LoginActivity
import se.lu.humlab.langtrackapp.screen.overview.OverviewActivity
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity
import se.lu.humlab.langtrackapp.util.MyFirebaseInstanceIDService
import se.lu.humlab.langtrackapp.util.getVersionNumber
import se.lu.humlab.langtrackapp.util.showApiFailInfo


class MainActivity : AppCompatActivity() {

    private lateinit var mBind : ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    lateinit var mAuth: FirebaseAuth
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SurveyAdapter
    lateinit var drawerToggle: ActionBarDrawerToggle
    private var inTestMode = false


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

        // only gets this if app is in foreground...
        val theMessageText = intent.getStringExtra(MyFirebaseInstanceIDService.MESSAGE_TEXT)
        if (!theMessageText.isNullOrBlank()) {
            println("messaging MainActivity onCreate theMessageText: $theMessageText")
        }

        recycler = mBind.surveyRecycler
        linearLayoutManager = LinearLayoutManager(this)
        recycler.layoutManager = linearLayoutManager
        adapter = SurveyAdapter()
        recycler.adapter = adapter

        //swipeRefresh
        mBind.surveyRecyclerRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.lta_blue))
        mBind.surveyRecyclerRefreshLayout.setColorSchemeColors(Color.WHITE)

        mBind.surveyRecyclerRefreshLayout.setOnRefreshListener {
            if (mAuth.currentUser != null){
                viewModel.getAssignments()
            }
            mBind.surveyRecyclerRefreshLayout.isRefreshing = false
        }

        mBind.surveyRecycler.addItemDecoration(MyItemDecorator(4,28))
        adapter.setOnRowClickedListener(object: OnSurveyRowClickedListener {
            override fun rowClicked(item: Assignment) {

                viewModel.setSelectedAssignment(item)
                if (inTestMode) {
                    // in testMode, always show survey
                    SurveyContainerActivity.start(this@MainActivity, item)
                    viewModel.surveyOpened()
                } else {
                    if (item.isActive()) {
                        // show survey - if api is responding
                        viewModel.apiIsAlive { alive, _ ->
                            if (alive){
                                SurveyContainerActivity.start(this@MainActivity, item)
                                viewModel.surveyOpened()
                            }else{
                                showApiFailInfo(this@MainActivity)
                            }
                        }

                    } else {
                        if (item.dataset != null) {
                            // show overview
                            OverviewActivity.start(this@MainActivity, item)
                        } else {
                            // show popup
                            showPopupSurveyInfo(item = item)
                        }
                    }
                }
            }

        })

        adapter.setAssignments(viewModel.assignmentList)

        viewModel.assignmentListLiveData.observeForever {
            adapter.setAssignments(it)
            if (it.isNullOrEmpty()){
                mBind.surveyRecyclerRefreshLayout.visibility = View.GONE
                mBind.mainEmptyListInfoTextView.visibility = View.VISIBLE
            }else{
                mBind.surveyRecyclerRefreshLayout.visibility = View.VISIBLE
                mBind.mainEmptyListInfoTextView.visibility = View.GONE
            }
        }

        setSupportActionBar(mBind.toolbar)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            mBind.toolbar,
            R.string.Open,
            R.string.Close
        )
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        menuLogOutTextView.setOnClickListener {
            showLogOutPopup()
        }

        menuInstructionsContactButton.setOnClickListener {
            InstructionsActivity.start(this)
        }

        menuAboutButton.setOnClickListener {
            AboutActivity.start(this)
        }
        menuContactButton.setOnClickListener {
            ContactActivity.start(this)
            //drawerLayout.closeDrawer(GravityCompat.START)
        }

        menuTestSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            inTestMode = isChecked
        }

        menuServerSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setStagingUrl(isChecked)
            viewModel.postDeviceToken()
            viewModel.clearAssignmentsList()
            viewModel.getAssignments()
        }

    }

    override fun onStart() {
        super.onStart()

        val verNum = getVersionNumber(this)
        // save userId to repository
        if (mAuth.currentUser == null){
            LoginActivity.start(this)
        }else{
            val userEmail = mAuth.currentUser!!.email
            val userName = userEmail?.substringBefore('@')
            viewModel.setCurrentUser(User(userName ?: "",userName ?: "", userEmail ?: ""))
            menuUserNameTextView.text = userName ?: "noName"
            menuVersionTextView.text = verNum
            viewModel.getAssignments()

            mAuth.currentUser!!.getIdToken(false).addOnSuccessListener{
                val idToken = it.token
                if (!idToken.isNullOrBlank()){
                    viewModel.setIdToken(idToken)
                    println("idToken: $idToken")
                }
            }
            setTestModeIfTeam(userName ?: "")

            menuServerSwitch.isChecked = viewModel.isInStagingUrl()
            //push deviceToken to backend every time app starts
            viewModel.postDeviceToken()
        }
    }

    private fun setTestModeIfTeam(userName: String){
        testView.visibility = if (
            userName == "stephan" ||
            userName == "josef" ||
            userName == "marianne" ||
            userName == "jonas" ||
            userName == "henriette" ||
            userName == "stephandroid") View.VISIBLE else View.GONE
    }

    fun unsubscribeToTopic(){
        val topic = viewModel.getCurrentUser().userName
        if (topic != "") {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        println("unsubscribeToTopic ERROR: ${task.exception?.localizedMessage}")
                    } else {
                        println("unsubscribeToTopic, subscribed to topic: $topic")
                    }
                }
        }
    }

    private fun showLogOutPopup(){
        val alertFm = supportFragmentManager.beginTransaction()
        val width = (main_layout.measuredWidth * 0.75).toInt()
        val oneChoicePopup = OneChoicePopup.show(
            width = width,
            title = getString(R.string.log_out),
            infoText = getString(R.string.doYouWantToLogOut, viewModel.getCurrentUser().userName),
            okButtonText = getString(R.string.log_out),
            cancelButtonText = getString(R.string.cancel),
            placecenter = true,
            cancelable = true
        )
        oneChoicePopup.setCompleteListener(object : OnBoolPopupReturnListener {
            override fun popupReturn(value: Boolean) {
                if (value){
                    viewModel.clearAssignmentsList()
                    mAuth.signOut()
                    unsubscribeToTopic()
                    LoginActivity.start(this@MainActivity)
                }
            }
        })
        oneChoicePopup.show(alertFm, "oneChoicePopup")
    }

    fun showPopupSurveyInfo(item: Assignment){
        val alertFm = supportFragmentManager.beginTransaction()
        val width = (main_layout.measuredWidth * 0.85).toInt()

        val alertPopup = ExpiredSurveyPopup.show(
            width = width,
            published = item.publishAt,
            expired = item.expireAt,
            numberOfQuestions = item.survey.questions?.size ?: 0,
            textViewText = item.survey.title,
            placecenter = true
        )
        alertPopup.setCompleteListener(object : OnBoolPopupReturnListener{
            override fun popupReturn(value: Boolean) {
                onBackPressed()
            }
        })
        alertPopup.show(alertFm, "surveyInfoPopup")
    }

    override fun onBackPressed() {
        //close menu if open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
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
