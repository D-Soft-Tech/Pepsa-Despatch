package com.pepsa.pepsadispatch.orders.presentation.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.TaskDialogLayoutBinding
import com.pepsa.pepsadispatch.orders.presentation.viewModels.OrdersViewModel
import com.pepsa.pepsadispatch.shared.utils.ringer.RingerUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncomingOrderDialog @Inject constructor(
    private val ringerUtil: RingerUtil
) : DialogFragment() {
    private lateinit var binding: TaskDialogLayoutBinding
    private val ordersViewModel by activityViewModels<OrdersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.task_dialog_layout, container, false)
        dialog?.setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderViewModel = ordersViewModel
        binding.executePendingBindings()
        ordersViewModel.incomingOrder.observe(viewLifecycleOwner) {
            it?.let { incomingOrder ->
                binding.incomingOrder = incomingOrder
                binding.executePendingBindings()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ordersViewModel.incomingOrder.observe(viewLifecycleOwner) {
            it?.let { incomingOrder ->
                incomingOrder.accepted?.let { isOrderAccepted ->
                    if (isOrderAccepted) {
                        dismiss()
                        ordersViewModel.resetIncomingOrderToNull()
                        ringerUtil.stopRinging()
                    } else {
                        dismiss()
                        ordersViewModel.resetIncomingOrderToNull()
                        ringerUtil.stopRinging()
                    }
                }
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        ringerUtil.startRinging()
    }
}
