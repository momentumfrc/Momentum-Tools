# Momentum-Tools
Source code and maven repository for various momentum tools

Maven repository url: https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/mvn-repo/

## Momentum-Tuners

A set of wrappers and utility classes for storing PID values in a .ini file, while also listening for updates
to these values over the network tables.

Very handy for using Glass to tune PID values.

Which vendor file you need depends on which wrappers you want included with the library.

| Wrappers               | Vendor File                                                                                                            |
|------------------------|------------------------------------------------------------------------------------------------------------------------|
| *None*                 | <https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/json/momentum-tuners/momentum-tuners-core.json>   |
| REVLib (SparkMax)      | <https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/json/momentum-tuners/momentum-tuners-revlib.json  |
| CTRE Phoenix (TalonFX) | <https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/json/momentum-tuners/momentum-tuners-phoenix.json |
| *All of the above*     | <https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/json/momentum-tuners/momentum-tuners-all.json     |

## Momentum-PID (**DEPRECATED**)

Custom momentum pid controller.

This project was created because we had several issues with the default
wpilib-provided PID implementation. However, in the last few years,
wpilib has updated their PID implementation to fix these issues. Thus
this project has been deprecated.

GroupId: org.usfirst.frc.team4999 \
ArtifactId: momentum-pid

Vendor file url: https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/json/momentum-pid.json

## Neopixel-LEDs
I2C driver for interfacing with an arduino to run Neopixel LEDs. See [blinky-lights](https://github.com/momentumfrc/blinky-lights).

GroupId: org.usfirst.frc.team4999 \
ArtifactId: neopixel-leds

Vendor file url: https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/json/neopixel-leds.json

## Momentum-Utils
Various utility functions, such as `clip()`, `deadzone()`, and `map()`.

GroupID: com.momentum4999 \
ArtifactId: momentum-utils

Vendor file url: https://raw.githubusercontent.com/momentumfrc/Momentum-Tools/master/json/momentum-utils.json

## Momentum-PID-Shuffleboard
Shuffleboard plugin to display a Momentum-PID object

GroupId: org.usfirst.frc.team4999 \
ArtifactId: momentum-pid-shuffleboard
