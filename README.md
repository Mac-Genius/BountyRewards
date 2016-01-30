Project Pages
------

<a href="http://dev.bukkit.org/bukkit-plugins/bountyrewards/"><img src="http://i.imgur.com/igYbvzR.png" height="7%" width="7%"></a>
<a href="https://www.spigotmc.org/resources/bountyrewards.17589/"><img src="https://static.spigotmc.org/img/spigot.png" height="10%" width="10%"></a>

# BountyRewards

### Description

Placing bounties has never been easier! You can place, increase, and remove bounties all with a few easy commands! Whether you are placing, increasing, or removing a bounty, the new chat interface streamlines the process. You no longer have to type a long command, but rather answer the simple questions provided. If allowed, a player can place a bounty anonymously!

![Place Bounty](http://i.imgur.com/UmUlGmD.png "Place a Bounty")

### Double the Storage!

BountyRewards now supports both local bounty storage and MySQL! Whether you are hosting a local server for you and your friends, or running multiple servers, BountyRewards has the scalability you require.

![Database Connection](http://i.imgur.com/DV9IaD0.png "Database connection")

When using MySQL, bounties and your settings will be saved to the database. That way if you have multiple servers running, you will always have the same information on each one.

BountyRewards also has Bungeecord support built in, so messsages can be announced on all the servers using the plugin!

![MultiServer](http://i.imgur.com/tNootl3.png "MultiServer")

When using MySQL, settings and messages from the database are cached so the plugin can retrieve them faster. If Bungeecord is enabled, then updating the database and cache on one server will update the cache on all servers running the plugin!

### MCStats

BountyRewards is now bundled with MCStats! You can check out the [BountyRewards page](http://mcstats.org/plugin/BountyRewards) on MCStats and see what cool statistics are being collected! All statistics are collected anonymously and you can learn more about MCStats [here](http://mcstats.org/learn-more/).

### Update Checking

BountyRewards will now automatically check to see if there is an update for the plugin! It will display the latest version and give you a link if you want to download it. **The latest version of the plugin will not be downloaded for you. If you want to disable this feature change "UpdateChecking" in the config.yml to false.**

### Config Updates

Configs are now automatically updated when using 1.3 or later! All changes to the config.yml you may have made will be preserved in the update process.

## Permissions

* br.* - allows a user to use all commands

* br.setbounty - allows a user to setbounties

* br.increasebounty - allows a player to increase the amount a bounty is worth

* br.removebounty - allows a player to remove a bounty

* br.list - allows a user to see all current bounties

* br.anonymous - allows a player to place a bounty anonymously

* br.help - allows a user to see the commands

* br.reload - allows a player to reload the config

* br.updatesql - allows a player to update the MySQL database

* br.showupdate - will display an update message to the player if there is an update

## Commands

* /bounty help - The help menu

* /bounty reload - Reloads the config

* /bounty set - Sets the bounty on a player

* /bounty increase - Increases the amount a bounty is worth

* /bounty remove - Removes a bounty

* /bounty list - Shows all current bounties

* /bounty updatesql - Updates the MySQL database

## Dependencies

* The latest version of Vault

* An economy plugin (Essentials has one)

* Java 1.8
