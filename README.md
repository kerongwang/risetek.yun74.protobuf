# risetek.yun74.protobuf
Protobuf files for yun74 project
#### NOTE
* client depend on protobuf module, do ``mvn clean install`` when protobuf files changed.
* TODO: protobuf generated code to /src/main/generated, so, within eclipse, java builder path should add it. Properties-> Java Build Path -> Add Folder -> generated

#### DEBUG
```
mvn exec:java -pl client
```