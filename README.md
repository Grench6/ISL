# Image scripting language.
## Content
1. Introduction
1. Quick guide
1. Pratical example

## Introduction

A small scripting language for automatization of GUI made in Java. You can automate almost anything with this tool, as long as you know what to look for.

This scripting language was designed to be simple enough to be learned in just a few minutes, and still giving the user a complete toolbox to create whatever they need. Just remember: keep it simple, this is still a protoype.

Key features:

- Simple and easy to learn
- Fast to implement
- Performant

## Quick guide
```Python
#This is a comment
click_coordinate(500, 250)
```
The first line from the above script is a line comment. A line comment is definied by the special char ***#***, everything that follows the hashtag character will be ignored by the compiler.

The second line makes use of the **function** ***click_coordinate(x, y)*** to click the coordinates X:500, Y:250 on the screen.

---
```Python
@imgbtn = "C:\Users\User\Imagenes\imgbtn.png"
click(imgbtn)
```
The special char ***@*** is used to declare a **resouce**, which are variables that can hold images. In order to load an image you must choose a resouce identifier and specify a full path to where the image is located. **Resources must be declared at the very begining of the script and before anything else**, otherwise the compiler will throw an exception.

The function ***click(resource)*** will take a screenshot, and the search on it for the resource specified which is, in this case, the image of a button of a program. Once the program finds its position, it will be clicked. If the resource was not present in the screenshot, the program will not click anything and the execution of the script will continue to the nex line.

---
```Python
@imgbtn = "C:\Users\User\Imagenes\imgbtn.png"
click(imgbtn);
```
This is the same script as the one shown before, except for one thing: the special semicolon char ( ***;*** ) at the end of the click function. The semicolon represents a **deadlock**, which means **the execution of the code will not proceed until the condition of the function is met**. In the case of this script, the code will not proceed until the imgbtn can be found on the screen by the program, and once it is found the program will click it and exit the deadlock (continuing the execution of the script as normally).

When the deadlock is found the program will enter an infinite loop, taking multiple instrucions at certain rate (which will be explained in the next example) searching for the resouce. This simple concept can be very useful when you want the script to wait for a certain picture to appear on screen.

Note: Deadlocks can only be used with the following functions: ***contains(resouce); not_contains(resource); click(resouce);***

---
```Python
@imgbtn = "C:\Users\User\Imagenes\imgbtn.png"
rate = 1000
contains(imgbtn);
```
There are 2 new things on this script: the ***contains(resouce)*** function, and the ***rate*** **directive**.

The contains function will simply look for the image resource on screen, without clicking if found. It can be useful if used along with a deadlock, and so you can block the execution of your script until there is *something* (the resource) on the screen, and then resume the execution without making any interaction with the found object.

The rate directive will tell the program to change *the rate at which screenshots are taken when on a deadlock* and its meassured in milliseconds. The value of 1000 means the program will take screenshots every 1 second from there on, but there can be another rate directive changing the speed later on the script. This can be used to enter somewhat like an *idle state*, where you know you dont need precision and high speed detection of images.

---
```Python
@menu = "C:\Users\User\Images\menu.png"
@imgbtn = "C:\Users\User\Images\imgbtn.png"

reif contains(menu)
    click(imgbtn);
```
There is one last important thing to learn: the ***reif*** control sentece. **There can only be one *reif* per script.**

A ***reif*** is something like a save point or a return point. Since this script makes it really easy to create deadlocks, there must be a easy way out of them, and that is what a reif is for. 

You may know very well how to interact with a GUI, which images to click, but what if the GUI proccess fails, by the nature of the GUI you are working with? What would happen if you know that you must first wait and click button1, then wait and click button2, but between those operations something can fail, taking you back to the button1, while your script is waiting for button2? That is a real deadlock, and an endless loop, the script will get stuck in the deadlock searching for button2. This is a problem, but it can be easily solved with a reif. You could add a reif in searching for button1, so in case of fail, the script can restart its operation from the beggining.

As an example from the script above, each time the `click(imgbtn);` makes a screenshot, it will first evaluate if the resource of click can be found on screen, if it cannot, then the reif condition will be tested on that same screenshot (with the resouce of the condition of the reif). That will happen again, again and again, until either the reif or the click condition are met, moving the code execution to different ways.

TLDR: A ***reif*** is a simple concept: The script will return to the point where the reif is located (and continue execution from there again) if the condition of the reif is true. The reif condition will be tested each time an instruction (with or without deadlock) that is inside the reif body makes a screenshot and the condition of the instruction fails.

A reif can only have two conditions: ***contains(resouce)*** and ***not_contains(resource)***. Deadlocks have no effects when written on reif conditions.

## Pratical example
In this example we will create a script that will auto-select our character in the videogame Valorant.

Lets go!

1. We first have to open notepad to start writting our script.

```python
@sova = "C:\Users\User\Desktop\sova.png"
click(sova);
click_offset(0, -150)
```
Wait, is that it? 

Yes, it is very simple to automate things with this tool. In just 3 lines of code we automated our character selection!

- The first line loads the sova character image (which I recorted from a screenshot of the selection menu and saved it to a file in under 1 minute, using Paint!) and assigns it to the resource "sova".

- The second line is a *click* with *deadlock*, which means the program will wait for the image of the resource "sova" to appear on screen, immediatly clicking on it.

- The third line will click the "lock" button, which we know is 150 pixels above the sova image (as easily saw on Paint on the coordiantes bellow, making a easy substraction).

2. Once we are done writting the script, we save it to a file. I saved it to `myscript.txt` on my Desktop.

3. Finally, we execute our script with the following command on cmd: `java -jar isl.jar -execute myscript.txt`

And we can now open the game and test it out. We will see that just as the character select screen appears, our script will instantly click and lock on it. There are still some upgrades we can make to this script, check [this file](https://github.com/Grench6/ISL/blob/main/EXAMPLE.md) to see a more complete version of this script.

**There is a lot more this scripting language can do! This is only an example to show how easy and fast you can automate any GUI!**
