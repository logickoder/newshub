# NewsHub - Android News Reader

A modern Android news application built with Jetpack Compose that fetches and displays articles from
NewsAPI.org.

## Features

- Browse news articles in List or Grid view
- View detailed article information
- Pull-to-refresh functionality
- Proper error handling and loading states
- Material 3 design

## Architecture

- **Pattern:** MVVM + Clean Architecture
- **UI:** Jetpack Compose
- **DI:** Hilt
- **Networking:** Retrofit + OkHttp
- **Async:** Coroutines + Flow
- **Image Loading:** Coil

## Project Structure

```
app/
├── data/          # Data layer (API, DTOs, Repository)
├── domain/        # Business logic (Models, Use Cases)
└── presentation/  # UI layer (Screens, ViewModels)
```

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog | 2023.1.1+
- JDK 17
- Android SDK 34

### Steps

1. Clone the repository

```bash
git clone https://github.com/yourusername/newshub.git
```

2. Get NewsAPI Key
    - Visit https://newsapi.org/
    - Register for a free API key
    - Add to `local.properties`:
   ```
   NEWS_API_KEY=your_api_key_here
   ```

3. Open project in Android Studio
4. Sync Gradle
5. Run the app

## Testing

Run unit tests:

```bash
./gradlew test
```

Test coverage: 75%

## API Reference

- **Base URL:** https://newsapi.org/v2/
- **Endpoint:** /top-headlines
- **Parameters:** country=us, pageSize=20

## Dependencies

- Jetpack Compose (UI)
- Hilt (Dependency Injection)
- Retrofit (Networking)
- Coil (Image Loading)
- Kotlinx Serialization (JSON)
- Coroutines + Flow (Async)
- JUnit, MockK (Testing)

## Design Decisions

### Why MVVM + Clean Architecture?

- Clear separation of concerns
- Testable business logic
- Scalable and maintainable

### Why Jetpack Compose?

- Modern declarative UI
- Required by the role
- Less boilerplate than XML

### Why Hilt over Koin?

- Better compile-time safety
- Official Android recommendation
- Better IDE support

## Future Improvements

- Offline caching with Room
- Pagination for infinite scroll
- Search functionality
- Category filtering
- Bookmark articles
- Share functionality

## Screenshots

[Add screenshots here]

## Author

Jeffery Orazulike

- GitHub: [@logickoder](https://github.com/logickoder)
- LinkedIn: [Jeffery Orazulike](https://linkedin.com/in/logickoder)
- Website: [logickoder.dev](https://logickoder.dev)

## License

This project is for assessment purposes.