package com.zgame.zgame.base

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var binding: T
    abstract fun getContentView(): Int
    lateinit var mAuth: FirebaseAuth

    abstract fun initView(binding: T)
    abstract fun initNav(view: View)


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAuth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        initView(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNav(view)
    }


    fun showToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun hideSoftKeyboard(view : View?){

        view?.let {
            val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    protected open fun
            fragmentTransaction(
        transactionType: Int,
        fragment: Fragment,
        container: Int,
        isAddToBackStack: Boolean,
        bundle: Bundle?
    ) {
        if (bundle != null) {
            fragment.arguments = bundle
        }

        val trans = activity!!.supportFragmentManager.beginTransaction()
        when (transactionType) {
            ADD_FRAGMENT -> {
                trans.add(container, fragment, fragment.javaClass.simpleName)
                if (isAddToBackStack) trans.addToBackStack(null)
            }
            REPLACE_FRAGMENT -> {
                trans.replace(container, fragment, fragment.javaClass.simpleName)
                if (isAddToBackStack) trans.addToBackStack(null)
            }
        }
        trans.commit()
    }

    companion object {
        const val ADD_FRAGMENT = 0
        const val REPLACE_FRAGMENT = 1
    }
}