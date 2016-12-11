# stopped-app-detector
Detects which apps are in a Stopped state on Android

This app lists all installed apps that are in a 'stopped' state, those that have never been manually launched, or were force stopped in Settings -> Applications.  On some non-standard ROMs, apps are put into a stopped state by swiping them off the screen from the task switcher.  You may test if this is such a ROM by launching your app, swiping it off the screen with the task switcher, and checking to see if it gets added to the list.

This is important to know in building apps that start services automatically, as they will not be able to do so if put into a stopped state.
