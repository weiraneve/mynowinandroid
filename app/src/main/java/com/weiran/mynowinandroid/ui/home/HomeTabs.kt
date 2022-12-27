package com.weiran.mynowinandroid.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.weiran.mynowinandroid.R

enum class HomeTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val clickIcon: Int
) {
    FOR_PAGE(R.string.for_page, R.drawable.ic_upcoming_border, R.drawable.ic_upcoming),
    SAVED_PAGE(R.string.saved, R.drawable.ic_bookmarks_border, R.drawable.ic_bookmarks),
    INTEREST_PAGE(R.string.interests, R.drawable.ic_menu_book_border, R.drawable.ic_menu_book),
}