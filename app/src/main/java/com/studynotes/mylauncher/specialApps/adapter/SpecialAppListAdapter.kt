package com.studynotes.mylauncher.specialApps.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.studynotes.mylauncher.databinding.IconSwitchSettingItemBinding
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.roomDB.Dao.HiddenAppDao
import com.studynotes.mylauncher.roomDB.Model.HiddenApps
import com.studynotes.mylauncher.viewUtils.ViewUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpecialAppAdapter(
    private val specialAppList: List<AppInfo>,
    private val context: Context,
    private val hiddenAppDao: HiddenAppDao
) :
    RecyclerView.Adapter<SpecialAppAdapter.SpecialAppViewHolder>() {

    inner class SpecialAppViewHolder(val binding: IconSwitchSettingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialAppViewHolder {
        val binding =
            IconSwitchSettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialAppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecialAppViewHolder, position: Int) {
        val app = specialAppList[position]

        with(holder.binding) {
            title.text = app.label
            leadingIcon.setImageDrawable(app.icon)
            selectCheckbox.setOnCheckedChangeListener(null)
            checkIfAppIsAddictive(app.packageName!!) { isChecked ->
                selectCheckbox.isChecked = isChecked
            }
            root.setOnClickListener {
                checkIfAppIsAddictive(packageName = app.packageName){isChecked->
                    selectCheckbox.isChecked = !isChecked
                    addRemoveFromHiddenAppList(!isChecked, app)
                }
            }
        }
    }

    override fun getItemCount(): Int = specialAppList.size

    private fun checkIfAppIsAddictive(packageName: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val isAddictive = hiddenAppDao.isAppAddictive(packageName)
            withContext(Dispatchers.Main) {
                callback(isAddictive)
            }
        }
    }

    private fun addRemoveFromHiddenAppList(isAppInHiddenList: Boolean, appInfo: AppInfo) {
        val packageName = appInfo.packageName ?: return
        CoroutineScope(Dispatchers.IO).launch {
            if (!isAppInHiddenList) {
                hiddenAppDao.removeFromAddictiveApps(packageName)
                withContext(Dispatchers.Main) { ViewUtils.showToast(context, "Removed from hidden app list") }
            } else {
                hiddenAppDao.insertAddictiveApp(HiddenApps(packageName = packageName))
                withContext(Dispatchers.Main) { ViewUtils.showToast(context, "Added to hidden app list") }
            }
        }
    }

}
