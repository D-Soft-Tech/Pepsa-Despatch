package com.pepsa.pepsadispatch.shared.utils.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.pepsa.pepsadispatch.mian.presentation.ui.activities.MainAppActivity
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_INCOMING_ORDER_INTENT_ACTION
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.TAG_INCOMING_ORDER_RECEIVED

class IncomingOrderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.let { incomingOrderAction ->
            intent.getStringExtra(TAG_INCOMING_ORDER_RECEIVED)
                .let { incomingOrder ->
                    if (incomingOrderAction == STRING_INCOMING_ORDER_INTENT_ACTION) {
                        context?.let {
                            val startActivityIntent = Intent(context, MainAppActivity::class.java)
                            startActivityIntent.apply {
                                action = STRING_INCOMING_ORDER_INTENT_ACTION
                                putExtra(TAG_INCOMING_ORDER_RECEIVED, incomingOrder)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(startActivityIntent)
                        }
                    }
                }
        }
    }
}
