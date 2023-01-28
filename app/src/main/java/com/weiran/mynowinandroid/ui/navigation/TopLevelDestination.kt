package com.weiran.mynowinandroid.ui.navigation

import com.weiran.mynowinandroid.R

enum class TopLevelDestination(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconTextId: Int,
    val titleTextId: Int
) {
    FOR_YOU(
        selectedIcon = R.drawable.ic_upcoming,
        unselectedIcon = R.drawable.ic_upcoming_border,
        iconTextId = R.string.for_page,
        titleTextId = R.string.for_page
    ),
    SAVED(
        selectedIcon = R.drawable.ic_bookmarks,
        unselectedIcon = R.drawable.ic_bookmarks_border,
        iconTextId = R.string.saved,
        titleTextId = R.string.saved
    ),
    INTERESTS(
        selectedIcon = R.drawable.ic_menu_book,
        unselectedIcon = R.drawable.ic_menu_book_border,
        iconTextId = R.string.interests,
        titleTextId = R.string.interests
    )
}
