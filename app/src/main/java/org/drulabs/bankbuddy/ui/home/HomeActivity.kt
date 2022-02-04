package org.drulabs.bankbuddy.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import org.drulabs.bankbuddy.R
import org.drulabs.bankbuddy.presentation.factory.ViewModelFactory
import org.drulabs.bankbuddy.presentation.model.Status
import org.drulabs.bankbuddy.presentation.model.UserInfo
import org.drulabs.bankbuddy.presentation.viewmodels.HomeVM
import org.drulabs.bankbuddy.ui.transactions.TransactionList
import org.drulabs.bankbuddy.utils.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("MMM - dd", Locale.getDefault())

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var homeVM: HomeVM

    private var userInfo: UserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeVM = ViewModelProviders.of(this, viewModelFactory).get(HomeVM::class.java)
        homeVM.userInfoResource.observe(this, Observer { resource ->
            println("Observed...")
            when (resource.status) {
                Status.LOADING -> {
                    println("Loading")
                    showLoader()
                }
                Status.ERROR -> {
                    println("ERROR")
                    userInfo = null
                    hideLoader()
                    tvHomeMessage.text = getText(R.string.msg_something_went_wrong)
                }
                Status.SUCCESS -> {
                    println("Success: ${resource.data}")
                    hideLoader()
                    resource.data?.let {
                        tvHomeUserName.text = it.displayName
                        tvHomeAccountNumber.text =
                            String.format(
                                getString(R.string.display_account_num),
                                it.accountNumber
                            )
                        tvHomeBalance.text =
                            String.format(
                                getString(R.string.display_account_balance),
                                it.accountBalance.toCurrency()
                            )

                        tvHomePremiumAccount.text = when (it.premiumCustomer) {
                            true -> getText(R.string.title_premium_perks)
                            false -> getText(R.string.title_upgrade_to_premium)
                        }

                        tvHomeAccountType.text = it.accountType.capitalize()

                        userInfo = it
                    }
                }
            }
        })

        displayBankOperationsClosedMessage()

        setClickListeners()
    }

    private fun setClickListeners() {
        llHomePremiumAccount.setOnClickListener {
            userInfo?.let {
                val title = when (it.premiumCustomer) {
                    true -> getText(R.string.title_premium_perks)
                    false -> getText(R.string.title_upgrade_to_premium)
                }
                showAlertDialog(
                    title,
                    R.string.msg_premium_account_benefits
                )

            }
        }

        llHomeImportantInfo.setOnClickListener {
            userInfo?.let {
                showAlertDialog(
                    getText(R.string.title_important_info),
                    R.string.msg_important_information
                )
            }
        }

        llHomeAccountType.setOnClickListener {
            userInfo?.let {
                showAlertDialog(
                    getText(R.string.title_important_info),
                    R.string.msg_account_type_info
                )
            }
        }

        llHomeTransactions.setOnClickListener {
            val transactionsIntent = Intent(this, TransactionList::class.java)
            startActivity(transactionsIntent)
        }
    }

    /**
     * SHows loading indicator and blurs other views
     */
    private fun showLoader() {
        cvAccountInfoHolder.alpha = ALPHA_HIDDEN
        tvHomeMessage.alpha = ALPHA_HIDDEN
        llHomeActionHolder.alpha = ALPHA_HIDDEN
        pbHomeLoader.visibility = View.VISIBLE
    }

    /**
     * Hides loading indicator
     */
    private fun hideLoader() {
        cvAccountInfoHolder.alpha = ALPHA_VISIBLE
        tvHomeMessage.alpha = ALPHA_VISIBLE
        llHomeActionHolder.alpha = ALPHA_VISIBLE
        pbHomeLoader.visibility = View.GONE
    }

    /**
     * Displays dummy Message, shows (current date + 3) days as bank closed day
     */
    private fun displayBankOperationsClosedMessage() {
        val operationShutDay = Calendar.getInstance().addDays(DAYS_AFTER_CURRENT_DATE)
        tvHomeMessage.text = String.format(
            getString(R.string.msg_operations_closed),
            dateFormat.format(operationShutDay.time)
        )
    }

    /**
     * Displays an alert dialog
     * @param title Title of the alert dialog
     * @param contentId string resource id of the content
     */
    private fun showAlertDialog(title: CharSequence, @StringRes contentId: Int) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(contentId)
            .setCancelable(true)
            .setPositiveButton(R.string.btn_txt_dismiss) { dialog, _ ->
                dialog.dismiss()
            }.create()
            .show()
    }
}
