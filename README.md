# Network BattleShip Game

**Created for university classes "Sieci komputerowe 2".**

# Functionality
The BattleShip Game is made on sockets with client-server architecture.

### Server

The server is responsible for executing all game logic and it listens on port 12345. 
Upon a client connection, it creates an ClientHandler object and adds it to the WaitingRoom. 
Once two clients are present in the WaitingRoom, the game starts between them, and all ensuing game logic is encapsulated within the Game module.

### Client
The client connects by the network socket on the port 12345 to the Server and his main role is to receive messages from the server and display them on the console.

# Rules of BattleShip Game
The game is played alternately between two players and first who sink all enemy ships wins.
Each player has their own board on which ships are randomly placed before the game begins.
To make a move, a player gives a place to shoot, e.g: A3.

**Legend of markings:**
'-' - possible ship, 
'S' - ship, 
'o' - miss, 
'X' - hit


# How to start project on your own
**Requirements:** Java JDK (recommended java 17)

1. Clone repository
```
git clone https://github.com/Gladziu/socket-battleship.git
```

2. Go to server directory, compile all files and run Server
```
javac ClientHandler.java
javac Game.java
javac WaitingRoom.java
javac Server.java
```
```
java Server
```

3. In another terminal go to client directory, compile it and run Client
```
javac Client.java
```
```
java Client
```
4. To finally start game you need to run one more client so in next terminal run Client


