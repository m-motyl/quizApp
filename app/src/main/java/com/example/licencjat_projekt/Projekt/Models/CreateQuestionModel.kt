package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

data class CreateQuestionModel(
    var question_text: String,
    var question_image: ByteArray,
    var question_pts: Int,
    var question_answers: ArrayList<AnswerModel>
): Serializable