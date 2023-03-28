# NFServer
simple minestom based server with some integrated libraries
## Used libraries
- Configurate HOCON
- MinestomPVP
- MiniMessage
## Building
`.\gradlew`
## Running
Java 17 or newer is required
`java -jar NFServer-(version)-all.jar`
## Usage
- By default, new `server-properties.conf` will be created near server file;
- You can easily access configuration in code by calling `ServerProperties`;
- That's all I think. It should be enough to create your own things