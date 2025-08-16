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




Summary of Files
Kotlin / Activity Files
File	Purpose
MainActivity.kt	App main menu with Play and Library buttons. Launches GameActivity or LibraryActivity.
GameActivity.kt	Core game logic. Displays top/bottom animations, scrollable image-only item selection, handles rounds, score, streaks, and persistent storage.
LibraryActivity.kt	Displays purchased items and allows user to purchase new items (currently stores in SharedPreferences).
Utils.kt (optional)	Helper functions like JSON ↔ Map conversion, if needed.
Drawable / Image Files (.png)

These are your game assets (placed in res/drawable/):

rock.png – rock image

paper.png – paper image

scissors.png – scissors image

selected_border.xml – highlight border for selected items

Any additional items purchased later: e.g., fire.png, water.png, etc.

XML Layout Files
File	Purpose
activity_main.xml	Main menu layout (Play and Library buttons).
activity_game.xml	Game screen layout with: top/bottom animation images, score/streak TextViews, scrollable item selection, start button.
activity_library.xml	Layout for Library screen (buttons or icons for purchased items).



MyRPSApp/
├─ app/
│  ├─ src/
│  │  ├─ main/
│  │  │  ├─ java/com/example/rpsgame/
│  │  │  │  ├─ MainActivity.kt
│  │  │  │  ├─ GameActivity.kt
│  │  │  │  ├─ LibraryActivity.kt
│  │  │  │  └─ Utils.kt (optional)
│  │  │  ├─ res/
│  │  │  │  ├─ drawable/
│  │  │  │  │  ├─ rock.png
│  │  │  │  │  ├─ paper.png
│  │  │  │  │  ├─ scissors.png
│  │  │  │  │  ├─ selected_border.xml
│  │  │  │  │  └─ any additional purchased items (.png)
│  │  │  │  ├─ layout/
│  │  │  │  │  ├─ activity_main.xml
│  │  │  │  │  ├─ activity_game.xml
│  │  │  │  │  └─ activity_library.xml
│  │  │  │  ├─ values/
│  │  │  │  │  ├─ colors.xml
│  │  │  │  │  └─ dimens.xml
│  │  │  │  └─ (other Android resources)
│  │  │  └─ AndroidManifest.xml
│  │  └─ (test folders)
└─ build.gradle, settings.gradle, etc.



✅ Current Functionality

Main menu → choose Play or Library.

GameActivity:

Scrollable image selection of active items.

Top/bottom animations for the round.

Streak & total score system with persistent storage.

Manual “Start Round” button to play next round.

LibraryActivity (basic) stores purchased items in SharedPreferences.

Dynamic framework for adding new items with their beat rules later.




