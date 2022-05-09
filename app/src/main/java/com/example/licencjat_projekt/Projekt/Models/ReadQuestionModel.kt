package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

data class ReadQuestionModel(
    var question_text: String? = null,
    var question_image: ByteArray? = null,
    var question_pts: Int,
    var question_answers: ArrayList<ReadAnswerModel>,
    var number: Int
): Serializable