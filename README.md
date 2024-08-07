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
TBD

## documents
https://gem-api-xphhnkqc6a-du.a.run.app/swagger-ui/index.html

---
## extra descriptions
#### Gemini AI 호출 방법
- spring-ai에 정의된 VertexAiGeminiChatModel을 사용하여 호출하였습니다.
- https://docs.spring.io/spring-ai/reference/api/chat/vertexai-gemini-chat.html
- 서버단에서 GCP_CREDENTIALS을 사용하여 인증하고, GCP console에 정의해둔 vertex api 설정(PROJECT_ID, LOCATION_ID, AGENT_ID)을 사용하여 호출하였습니다.
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

#### 어떤 메소드를 사용하여 입력 메시지에 대한 응답을 받을 수 있었는지

spring-ai의 ChatClient를 사용하여 입력 메시지에 대한 응답을 받았습니다. 

spring-ai의 래핑된 객체를 사용한 것이고 사실상 안에 코드를 까보았을 때 : {Location}-aiplatform.googleapis.com을 엔드포인트로 요청을 보내고있습니다.

공식문서는 : https://docs.spring.io/spring-ai/reference/api/chat/vertexai-gemini-chat.html
```
return chatClient.prompt()
.system(systemPrompt)
.user(userInput)
.call()
.entity(ChatPromptResponse::class.java)
```

#### 응답 결과 예시
이걸 원하시는건가해서.. 우선은 gem api를 사용하는 api 응답 결과만 넣었습니다. 아니면 연희님이 직접 요청해보고싶은 예시 request 주세요

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