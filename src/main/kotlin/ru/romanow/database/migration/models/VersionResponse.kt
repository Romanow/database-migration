package ru.romanow.database.migration.models

data class VersionResponse(
    var version: String,
    var commit: String,
    var time: String,
)
