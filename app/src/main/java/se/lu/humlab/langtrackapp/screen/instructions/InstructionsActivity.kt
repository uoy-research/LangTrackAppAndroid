package se.lu.humlab.langtrackapp.screen.instructions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.instructions_activity.*
import se.lu.humlab.langtrackapp.R

class InstructionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.instructions_activity)

        instructionsCloseButton.setOnClickListener {
            onBackPressed()
        }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, InstructionsActivity::class.java))
        }
    }
}
