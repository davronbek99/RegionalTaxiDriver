package dev.davron.regionaltaxidriver.fragment.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.davron.regionaltaxidriver.adapters.orders.OrdersAdapter
import dev.davron.regionaltaxidriver.databinding.FragmentActivityBinding
import dev.davron.regionaltaxidriver.utils.*
import dev.davron.regionaltaxidriver.utils.Convertor.isOnline
import dev.davron.regionaltaxidriver.utils.MySharedPreferences

class ActivityFragment : Fragment() {

    private lateinit var binding: FragmentActivityBinding
    private var TAG = "OrderFragment"
    private var token = ""
    private var lang = ""
    private lateinit var orderAdapterTaxi: OrdersAdapter
    var countTaxiList: Int = 0
    var countPostList: Int = 0
    var isTheme = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        listener()

        onBackPassed()

        uIOnItemClick()

        Log.d(TAG, "onViewCreated: order")
    }

    private fun listener() {

//        newsViewModel.notificationsLiveData.observe(viewLifecycleOwner, Observer {
//            val count = it.content.total - navigatorViewModel.getCountAllNotifs()
//
//            if (count > 0) {
//                binding.txtCount.visibility = View.VISIBLE
//                binding.fab.visibility = View.VISIBLE
//                binding.txtCount.text = it.content.total.toString()
//            } else {
//                binding.txtCount.visibility = View.GONE
//                binding.fab.visibility = View.GONE
//            }
//        })
//
//        newsViewModel.error.observe(viewLifecycleOwner, Observer {
//            toast(requireContext(), it)
//        })

        if (Convertor.taxi) {
            binding.topMenuNavigation.updateUi(1)
            binding.orderPackageListRv.visibility = View.GONE
            binding.orderTaxiListRv.visibility = View.VISIBLE
        } else if (Convertor.isPost) {
            binding.topMenuNavigation.updateUi(2)
            binding.orderPackageListRv.visibility = View.VISIBLE
            binding.orderTaxiListRv.visibility = View.GONE
        }

//        orderViewModel.orderTaxiDataLiveData.observe(requireActivity()) {
//            binding.loader.visibility = View.GONE
//
//            binding.swipe.isRefreshing = false
//
//            countTaxiList = it.content.size
//
//            val list = ArrayList<Content>()
//
//            for (content in it.content) {
//                if (content.order_type != "city") {
//                    list.add(content)
//                }
//            }
//
//            orderAdapterTaxi = OrdersAdapter(isTheme, requireContext(), list, this)
//
//            binding.orderTaxiListRv.adapter = orderAdapterTaxi
//            orderAdapterTaxi.setItemClickListener { pos ->
//                val bundle = Bundle()
//                bundle.putString("id", it.content[pos].id.toString())
//                findNavController().navigate(
//                    R.id.action_ordersFragment_to_ordersDetailsFragment,
//                    bundle
//                )
//            }
//        }
//
//        orderViewModel.orderPostActiveModel.observe(requireActivity()) {
//
//            countPostList = it.content.size
//            orderAdapterPost = OrderPostAdapter(isTheme, requireContext(), it.content, this)
//
//            binding.orderPackageListRv.adapter = orderAdapterPost
//            binding.swipe.isRefreshing = false
//
//            orderAdapterPost.setItemClickListener { item ->
//                val bundle = Bundle()
//                bundle.putString("id", it.content[item].id.toString())
//                findNavController().navigate(
//                    R.id.action_ordersFragment_to_orderpackageFragment,
//                    bundle
//                )
//            }
//        }
//
//        orderAdapterPostViewModel.errorLiveData.observe(requireActivity()) {
//            Log.d(TAG, "listener:$it")
//            binding.swipe.isRefreshing = false
//        }
    }

    private fun init() {
//        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        token = MySharedPreferences.getData(requireContext()).toString()
        lang = MySharedPreferences.getLang(requireContext()).toString()
//        orderViewModel.getOrderTaxi(lang, token)
//        orderViewModel.orderPost(lang, token)
//        navigatorViewModel = ViewModelProvider(this)[NavigatorViewModel::class.java]
//
//        //news view model
//        newsViewModelModel = ViewModelProvider(this)[NotificationFragmentViewModel::class.java]

        if (isOnline) {
            Common.allNewsCounter = 1
//            newsViewModel.fetchNotifications(token, lang)
        }


        binding.orderPackageListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.orderTaxiListRv.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun onBackPassed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    @SuppressLint("SetTextI18n")
    private fun uIOnItemClick() {

        binding.swipe.setOnRefreshListener {
            init()
        }

        val textLeft = binding.topMenuNavigation.leftText
        val textRight = binding.topMenuNavigation.rightText
        if (lang == "ru") {
            textLeft.text = "Такси"
            textRight.text = "Доставка"
        }

        if (lang == "uz") {
            textLeft.text = "Taksi"
            textRight.text = "Jo'natma"
        }
        binding.btnNotify.setOnClickListener {
//            findNavController().navigate(R.id.action_ordersFragment_to_notifyFragment_order)
        }

        binding.topMenuNavigation.setItemMenuClickListener {
            when (it) {
                1 -> {
                    //  BackAnimation()

                    Convertor.taxi = true
                    Convertor.isPost = false
//                    if (countTaxiList == 0) {
//                        binding.emptyTv.visibility = View.VISIBLE
//                        binding.orderPackageListRv.visibility = View.GONE
//                        binding.orderTaxiListRv.visibility = View.GONE
//                    } else {
                    binding.orderPackageListRv.visibility = View.GONE
                    binding.orderTaxiListRv.visibility = View.VISIBLE
//                    }

                }

                2 -> {
                    // animation()
                    Convertor.taxi = false
                    Convertor.isPost = true
//                    if (countPostList == 0) {
//                        binding.emptyTv.visibility = View.VISIBLE
//                        binding.orderPackageListRv.visibility = View.GONE
//                        binding.orderTaxiListRv.visibility = View.GONE
//                    } else {
                    binding.orderPackageListRv.visibility = View.VISIBLE
                    binding.orderTaxiListRv.visibility = View.GONE
//                    }
                }
            }
        }
    }

    //theme change app
    private fun themeChange() {
        val themeApp = MySharedPreferences.getNightMode(requireContext())
        if (themeApp == "white") {
            //day...
            isTheme = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            //night...
            isTheme = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Convertor.taxi = true
        Convertor.isPost = false
    }

}