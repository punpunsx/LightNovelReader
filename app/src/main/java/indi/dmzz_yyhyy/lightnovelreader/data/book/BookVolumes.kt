package indi.dmzz_yyhyy.lightnovelreader.data.book

data class BookVolumes(
    val volumes: List<Volume>
) {
    companion object {
        fun empty() = BookVolumes(emptyList())
    }
}
