require gdb-common.inc

DEPENDS = "ncurses-native"

#EXTRA_OEMAKE += "LDFLAGS='${BUILD_LDFLAGS}'"

GDBPROPREFIX = ""

inherit cross
inherit gettext
