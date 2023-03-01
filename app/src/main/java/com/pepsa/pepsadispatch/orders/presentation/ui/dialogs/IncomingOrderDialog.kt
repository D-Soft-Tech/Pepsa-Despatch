package com.pepsa.pepsadispatch.orders.presentation.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.TaskDialogLayoutBinding
import com.pepsa.pepsadispatch.orders.presentation.viewModels.OrdersViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncomingOrderDialog @Inject constructor() : DialogFragment() {
    private lateinit var binding: TaskDialogLayoutBinding
    private val ordersViewModel by activityViewModels<OrdersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.task_dialog_layout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    } else {
                        dismiss()
                    }
                }
            }
        }
    }
}
