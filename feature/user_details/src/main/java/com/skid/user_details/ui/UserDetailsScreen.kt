package com.skid.user_details.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skid.user_details.R
import com.skid.users.domain.model.UserDetailsItem


@Composable
fun UserDetailsScreen(
    userDetailsItem: UserDetailsItem,
    onBackButtonClick: () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.secondary
    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
    }

    Column {
        UserDetailsTopAppBar(
            avatarUrl = userDetailsItem.avatarUrl,
            name = userDetailsItem.name,
            userTag = userDetailsItem.userTag,
            department = userDetailsItem.department,
            onBackButtonClick = onBackButtonClick
        )
        UserDetailsContent(
            birthday = userDetailsItem.birthday,
            age = userDetailsItem.age,
            phone = userDetailsItem.phone
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsScreenPreview() {
    UserDetailsScreen(
        userDetailsItem = UserDetailsItem(
            id = "",
            avatarUrl = "",
            name = "Алиса Иванова",
            userTag = "al",
            department = "Дизайн",
            birthday = "5 июня 1996",
            age = "24 года",
            phone = "+7 (999) 900 90 90"
        ),
        onBackButtonClick = {}
    )
}


@Composable
fun UserDetailsTopAppBar(
    avatarUrl: String,
    name: String,
    userTag: String,
    department: String,
    onBackButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        IconButton(onClick = onBackButtonClick) {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = ""
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val asyncImagePainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .placeholder(R.drawable.user_photo_stub)
                    .error(R.drawable.user_photo_stub)
                    .build()
            )
            Image(
                painter = asyncImagePainter,
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = userTag,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = department,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}


@Composable
fun UserDetailsContent(
    birthday: String,
    age: String,
    phone: String,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_favorite),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = birthday,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = age,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Row(
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                    context.startActivity(intent)
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_phone_alt),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = phone,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

