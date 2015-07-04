# PopularMovies
Android application for getting the list of popular movies from TMDB. I did this to practice the android concepts that I have learned

App fetches data from TMDB and shows the details of the most popular movies and the highest user rated movies. The app stores data in a DB so that it need not be fetched always. The app stores state between orientation changes as well.
The app uses 2 pane layout for tablets and single pane layout for mobiles.You can also mark your movies as favorites and the data will get stored offline.

Concepts used
1. Android Layout support for different devices
2. Content Resolvers and Cursor Adapters for putting data into the DB and updating the listView
3. Sync Adapter for efficiently fetching the data.
4. Custom View for generating the square tiles.
5. Async Task for getting the data related to tarilers and reviews.
