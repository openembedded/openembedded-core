require libx11.inc
require libx11_git.inc

EXTRA_OECONF += "--without-xcb"

inherit nativesdk
