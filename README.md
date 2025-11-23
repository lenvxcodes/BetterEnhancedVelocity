# About
EnhancedVelocity is a powerful Velocity plugin designed to significantly improve your server experience. This project has undergone a comprehensive optimization and feature enhancement process, migrating to modern Kotlin practices and introducing new, highly requested functionalities.

More information can be found at [Why EnhancedVelocity](https://github.com/Syrent/EnhancedVelocity/wiki/Why-EnhancedVelocity%3F)? [wiki page](https://github.com/Syrent/EnhancedVelocity/wiki).

# What is it?
With EnhancedVelocity, you gain access to a suite of versatile commands and features tailored for efficient server management:
*   `/glist`: View a list of players across all connected servers.
*   `/find`: Locate a specific player and their current server.
*   `/ping`: Check server latency for yourself or other players.
*   `/send`: Transfer individual players to a specified server.
*   `/alert`: Broadcast messages across the proxy.
*   `/kickall`: Kick all players from a server or the entire proxy.
*   **/move**: (NEW!) Seamlessly send players, entire servers, or all online players to a different destination server.
*   **Startup Commands**: (NEW!) Configure commands to be executed automatically when the Velocity proxy fully initializes.

These commands and features are designed to be highly configurable and integrate smoothly into your Velocity setup.

# Improvements & New Features

This version of EnhancedVelocity brings significant enhancements:

*   **Kotlin Migration**: The codebase has been largely migrated from Java to Kotlin, resulting in more concise, readable, and maintainable code.
*   **Code Optimization**: Extensive refactoring has been performed across the plugin, including:
    *   Simplified command registration and logic.
    *   Optimized configuration loading and management.
    *   Streamlined utility functions.
    *   Improved handling of vanished players across `Ping`, `Send`, and `Move` commands, respecting the `SEE_VANISHED` permission.
*   **New Features**:
    *   **Move Command (`/move`)**: A powerful new command to transfer players efficiently. Supports moving individual players, all players on a specific server (`server:<server_name>`), or all players on the proxy (`all`) to a designated server.
    *   **Startup Commands**: A new configuration option (`startup_commands` in `settings.yml`) allows administrators to define a list of commands to be executed automatically upon proxy initialization.
*   **Dependency Updates**: Key dependencies, including `velocity-api`, `bstats-velocity`, `adventure-text-minimessage`, and `commons-io`, have been updated to their latest versions for improved performance, security, and compatibility with Velocity 3.*.

# Compiling
Compilation requires JDK 8 and up.   
To compile the plugin, run `./gradlew build` from the terminal.   
Once the plugin compiles, grab the jar from `/bin` folder.   

# Links
The precompiled JAR can be downloaded for free from:

* GitHub releases: https://github.com/Syrent/EnhancedVelocity/releases
* Modrinth (Recommended): https://modrinth.com/plugin/enhancedvelocity
* Spigot: (Can't upload because of Spigot TOS)
* Polymart: https://polymart.org/resource/enhancedvelocity.3070
* Paper's forum: https://forums.papermc.io/threads/enhancedvelocity.556/

Other links:

* Wiki: https://github.com/Syrent/EnhancedVelocity/wiki

# Contributing
If this plugin has helped you in any way, and you would like to return something back to make the plugin even better, there is a lot of ways to contribute:

* Open a **pull request** containing a new feature or a bug fix, which you believe many users will benefit from.
Make detailed high-quality bug reports. The difference between a bug getting fixed in 1 week vs 1 hour is in quality of the report (typically providing correct steps to reproduce that actually work).
* Help improve the wiki by opening a Wiki change issue, where you can improve existing descriptions, add information you found missing, fix typos / grammar mistakes or add more examples of usage of functions.
