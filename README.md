# 🎉 VoteParty (Fabric)

**VoteParty** is a lightweight Fabric **server-side mod** that tracks player votes and triggers a server-wide **Vote Party** when a configurable goal is reached. It is designed to integrate cleanly with vote listeners, placeholders, and reward systems.

---

## ✨ Features

* 📊 Persistent vote tracking (saved to JSON)
* 🎯 Automatic Vote Party triggering
* 🛠 Manual admin controls
* 📈 Visual vote progress bars
* 🔁 Reloadable config (no restart required)
* 🧩 pb4 Placeholders API support
* ⚙️ Console-executed reward commands

---

## 📦 How It Works

1. Votes are added using a command (usually by a vote listener)
2. Vote progress is saved to disk
3. When the required vote amount is reached:

    * A Vote Party is triggered
    * Titles are shown to players
    * Reward commands are executed
    * Vote count is reset
4. Admins can manually trigger or reset at any time

---

## 🧾 Commands

### `/vpadd`

* **Permission level:** 4
* Adds **1 vote**
* Intended for vote listeners or admin use

---

### `/vp info`

* Displays current vote progress
* Includes a formatted progress bar

---

### `/vp trigger`

* **Permission level:** 4
* Manually triggers a Vote Party
* Executes reward commands
* Resets vote count

---

### `/vp reset`

* **Permission level:** 4
* Resets the vote count without triggering rewards

---

### `/vpreload`

* Reloads the config file without restarting the server

---

## 📊 Progress Display

VoteParty includes built-in text-based progress bars that can be shown in:

* Chat
* Scoreboards
* Placeholder-based UIs

---

## 🧩 Placeholder Support

VoteParty integrates with **pb4 Placeholders API**, allowing vote data to be used across other mods.

Common placeholders include:

* Current votes
* Required votes
* Vote progress bar

These can be used in:

* Scoreboards
* MOTDs
* Chat formats
* UI mods

---

## ⚙️ Configuration

VoteParty is configured using a JSON file.

### Example `config/voteparty.json`

```json
{
  "requiredVotes": 50,
  "title": "§6§lVOTE PARTY!",
  "subtitle": "§e%current_votes% votes reached!",
  "commands": [
    "effect give @a cobblecuisine:shinyspawn 60 0 false"
  ]
}
```

---

### 🧾 Config Options

* **`requiredVotes`**
  Number of votes required to trigger a Vote Party.

* **`title`**
  Title shown to players when the Vote Party triggers.
  Supports Minecraft color codes (`§`).

* **`subtitle`**
  Subtitle shown under the title.
  Supports placeholders such as `%current_votes%`.

* **`commands`**
  List of commands executed by the **console** when the Vote Party triggers.
  Commands may target all players (`@a`) or individual players (`{player}`).

---

## 🗂 Data Storage

* Vote progress is stored as a **JSON file**
* Automatically loads on server startup
* Automatically saves on vote changes
* Safely resets if the file is missing or invalid

---

## 🔌 Integration Tips

* Use a vote listener to run `/vpadd` on each vote
* Display progress using placeholders in scoreboards or hubs
* Configure rewards such as:

    * Crate keys
    * Economy money
    * Items
    * Permissions or ranks

---

## 🧠 Technical Info

* Fabric server-side mod
* Uses Fabric Command API v2
* Gson for JSON serialization
* Singleton-based vote manager
* No client installation required

---

## 🎉 Credits
- Created and maintained by **FreeSpinner**.
- Created and Primarily used on [Cobblemon Vulkan](https://discord.gg/KuHnJSh8dz).