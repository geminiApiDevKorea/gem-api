package com.jyami.gemapi.client

import feign.ExceptionPropagationPolicy
import feign.Logger
import feign.RequestInterceptor
import feign.RequestTemplate
import feign.Retryer
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "youtubeClient", url = "https://youtube.googleapis.com/youtube/v3", configuration = [YoutubeClientConfig::class])
interface YoutubeClient {

    @GetMapping("/search")
    fun search(
        @RequestParam("part") part: String,
        @RequestParam("maxResults") maxResults: Int,
        @RequestParam("q") query: String,
        @RequestParam("type") type: String,
    ): YoutubeSearchResponse

    data class YoutubeSearchResponse(
        val items: List<YoutubeItem>
    )

    data class YoutubeItem(
        val id: VideoIdType,
        val snippet: Snippet
    )

    data class VideoIdType(val videoId: String)
    data class Snippet(val title: String, val description: String, val thumbnails: Thumbnails)
    data class Thumbnails(val default: Thumbnail, val medium: Thumbnail, val high: Thumbnail)
    data class Thumbnail(val url: String, val width: Int, val height: Int)

}



class YoutubeClientConfig {

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        val youtubeApiKey = System.getenv(YOUTUBE_API_KEY)
        return RequestInterceptor { requestTemplate: RequestTemplate ->
            requestTemplate.query("key", youtubeApiKey)
        }
    }

    @Bean
    fun retryer(): Retryer {
        return Retryer.Default(100, 500, 3)
//        return Retryer.NEVER_RETRY
    }

    @Bean
    fun exceptionPropagationPolicy(): ExceptionPropagationPolicy {
        return ExceptionPropagationPolicy.UNWRAP
    }

    companion object{
        const val YOUTUBE_API_KEY = "YOUTUBE_API_KEY"
    }
}
