package se.lu.humlab.langtrackapp.screen.contact

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.contact_activity.*
import se.lu.humlab.langtrackapp.R

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_activity)

        contactCloseButton.setOnClickListener {
            onBackPressed()
        }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, ContactActivity::class.java))
        }
    }
}
