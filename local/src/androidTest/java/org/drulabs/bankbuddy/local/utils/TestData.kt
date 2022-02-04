package org.drulabs.bankbuddy.local.utils

import org.drulabs.bankbuddy.local.model.TransactionLocal
import org.drulabs.bankbuddy.local.model.UserInfoLocal

class TestData{

    companion object {
        fun generateUserInfo(): UserInfoLocal {
            return UserInfoLocal(
                "1BFC9A38E6C7",
                "John Doe",
                "307, Palm drive, Virdigris Square, CA - 95014",
                "March-12, 2018",
                false,
                4579.75,
                "savings",
                4
            )
        }

        fun generateTransactions(): List<TransactionLocal> {
            val t1 = TransactionLocal(
                "B2C148D3-F48A-6757-FADF-1BFC9A38E6C7",
                "debit",
                42125,
                "retail, manual, debit-card",
                1541007660,
                false,
                ""
            )
            val t2 = TransactionLocal(
                "A1D959FF-51A9-8C8F-EC86-016AE83B0033",
                "credit",
                155780,
                "transfer, credit-card, online, adjusted, auto",
                1551162246,
                true,
                "inward remittance by Globalmantics LLC"
            )
            val t3 = TransactionLocal(
                "839F206D-6CE4-9C7E-073A-64E8EB190222",
                "debit",
                42076,
                "credit-card, retail",
                1551591517,
                false,
                "purchase at XYZ retail store"
            )

            return listOf(t1, t2, t3)
        }
    }
}