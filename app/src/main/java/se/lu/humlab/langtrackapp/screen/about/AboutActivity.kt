package se.lu.humlab.langtrackapp.screen.about

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.about_activity.*
import se.lu.humlab.langtrackapp.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)

        aboutCloseButton.setOnClickListener {
            onBackPressed()
        }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
