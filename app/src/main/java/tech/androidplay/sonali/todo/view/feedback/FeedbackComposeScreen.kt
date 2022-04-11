package tech.androidplay.sonali.todo.view.feedback

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.airbnb.lottie.compose.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.view.common.*

@Preview(showBackground = true)
@Composable
fun FeedbackComposeScreen() {
    Box(
        modifier = Modifier
            .background(White100)
            .fillMaxSize()
    ) {
        Column {
            ToolBarLayout()
            AppLogoPart()
            FeedbackInformationLayout()
            FeedbackInputLayout()
            FeedbackSubmitLayout()
        }
    }
}


@Composable
fun ToolBarLayout() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(modifier = Modifier.size(60.dp).padding(all = 16.dp),
            onClick = { }
        ) {
            Icon(
                painterResource(R.drawable.ic_back),
                contentDescription = "Go back from feedback screen.",
                tint = GreyDark,
            )
        }
        Text(
            text = "Feedback",
            style = MaterialTheme.typography.h6,
            color = GreyDark,
        )
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie_auth.json"))
        val progress by animateLottieCompositionAsState(composition)
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .height(90.dp)
                .width(90.dp)
                .align(Alignment.CenterVertically)
                .padding(all = 16.dp)
        )
    }
}


@Composable
fun AppLogoPart() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_app_icon),
            contentDescription = "App logo",
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "How did you like this app?",
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.h6,
            fontFamily = FontFamily(Font(R.font.calibre, FontWeight.Medium)),
            fontSize = 22.sp,
            color = GreyDark
        )
    }
}


@Composable
fun FeedbackInformationLayout(color: Color = GreyLight) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Icon(
            painterResource(R.drawable.ic_info),
            contentDescription = "Feedback info icon",
            modifier = Modifier.padding(bottom = 10.dp),
            tint = GreyMedium
        )
        Text(
            text = stringResource(R.string.feedback_story),
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.h6,
            fontFamily = FontFamily(Font(R.font.calibre, FontWeight.Medium)),
            fontSize = 18.sp,
            color = GreyDark
        )
    }
}

@Composable
fun FeedbackInputLayout() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 36.dp)
            .fillMaxWidth()
    ) {
        val maxChar = 100
        val titleTextState = remember { mutableStateOf("") }
        Text(
            text = "What you think about this app?",
            style = MaterialTheme.typography.h6,
            fontFamily = FontFamily(Font(R.font.calibre, FontWeight.Medium)),
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            color = GreyDark,
        )
        TextField(
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
            shape = RoundedCornerShape(10.dp),
            value = titleTextState.value,
            onValueChange = { if (it.length <= maxChar) titleTextState.value = it },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = GreyLight,
                cursorColor = Black100,
                disabledLabelColor = GreyMedium,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Text(
            text = "${titleTextState.value.length} / $maxChar",
            textAlign = TextAlign.End,
            color = GreyLight,
            style = MaterialTheme.typography.caption, //use the caption text style
            modifier = Modifier.fillMaxWidth()
        )

        val descriptionTextState = remember { mutableStateOf("") }
        Text(
            text = "Can you give little more info?",
            style = MaterialTheme.typography.h6,
            fontFamily = FontFamily(Font(R.font.calibre, FontWeight.Medium)),
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            color = GreyDark,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        )
        TextField(
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
            shape = RoundedCornerShape(10.dp),
            value = descriptionTextState.value,
            onValueChange = { if (it.length <= maxChar) descriptionTextState.value = it },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = GreyLight,
                cursorColor = Black100,
                disabledLabelColor = GreyMedium,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Text(
            text = "${descriptionTextState.value.length} / $maxChar",
            textAlign = TextAlign.End,
            color = GreyLight,
            style = MaterialTheme.typography.caption, //use the caption text style
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun FeedbackSubmitLayout() {
    Button(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth(),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, AppBlue),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = AppBlue
        ),
        onClick = { }
    ) {
        Text(
            text = "Submit",
            fontFamily = FontFamily(Font(R.font.calibre, FontWeight.Medium)),
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
        )
    }
}


