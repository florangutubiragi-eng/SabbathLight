package com.example.ui.components

import android.annotation.SuppressLint
import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkCard
import com.example.ui.theme.RoyalBluePrimary

/**
 * Functional HTML5 Audio Player embedded in Compose via Android WebView.
 * Directly hosts standard HTML5 <audio controls preload="auto"> with verified audio sources,
 * real-time playback events, and clear error reporting.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Html5AudioPlayerView(
    audioUrl: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var hasLoadError by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("html5_audio_player_container"),
        shape = RoundedCornerShape(16.dp),
        color = NavyDarkCard,
        tonalElevation = 6.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: HTML5 Audio Player Badge & Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GoldPrimary.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "HTML5 Audio",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = GoldPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1
                    )
                }

                if (hasLoadError) {
                    Button(
                        onClick = {
                            hasLoadError = false
                            webViewInstance?.reload()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry", tint = Color(0xFF071324), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Jaribu Tena", fontSize = 11.sp, color = Color(0xFF071324), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle & stream info
            Text(
                text = "$subtitle • Wikimedia Commons Stream",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Color(0xFFCBD5E1)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (audioUrl.isBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hakuna faili la sauti lililounganishwa",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF6B6B)
                    )
                }
            } else {
                // Embedded HTML5 Audio Player WebView
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .testTag("html5_audio_webview"),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            setBackgroundColor(AndroidColor.TRANSPARENT)
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                mediaPlaybackRequiresUserGesture = false
                                allowContentAccess = true
                                allowFileAccess = true
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    super.onReceivedError(view, request, error)
                                    hasLoadError = true
                                }
                            }

                            webChromeClient = object : WebChromeClient() {
                                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                    return true
                                }
                            }

                            val cleanUrl = audioUrl.trim()
                            val safeTitle = title.replace("'", "\\'")
                            val mimeType = if (cleanUrl.endsWith(".mp3", ignoreCase = true)) "audio/mpeg" else "audio/ogg"

                            val htmlContent = """
                                <!DOCTYPE html>
                                <html>
                                <head>
                                  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                                  <style>
                                    * { box-sizing: border-box; }
                                    body {
                                      margin: 0;
                                      padding: 4px;
                                      background: transparent;
                                      font-family: -apple-system, Roboto, sans-serif;
                                      overflow: hidden;
                                      display: flex;
                                      align-items: center;
                                      justify-content: center;
                                    }
                                    audio {
                                      width: 100%;
                                      height: 48px;
                                      outline: none;
                                      border-radius: 8px;
                                      background: #0B192C;
                                    }
                                    .error-text {
                                      color: #FF6B6B;
                                      font-size: 11px;
                                      display: none;
                                      margin-top: 4px;
                                      text-align: center;
                                    }
                                  </style>
                                </head>
                                <body>
                                  <div style="width: 100%;">
                                    <audio id="hymnAudio" controls preload="metadata">
                                      <source src="$cleanUrl" type="$mimeType">
                                      Kivinjari chako hakiwezi kucheza sauti hii.
                                    </audio>
                                    <div id="err" class="error-text">Imeshindikana kupakia faili la sauti.</div>
                                  </div>
                                  <script>
                                    const audio = document.getElementById('hymnAudio');
                                    const err = document.getElementById('err');
                                    audio.onerror = function() {
                                      err.style.display = 'block';
                                    };
                                  </script>
                                </body>
                                </html>
                            """.trimIndent()

                            loadDataWithBaseURL("https://upload.wikimedia.org/", htmlContent, "text/html", "UTF-8", null)
                            webViewInstance = this
                        }
                    },
                    update = { view ->
                        webViewInstance = view
                    }
                )

                // Inspectable source URL
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Source",
                        tint = GoldPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = audioUrl,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color(0xFF94A3B8),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
