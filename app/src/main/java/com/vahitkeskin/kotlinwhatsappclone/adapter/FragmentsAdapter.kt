package com.vahitkeskin.kotlinwhatsappclone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vahitkeskin.kotlinwhatsappclone.fragment.CallsFragment
import com.vahitkeskin.kotlinwhatsappclone.fragment.ChatsFragment
import com.vahitkeskin.kotlinwhatsappclone.fragment.StatusFragment

@Suppress("DEPRECATION")
class FragmentsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return ChatsFragment()
            1 -> return StatusFragment()
            2 -> return CallsFragment()
            else -> {
                return ChatsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> {
                title = "Chats"
            }
            1 -> {
                title = "Status"
            }
            2 -> {
                title = "Calls"
            }
        }
        return title
    }
}