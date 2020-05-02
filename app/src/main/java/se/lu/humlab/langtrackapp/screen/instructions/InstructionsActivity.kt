package se.lu.humlab.langtrackapp.screen.instructions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.instructions_activity.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.databinding.InstructionsActivityBinding

class InstructionsActivity : AppCompatActivity() {

    lateinit var mBind: InstructionsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.instructions_activity)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()

        mBind.instructionsScroll.setOnScrollChangeListener { _, _, _, _, _ ->
            mBind.instructionsTopView.isSelected = mBind.instructionsScroll.canScrollVertically(-1)
        }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, InstructionsActivity::class.java))
        }
    }
}
