require gdb-common.inc

DEPENDS = "ncurses-nativesdk expat-nativesdk gettext-nativesdk"

inherit cross-canadian

PR = "r10"

GDBPROPREFIX = "--program-prefix='${TARGET_PREFIX}'"
