package com.skid.main_screen.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skid.main_screen.R
import com.skid.users.domain.model.SeparatorItem
import com.skid.users.domain.model.Sorting
import com.skid.users.domain.model.UserItem
import com.skid.users.domain.model.UserListItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainScreenUiState,
    onEvent: (MainScreenEvent) -> Unit,
    onUserDetailsScreen: (String) -> Unit,
    onErrorScreen: () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.primary
    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
    }

    val tabTitles = stringArrayResource(R.array.departments).toList()
    val pagerState = rememberPagerState(
        initialPage = state.page,
        initialPageOffsetFraction = 0f,
        pageCount = tabTitles::size
    )

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    val loadSnackbarColor = MaterialTheme.colorScheme.onPrimary
    val errorSnackbarColor = MaterialTheme.colorScheme.error

    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarColor by remember { mutableStateOf(loadSnackbarColor) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onEvent(MainScreenEvent.OnPageChange(page))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                SearchField(
                    query = state.query,
                    onQueryChange = { onEvent(MainScreenEvent.OnQueryChange(it)) },
                    sortBy = Sorting.BY_ALPHABET,
                    onSortButtonClick = { showBottomSheet = true }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TabLayout(tabTitles = tabTitles, pagerState = pagerState)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.33.dp)
                        .background(MaterialTheme.colorScheme.onSecondary)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            ) {
                SnackbarSample(
                    text = it.visuals.message,
                    containerColor = snackbarColor
                )
            }
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Pager(
                tabTitles = tabTitles,
                pagerState = pagerState,
                userList = state.userList,
                sortBy = state.sortBy,
                isRefresh = state.isRefresh,
                onRefresh = { onEvent(MainScreenEvent.OnIsRefreshChange(true)) },
                wasSkeletonShown = state.wasSkeletonShown,
                onWasSkeletonShownChange = { onEvent(MainScreenEvent.OnWasSkeletonShownChange(it)) },
                onItemClick = onUserDetailsScreen
            )
        }

        if (showBottomSheet) {
            SortingBottomSheet(
                sortBy = state.sortBy,
                onSortByChange = { onEvent(MainScreenEvent.OnSortByChange(it)) },
                onShowBottomSheetChange = { showBottomSheet = it },
                bottomSheetState = bottomSheetState
            )
        }
    }

    val loadingSnackbarText = stringResource(R.string.wait_a_second_im_loading_up)
    LaunchedEffect(state.isRefresh) {
        if (state.isRefresh) {
            snackbarColor = loadSnackbarColor
            snackbarHostState.showSnackbar(message = loadingSnackbarText)
        }
    }

    LaunchedEffect(state.networkError) {
        if (state.networkError != null && state.userList.isEmpty()) {
            onErrorScreen()
        } else if (state.networkError != null) {
            snackbarColor = errorSnackbarColor
            snackbarHostState.showSnackbar(
                message = state.networkError,
                duration = SnackbarDuration.Long
            )
            onEvent(MainScreenEvent.OnNetworkErrorChange(null))
        }
    }
}


