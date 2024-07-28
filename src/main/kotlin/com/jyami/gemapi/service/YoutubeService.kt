package com.jyami.gemapi.service

import com.jyami.gemapi.client.YoutubeClient
import org.springframework.stereotype.Service

@Service
class YoutubeService(
    private val youtubeClient: YoutubeClient
) {

    fun searchMusic(query: String): YoutubeClient.YoutubeItem {
        return youtubeClient.search(part = "snippet", maxResults = 1, query = query, type = "video")
            .items.first()
    }

}
