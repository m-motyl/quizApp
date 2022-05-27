package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

data class ReadReportModel(
    var id: Int,
    var title: String,
    var time_limit: Int,
    var description: String,
    var tags: String,
    var invitation_code: String,
    var image: ByteArray,
    var author:String,
    var no_questions: Int,
    var points: Double,
    var by: Int,
    var max_points: Int
): Serializable