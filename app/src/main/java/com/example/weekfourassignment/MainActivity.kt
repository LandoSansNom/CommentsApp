package com.example.weekfourassignment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weekfourassignment.databinding.ActivityMainBinding
import com.example.weekfourassignment.remote.Status
import com.example.weekfourassignment.ui.CommentsLiveDataViewModel
import com.example.weekfourassignment.ui.CommentsViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // create a CommentsViewModel
    // variable to initialize it later
    private lateinit var viewModel: CommentsViewModel

    private lateinit var viewLiveDataModel: CommentsLiveDataViewModel


    // create a view binding variable
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // instantiate view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize viewModel
        //  viewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)
        viewLiveDataModel = ViewModelProvider(this).get(CommentsLiveDataViewModel::class.java)


        // Listen for the button click event to search
        binding.button.setOnClickListener {

            // check to prevent api call with no parameters
            if (binding.searchEditText.text.isNullOrEmpty()) {
                Toast.makeText(this, "Query Can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                // if Query isn't empty, make the api call
                //  viewModel.getNewComment(binding.searchEditText.text.toString().toInt())
                viewLiveDataModel.getNewComment(binding.searchEditText.text.toString().toInt())
            }
        }
        // Since flow run asynchronously,
        // start listening on background thread

        viewLiveDataModel.commentState.observe(this) { commentApiState ->
            // Handle the changes in the commentState LiveData here
            when (commentApiState.status) {
                Status.LOADING -> {
                    // Show loading state
                    binding.progressBar.isVisible = true
                }

                Status.SUCCESS -> {
                    val commentModel = commentApiState.data
                    binding.progressBar.isVisible = false
                    // Received data can be null, put a check to prevent
                    // null pointer exception
                    commentApiState.data?.let { comment ->
                        binding.commentIdTextview.text = comment.id.toString()
                        binding.nameTextview.text = comment.name
                        binding.emailTextview.text = comment.email
                        binding.commentTextview.text = comment.comment
                    }
                }

                Status.ERROR -> {
                    val errorMessage = commentApiState.message
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        this@MainActivity,
                        "${commentApiState.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
      //  viewLiveDataModel.getNewComment(1)

    }

    // Call the function to fetch new comments


//        lifecycleScope.launch {
//
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.commentState.collect {
//
//
//                    // When state to check the
//                    // state of received data
//                    when (it.status) {
//
//                        // If its loading state then
//                        // show the progress bar
//                        Status.LOADING -> {
//                            binding.progressBar.isVisible = true
//                        }
//                        // If api call was a success , Update the Ui with
//                        // data and make progress bar invisible
//                        Status.SUCCESS -> {
//                            binding.progressBar.isVisible = false
//
//                            // Received data can be null, put a check to prevent
//                            // null pointer exception
//                            it.data?.let { comment ->
//                                binding.commentIdTextview.text = comment.id.toString()
//                                binding.nameTextview.text = comment.name
//                                binding.emailTextview.text = comment.email
//                                binding.commentTextview.text = comment.comment
//                            }
//                        }
//                        // In case of error, show some data to user
//                        else -> {
//                            binding.progressBar.isVisible = false
//                            Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//                }
//            }
//        }
}

