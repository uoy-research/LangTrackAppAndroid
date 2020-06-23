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
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.databinding.AboutActivityBinding
import se.lu.humlab.langtrackapp.util.asUri
import se.lu.humlab.langtrackapp.util.openInBrowser


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
    }

    fun setTeamText(){

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

        val nameMarianne = "Marianne Gullberg"
        val techStartIndex = getString(R.string.marianneInfo).indexOf(nameMarianne)
        val techEndIndex = techStartIndex + nameMarianne.count()
        val spannableStringM = SpannableString(getString(R.string.marianneInfo))
        spannableStringM.setSpan(ForegroundColorSpan(getColor(R.color.lta_red)), techStartIndex, techEndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        builder.append(spannableStringM)
        builder.append("\n\n")

        val nameJonas = "Jonas Granfeldt"
        val jonasStartIndex = getString(R.string.jonasInfo).indexOf(nameJonas)
        val jonasEndIndex = jonasStartIndex + nameJonas.count()
        val spannableStringJ = SpannableString(getString(R.string.jonasInfo))
        spannableStringJ.setSpan(ForegroundColorSpan(getColor(R.color.lta_red)), jonasStartIndex, jonasEndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        builder.append(spannableStringJ)
        builder.append("\n\n")

        val nameHenriette = "Henriette Arndt"
        val henrietteStartIndex = getString(R.string.henrietteInfo).indexOf(nameHenriette)
        val henrietteEndIndex = henrietteStartIndex + nameHenriette.count()
        val spannableStringH = SpannableString(getString(R.string.henrietteInfo))
        spannableStringH.setSpan(ForegroundColorSpan(getColor(R.color.lta_red)), henrietteStartIndex, henrietteEndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        builder.append(spannableStringH)
        builder.append("\n\n")

        val nameJosef = "Josef Granqvist"
        val josefStartIndex = getString(R.string.josefInfo).indexOf(nameJosef)
        val josefEndIndex = josefStartIndex + nameJosef.count()
        val spannableStringJosef = SpannableString(getString(R.string.josefInfo))
        spannableStringJosef.setSpan(ForegroundColorSpan(getColor(R.color.lta_red)), josefStartIndex, josefEndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        builder.append(spannableStringJosef)
        builder.append("\n\n")

        val nameStephan = "Stephan Björck"
        val stephanStartIndex = getString(R.string.stephanInfo).indexOf(nameStephan)
        val stephanEndIndex = stephanStartIndex + nameStephan.count()
        val spannableStringStephan = SpannableString(getString(R.string.stephanInfo))
        spannableStringStephan.setSpan(ForegroundColorSpan(getColor(R.color.lta_red)), stephanStartIndex, stephanEndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        builder.append(spannableStringStephan)

        mBind.aboutTeamTextView.text = builder
    }


    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
