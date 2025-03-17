package com.pawatr.coinmarketcap.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.pawatr.coinmarketcap.R

class SearchEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var searchIcon: Drawable? = null
    private var clearIcon: Drawable? = null
    private var hintText: String? = null
    private var textWatcher: TextWatcher? = null
    private var focusChangeListener: OnFocusChangeListener? = null

    init {
        initIcons()
        initAttributes(attrs)
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateClearIconVisibility()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        addTextChangedListener(textWatcher)
        focusChangeListener = OnFocusChangeListener { _, _ ->
            updateClearIconVisibility()
        }
        onFocusChangeListener = focusChangeListener
    }

    private fun initIcons() {
        searchIcon = ContextCompat.getDrawable(context, R.drawable.ic_search)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            DrawableCompat.setTint(this, ContextCompat.getColor(context, R.color.gray))
        }
        clearIcon = ContextCompat.getDrawable(context, R.drawable.ic_clear)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            DrawableCompat.setTint(this, ContextCompat.getColor(context, R.color.gray))
        }
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText)
        hintText = typedArray.getString(R.styleable.SearchEditText_hintText)
        typedArray.recycle()
        hint = hintText
    }

    private fun updateClearIconVisibility() {
        val hasText = text.isNullOrEmpty()
        val hasFocus = hasFocus()
        val showClearIcon = hasText && hasFocus
        val rightIcon = if (showClearIcon) clearIcon else null
        setCompoundDrawables(searchIcon, null, rightIcon, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && clearIcon != null) {
            val clearIconStart = width - paddingRight - clearIcon!!.intrinsicWidth
            if (event.x >= clearIconStart) {
                text?.clear()
                clearFocus()
                hideKeyboard()
                return true
            }
        } else if (event.action == MotionEvent.ACTION_DOWN) {
            hideKeyboard()
        }
        return super.onTouchEvent(event)
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}