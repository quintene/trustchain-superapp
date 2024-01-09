package nl.tudelft.trustchain.peerai

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso
import nl.tudelft.trustchain.common.ui.BaseFragment


data class Album(val artist: String, val title: String, val year: String, val tags: List<String>, val magnet: String, val songs: List<String>, val payment: String, val author_image: String,val artwork: String,val author_description: String, val author_upcoming: List<Event>)



data class YoutubeItem(val artist: String, val title: String, val id: String)

data class Event(val context: String, val type: String, val startDate:String, val offers:String, val name:String, val location: Location);
data class Location(val type: String, val addressLocality:String)

class PeerAIFragment : BaseFragment(R.layout.fragment_peer_a_i) {


    private lateinit var textSearcherClient: TextSearcherClient;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val t = TextSearcherClient.create(requireContext());
            if (t!= null) {
                textSearcherClient =t;
            }

        } catch (exception: Exception ) {
            var test = "";
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // on below line we are initializing adapter for our list view.
        val view: View = inflater.inflate(R.layout.fragment_peer_a_i, container, false)

        view.findViewById<ListView>(R.id.results).adapter = YoutubeAdapter(
            requireContext(),
            emptyList<YoutubeItem>()
            )


        view.findViewById<SearchView>(R.id.searchview).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are checking
                // if query exist or not.


                return false
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onQueryTextChange(newText: String): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.


                try {
                    var results = listOf<Result>();

                    if (newText.length != 0) {
                        results = textSearcherClient.search(newText);
                    }
                    val gson = Gson()
                    val songs = ArrayList<YoutubeItem>();

                    for (res in results) {
                        val song = gson.fromJson(res.url, YoutubeItem::class.java)
                        songs.add(song);
                    }


                    val adapter = YoutubeAdapter(requireContext(), songs);
                    view.findViewById<ListView>(R.id.results).adapter = adapter;

                    (view.findViewById<ListView>(R.id.results).adapter as YoutubeAdapter).notifyDataSetChanged();

                }catch (e : Exception) {
                    var test = "";
                }


                return false
            }
        });


        // on below line we are adding on query
        // listener for our search view.



        return view;
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun findMostSimilarItems(query: String, threshold: Double): List<String> {
        /*
        val cosine = CosineSimilarity()


        // Create term frequency vector for query
        val queryVec = createTermFrequencyVector(query)

        // Create list of items with their cosine similarity scores
        val scores = items.map { item ->
            val itemVec = createTermFrequencyVector(item)
            val score = cosine.cosineSimilarity(queryVec, itemVec)
            Pair(item, score)
        }

        // Sort the list of items by their cosine similarity scores in descending order
        val sortedScores = scores.sortedByDescending { it.second }

        // Extract the list of items from the sorted list of item scores that meet the threshold
        val sortedItems = sortedScores.filter { it.second >= threshold }.map { it.first }

        return sortedItems*/

        return  listOf("");
    }


    class YoutubeAdapter(private val context: Context, private val arrayList: List<YoutubeItem>) : BaseAdapter() {
        private lateinit var titleTextView: TextView
        private lateinit var authorTextView: TextView

        override fun getCount(): Int {
            return arrayList.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var convertView = convertView
            convertView = LayoutInflater.from(context).inflate(R.layout.item_youtube, parent, false)

            // On Click play youtube url
            convertView.setOnClickListener {
                val item = arrayList[position]

                // Start Youtube Item activity with item which is unique class

                val intent = Intent(context, YoutubeItemActivity::class.java);
                intent.putExtra("title", item.title)
                intent.putExtra("artist", item.artist)
                intent.putExtra("id", item.id)
                startActivity(context, intent, Bundle())



                //showURLDialog(context, item)
            }

            titleTextView = convertView.findViewById(R.id.titleTextView)
            authorTextView = convertView.findViewById(R.id.authorTextView)

            titleTextView.text = " " + arrayList[position].title
            authorTextView.text = " " + arrayList[position].artist


            return convertView
        }

        private fun showURLDialog(context: Context, item: YoutubeItem) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.youtubeitem_info)


            // pass videoId to youtube player view to play video
            val youTubePlayerView: YouTubePlayerView = dialog.findViewById(R.id.youtube_player_view)
            // pass videoId to youTubePlayerView to play video
            youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    // do stuff with it
                    if (item.id != null) {
                        youTubePlayer.loadVideo(item.id, 0f)
                    }
                }
            })

            //getLifecycle().addObserver(youTubePlayerView);
            /*
                YouTubePlayerTracker tracker = new YouTubePlayerTracker();
                youTubePlayer.addListener(tracker);


                youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                    // do stuff with it
                    youTubePlayer.loadVideo(videoId, 0f)

                });*/

                /*
                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                        if (videoId != null) {
                            youTubePlayer.loadVideo(videoId, 0f)
                        }
                    }
                })
            */

            //magnetTextView.text = album.magnet
            //authorDescriptionTextView.text = album.author_description
            //yearTextView.text = album.year

            dialog.show()
        }

        fun extractVideoId(url: String): String? {
            val key = "v="
            val index = url.indexOf(key)

            return if (index != -1) {
                val startIndex = index + key.length
                val endIndex = url.indexOf('&', startIndex).takeIf { it != -1 } ?: url.length
                url.substring(startIndex, endIndex)
            } else {
                null
            }
        }
    }



    //Class MyAdapter
    class AlbumAdapter(private val context: Context, private val arrayList: List<Album>) : BaseAdapter() {
        private lateinit var titleTextView: TextView
        private lateinit var authorTextView: TextView
        private lateinit var albumImageView: ImageView
        override fun getCount(): Int {
            return arrayList.size
        }
        override fun getItem(position: Int): Any {
            return position
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var convertView = convertView
            convertView = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)


            convertView.setOnClickListener {
                val album = arrayList[position]
                showAuthorDescriptionDialog(context, album)
            }

            titleTextView = convertView.findViewById(R.id.titleTextView)
            authorTextView = convertView.findViewById(R.id.authorTextView)
            albumImageView= convertView.findViewById(R.id.albumImageView)

            titleTextView.text = " " + arrayList[position].title
            authorTextView.text = " " + arrayList[position].artist

            if (arrayList[position].artwork != null && arrayList[position].artwork.isNotEmpty()) {
                //albumImageView.setImageURI(arrayList[position].author_image.toUri())

                Picasso.get().load(arrayList[position].artwork).into(albumImageView)
            }

            return convertView
        }
        private fun showAuthorDescriptionDialog(context: Context, album: Album) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.album_info)

            val authorDescriptionTextView: TextView = dialog.findViewById(R.id.authorDescriptionTextView)
            val authorImageView: ImageView = dialog.findViewById(R.id.authorImageView)
            val magnetTextView: TextView = dialog.findViewById(R.id.magnetTextView)
            val yearTextView: TextView = dialog.findViewById(R.id.yearTextView)


            if (album.author_image != null && album.author_image.isNotEmpty()) {
                //albumImageView.setImageURI(arrayList[position].author_image.toUri())

                Picasso.get().load(album.author_image).into(authorImageView)
            }
            magnetTextView.text = album.magnet
            authorDescriptionTextView.text = album.author_description
            yearTextView.text = album.year

            dialog.show()
        }


    }

}
