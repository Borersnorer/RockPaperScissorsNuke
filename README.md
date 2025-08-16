# RockPaperScissorsNuke
An expanding game of Rock Paper Scissors: win and add in your own chosen weapons with their own interactions with existing ones!


app/
 ├── manifests/
 │    └── AndroidManifest.xml
 ├── java/
 │    └── com.example.rockpaperscissors/
 │         ├── MainActivity.kt
 │         └── GameActivity.kt
 └── res/
      ├── drawable/
      │    ├── rock.png
      │    ├── paper.png
      │    └── scissors.png
      ├── layout/
      │    ├── activity_main.xml
      │    └── activity_game.xml
      └── values/
           └── strings.xml


RockPaperScissors/             <-- Root project folder
│
├─ app/
│  ├─ src/
│  │  ├─ main/
│  │  │  ├─ java/com/example/rockpaperscissors/
│  │  │  │  ├─ MainActivity.kt          <-- Main menu (Play + Library)
│  │  │  │  ├─ GameActivity.kt          <-- Rock/Paper/Scissors game logic
│  │  │  │  ├─ LibraryActivity.kt       <-- Shop screen
│  │  │  │  ├─ ItemRotationActivity.kt  <-- Define beats/loses for new items
│  │  │  │
│  │  │  ├─ res/
│  │  │  │  ├─ layout/
│  │  │  │  │  ├─ activity_main.xml
│  │  │  │  │  ├─ activity_game.xml
│  │  │  │  │  ├─ activity_library.xml
│  │  │  │  │  ├─ activity_item_rotation.xml
│  │  │  │  │
│  │  │  │  ├─ drawable/
│  │  │  │  │  ├─ rock.png
│  │  │  │  │  ├─ paper.png
│  │  │  │  │  ├─ scissors.png
│  │  │  │  │  ├─ item_fireball.png
│  │  │  │  │  └─ ...other purchasable item icons
│  │  │  │  │
│  │  │  │  └─ values/
│  │  │  │     ├─ strings.xml
│  │  │  │     ├─ colors.xml
│  │  │  │     └─ styles.xml
│  │  │
│  │  │  └─ AndroidManifest.xml
│  │
│  └─ build.gradle
│
└─ build.gradle


MainActivity
  ├─ Play → GameActivity
  │       ├─ uses activeItems & beatsMap
  │       └─ dynamic didPlayerWin()
  └─ Library → LibraryActivity
          ├─ purchase items → update totalScore
          └─ opens ItemRotationActivity
                  └─ define beats/loses → update beatsMap


📌 Roadmap for Library & Dynamic Items

Step 1 — Persistent Data Setup

Store in SharedPreferences (or JSON for maps):

totalScore → Int

purchasedItems → Set<String>

activeItems → Set<String> (rock, paper, scissors + purchased)

beatsMap → JSON string mapping item → list of items it beats

Step 2 — LibraryActivity (Shop Screen)

Display all purchasable items as buttons with icons

When an item is clicked:

Check if totalScore >= cost

Subtract cost and save updated totalScore

Add item to purchasedItems and activeItems

Open ItemRotationActivity to define relationships

Step 3 — ItemRotationActivity (Define Beats/Loses)

Show all items in rotation as clickable icons

User selects which items the new item beats and which it loses to

If number of items is odd → allow 1 extra “beats” selection to balance

Save resulting relationships in beatsMap

Step 4 — GameActivity Integration

Load activeItems and beatsMap at the start of each round

Replace static didPlayerWin() logic with dynamic check:

fun didPlayerWin(player: String, opponent: String) =
    beatsMap[player]?.contains(opponent) == true


Everything else (animations, scoring, streaks) remains unchanged

Step 5 — Optional UI Enhancements

Library: show purchased vs unpurchased items

Game: allow player to select rotation items or view “who beats who”

Library: add icons for all items (rock/paper/scissors included)




