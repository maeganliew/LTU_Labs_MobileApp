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
                val navController = rememberNavController() // The "Pilot" of your navigation

                NavHost(
                    navController = navController,
                    startDestination = Screen.MovieList.route
                ) {
                    // Route 1: The List
                    composable(Screen.MovieList.route) {
                        MovieDBApp(navController = navController)
                    }

                    // Route 2: The Detail Screen
                    composable(Screen.MovieDetail.route) { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")?.toLong()
                        MovieDetailScreen(movieId = movieId, navController = navController)
                    }

                    // Route 3: The Empty Screen (Requirement)
                    composable(Screen.ThirdScreen.route) {
                        ThirdScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDBApp(navController: NavHostController, modifier: Modifier = Modifier) {
    MovieList(
        movieList = Movies.getMovies(),
        modifier = modifier,
        navController = navController
    )
}

// iterating movie list, call function to draw movie card
@Composable
fun MovieList(movieList: List<Movie>, navController: NavHostController, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(movieList) { movie ->
            MovieListItemCard(movie, modifier = Modifier.padding(8.dp), navController = navController,)
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
        Row {
            Row {
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL
                            + Constants.POSTER_IMAGE_BASE_WIDTH
                            + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = modifier
                        .width(92.dp)
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
    val movie = Movies.getMovies().find { it.id == movieId }

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
            if (movie != null) {
                // Title
                Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Movie genre
                val genreNames = movie.genres.mapNotNull { genre ->
                    (genre as? Map<*, *>)?.get("name")?.toString()
                }

                Text(
                    text = "Genres: ${genreNames.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Link to movie homepage (Opens in Browser)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.homepage))
                        context.startActivity(intent)
                    }
                ) {
                    Text("Visit Official Homepage")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // open IMDB
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val imdbId = movie.imdbId // e.g., "tt0111161"

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