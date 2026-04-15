package com.example.moviedb.database

import com.example.moviedb.models.Movie
import com.example.moviedb.models.Genre

object Movies{
    fun getMovies(): List<Movie>{
        return listOf(
            Movie(
                1523145,
                "Your Heart Will Be Broken",
                "/iGpMm603GUKH2SiXB2S5m4sZ17t.jpg",
                "/1x9e0qWonw634NhIsRdvnneeqvN.jpg",
                "2026-03-26",
                "High school student Polina is saved from bullying at her new school and makes a deal with the main bully Bars: he must pretend to be her boyfriend and protect her, and she must do everything he says. During this game, the couple develops real feelings, but her family and classmates have reasons to separate the lovers.",
                listOf(Genre(10749, "Romance"), Genre(18, "Drama")),
                "",
                "tt38190257"
            ),
            Movie(
                83533,
                "Avatar: Fire and Ash",
                "/bRBeSHfGHwkEpImlhxPmOcUsaeg.jpg",
                "/iN41Ccw4DctL8npfmYg1j5Tr1eb.jpg",
                "2025-12-17",
                "In the wake of the devastating war against the RDA and the loss of their eldest son, Jake Sully and Neytiri face a new threat on Pandora: the Ash People, a violent and power-hungry Na'vi tribe led by the ruthless Varang. Jake's family must fight for their survival and the future of Pandora in a conflict that pushes them to their emotional and physical limits.",
                listOf(Genre(878, "Science Fiction"), Genre(12, "Adventure"),Genre(14, "Fantasy")),
                "https://www.avatar.com/movies/avatar-fire-and-ash",
                "tt1757678"
            ),
            Movie (
                1327819,
                "Hoppers",
                "/xjtWQ2CL1mpmMNwuU5HeS4Iuwuu.jpg",
                "/u53UYu5XG2hNgWGvs3xGhAVzypl.jpg",
                "2026-03-04",
                "Scientists have discovered how to 'hop' human consciousness into lifelike robotic animals, allowing people to communicate with animals as animals. Animal lover Mabel seizes an opportunity to use the technology, uncovering mysteries within the animal world beyond anything she could have imagined.",
                listOf(Genre(878, "Science Fiction"), Genre(12, "Adventure"),Genre(16, "Animation"),Genre(35, "Comedy"),Genre(10751, "Family")),
                "https://www.pixar.com/hoppers",
                "tt26443616"
            ),
            Movie (
                1171145,
                "Crime 101",
                "/tVvpFIoteRHNnoZMhdnwIVwJpCA.jpg",
                "/kKF4gEiBDArhONlCcunHVgBp3bA.jpg",
                "2026-02-11",
                "When an elusive thief whose high-stakes heists unfold along the iconic 101 freeway in Los Angeles eyes the score of a lifetime, with hopes of this being his final job, his path collides with a disillusioned insurance broker who is facing her own crossroads. Determined to crack the case, a relentless detective closes in on the operation, raising the stakes even higher.",
                listOf(Genre(80, "Crime"),Genre(53, "Thriller")),

                "https://www.amazon.com/salp/crime101?hhf",
                "tt32430579"
            ),
            Movie (
                1115544,
                "Mike & Nick & Nick & Alice",
                "/7F0jc75HrSkLVcvOXR2FXAIwuEv.jpg",
                "/uNToXatdunyvWXyXMrTI1nLvh6r.jpg",
                "2026-03-14",
                "Two gangsters and the woman they love try to survive the most dangerous night of their lives. As if that wasn’t enough, there’s one wild ingredient added to the mix: a time machine.",
                listOf(Genre(878, "Science Fiction"), Genre(80, "Crime"),Genre(35, "Comedy")),
                "https://www.20thcenturystudios.com/movies/mike-and-nick-and-nick-and-alice",
                "tt27552099"
            )
        )
    }
}
