package se.lu.humlab.langtrackapp.screen.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.databinding.LoginActivityBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    lateinit var mBind: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login_activity)
        mBind = DataBindingUtil.setContentView(this,R.layout.login_activity)

        mBind.logInButton.setOnClickListener {
            checkTextAndLogIn()
        }
        mBind.loginProgressbar.visibility = View.GONE
        mBind.logInEmailEditText.requestFocus()
    }

    private fun checkTextAndLogIn(){
        val email = mBind.logInEmailEditText.text.toString().trim()
        val password = mBind.logInPasswordEditText.text.toString().trim()

        if (email.isEmpty()){
            mBind.logInEmailEditText.error = "Ange e-post"
            mBind.logInEmailEditText.requestFocus()
            return
        }
        if (!isValidEmail(email)){
            mBind.logInEmailEditText.error = "E-postadressen ser inte ut att stämma"
            mBind.logInEmailEditText.requestFocus()
            return
        }
        if (password.isEmpty()){
            mBind.logInPasswordEditText.error = "Ange lösenord"
            mBind.logInPasswordEditText.requestFocus()
            return
        }
        mBind.loginProgressbar.visibility = View.VISIBLE
        logIn(email,password)
    }

    private fun isValidEmail(email: String): Boolean{
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun logIn(email: String, password: String){
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                mBind.loginProgressbar.visibility = View.GONE
                if (it.isSuccessful){
                    //val user: FirebaseUser? = mAuth.currentUser
                    //saveInstanceId(user)
                    onBackPressed()
                }else{
                    Toast.makeText(this@LoginActivity,
                        "Autentisering misslyckades.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,LoginActivity::class.java).apply {

            })
        }
    }
}
