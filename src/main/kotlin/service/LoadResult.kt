package coursework.service

sealed class LoadResult {
    data class Success(val count: Int) : LoadResult()
    data object FileNotFound : LoadResult()
    data class Error(val message: String) : LoadResult()
}

