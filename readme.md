# NFServer ![actions](https://github.com/notfoundname/NFServer/actions/workflows/gradle.yml/badge.svg)
simple minestom based server with some integrated libraries
## Used libraries
- [Configurate HOCON](https://github.com/SpongePowered/Configurate/)
- [MinestomBasicLight](https://github.com/notfoundname/MinestomBasicLight)
- [MinestomFluids](https://github.com/TogAr2/MinestomFluids)
- [MiniMessage](https://github.com/KyoriPowered/adventure)
## Building
`.\gradlew`
## Running
Java 17 or newer is required
`java -jar NFServer-(version)-all.jar`
## Usage
- By default, new `server-properties.conf` will be created near server file;
- You can easily access configuration in code by calling `ServerProperties`;
- That's all I think. It should be enough to create your own things