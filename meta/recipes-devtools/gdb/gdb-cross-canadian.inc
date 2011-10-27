inherit cross-canadian

DESCRIPTION = "cross-canadian gdb for ${TARGET_ARCH} target - GNU debugger"
PN = "gdb-cross-canadian-${TRANSLATED_TARGET_ARCH}"
BPN = "gdb"

DEPENDS = "ncurses-nativesdk expat-nativesdk gettext-nativesdk readline-nativesdk"