@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    sortBy: Sorting,
    onSortButtonClick: () -> Unit,
) {

    var textFieldFocused: Boolean by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { textFieldFocused = it.hasFocus },
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_field_hint),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "",
                    tint = if (textFieldFocused) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSecondary
                )
            },
            trailingIcon = {
                if (!textFieldFocused && query.isBlank()) {
                    IconButton(onClick = onSortButtonClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sort),
                            contentDescription = "",
                            tint = if (sortBy == Sorting.BY_BIRTHDAY) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSecondary
                        )
                    }
                } else if (textFieldFocused && query.isNotBlank()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        AnimatedVisibility(visible = textFieldFocused) {
            Text(
                text = stringResource(R.string.cancel),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable {
                        onQueryChange("")
                        localFocusManager.clearFocus()
                    },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(tabTitles: List<String>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 16.dp,
        indicator = {
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(it[pagerState.currentPage])
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.tertiary
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(12.dp, 8.dp),
                    style = if (pagerState.currentPage == index) MaterialTheme.typography.bodyLarge
                    else MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Pager(
    tabTitles: List<String>,
    pagerState: PagerState,
    userList: List<UserListItem>,
    sortBy: Sorting,
    isRefresh: Boolean,
    onRefresh: () -> Unit,
    wasSkeletonShown: Boolean,
    onWasSkeletonShownChange: (Boolean) -> Unit,
    onItemClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var filteredUsers by remember {
        mutableStateOf(List(pagerState.pageCount) { emptyList<UserListItem>() })
    }
    val refreshState = rememberPullRefreshState(refreshing = isRefresh, onRefresh = onRefresh)

    HorizontalPager(
        state = pagerState,
    ) { page ->
        Box(modifier = Modifier.pullRefresh(refreshState)) {
            when {
                !wasSkeletonShown -> {
                    SkeletonList()
                }

                filteredUsers[page].isNotEmpty() -> {
                    UserList(
                        userList = filteredUsers[page],
                        sortBy = sortBy,
                        onItemClick = onItemClick
                    )
                }

                filteredUsers[page].isEmpty() -> {
                    ListEmptyView()
                }
            }

            PullRefreshIndicator(
                refreshing = isRefresh,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    LaunchedEffect(userList) {
        scope.launch {
            if (userList.isNotEmpty()) {
                onWasSkeletonShownChange(true)
            }
        }

        scope.launch {
            val users = tabTitles.map { department ->
                val filtered = userList.filter {
                    if (it is UserItem) {
                        it.department == department || department == "Все"
                    } else true
                }
                if (filtered.size == 1 && filtered.first() is SeparatorItem) emptyList()
                else filtered
            }
            filteredUsers = users
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserList(
    userList: List<UserListItem>,
    sortBy: Sorting,
    onItemClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp)
    ) {
        userList.forEach {
            item {
                if (it is UserItem) {
                    UserItem(
                        userItem = it,
                        sortBy = sortBy,
                        modifier = Modifier.animateItemPlacement(),
                        onItemClick = onItemClick
                    )
                } else if (it is SeparatorItem) {
                    SeparatorItem(
                        modifier = Modifier.animateItemPlacement(),
                        text = it.text
                    )
                }
            }
        }
    }
}


@Composable
fun SkeletonList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        repeat(8) {
            AnimatedSkeletonItem()
        }
    }
}


@Composable
fun UserItem(
    userItem: UserItem,
    sortBy: Sorting,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(userItem.id) }
            .padding(16.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val asyncImagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userItem.avatarUrl)
                .placeholder(R.drawable.user_photo_stub)
                .error(R.drawable.user_photo_stub)
                .build()
        )

        Image(
            painter = asyncImagePainter,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = userItem.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = userItem.userTag,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Text(
                text = userItem.department,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        if (sortBy == Sorting.BY_BIRTHDAY) {
            Text(
                text = userItem.monthDayOfBirthday,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.End
            )
        }
    }
}


@Composable
fun SeparatorItem(modifier: Modifier = Modifier, text: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .width(72.dp)
                .background(MaterialTheme.colorScheme.onSecondary)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .width(72.dp)
                .background(MaterialTheme.colorScheme.onSecondary)
        )
    }
}


@Composable
fun SkeletonItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(brush = brush)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Spacer(
                modifier = Modifier
                    .size(144.dp, 16.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(brush = brush)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Spacer(
                modifier = Modifier
                    .size(80.dp, 12.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(brush = brush)
            )
        }
    }
}


@Composable
fun AnimatedSkeletonItem() {
    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.outline,
            MaterialTheme.colorScheme.outlineVariant
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    SkeletonItem(brush = brush)
}


@Composable
fun ListEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.left_pointing_magnifying_glass_1f50d),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.we_didnt_find_anyone),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.try_to_adjust_the_request),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingBottomSheet(
    sortBy: Sorting,
    onSortByChange: (Sorting) -> Unit,
    onShowBottomSheetChange: (Boolean) -> Unit,
    bottomSheetState: SheetState,
) {
    val radioOptions = Sorting.values()
    var selectedOption by remember { mutableStateOf(sortBy) }

    ModalBottomSheet(
        onDismissRequest = { onShowBottomSheetChange(false) },
        modifier = Modifier.padding(horizontal = 8.dp),
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.sorting),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        radioOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedOption == option),
                        onClick = {
                            selectedOption = option
                            onSortByChange(option)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedOption == option),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Text(
                    text = option.text,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun SnackbarSample(
    text: String,
    containerColor: Color,
) {
    Snackbar(
        shape = RoundedCornerShape(12.dp),
        containerColor = containerColor,
        contentColor = Color.White
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
fun SnackbarPreview() {
    SnackbarSample(
        text = "Секундочку, гружусь",
        containerColor = MaterialTheme.colorScheme.onPrimary
    )
}
