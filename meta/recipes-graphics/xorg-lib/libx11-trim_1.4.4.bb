require libx11.inc

DESCRIPTION += " Support for XCMS is disabled in this version."

LICENSE = "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=172255dee66bb0151435b2d5d709fcf7"

PR = "r0"

DEPENDS += "libxcb xproto xextproto xtrans libxau kbproto inputproto xf86bigfontproto xproto-native"

SRC_URI += "file://x11_disable_makekeys.patch \
            file://keysymdef_include.patch \
            file://makekeys_crosscompile.patch"


SRC_URI[md5sum] = "f65c9c7ecbfb64c19dbd7927160d63fd"
SRC_URI[sha256sum] = "88d7238ce5f7cd123450567de7a3b56a43556e4ccc45df38b8324147c889a844"

EXTRA_OECONF += "--with-keysymdef=${STAGING_INCDIR}/X11/ --disable-xcms "
