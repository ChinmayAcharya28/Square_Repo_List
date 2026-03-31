# Square repos (GitHub)

This is a small Android app that hits GitHub’s public API and lists repositories for the **square** organization. There’s a single screen: a scrollable list with each repo’s name, description, owner avatar, and a few stats (language, license SPDX if present, stars, forks, last activity date). Nothing fancy—loading, empty, and error states are handled explicitly so the UI doesn’t flash or hang silently.

The project asked for `GET https://api.github.com/orgs/square/repos`; the app uses that endpoint (first page, 100 items) via Retrofit.

## Output


## Running it

You’ll want a recent Android Studio and SDK 36, or change `compileSdk` in `app/build.gradle.kts` to whatever you have. **minSdk is 24**, Java **11**.

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

Instrumented tests (needs a device or emulator):

```bash
./gradlew connectedDebugAndroidTest
```

## What I chose and why

**UI:** Jetpack Compose and Material 3. One `MainActivity`, one composable tree. The list row is split into `RepositoryCard` and `RepoOwnerAvatar` so the main screen file doesn’t turn into a 500-line blob. Spacing and sizes live in `RepositoriesDimens` so tweaking padding doesn’t mean editing half a dozen files.

**State:** MVVM with a `StateFlow` of `RepositoriesUiState`—either loading, a list (possibly empty), or an error with retry. That maps cleanly onto the challenge’s expectation of handling failure as well as success.

**Data:** Retrofit + Moshi for JSON, OkHttp with logging only in debug. DTOs map to a small `Repo` domain type; the UI gets a `RepoUiModel` with display-ready strings (e.g. formatted dates) via `RepoUiModelMapper`, so the ViewModel doesn’t own formatting rules.

**DI:** Koin with two modules—`networkModule` (Moshi, client, Retrofit, API interface) and `appModule` (repository + ViewModel). I went with Koin because Hilt’s Gradle plugin and AGP 9 were fighting in this project; the brief allowed any reasonable DI choice.

**Images:** Coil loads `owner.avatar_url`; if the URL is missing or fails, you see a simple code icon instead of a broken image.

## Tests

There are JVM unit tests for the DTO → domain mapper, the ViewModel (including error paths) with a fake repository, and the date formatter. There’s a small Compose UI test that checks loading and error screens use the right test tags.

After `./gradlew testDebugUnitTest`, the usual Gradle HTML report is under `app/build/reports/tests/testDebugUnitTest/index.html`. For line coverage on debug bytecode:

```bash
./gradlew :app:jacocoTestReport
```

HTML: `app/build/reports/jacoco/jacocoTestReport/html/index.html`. That coverage is from **unit tests only**—Compose UI is mostly covered by instrumented tests, so don’t expect high % on composables unless you add more tests there.

## Assumptions

Only the **first page** of results is loaded (`per_page=100`). No auth token, so normal GitHub rate limits apply. Missing descriptions show as “No description” (still two lines max with ellipsis). Language and license rows omit themselves when GitHub sends null/blank.
