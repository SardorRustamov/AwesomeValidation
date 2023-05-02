package uz.sr.awesomelibrary

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import uz.sr.mylibrary.ValidationStyle.*
import uz.sr.mylibrary.utility.RegexTemplate
import uz.sr.mylibrary.utility.custom.SimpleCustomValidation
import uz.sr.mylibrary.AwesomeValidation
import uz.sr.mylibrary.ValidationStyle
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {


    private lateinit var mStyles: Array<String>
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerList: ListView? = null
    private val mDrawerItemClickListener: DrawerItemClickListener = DrawerItemClickListener()
    private var mPosition = 0
    private var mAwesomeValidation: AwesomeValidation? = null
    private var mViewSuccess: LinearLayout? = null
    private var mScrollView: ScrollView? = null
    private var mViewContainerEditText: View? = null
    private var mViewContainerTextInputLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewSuccess = findViewById<View>(R.id.container_success) as LinearLayout
        mScrollView = findViewById<View>(R.id.scroll_view) as ScrollView
        mViewContainerEditText = findViewById(R.id.container_edit_text)
        mViewContainerTextInputLayout = findViewById(R.id.container_text_input_layout)
        mStyles = resources.getStringArray(R.array.styles)
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar


//        if (toolbar != null) {
//            setSupportActionBar(toolbar)
//            val actionBar = supportActionBar
//            actionBar?.setDisplayHomeAsUpEnabled(true)
//        }

        setupSpinner()
        // AwesomeValidation.disableAutoFocusOnFirstFailure();
        // AwesomeValidation.disableAutoFocusOnFirstFailure();

        mDrawerToggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            toolbar,
            R.string.app_name,
            R.string.app_name
        )
        mDrawerLayout!!.addDrawerListener(mDrawerToggle!!)
        mDrawerList = findViewById<View>(R.id.left_drawer) as ListView
        mDrawerList!!.adapter = ArrayAdapter(this, R.layout.drawer_list_item, mStyles)
        mDrawerList!!.onItemClickListener = mDrawerItemClickListener
        mDrawerItemClickListener.selectItem(mPosition)

    }


