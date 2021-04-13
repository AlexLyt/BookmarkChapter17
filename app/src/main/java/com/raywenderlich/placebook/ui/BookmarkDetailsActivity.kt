package com.raywenderlich.placebook.ui

import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.placebook.R
//import kotlinx.android.synthetic.main.activity_bookmark_details.*
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer
import com.raywenderlich.placebook.databinding.ActivityBookmarkDetailsBinding
import com.raywenderlich.placebook.viewmodel.BookmarkDetailsViewModel


class BookmarkDetailsActivity : AppCompatActivity() {

    private lateinit var databinding: ActivityBookmarkDetailsBinding
    private val bookmarkDetailsViewModel by viewModels<BookmarkDetailsViewModel>()
    private var bookmarkDetailsView:BookmarkDetailsViewModel.BookmarkDetailsView? = null


    override fun onCreate(
        savedInstanceState:
        android.os.Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark_details)
        setupToolbar()
        getIntentData()
    }

    private fun setupToolbar() {                 // this is an issue page 361
        setSupportActionBar(toolbar)             // page 370
    }

    private fun populateFields() {                     // page 370
        bookmarkDetailsView?.let { bookmarkView ->
            editTextName.setText(bookmarkView.name)
            editTextPhone.setText(bookmarkView.phone)
            editTextNotes.setText(bookmarkView.notes)
            editTextAddress.setText(bookmarkView.address)
        }
    }


    private fun populateImageView() {
        bookmarkDetailsView?.let { bookmarkView ->
            val placeImage = bookmarkView.getImage(this)
            placeImage?.let {
                imageViewPlace.setImageBitmap(placeImage)
            }
        }
    }

    private fun getIntentData() {

        val bookmarkId = intent.getLongExtra(
            MapsActivity.Companion.EXTRA_BOOKMARK_ID, 0)

        bookmarkDetailsViewModel.getBookmark(bookmarkId)?.observe(
            this,
            Observer<BookmarkDetailsViewModel.BookmarkDetailsView> {

                it?.let {
                    bookmarkDetailsView = it
// Populate fields from bookmark
                    populateFields()
                    populateImageView()
                }
            })
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu):
            Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_bookmark_details, menu)
        return true
    }

    private fun saveChanges() {
        val name = editTextName.text.toString()
        if (name.isEmpty()) {
            return
        }
        bookmarkDetailsView?.let { bookmarkView ->
            bookmarkView.name = editTextName.text.toString()
            bookmarkView.notes = editTextNotes.text.toString()
            bookmarkView.address = editTextAddress.text.toString()
            bookmarkView.phone = editTextPhone.text.toString()
            bookmarkDetailsViewModel.updateBookmark(bookmarkView)
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveChanges()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}