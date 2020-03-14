package com.robiumautomations.polyhex.enums

import java.util.*

enum class DataType {

  image {
    override fun getPossibleExtensions() = listOf("png", "jpg", "jpeg", "svg", "gif")
  },
  video {
    override fun getPossibleExtensions() = listOf("mp4", "webm", "mkv", "wmv", "avi", "m4p", "3gp")
  },
  audio {
    override fun getPossibleExtensions() = listOf("mp3", "wav")
  },
  document {
    override fun getPossibleExtensions() = listOf("pdf, doc", "docx", "txt", "odt")
  },
  other {
    override fun getPossibleExtensions() = emptyList<String>()
  };

  abstract fun getPossibleExtensions(): List<String>

  companion object {
    fun resolveDataType(filename: String): DataType {
      return when (getFilenameExtension(filename)) {
        in image.getPossibleExtensions() -> image
        in video.getPossibleExtensions() -> video
        in audio.getPossibleExtensions() -> audio
        in document.getPossibleExtensions() -> document
        else -> other
      }
    }

    private fun getFilenameExtension(filename: String): String {
      return Optional.ofNullable(filename)
          .filter { f -> f.contains(".") }
          .map { f -> f.substring(filename.lastIndexOf(".") + 1) }.get()
    }
  }
}