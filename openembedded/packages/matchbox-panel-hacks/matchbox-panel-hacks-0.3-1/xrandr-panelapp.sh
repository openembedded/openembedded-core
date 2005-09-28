#!/bin/sh
if [ -n "$(xrandr | grep rotation | grep left)" ]
then 
xrandr -o normal
xmodmap /etc/X11/xmodmap-portrait
else 
xrandr -o left
xmodmap /etc/X11/xmodmap-left
fi
