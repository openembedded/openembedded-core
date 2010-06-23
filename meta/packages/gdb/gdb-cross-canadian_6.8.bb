require gdb-common.inc

DEPENDS = "ncurses-nativesdk expat-nativesdk gettext-nativesdk"

inherit cross-canadian

PR = "r9"

GDBPROPREFIX = "--program-prefix='${TARGET_PREFIX}'"
