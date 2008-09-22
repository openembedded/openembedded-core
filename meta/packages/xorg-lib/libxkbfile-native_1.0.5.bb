require libxkbfile_${PV}.bb

DEPENDS = "libx11-native kbproto-native"
PE = "1"

XORG_PN = "libxkbfile"

inherit native
