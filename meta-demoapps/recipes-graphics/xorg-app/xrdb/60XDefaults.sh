if [ -e $HOME/.Xdefaults ]; then
    xrdb -merge -nocpp < $HOME/.Xdefaults
fi
