package brandenc.com.chatmatch

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import brandenc.com.chatmatch.Activities.MainActivity
import android.support.test.rule.ActivityTestRule
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.assertion.ViewAssertions.matches

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun testSubmit() {
        val input = "Hello @Branden, How are you doing today? :yougotitdude: :woah:"

        onView(withId(R.id.chatText))
                .perform(typeText(input), closeSoftKeyboard())

        onView(withId(R.id.submit)).perform(click())

        val expectedOutputJson = "{\n    \"mentions\": \"[Branden]\",\n    \"emojis\": " +
                "\"[yougotitdude, woah]\",\n    \"links\": \"[]\"\n}"

        onView(withId(R.id.jsonOutput))
                .check(matches(withText(expectedOutputJson)))
    }
}
