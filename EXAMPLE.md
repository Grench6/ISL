```python
#Sova auto-select script
@sova = "C:\Users\User\Desktop\sova.png"
@found = "C:\Users\User\Desktop\match_found.png"

rate = 1000
contains(found);
rate = 25
reif contains(found)
	click(sova);
	click_offset(0,0)
	click_offset(0,-150)
	click_offset(0,-150)
	rate = 1000
	contains(found);
	rate = 25
	recall
```

The upgrades on this script are the following:

- I made the script fail-proof (using *reif* to detect if someone didnt selected character, sending me back to menu)

- I made the script infinite (with *reif* and *recall*)

- I implemented an idle mode (changing screenshots rate when they are not neccessary)

- I click twice both sova and the lock button in case the game didnt detected it.
