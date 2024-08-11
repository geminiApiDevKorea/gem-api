# GemAPI

## Overview
GemAPI is a Spring Boot application written in Kotlin and Java. 

It provides endpoints for user management and diary entries. 

The project uses Gradle for build automation.

## Technologies Used
#### code
- Kotlin
- Spring Boot : Web MVC, Spring ai, Spring Security
- Gradle
#### infra
- GCP : Vertex APi, Cloud Run, Container Registry
- Firebase : Authentication, FireStore
#### CI/CD
- GitHub Actions

## Project Structure
![muse-dairy_ drawio](https://github.com/user-attachments/assets/052a8f77-70e3-466c-9234-975d63f6350c)


## documents
https://gem-api-xphhnkqc6a-du.a.run.app/swagger-ui/index.html

---
## extra descriptions
#### Gemini AI 호출 방법
- Called using VertexAiGeminiChatModel defined in spring-ai.
- https://docs.spring.io/spring-ai/reference/api/chat/vertexai-gemini-chat.html
- Authenticated using GCP_CREDENTIALS on the server side and called using the vertex API settings (PROJECT_ID, LOCATION_ID, AGENT_ID) defined in the GCP console.
- [ai config source code](./src/main/kotlin/com/jyami/gemapi/config/AiConfig.kt)


#### 프롬프팅 방법

- **Role Specification Prompting** : The AI is again assigned a specific role
  - `"You are the world's best psychotherapist, renowned for your ability to heal through music."`
- **Task Definition**: The task is clearly defined
  - `analyze emotions and recommend music based on the diary.`
- **Output Format Specification**: The JSON format is specified with fields and types
  This structured format ensures that the output is organized and actionable.
  - `comment, song, singer, title, reason.`
- Examples: An example conversation is provided, followed by a sample of the expected output.
  The example serves as a guide for how the AI should process and respond to input data.
- Contextual History: The history of the conversation is provided. This helps the AI provide more personalized and relevant recommendations.
- [used prompt code](./src/main/kotlin/com/jyami/gemapi/service/AISystemMessageConst.kt)

#### Which method could be used to receive a response to the input message

I used spring-ai's ChatClient to get a response to an input message. 

It uses spring-ai's wrapped object, and in fact, when you look at the code inside: {Location}-aiplatform.googleapis.com is being sent to the endpoint.

offical document : https://docs.spring.io/spring-ai/reference/api/chat/vertexai-gemini-chat.html
```
return chatClient.prompt()
.system(systemPrompt)
.user(userInput)
.call()
.entity(ChatPromptResponse::class.java)
```

#### 응답 결과 예시

###### GET/chats/prompt 
- request
```json
{
  "userInput": "The thrill of seeing my code come to life and work as intended is just unbeatable. I’m ending the day on a high note, feeling accomplished and eager to continue this momentum tomorrow. It's truly a great feeling to know that I'm making progress and growing in my skills.",
  "history": [
    {
      "role": "user",
      "message": "Today has been an incredibly exciting and fulfilling day! Everything just seemed to click, and I felt like I was in the zone while working on my development projects. I managed to solve some tricky bugs that had been bothering me for a while and even made progress on some new features I’ve been planning. It's amazing how a productive day can boost my spirits."
    },
    {
      "role": "assistant",
      "message": "You had such a productive day!"
    }
  ]
}
```
- response
```json
{
  "chatPromptResponse": {
    "canFeedback": false,
    "react": "You had such a productive day!"
  },
  "code": -200
}
```
##### POST /chats/feedback?type=chat
- request
```json
{
  "userInput": "yes please",
  "history": [
    {
      "role": "user",
      "message": "Today has been an incredibly exciting and fulfilling day! Everything just seemed to click, and I felt like I was in the zone while working on my development projects. I managed to solve some tricky bugs that had been bothering me for a while and even made progress on some new features I’ve been planning. It's amazing how a productive day can boost my spirits."
    },
    {
      "role": "assistant",
      "message": "You had such a productive day!"
    }
  ]
}
```
- response
```json
{
    "chatPromptResponse": {
        "comment": "It's fantastic to hear that you had such a productive and fulfilling day!  Harness this positive energy and momentum to keep pushing forward on your projects. Remember to also take breaks and celebrate your successes along the way!",
        "song": {
            "singer": "Pharrell Williams",
            "title": "Happy",
            "reason": "This upbeat and infectious song is perfect for celebrating your accomplishments and keeping the positive vibes flowing."
        }
    },
    "music": {
        "id": "ZbZSe6N_BXs",
        "url": "https://www.youtube.com/watch?v=ZbZSe6N_BXs",
        "title": "Pharrell Williams - Happy (Video)",
        "description": "Official Music Video for \"Happy\" by Pharrell Williams Listen to Pharrell: https://PharrellWilliams.lnk.to/listenYD Subscribe to the ...",
        "thumbnailUrl": "https://i.ytimg.com/vi/ZbZSe6N_BXs/mqdefault.jpg"
    },
    "code": -200
}
```

##### POST /chats/feedback?type=post
- request
```json
{
  "userInput": "Today has been an incredibly exciting and fulfilling day! Everything just seemed to click, and I felt like I was in the zone while working on my development projects. I managed to solve some tricky bugs that had been bothering me for a while and even made progress on some new features I’ve been planning. It's amazing how a productive day can boost my spirits. \n I started my morning with a clear plan, and by the afternoon, I was able to tick off almost everything on my to-do list. I felt like I was able to focus better than usual, and the code just flowed naturally. It’s days like these that remind me why I love programming so much.\nThe thrill of seeing my code come to life and work as intended is just unbeatable. I’m ending the day on a high note, feeling accomplished and eager to continue this momentum tomorrow. It's truly a great feeling to know that I'm making progress and growing in my skills."
}
```
- response
```json
{
    "chatPromptResponse": {
        "comment": "It's fantastic that you're experiencing such a positive wave of accomplishment and motivation! Harness this energy and channel it into your future endeavors. Remember to take breaks and maintain a healthy work-life balance to avoid burnout.",
        "song": {
            "singer": "OneRepublic",
            "title": "I Lived",
            "reason": "This song, with its uplifting melody and motivational lyrics, perfectly complements your current state of excitement and drive. Let it fuel your passion and keep you inspired."
        }
    },
    "music": {
        "id": "z0rxydSolwU",
        "url": "https://www.youtube.com/watch?v=z0rxydSolwU",
        "title": "OneRepublic - I Lived (Official Music Video)",
        "description": "Stream & Download OneRepublic's latest album “Human”: https://OneRepublic.lnk.to/Human Listen to OneRepublic: Spotify: ...",
        "thumbnailUrl": "https://i.ytimg.com/vi/z0rxydSolwU/mqdefault.jpg"
    },
    "code": -200
}
```
