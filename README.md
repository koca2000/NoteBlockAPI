# NoteBlockAPI
[![](https://jitpack.io/v/koca2000/NoteBlockAPI.svg)](https://jitpack.io/#koca2000/NoteBlockAPI)

For information about this Spigot/Bukkit API go to https://www.spigotmc.org/resources/noteblockapi.19287/

### How to include in your plugin:
#### Step 1:
Maven:  
```maven
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.koca2000</groupId>
        <artifactId>NoteBlockAPI</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>
</dependencies>
```
Gradle:
```groovy
repositories {
  maven {
    url 'https://jitpack.io'
  }
  dependencies {
    compile group: 'com.github.koca2000', name: 'NoteBlockAPI', version: '-SNAPSHOT'
  }
}
```
#### Step 2
Add `depend: [NoteBlockAPI]` or `softdepend: [NoteBlockAPI]` to your `plugin.yml`.

### Code example: 
```java
Song s = NBSDecoder.parse(new File(getDataFolder(), "Song.nbs"));
// or
Song s = NBSDecoder.parse(new File("/plugins/myplugin/Song.nbs"));

SongPlayer sp = new RadioSongPlayer(s);
sp.setAutoDestroy(true);
sp.addPlayer(player);
sp.setPlaying(true);
```
