package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

data class ReadAnswerModel(
    var answer_text: String? = null,
    var answer_image: ByteArray? = null,
    var is_Correct: Boolean = false,
    var is_Selected: Boolean = false
): Serializable