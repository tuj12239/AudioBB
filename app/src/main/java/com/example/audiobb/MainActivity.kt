package com.example.audiobb

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import edu.temple.audlibplayer.PlayerService
import java.lang.NullPointerException

class MainActivity : AppCompatActivity(), BookListFragment.DoubleLayout, BookListFragment.Search,
                                            ControlFragment.Controller{

    //Books
    private val blankBook = Book("", "", -1, "", 0)
    var doubleFragment = false
    lateinit var bookViewModel: BookViewModel
    val bookList = BookList()
    var firstLoad = false
    lateinit var startForResult: ActivityResultLauncher<Intent>

    //Service
    lateinit var playerBinder: PlayerService.MediaControlBinder

    val timerHandler = Handler(Looper.getMainLooper()) {
        try {
            bookViewModel.setBookProgress((it.obj as PlayerService.BookProgress).progress)
            Log.i("Got message: ", "${(it.obj as PlayerService.BookProgress).progress}")
        } catch (npe: NullPointerException) {Log.i("Got message: ", "not playing")}
        true
    }

    var isConnected = false

    val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isConnected = true

            playerBinder = service as PlayerService.MediaControlBinder
            playerBinder.setProgressHandler(timerHandler)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("AudioBB")

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        doubleFragment = findViewById<FragmentContainerView>(R.id.fragmentContainerView2) != null

        startForResult = registerForActivityResult(StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    val jsonbooks = it.data?.getSerializableExtra("bookjson") as BookList
                    bookList.clear()
                    for (i in 0 until jsonbooks.size()) {
                        Log.i("Received Book:", jsonbooks.get(i).name)
                        bookList.add(jsonbooks.get(i))
                    }

                    if (firstLoad) {
                        loadFragments()
                        firstLoad = false
                    } else {
                        if (supportFragmentManager.fragments[0] !is BookListFragment) {
                            (supportFragmentManager.fragments[1] as BookListFragment)
                                .updateList(bookList)
                        } else {
                            (supportFragmentManager.fragments[0] as BookListFragment)
                                .updateList(bookList)
                        }
                    }
                } else {
                    Log.e("Error", "IS NULL LMAOOO")
                }
            }
        }

        //First load
        if (savedInstanceState == null) {

            bindService(Intent(this, PlayerService::class.java),
                serviceConnection,
                BIND_AUTO_CREATE)

            firstLoad = true
            makeSearch()
        } else {
            loadFragments()
        }
    }

    override fun selectionMade() {
        if (!doubleFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadFragments() {
        //First load
        if (firstLoad) {

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView3, ControlFragment.newInstance())
                .commit();

            bookViewModel.setSelectedBook(blankBook)

            if (doubleFragment) {
                //Don't add to back stack
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView1, BookListFragment.newInstance(bookList))
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView1, BookListFragment.newInstance(bookList))
                    .addToBackStack(null)
                    .commit()
            }
        }

        //Double screen
        if (doubleFragment) {

            if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment) {
                supportFragmentManager.popBackStack()
            }

            //If was single, and is now double
            if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null) {

                //Put RecyclerView back in fragmentContainerView1
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView2, BookDetailsFragment.newInstance())
                    .commit()
            }
        } else if (bookViewModel.getSelectedBook().value != blankBook) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun makeSearch() {
        startForResult.launch(Intent(this, BookSearchActivity::class.java))
    }

    override fun play() {
        bookViewModel.getSelectedBook().value?.let { playerBinder.play(it.id) }
    }

    override fun pause() {
        playerBinder.pause()
    }

    override fun stop() {
        playerBinder.stop()
    }

    override fun seek(progress: Int) {
        playerBinder.seekTo(progress)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBinder("playerBinder", playerBinder)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        playerBinder = savedInstanceState.getBinder("playerBinder") as PlayerService.MediaControlBinder
    }
}