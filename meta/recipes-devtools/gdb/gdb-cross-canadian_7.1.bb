require gdb-common.inc

DEPENDS = "ncurses-nativesdk expat-nativesdk gettext-nativesdk"

inherit cross-canadian

PR = "r1"

GDBPROPREFIX = "--program-prefix='${TARGET_PREFIX}'"
EXPAT = "--with-expat"
