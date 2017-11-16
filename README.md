# ChatMatch

ChatMatch is a string parser application that will filter input for mentions, emojis, and
urls.

## About Creation
This project was built and test with Android Studio 2.3.3
 
## Criteria
1. Mentions - A way to mention a user. Always starts with an '@' and ends when hitting a non-word 
character.
2. Emoji - For this exercise, you only need to consider 'custom' emoji which are alphanumeric 
strings, no longer than 15 characters, contained in colons. You can assume that anything matching 
this format is an emoticon.
3. Links - Any URLs contained in the message, along with the page's title.
4. The output should be a JSON object containing arrays of all matches parsed from the input string,
 displayed in the UI.

## How-To
- Generate docs
    - ./gradlew dokka
- Run unit tests
    - ./gradlew clean check
    - Note: I added tests for the JSoupTitleRetriever but left them commented out. They run and are
    viable, but they increase run times of a gradle clean check from 20 seconds to 1 minute and 20
    seconds. They also feel more like integration tests since they half way test Jsoup functionality
    which goes against the point of making an interface for that class to implement. Please feel 
    free to uncomment them and view that they do work!
- Run UI tests
    - ./gradlew connectedAndroidTest (with a device/emulator attached)

## Documents
- Kotlin docs for the internal logic classes can be found in the "docs" folder of this project

## Assumptions
- Assume that you can have duplicates of mentions, emojis, and links

## Libraries Used

| Component     | Description   | License  |
| ------------- |:-------------:| -----:|
| [Jsoup](https://jsoup.org/)        |  is a Java library for working with real-world HTML. | [MIT License](https://jsoup.org/license) |
| [Anko](https://github.com/google/auto/tree/master/value)        | a Kotlin library which makes Android application development faster and easier. | [Apache 2.0](https://github.com/Kotlin/anko/blob/master/LICENSE) |
| [Dagger 2](https://github.com/google/dagger)        | A compile-time evolution approach to dependency injection. | [Apache 2.0](https://github.com/google/dagger/blob/master/LICENSE.txt) |
| [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin)        | A small library that provides helper functions to work with Mockito in Kotlin. | [MIT License](https://github.com/nhaarman/mockito-kotlin/blob/master/LICENSE) |
| [Android-Check](https://github.com/noveogroup/android-check)        | Static code analysis plugin for Android project. | [2017 Noveo Group](https://github.com/noveogroup/android-check/blob/master/LICENSE.txt) |

### Why These Libraries
- Jsoup
    - Jsoup was used to grab the title of a page given a URL. This library was selected
    because of it's long running support and ease of use. There are a couple of different ways to 
    perform the grab, an inefficient way would be loading a WebView and now showing it, but Jsoup 
    is easy to use and has a clean way of retrieving a page's title. Ideally I would 
    like to use a library that had better error handling.
- Anko
    - Anko was used because it's simply a library that makes kotlin more readable and concise 
    without making things too obscure. Specficially the doAsync coroutine made the code more 
    readable.
- Dagger 2
    - In this case, the decision to use Dagger 2 in this case was purely to make our MainActivity more readable 
    and to hide the creation of the JsonCreator. Additionally, in a larger scale app the use of 
    dependency injection via Dagger 2 would make more sense since you'd have a centrally located 
    area for various dependencies.
- Mockito-Kotlin
    - Mockito-Kotlin was used so that we can select which classes / objects to test. This creates a
    nice separation of concerns when testing and helps to ensure that you only test what you want.
- Android-Check
    - The Android-Check is used to be able to run static analysis checks against the code base. 
    Checkstyle acts as a heavy linter on the project, helping to maintain a consistent code style 
    across the project which makes it easier for collaboration. The Android linter that runs this 
    gives suggestions and risk assessments against the code while suggesting best practices for 
    Android (Note: It's kotlin support isn't all there and it suggests fixes that don't work in 
    kotlin / don't make sense to change, hence the 17 violations). PMD is another static analysis 
    tool that is used to find common code mistakes before it's too late. Findbugs is also normally 
    used as a static analysis tool again used to find common programming mistakes. In this case, 
    however, the script was not running and would crash therefore we skip it's check on the project.
    
## Future Improvements

- Find A better way to handle the error cases from Jsoup. In Kotlin at least, there is no ask for 
protection of a try/catch so right now I catch an IOException and populate the link title with 
badRequest. This key could be used on the backend as a check or we could come up with a better way
 to handle bad cases while still providing a good user experience.
- Error handling for the callbacks. Right now the callbacks have a success case but it would be good
 to improve upon error cases and test that they are valid. If this was an SDK I would suggest having
 a fail case for the callback and return the exception to let the user handle the error case as they
 wish.
- Fully automated build system. On a larger scale having thorough testing / static analysis on the 
whole code base. I would additionally spend time to generate some code coverage metrics 
(though that metric and chasing 100% isn't always a good goal).
- Find a replacement for Findbugs for Kotlin to find simple code mistakes.
- Refine the UI even more
- Handle more edge cases for url processing (I'm particularly interested in how this is done on 
Stride)
- If layout handling got more complicated I would consider using anko layouts to clean things up, 
though kotlin out of the box with synthetic layouts handles this well
