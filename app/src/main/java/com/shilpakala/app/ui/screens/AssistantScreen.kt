package com.shilpakala.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shilpakala.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Message(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(onBack: () -> Unit) {
    var messages by remember { 
        mutableStateOf(listOf(Message("Namaskara! I am your Shilpa-Kala guide. How can I help you today?", false)))
    }
    var inputText by remember { mutableStateOf("") }
    var isTyping  by remember { mutableStateOf(false) }
    var showFaq   by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope     = rememberCoroutineScope()

    val faqData = listOf(
        "What is the purpose of this app?" to "This app helps artisans create professional-looking product photos easily.",
        "How do I start using the app?" to "Open the app and allow camera permissions.",
        "How do I take a product photo?" to "Place the product inside the guide frame and tap the capture button.",
        "Why is there a white frame on the camera?" to "It helps position the product properly for better photos.",
        "How do I save my photo?" to "After previewing the image, tap the Save button.",
        "Can I retake the photo?" to "Yes, tap the Retake button anytime.",
        "How do I add my name to the image?" to "Enter your artisan name before saving the photo.",
        "Can I add the product price?" to "Yes, you can enter the price before saving.",
        "Where are my saved photos stored?" to "They are stored in the app gallery/local storage.",
        "How do I open the gallery?" to "Tap the Gallery button on the home screen.",
        "Can I share photos on WhatsApp?" to "Yes, use the Share button after saving the image.",
        "Can I share photos on Facebook?" to "Yes, the app supports social media sharing.",
        "Why does the app need camera permission?" to "The app needs camera access to capture product photos.",
        "Can I use old photos from gallery?" to "Currently the app focuses on live camera capture.",
        "What kind of products can I upload?" to "Handicrafts, carvings, toys, paintings, and handmade items.",
        "How do I make my product look professional?" to "Use proper lighting and align the product inside the frame.",
        "What is the “Handmade in Karnataka” label?" to "It is a branding label added to the product photo.",
        "Can I remove the branding label?" to "The branding is added automatically for presentation.",
        "Why does my image look blurry?" to "Try cleaning the camera lens and improving lighting.",
        "Can I zoom the camera?" to "Zoom support may depend on device compatibility.",
        "How do I improve photo quality?" to "Use bright lighting and keep the camera steady.",
        "Can I use the app offline?" to "Yes, most features work offline.",
        "Does the app require login?" to "Login features may be added in future updates.",
        "How do I delete saved images?" to "Open the gallery and remove unwanted images.",
        "Why is my camera not opening?" to "Check if camera permission is enabled in settings.",
        "Can multiple products be captured?" to "Yes, you can capture and save multiple products.",
        "What should I place inside the guide frame?" to "Place the handicraft product fully inside the outline.",
        "Can I use the app for jewelry photos?" to "Yes, small handcrafted products can also be photographed.",
        "What happens after taking a photo?" to "The app opens a branded preview screen.",
        "Can I edit the product name later?" to "Currently details are added during preview.",
        "How do I make the background look better?" to "Use plain surfaces and proper lighting.",
        "Can I capture landscape photos?" to "The app is optimized mainly for product-focused images.",
        "What if the app crashes?" to "Restart the app and reopen the camera.",
        "Why is the preview screen important?" to "It shows the final branded image before saving.",
        "Can I save images without sharing?" to "Yes, sharing is optional.",
        "How do I close the camera screen?" to "Use the back button on your device.",
        "Can I use this app for online selling?" to "Yes, the photos can be used for catalogs and online promotion.",
        "How do I add product details?" to "Enter details in the provided text fields.",
        "Why does the app focus on handicrafts?" to "It is designed specifically for artisans and handmade products.",
        "Can I change the branding text?" to "Customization features can be added later.",
        "What devices support this app?" to "Most Android phones support the app.",
        "Can I reopen saved images later?" to "Yes, saved photos remain in the gallery.",
        "How do I capture centered images?" to "Align the object inside the overlay frame.",
        "Does the app automatically enhance photos?" to "The app mainly improves presentation using branding and layout.",
        "Can I save photos in high quality?" to "Yes, captured images are saved in good quality.",
        "Why should artisans use this app?" to "It helps products look premium and market-ready.",
        "How do I restart the photo process?" to "Tap Retake and capture again.",
        "Can I take photos in low light?" to "Yes, but bright lighting gives better results.",
        "What is the main benefit of this app?" to "It helps create attractive product images quickly.",
        "How do I get the best results?" to "Use clean backgrounds, good lighting, and proper positioning."
    )

    fun handleSend(text: String, preloadedAnswer: String? = null) {
        if (text.isBlank()) return
        messages = messages + Message(text, true)
        inputText = ""
        isTyping = true
        showFaq = false
        
        scope.launch {
            delay(800)
            val response = preloadedAnswer ?: getAIResponse(text, faqData)
            messages = messages + Message(response, false)
            isTyping = false
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMaroon)
    ) {
        // Top Bar
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SupportAgent, contentDescription = null, tint = Gold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Assistant", color = Gold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Gold)
                }
            },
            actions = {
                // Small FAQ Toggle Button
                IconButton(onClick = { showFaq = !showFaq }) {
                    Icon(
                        Icons.Default.QuestionAnswer, 
                        contentDescription = "FAQ", 
                        tint = if (showFaq) GoldLight else Gold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaroonDark)
        )

        // FAQ Drawer / Section
        AnimatedVisibility(visible = showFaq) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaroonCard)
                    .padding(12.dp)
            ) {
                Text("Frequently Asked Questions", color = GoldLight, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 4.dp)
                ) {
                    items(faqData) { (q, a) ->
                        SuggestionChip(q) { handleSend(q, a) }
                    }
                }
            }
        }

        // Chat Area
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(msg)
            }
            if (isTyping) {
                item {
                    Text("Assistant is typing...", color = White80, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        // Input Area
        Surface(
            color = MaroonDark,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask anything...", color = White80.copy(alpha = 0.5f)) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DeepMaroon,
                        unfocusedContainerColor = DeepMaroon,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Gold,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { handleSend(inputText) },
                    enabled = inputText.isNotBlank(),
                    modifier = Modifier.background(Gold, CircleShape)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = DeepMaroon)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bgColor   = if (message.isUser) Gold else MaroonCard
    val textColor = if (message.isUser) DeepMaroon else White

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Surface(
            color = bgColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 0.dp,
                bottomEnd = if (message.isUser) 0.dp else 16.dp
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = textColor,
                fontSize = 15.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(text, fontSize = 11.sp, color = Gold) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = DeepMaroon,
            labelColor = Gold
        ),
        border = FilterChipDefaults.filterChipBorder(borderColor = Gold.copy(alpha = 0.4f))
    )
}

private fun getAIResponse(input: String, faq: List<Pair<String, String>>): String {
    val low = input.lowercase()
    
    // Check FAQ list for exact or partial matches first
    faq.forEach { (q, a) ->
        if (low.contains(q.lowercase()) || q.lowercase().contains(low)) {
            return a
        }
    }

    return when {
        low.contains("photo") || low.contains("capture") -> 
            "To take a great photo, place your product inside the Gold corner brackets on the camera screen. Make sure the lighting is bright and steady!"
        low.contains("gallery") || low.contains("save") -> 
            "Your branded photos are saved in the 'ShilpaKala' folder on your device. You can view them anytime by clicking 'My Gallery' in the app."
        low.contains("share") || low.contains("whatsapp") -> 
            "After capturing or in the gallery, click the 'Share' button. You can then select WhatsApp, Instagram, or any other app to showcase your work!"
        low.contains("artisan") || low.contains("name") -> 
            "You can type the artisan's name in the text field at the bottom of the camera screen. It will appear beautifully in the gold badge on the final photo."
        low.contains("price") || low.contains("cost") -> 
            "Enter the price in the field provided. It will be added to the bottom right of your photo in a premium gold font."
        else -> "I'm here to help you showcase your handcrafted products! Feel free to pick a question from the FAQ (top right) or type your query here."
    }
}
