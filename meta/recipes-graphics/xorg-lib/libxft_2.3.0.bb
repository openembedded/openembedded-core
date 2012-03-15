SUMMARY = "XFt: X FreeType libary"

DESCRIPTION = "Xft was designed to provide good support for scalable \
fonts, and to do so efficiently.  Unlike the core fonts system, it \
supports features such as anti-aliasing and sub-pixel rasterisation. \
Perhaps more importantly, it gives applications full control over the \
way glyphs are rendered, making fine typesetting and WYSIWIG display \
possible. Finally, it allows applications to use fonts that are not \
installed system-wide for displaying documents with embedded fonts.  Xft \
is not compatible with the core fonts system: usage of Xft requires \
fairly extensive changes to toolkits (user-interface libraries)."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=90b90b60eb30f65fc9c2673d7cf59e24"

DEPENDS += "virtual/libx11 libxrender freetype fontconfig"
PROVIDES = "xft"

PR = "r0"
PE = "1"

XORG_PN = "libXft"

BBCLASSEXTEND = "native"

python () {
        if d.getVar('DEBIAN_NAMES', True):
            d.setVar('PKG_${PN}', '${MLPREFIX}libxft2')
}

SRC_URI[md5sum] = "bd0a8d8cace1dfbb963250bf3eb9a19a"
SRC_URI[sha256sum] = "3426393ad72a5c47006536d474e396c7a21c33131bb28f495578e05a5858b044"
