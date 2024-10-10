package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var clearSearchButton: ImageView
    private lateinit var searchInput: EditText
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<Toolbar>(R.id.search_toolbar).setNavigationOnClickListener() {
            finish()
        }

        searchInput = findViewById(R.id.input_search_form)
        searchInput.requestFocus()
        searchInput.addTextChangedListener(inputTextWatcher)

        clearSearchButton = findViewById(R.id.clear_form)
        clearSearchButton.visibility = View.GONE
        clearSearchButton.setOnClickListener {
            clearSearchForm()
        }

    }

    private val inputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearSearchButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            searchQuery = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun clearSearchForm() {
        searchInput.setText("")

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        const val SAVED_SEARCH = "SAVED_SEARCH"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SEARCH, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SAVED_SEARCH, "")
        searchInput.setText(searchQuery)
    }

}