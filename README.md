Check Repository = https://jitpack.io/#AchyMake/Replant
```xml
<repositories>
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.AchyMake</groupId>
        <artifactId>Replant</artifactId>
        <version>LATEST</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
Example

```java
package org.example.yourplugin;

import org.achymake.replant.events.PlayerReplantEvent;

public class PlayerReplant implements Listener {
    private YourPlugin getInstance() {
        return YourPlugin.getInstance();
    }

    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }

    public PlayerReplant() {
        getPluginManager().registerEvents(this, getInstance());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerReplant(PlayerReplantEvent event) {
        var player = event.getPlayer();
        var clickedBlock = event.getClickedBlock();
        //cancel event if you want
        event.setCancelled(true);
    }
}
```