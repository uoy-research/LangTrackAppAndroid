package se.lu.humlab.langtrackapp.screen.about

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.about_activity.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.databinding.AboutActivityBinding
import se.lu.humlab.langtrackapp.util.asUri
import se.lu.humlab.langtrackapp.util.getLanguageCode
import se.lu.humlab.langtrackapp.util.openInBrowser


class AboutActivity : AppCompatActivity() {

    private lateinit var mBind :AboutActivityBinding
    private lateinit var viewModel :TeamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.about_activity)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()

        viewModel = ViewModelProvider(this, TeamViewModelFactory(this))
            .get(TeamViewModel::class.java)

        mBind.aboutScroll.setOnScrollChangeListener { _, _, _, _, _ ->
            mBind.aboutTopView.isSelected = mBind.aboutScroll.canScrollVertically(-1)
        }

        mBind.aboutTopCloseButton.setOnClickListener {
            onBackPressed()
        }

        mBind.aboutFoundingTextView.text = getString(R.string.founding)

        val resourceId = resources.getIdentifier(getString(R.string.founder_image_name), "drawable", packageName)
        mBind.aboutFoundingImageView.setImageDrawable(getDrawable(resourceId))
        mBind.aboutFoundingImageView.setOnClickListener {
            getString(R.string.founding_link_address)
                .asUri().openInBrowser(this)
        }
        setTeamText()

        mBind.textView18.text = getString(R.string.about)
    }

    private fun setTeamText(){

        viewModel.getTeamsText(){ memberList ->

            val builder = SpannableStringBuilder()

            val spannableHeader = SpannableString(getString(R.string.team))
            val boldSpan = StyleSpan(Typeface.BOLD)
            val sizeSpan = RelativeSizeSpan(1.3f)
            val colourSpan = ForegroundColorSpan(getColor(R.color.lta_blue))
            spannableHeader.setSpan(boldSpan,0,getString(R.string.team).count(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableHeader.setSpan(sizeSpan,0,getString(R.string.team).count(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableHeader.setSpan(colourSpan,0,getString(R.string.team).count(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            builder.append(spannableHeader)
            builder.append("\n\n")

            val languageCode = getLanguageCode()
            for (i in memberList.indices){
                val member = memberList[i]
                println("member.name: ${member.name[getLanguageCode()]}, description: ${member.description[getLanguageCode()]}")

                val memberName = member.name[languageCode] ?: ""
                val memberDescription = member.description[languageCode] ?: ""
                if (memberName.isNotBlank()) {
                    val memberInfo = "$memberName, $memberDescription"
                    val techStartIndex = memberInfo.indexOf(memberName)
                    val techEndIndex = techStartIndex + memberName.count()
                    val spannableStringM = SpannableString(memberInfo)
                    spannableStringM.setSpan(ForegroundColorSpan(getColor(R.color.lta_red)),
                        techStartIndex,
                        techEndIndex,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    builder.append(spannableStringM)
                    if (i < memberList.size - 1)
                        builder.append("\n\n")
                }
            }
            mBind.aboutTeamTextView.text = builder
        }
    }


    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
