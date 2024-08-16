package com.example.logicgo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

data class DataModel(
        var web_url: String? = null,
        var name: String? = null,
        var thumbnail_url: String?,
        var group_id: String?,
        var video_duration: String?,
        var image_url_list: ArrayList<String>?,
        var video_url_list: ArrayList<VideoModel>?,
        var isVideo: Boolean = false,
        var isSelectedItem: Boolean = false,
        var length: Long = 0L
)

data class VideoModel(
        var video_url: String,
        var size: String,
        var quality: String,
        var format: String,
        var length:Long,
        var isSelected: Boolean = false
)

data class FileModel(
        var file: File
)

data class SeqModel(
        var question: String, var answer: String
)

@Entity(tableName = "History")
data class History(
        var title: String,
        var link: String,
        @PrimaryKey val id: Long = System.currentTimeMillis()
)


/*@Entity(tableName = "users")
data class User(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
        val email: String
)*/
