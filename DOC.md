# Special chars

- **@** Resource declaration.

- **#** Line comment.

- **;** Deadlock.

---
# Functions
## No deadlockable (They dont take screenshots)

- **sleep(*millis*)** Sleeps the thread for the specified millis.

- **write_line(*string*)** Sends keystrokes to write the given string.

- **click_coordinate(*x, y*)** Clicks the given coordinates on screen.

- **click_offset(*x, y*)** Clicks the position of the last found resouce, with the given offset.

## Deadlockable (They take screenshots)

- **contains(*resouce*)** Is resouce contained on current screen?

- **not_contains(*resouce*)** Is resouce not contained on current screen?

- **click(*resouce*)** Click resouce on screen if found.

---
# Directives

- **rate = *millis*** Changes screenshot rate.

---
# Control sentences

- **reif *function*** Returns execution of script to this point, if the given condition is met. Only valid conditions are *contains* or *not_contains*.
