package com.example.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.database.Movies
import com.example.moviedb.models.Movie
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.moviedb.utils.Constants
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moviedb.Screen.ThirdScreen
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import android.util.Log
import com.example.moviedb.utils.SECRETS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

sealed class Screen(val route: String) {
    object MovieList : Screen("movie_list")
    object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: Long) = "movie_detail/$movieId"
    }
    object ThirdScreen : Screen("third_screen")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieDBTheme {
                val navController = rememberNavController()
                val moviesState = remember { mutableStateOf<List<Movie>>(emptyList()) }
                val apiKey = SECRETS.API_KEY

                LaunchedEffect(Unit) {
                    try {
                        val response = RetrofitClient.instance.getNowPlayingMovies(apiKey)
                        moviesState.value = response.results
                    } catch (e: Exception) {
                        Log.e("API_ERROR", "Failed to fetch: ${e.message}")
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Screen.MovieList.route
                ) {
                    // The Grid
                    composable(Screen.MovieList.route) {
                        MovieDBApp(movieList = moviesState.value, navController = navController)
                    }

                    // The Detail Screen
                    composable(Screen.MovieDetail.route) { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")?.toLong()
                        MovieDetailScreen(movieId = movieId, navController = navController)
                    }

                    // The Empty Screen
                    composable(Screen.ThirdScreen.route) {
                        ThirdScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDBApp(movieList: List<Movie>, navController: NavHostController, modifier: Modifier = Modifier) {
    MovieList(
        movieList = movieList,
        modifier = modifier,
        navController = navController
    )
}

// iterating movie list, call function to draw movie card
@Composable
fun MovieList(
    movieList: List<Movie>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(movieList) { movie ->
            // will automatically resize for the grid
            MovieListItemCard(
                movie = movie,
                navController = navController,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
// draw one card for each movie
@Composable
fun MovieListItemCard(movie: Movie,
                      navController: NavHostController,
                      modifier: Modifier = Modifier) {
    Card(modifier = modifier.clickable {
        // Add navigation
        navController.navigate(Screen.MovieDetail.createRoute(movie.id))
    }) {
        Column {
            Column {
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL
                            + Constants.POSTER_IMAGE_BASE_WIDTH
                            + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier=Modifier.size(8.dp))

                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier=Modifier.size(8.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier=Modifier.size(8.dp))
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    MovieDBApp(
        movieList = emptyList(),
        navController = navController,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieDBTheme {
        Greeting()
    }
}

@Composable
fun ThirdScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("This is the Third Screen (Empty)")
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(movieId: Long?, navController: NavHostController) {
    val context = LocalContext.current
    var movieDetails by remember { mutableStateOf<Movie?>(null) }

    LaunchedEffect(movieId) {
        if (movieId != null) {
            try {
                movieDetails = RetrofitClient.instance.getMovieDetails(movieId, SECRETS.API_KEY)
            } catch (e: Exception) {
                Log.e("DETAIL_ERROR", "Error: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Important: Use the Scaffold padding
                .padding(24.dp)
        ) {
            if (movieDetails != null) {
                val currentMovie = movieDetails!!
                // Title
                Text(text = currentMovie.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Movie genre
                val genreNames = currentMovie.genres.joinToString(", ") { it.name }
                Text(text = "Genres: $genreNames", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                // Visit Homepage
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentMovie.homepage))
                        // Open with
                        val chooser = Intent.createChooser(webIntent, "Open homepage with:")
                        context.startActivity(chooser)
                    }
                ) {
                    Text("Visit Official Homepage")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // open IMDB
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val imdbId = currentMovie.imdbId // e.g., "tt0111161"

                        // This is the URI scheme that specificially tells the IMDB app to open a title page
                        val appUri = Uri.parse("imdb:///title/$imdbId/")
                        val webUri = Uri.parse("https://www.imdb.com/title/$imdbId/")

                        val intent = Intent(Intent.ACTION_VIEW, appUri)

                        try {
                            // Attempt to open the IMDB App directly to the movie page
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // If the IMDB app isn't installed OR doesn't handle that URI, use the browser
                            val browserIntent = Intent(Intent.ACTION_VIEW, webUri)
                            context.startActivity(browserIntent)
                        }
                    }
                ) {
                    Text("Open in IMDB App")
                }
            } else {
                Text("Movie not found")
            }

            Spacer(modifier = Modifier.weight(1f)) // Push the navigation button to the bottom
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.ThirdScreen.route) }
            ) {
                Text("Go to Empty Third Screen")
            }
        }
    }
}