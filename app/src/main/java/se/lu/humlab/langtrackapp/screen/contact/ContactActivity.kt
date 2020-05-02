package se.lu.humlab.langtrackapp.screen.contact

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.contact_activity.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.databinding.ContactActivityBinding
import se.lu.humlab.langtrackapp.util.asUri
import se.lu.humlab.langtrackapp.util.openInBrowser

class ContactActivity : AppCompatActivity() {


    private lateinit var mBind :ContactActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.contact_activity)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()

        mBind.contactScroll.setOnScrollChangeListener { _, _, _, _, _ ->
            mBind.contactTopView.isSelected = mBind.contactScroll.canScrollVertically(-1)
        }
        mBind.contactLtaLinkTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        mBind.contactLtaLinkTextView.setOnClickListener {
            "https://portal.research.lu.se/portal/en/projects/the-langtrackapp-studying-exposure-to-and-use-of-a-new-language-using-smartphone-technology(4e734940-981f-4dd0-841a-eb6ac760af0c).html"
                .asUri().openInBrowser(this)
        }

        mBind.contactHumLinkTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        mBind.contactHumLinkTextView.setOnClickListener {
            "https://www.humlab.lu.se/en/"
                .asUri().openInBrowser(this)
        }

        mBind.contactLuLinkTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        mBind.contactLuLinkTextView.setOnClickListener {
            "https://www.lu.se/"
                .asUri().openInBrowser(this)
        }
    }


    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, ContactActivity::class.java))
        }
    }
}
