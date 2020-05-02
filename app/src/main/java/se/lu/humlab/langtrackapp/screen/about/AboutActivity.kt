package se.lu.humlab.langtrackapp.screen.about

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.about_activity.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.databinding.AboutActivityBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var mBind :AboutActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.about_activity)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()

        mBind.aboutScroll.setOnScrollChangeListener { _, _, _, _, _ ->
            mBind.aboutTopView.isSelected = mBind.aboutScroll.canScrollVertically(-1)
        }

    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
