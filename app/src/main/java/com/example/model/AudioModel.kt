package com.example.model

import android.net.Uri

/**
 * Data representation of an imported audio file in TAVANA Studio.
 */
data class AudioFileInfo(
    val uri: Uri,
    val fileName: String,
    val mimeType: String,
    val durationMs: Long,
    val fileSizeBytes: Long,
    val formatDescription: String
)

/**
 * Playback state representing real-time audio playback.
 */
data class AudioPlaybackUiState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val isLoaded: Boolean = false,
    val errorMessage: String? = null
)
