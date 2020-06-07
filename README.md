# Advanced Software Development Project - milestone 4

Writing a code interpreter using lexical analysis and syntax analysis to parse a new programming language that controls an unmanned aircraft.

## Introduction
In this repository, I will introduce milestone 4 in the final project of the Advanced Software Development course.
The goal in this milestone was to write an interpreter for a code that controls an unmanned aircraft.
The aircraft will fly in the virtual space of the "FlightGear" flight simulator.
This simulator is also a server that can be connected as a client (and also as a server).
This allows us to easily retrieve information about the various flight parameters in real time and even inject commands to fly the aircraft.


## Script Reader (Interpreter)
As mentioned, we would like to write an interpreter for a new programming language aimed at flying the aircraft in the simulator.
First, we set a sample code that aims to make the plane take off in a straight line.

As in the following example:

```scala
openDataServer 5400 10
connect 127.0.0.1 5402
var breaks = bind "/controls/flight/speedbrake"
var throttle = bind "/controls/engines/current-engine/throttle"
var heading = bind "/instrumentation/heading-indicator/indicated-heading-deg"
var airspeed = bind "/instrumentation/airspeed-indicator/indicated-speed-kt"
var roll = bind "/instrumentation/attitude-indicator/indicated-roll-deg"
var pitch = bind "/instrumentation/attitude-indicator/internal-pitch-deg"
var rudder = bind "/controls/flight/rudder"
var aileron = bind "/controls/flight/aileron"
var elevator = bind "/controls/flight/elevator"
var alt = bind "/instrumentation/altimeter/indicated-altitude-ft"
sleep 1350
breaks = 0
throttle = 1
var h0 = heading
sleep 5000
while alt < 2500 {
	rudder = (h0 - heading)/180
	aileron = - roll / 70
	elevator = pitch / 50
	sleep 20
}
print "done"
```
For this purpose, we wrote an interpreter which allows us to open a server, connect to the simulator, and run various commands that control the plane and sample its data.

We can see that arithmetic expressions are supported as well, and to interpret them we use Dijkstra's Shunting Yard algorithm.


## So, how does it work?

### Lexer

A function called lexing whose job is to read the script that needs to be interpreted (a single line from the console or a complete file of commands) and it will return an ArrayList of strings. 
Each string is a word in the program that needs to be interpreted.

<p align="center"><img src="/readme_images/Lexer.jpg" width="650"></p>

### Parser

A function called parsing that passes through each string in the array that the lexer created.
Each string will be used as a key by which we will extract the appropriate command type object from the map,
for the execute method of that command we will enter the rest of the line as an array of strings.
The execute method will verify that the array size is the number of parameters in the script.

#### The design pattern

The design pattern we use to implement the Parser is Command Pattern.
To do this, we will define an interface named Command with method execute().
Each command in our system will be a class of this kind of Command.
Thus a polymorphic Command can be any specific command, and we will execute all commands in the same way.

The programming trick is to put all the commands into a hash-based map so that the key is a string, 
and the value is a specific type of command.
So given the string, we can immediately pull out the command that needs to be executed.

<p align="center">
  <img src="/readme_images/Parser.jpg" width="650">
</p>
