package app.com.chess.ai.views.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.utils.LoadingDialogFragment
import app.com.chess.ai.viewmodels.BaseViewModel
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.io.IOException
import java.io.InputStream
import java.util.*


abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity() {
    protected var binding: Binding? = null
    var mCurrentFragment: Fragment? = null
    var appController: _AppController? = null
    var baseViewModel: BaseViewModel? = null
    private val loadingDialogFragment by lazy { LoadingDialogFragment() }

    protected fun bindView(layoutId: Int) {
        binding = DataBindingUtil.setContentView<Binding>(this, layoutId)
    }

    private fun askPermissions() {
        Permissions.check(this, Manifest.permission.CAMERA, null,
            object : PermissionHandler() {
                override fun onGranted() {

                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    super.onDenied(context, deniedPermissions)
                    permissionDeniedDialog()
                }

                override fun onJustBlocked(
                    context: Context?,
                    justBlockedList: ArrayList<String>?,
                    deniedPermissions: ArrayList<String>?
                ) {
                    super.onJustBlocked(context, justBlockedList, deniedPermissions)
                    permissionDeniedDialog()
                }

                override fun onBlocked(
                    context: Context?,
                    blockedList: ArrayList<String>?
                ): Boolean {
                    return super.onBlocked(context, blockedList)
                    permissionDeniedDialog()
                }
            })
    }

    private fun permissionDeniedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Permissions Required")
        builder.setMessage("Requested permissions are required to perform properly!")
        builder.setPositiveButton(
            "Ok"
        ) { dialogInterface, i -> askPermissions() }
        builder.setNegativeButton(
            "Cancel"
        ) { dialogInterface, i -> }
        builder.show()
    }

    fun showAlertDialog(dialogTitle: String?, message: String?) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage(message).setTitle(dialogTitle)
        builder.setCancelable(false).setPositiveButton("Ok") { dialog, id ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.setTitle(dialogTitle)
        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources?.getColor(R.color.colorPrimaryDark)!!)
        }
        alert.show()
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun closeKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (this.currentFocus != null) inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    open fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun isKeyboardOpen(): Boolean {
        val inputManager =
            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        return inputManager.isAcceptingText
    }

    fun showHideProgress(show: Boolean) {
        if (show) {
            if (!loadingDialogFragment.isAdded) {
                loadingDialogFragment.addFragmentOnlyOnce(supportFragmentManager, "loader")
            }
        } else {
            if (loadingDialogFragment.isAdded) {
                loadingDialogFragment.dismissAllowingStateLoss()
            }
        }
    }

    override fun onBackPressed() {
        performBackStackManagement()
    }

    private fun performBackStackManagement() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount == 1) {
            finish()
        } else {
            val index = supportFragmentManager.backStackEntryCount - 2
            if (index >= 0) {
                val backEntry = supportFragmentManager.getBackStackEntryAt(index)
                val tag = backEntry.name
                mCurrentFragment = supportFragmentManager.findFragmentByTag(tag)

                showHideFragment(mCurrentFragment!!)
            }
            super.onBackPressed()
        }
    }

    fun finishAllFragmentsExceptFirstOne() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 1) {
            for (i in 0 until manager.backStackEntryCount - 1) {
                manager.popBackStack()
            }

            val backEntry = supportFragmentManager.getBackStackEntryAt(0)
            val tag = backEntry.name
            mCurrentFragment = supportFragmentManager.findFragmentByTag(tag)

            showHideFragment(mCurrentFragment!!)
        }
    }

    fun showHideFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val t = manager.beginTransaction()
        for (i in 0 until manager.fragments.size) {
            val f = manager.fragments[i]
            t.hide(f)
        }
        t.show(fragment).commit()
    }

    open fun replaceActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    open fun addActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    fun replaceFragment(fragment: Fragment) {
        val frag = Fragment()

        if (mCurrentFragment != null && mCurrentFragment!!::class.java.equals(fragment::class.java)) {
            return
        }
        val manager = supportFragmentManager
        mCurrentFragment = fragment
        try {
            val t = manager.beginTransaction()
            val currentFrag = manager.findFragmentByTag(fragment::class.java.simpleName)
            if (fragment.isAdded) {
                t.remove(frag).commit();
                return
            }
            if (currentFrag != null && currentFrag::class.java.simpleName.equals(fragment::class.java.simpleName)) {
                showHideFragment(currentFrag)
                manager.popBackStackImmediate(mCurrentFragment!!::class.java.simpleName, 1)
            }
            t.add(R.id.container, fragment, fragment::class.java.simpleName)
                .addToBackStack(fragment::class.java.simpleName).commit()
        } catch (e: Exception) {
            Log.e("ex", "repalceFrag funcation Exception in base Activity ")
            val t = manager.beginTransaction()
            t.add(R.id.container, fragment, fragment::class.java.simpleName)
                .addToBackStack(fragment::class.java.simpleName).commit()
        }
        showHideFragment(fragment)
    }

    fun setObservers(baseViewModel: BaseViewModel, baseActivity: BaseActivity<*>) {
        this.baseViewModel = baseViewModel
        try {
            baseViewModel.dialogLiveData.observe(this@BaseActivity, Observer {
                showHideProgress(it)
            })
            baseViewModel.errorLiveData.observe(this@BaseActivity, Observer {
                Log.d("errrorscreen", baseActivity::class.java.name + " Screen")
            })
        } catch (ex: java.lang.Exception) {
            print(ex.localizedMessage)
        }
    }

    fun decodeJson(inputStream: InputStream): String? {
        try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            return String(bytes)
        } catch (e: IOException) {
            return null
        }
    }
}