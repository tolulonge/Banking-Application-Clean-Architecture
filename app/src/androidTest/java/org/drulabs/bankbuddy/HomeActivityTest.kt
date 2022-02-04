package org.drulabs.bankbuddy

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.drulabs.bankbuddy.ui.home.HomeActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {

    @get:Rule
    val rule = ActivityTestRule<HomeActivity>(HomeActivity::class.java)

    @Test
    fun test_right_userName_displayed() {
        onView(withId(R.id.tvHomeUserName))
            .check(matches(withText("Susan Dunphee")))
    }

    @Test
    fun test_correct_balance_displayed() {
        onView(withId(R.id.tvHomeBalance))
            .check(matches(withText("Balance: $14,056.90")))
    }

    @Test
    fun test_correct_transaction_count_in_title() {
        onView(withId(R.id.llHomeTransactions))
            .perform(click())
        matchToolbarTitle("Transactions (4)")
    }

    @Test
    fun test_debit_filter_in_transaction_list() {
        onView(withId(R.id.llHomeTransactions))
            .perform(click())
        onView(withId(R.id.chkTransactionDebit))
            .perform(click())
        matchToolbarTitle("Transactions (2)")
    }

    @Test
    fun test_flagged_filter_in_transaction_list() {
        onView(withId(R.id.llHomeTransactions))
            .perform(click())
        onView(withId(R.id.chkTransactionFlagged))
            .perform(click())
        matchToolbarTitle("Transactions (1)")
    }

    @Test
    fun test_no_data_displayed_for_credit_flagged_filter() {
        onView(withId(R.id.llHomeTransactions))
            .perform(click())
        onView(withId(R.id.chkTransactionFlagged))
            .perform(click())
        onView(withId(R.id.chkTransactionCredit))
            .perform(click())
        matchToolbarTitle("Transactions (0)")
    }

    private fun matchToolbarTitle(title: String): ViewInteraction {
        return onView(
            allOf(
                instanceOf(TextView::class.java),
                withParent(withResourceName("action_bar"))
            )
        ).check(matches(withText(title)))
    }

}
