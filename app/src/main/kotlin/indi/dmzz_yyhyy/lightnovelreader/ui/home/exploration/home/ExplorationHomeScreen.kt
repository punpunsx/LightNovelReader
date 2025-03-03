package indi.dmzz_yyhyy.lightnovelreader.ui.home.exploration.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import indi.dmzz_yyhyy.lightnovelreader.R
import indi.dmzz_yyhyy.lightnovelreader.data.exploration.ExplorationBooksRow
import indi.dmzz_yyhyy.lightnovelreader.ui.components.Cover
import indi.dmzz_yyhyy.lightnovelreader.ui.components.Loading
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorationHomeScreen(
    topBar: (@Composable () -> Unit) -> Unit,
    onClickExpand: (String) -> Unit,
    onClickBook: (Int) -> Unit,
    uiState: ExplorationHomeUiState,
    init: () -> Unit,
    changePage: (Int) -> Unit,
    onClickSearch: () -> Unit,
    refresh: () -> Unit
) {
    val enterAlwaysScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    topBar {
        TopBar(
            scrollBehavior = enterAlwaysScrollBehavior,
            onClickSearch = onClickSearch
        )
    }
    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        init()
    }
    Column {
        PrimaryTabRow(selectedTabIndex = uiState.selectedPage) {
            uiState.pageTitles.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.selectedPage == index,
                    onClick = {
                        changePage(index)
                    },
                    text = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
        AnimatedVisibility(
            visible = uiState.explorationPageBooksRawList.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Loading()
        }
        AnimatedContent(
            targetState = uiState.explorationPageBooksRawList,
            contentKey = { uiState.selectedPage },
            transitionSpec = {
                    (fadeIn(initialAlpha = 0.7f)).togetherWith(fadeOut(targetAlpha = 0.7f))
                },
            label = "ExplorationPageBooksRawsAnime"
        ) {
            ExplorationPage(
                explorationPageBooksRawList = it,
                onClickExpand = onClickExpand,
                onClickBook = onClickBook,
                nestedScrollConnection = enterAlwaysScrollBehavior.nestedScrollConnection,
                refresh = refresh
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onClickSearch: () -> Unit
) {
    MediumTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.nav_exploration),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            Box(Modifier.size(48.dp)) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.outline_explore_24px),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onClickSearch) {
                Icon(
                    painter = painterResource(id = R.drawable.search_24px),
                    contentDescription = "search"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorationPage(
    explorationPageBooksRawList: List<ExplorationBooksRow>,
    onClickExpand: (String) -> Unit,
    onClickBook: (Int) -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    refresh: () -> Unit
) {
    val rememberPullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember{ mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            refresh()
            scope.launch {
                rememberPullToRefreshState.animateToHidden()
            }
            isRefreshing = false
        },
        state = rememberPullToRefreshState
    ) {
        LazyColumn(
            modifier = Modifier.nestedScroll(nestedScrollConnection)
        ) {
            items(explorationPageBooksRawList) { explorationBooksRow ->
                Column(
                    modifier = Modifier.animateItem()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(2f),
                            text = explorationBooksRow.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.W700,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (explorationBooksRow.expandable) {
                            IconButton(onClick = {
                                explorationBooksRow.expandedPageDataSourceId?.let {
                                    onClickExpand(it)
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_forward_24px),
                                    contentDescription = "expand"
                                )
                            }
                        }
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 11.dp, start = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(explorationBooksRow.bookList) { explorationDisplayBook ->
                            Column(
                                modifier = Modifier.clickable {
                                    onClickBook(explorationDisplayBook.id)
                                }
                            ) {
                                Cover(
                                    width = 88.dp,
                                    height = 125.dp,
                                    url = explorationDisplayBook.coverUrl
                                )
                                Text(
                                    modifier = Modifier.width(88.dp),
                                    text = explorationDisplayBook.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.W500,
                                    fontSize = 12.sp,
                                    lineHeight = 14.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    Box(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 0.dp)) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}