package com.example.licencjat_projekt.Projekt.Activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.CreateQuestionModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.database.Quiz
import com.example.licencjat_projekt.Projekt.database.Quizes
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_detail_quiz.*
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DetailQuizActivity : AppCompatActivity(), View.OnClickListener {
    private var quizDetails: ReadQuizModel? = null
    private var userIsAuthor: Boolean = false

    companion object {
        var QUESTION_DETAILS = "question_details"
        var QUIZ_DETAILS = "quiz_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quiz)

        if (intent.hasExtra(MainActivity.QUIZ_DETAILS)) {
            quizDetails = intent.getSerializableExtra(MainActivity.QUIZ_DETAILS) as ReadQuizModel
        }
        if (intent.hasExtra(UserQuizesActivity.QUIZ_DETAILS)) {
            quizDetails =
                intent.getSerializableExtra(UserQuizesActivity.QUIZ_DETAILS) as ReadQuizModel
        }
        if (intent.hasExtra(SearchActivity.QUIZ_DETAILS)) {
            quizDetails = intent.getSerializableExtra(SearchActivity.QUIZ_DETAILS) as ReadQuizModel
        }

        if (quizDetails != null) {
            setSupportActionBar(detail_quiz_toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            detail_quiz_toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            supportActionBar!!.title = quizDetails!!.title
            detail_quiz_image.setImageBitmap(byteArrayToBitmap(quizDetails!!.image))
            detail_quiz_title.text = quizDetails!!.invitation_code
            detail_quiz_description.text = quizDetails!!.description
            detail_quiz_tags.text = quizDetails!!.tags
            detail_quiz_timer.text = quizDetails!!.time_limit.toString() + " minut(y)"
        }
        checkIfUserIsAuthor()
        if (userIsAuthor) {
            detail_delete_quiz.visibility = View.VISIBLE
        } else {
            detail_delete_quiz.visibility = View.GONE
        }

        question_get_started.setOnClickListener(this)
        detail_quiz_invite_btn.setOnClickListener(this)
        detail_delete_quiz.setOnClickListener(this)
        detail_quiz_author_name.text = getAuthorName()
        detail_quiz_number_questions.text = getNOQuestions()
    }


    private fun byteArrayToBitmap(
        data: ByteArray
    ): Bitmap {
        return BitmapFactory.decodeByteArray(
            data,
            0,
            data.size
        )
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.detail_delete_quiz -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Czy na pewno chcesz usunąć quiz ?")
                val items = arrayOf(
                    "Tak",
                    "Nie"
                )
                alert.setItems(items) { _, n ->
                    when (n) {
                        0 -> {
                            deleteQuiz()
                            val intent = Intent(
                                this,
                                MainActivity::class.java
                            )
                            startActivity(intent)
                        }
                        1 -> goBack()
                    }
                }
                alert.show()
            }
            R.id.question_get_started -> {
                val intent = Intent(
                    this,
                    QuestionsShowActivity::class.java
                )
                intent.putExtra(QUESTION_DETAILS, quizDetails)
                startActivity(intent)

                finish()
            }
            R.id.detail_quiz_invite_btn -> {
                if(checkIfUserHaveAnyFriends()) {
                    val intent = Intent(
                        this,
                        CommunityQuizInviteActivity::class.java
                    )
                    intent.putExtra(QUIZ_DETAILS, quizDetails)
                    startActivity(intent)
                }else{
                    Toast.makeText(
                        this,
                        "Brak znajomych!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getAuthorName(): String {
        return quizDetails!!.author
    }

    private fun getNOQuestions(): String {
        return quizDetails!!.no_questions.toString()
    }

    private fun checkIfUserIsAuthor() {
        userIsAuthor = (quizDetails!!.author == currentUser!!.login)
    }

    private fun deleteQuiz() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            Quiz.findById(quizDetails!!.id)!!.delete()
        }
    }

    private fun goBack() {}
    private fun checkIfUserHaveAnyFriends(): Boolean {
        return true
    } //TODO WITOLD true - ma znajomych,
}