//    fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return if (mDrawerToggle!!.onOptionsItemSelected(item)) {
//            true
//        } else {
//            super.onOptionsItemSelected(item!!)
//        }
//    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawers()
            return
        }
        if (mPosition > 0) {
            mDrawerItemClickListener.selectItem(0)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinner_tech_stacks)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tech_stacks,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun clearValidation() {
        if (mAwesomeValidation != null) {
            mAwesomeValidation?.clear()
            mViewSuccess!!.visibility = View.GONE
        }
    }

    private fun initValidation(style: String) {
        when (ValidationStyle.valueOf(style)) {
            ValidationStyle.BASIC -> mAwesomeValidation = AwesomeValidation(BASIC)
            ValidationStyle.COLORATION -> {
                mAwesomeValidation = AwesomeValidation(COLORATION)
                mAwesomeValidation?.setColor(Color.YELLOW)
            }
            ValidationStyle.UNDERLABEL -> {
                mAwesomeValidation = AwesomeValidation(UNDERLABEL)
                mAwesomeValidation?.setContext(this)
            }
            ValidationStyle.TEXT_INPUT_LAYOUT -> {
                mAwesomeValidation = AwesomeValidation(TEXT_INPUT_LAYOUT)
                mAwesomeValidation?.setTextInputLayoutErrorTextAppearance(R.style.TextInputLayoutErrorStyle)
            }
        }
    }

    private fun addValidationForEditText(activity: Activity) {
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_userid,
            "[a-zA-Z0-9_-]+",
            R.string.err_userid
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_password,
            "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}",
            R.string.err_password
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_password_confirmation,
            R.id.edt_password,
            R.string.err_password_confirmation
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_firstname,
            "[a-zA-Z\\s]+",
            R.string.err_name
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_lastname,
            "[a-zA-Z\\s]+",
            R.string.err_name
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_email,
            Patterns.EMAIL_ADDRESS,
            R.string.err_email
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_ip,
            Patterns.IP_ADDRESS,
            R.string.err_ip
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.edt_tel,
            RegexTemplate.TELEPHONE,
            R.string.err_tel
        )
        mAwesomeValidation!!.addValidation(activity, R.id.edt_zipcode, "\\d+", R.string.err_zipcode)
        mAwesomeValidation?.addValidation(
            activity,
            R.id.edt_year,
            "Range.closed(1900, Calendar.getInstance()[Calendar.YEAR])",
            R.string.err_year
        )
        mAwesomeValidation?.addValidation(
            activity,
            R.id.edt_height,
            "Range.closed(0.0f, 2.72f)",
            R.string.err_height
        )
        mAwesomeValidation!!.addValidation(activity, R.id.edt_birthday,
            SimpleCustomValidation { input ->
                try {
                    val calendarBirthday = Calendar.getInstance()
                    val calendarToday = Calendar.getInstance()
                    calendarBirthday.time = SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(input)
                    val yearOfToday = calendarToday[Calendar.YEAR]
                    val yearOfBirthday = calendarBirthday[Calendar.YEAR]
                    if (yearOfToday - yearOfBirthday > 18) {
                        return@SimpleCustomValidation true
                    } else if (yearOfToday - yearOfBirthday == 18) {
                        val monthOfToday = calendarToday[Calendar.MONTH]
                        val monthOfBirthday = calendarBirthday[Calendar.MONTH]
                        if (monthOfToday > monthOfBirthday) {
                            return@SimpleCustomValidation true
                        } else if (monthOfToday == monthOfBirthday) {
                            if (calendarToday[Calendar.DAY_OF_MONTH] >= calendarBirthday[Calendar.DAY_OF_MONTH]) {
                                return@SimpleCustomValidation true
                            }
                        }
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                false
            }, R.string.err_birth
        )
        mAwesomeValidation!!.addValidation(activity, R.id.spinner_tech_stacks,
            { validationHolder ->
                (validationHolder.view as Spinner).selectedItem.toString() != "< Please select one >"
            }, { validationHolder ->
                val textViewError = (validationHolder.view as Spinner).selectedView as TextView
                textViewError.error = validationHolder.errMsg
                textViewError.setTextColor(Color.RED)
            }, { validationHolder ->
                val textViewError = (validationHolder.view as Spinner).selectedView as TextView
                textViewError.error = null
                textViewError.setTextColor(Color.BLACK)
            }, R.string.err_tech_stacks
        )
        setValidationButtons()
    }

    private fun addValidationForTextInputLayout(activity: Activity) {
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.til_email,
            Patterns.EMAIL_ADDRESS,
            R.string.err_email
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.til_password,
            "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}",
            R.string.err_password
        )
        mAwesomeValidation!!.addValidation(
            activity,
            R.id.til_password_confirmation,
            R.id.til_password,
            R.string.err_password_confirmation
        )
        mAwesomeValidation?.addValidation(
            activity,
            R.id.til_year,
            "Range.closed(1900, Calendar.getInstance()[Calendar.YEAR])",
            R.string.err_year
        )
        setValidationButtons()
    }

    private fun setValidationButtons() {
        val btnDone = findViewById<View>(R.id.btn_done) as Button
        btnDone.setOnClickListener {
            if (mAwesomeValidation!!.validate()) {
                mScrollView!!.fullScroll(View.FOCUS_DOWN)
                mViewSuccess!!.visibility = View.VISIBLE
            } else {
                mViewSuccess!!.visibility = View.GONE
            }
        }
        val btnClr = findViewById<View>(R.id.btn_clr) as Button
        btnClr.setOnClickListener {
            mAwesomeValidation!!.clear()
            mViewSuccess!!.visibility = View.GONE
        }
    }


    inner class DrawerItemClickListener : OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            selectItem(position)
        }

        fun selectItem(position: Int) {
            mDrawerList?.setItemChecked(position, true)
            mPosition = position
            val style: String = mStyles[mPosition]
            title = style
            mDrawerLayout?.closeDrawer(mDrawerList!!)
            mViewContainerEditText?.visibility = if (position < TEXT_INPUT_LAYOUT.value()) View.VISIBLE else View.GONE
            mViewContainerTextInputLayout?.visibility = if (position < TEXT_INPUT_LAYOUT.value()) View.GONE else View.VISIBLE
            clearValidation()
            initValidation(style)
            if (position < TEXT_INPUT_LAYOUT.value()) {
                addValidationForEditText(this@MainActivity)
            } else if (position == TEXT_INPUT_LAYOUT.value()) {
                addValidationForTextInputLayout(this@MainActivity)
            }
        }
    }

}


