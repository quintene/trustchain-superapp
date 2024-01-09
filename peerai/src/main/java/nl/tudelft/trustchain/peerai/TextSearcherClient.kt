package nl.tudelft.trustchain.peerai

import android.content.Context
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.processor.SearcherOptions
import org.tensorflow.lite.task.text.searcher.TextSearcher
import java.io.Serializable



// Imports from custom AAR.
//import org.tensorflow.lite.task.core.BaseOptions
//import org.tensorflow.lite.task.processor.SearcherOptions
//import org.tensorflow.lite.task.text.searcher.TextSearcher


class TextSearcherClient(private var textSearcher: TextSearcher) {

    companion object {
        private const val MODEL_PATH = "youtube_20K.tflite"
        private const val NUM_THREADS = 4
        private const val MAX_RESULTS = 10
        private const val IS_NORMALIZE = true
        fun create(context: Context): TextSearcherClient? {

            try {
                val baseOptions = BaseOptions.builder().setNumThreads(NUM_THREADS).build()
                val searchOptions =
                    SearcherOptions.builder().setMaxResults(MAX_RESULTS)
                        .setL2Normalize(IS_NORMALIZE).build()
                val options =
                    TextSearcher.TextSearcherOptions.builder()
                        .setBaseOptions(baseOptions)
                        .setSearcherOptions(searchOptions)
                        .build()
                val textSearcher =
                    TextSearcher.createFromFileAndOptions(context, MODEL_PATH, options)


                return TextSearcherClient(textSearcher)

            } catch (exception: Exception) {
                var test = "";
            }
            return null
        }
    }

    fun search(query: String): List<Result> {
        val results = mutableListOf<Result>()
        val modelResults = textSearcher.search(query)

        // Postprocess the model output to human readable class names
        modelResults.forEach { results.add(Result(it.distance, String(it.metadata.array()))) }
        return results
    }

    fun close() {
        textSearcher.close()
    }
}

data class Result(val distance: Float, val url: String) : Serializable
