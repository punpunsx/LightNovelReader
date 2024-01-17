package indi.dmzz_yyhyy.lightnovelreader.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadingBook(
    @PrimaryKey val id: Int,
    var bookName: String,
    var coverUrl: String,
    var introduction: String,
)