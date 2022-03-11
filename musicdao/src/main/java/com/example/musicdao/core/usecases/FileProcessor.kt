package com.example.musicdao.core.usecases

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.musicdao.core.torrent.ReleaseProcessor
import com.example.musicdao.core.util.Util
import com.mpatric.mp3agic.Mp3File
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.file.Path
import java.nio.file.Paths

@RequiresApi(Build.VERSION_CODES.O)
class FileProcessor {

    companion object {
        fun getTitle(mp3: Mp3File): String {
            val title = Util.getTitle(mp3)
            if (title != null) {
                return title
            }

            val sanitizedFileName = Util.checkAndSanitizeTrackNames(mp3.filename)
            if (sanitizedFileName != null) {
                return sanitizedFileName
            }

            return mp3.filename
        }

        /**
         * Heuristically retrieves cover art from root folder. If not available, attempts
         * to parse from MP3 id3v2Tag and saves it to root folder.
         *
         * @param root release root folder
         * @return cover art file
         */
        fun getCoverArt(root: Path): File? {
            val allowedExtensions = listOf("jpg", "png")
            val folder = root.toFile()

            if (!folder.exists()) {
                return null
            }

            val files = ReleaseProcessor.getFiles(folder) ?: return null
            val mp3Files = ReleaseProcessor.getMP3Files(folder.toPath()) ?: return null

            // 1. cover.jpg
            val first = files.find { file ->
                file.nameWithoutExtension.lowercase() == "cover" &&
                    (allowedExtensions.firstOrNull { it == file.extension } != null)
            }

            if (first != null) {
                return first
            }

            // 2. any other image
            val second = files.find { file ->
                allowedExtensions.firstOrNull { it == file.extension } != null
            }

            if (second != null) {
                return first
            }

            // 3. parse from mp3 and return if found
            var cover: File? = null
            val filesWithTags = mp3Files.filter { it.hasId3v2Tag() }
            for (mp3File in filesWithTags) {
                try {
                    val imageBytes = mp3File.id3v2Tag.albumImage
                    val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    val coverFile = Paths.get("$root/cover.png").toFile()
                    val fileOut = FileOutputStream(coverFile)
                    bmp.compress(Bitmap.CompressFormat.PNG, 85, fileOut)
                    fileOut.flush()
                    fileOut.close()
                    cover = coverFile
                    break
                } catch (exception: Exception) {
                }
            }

            if (cover != null) {
                return cover
            }

            return null
        }
    }
}