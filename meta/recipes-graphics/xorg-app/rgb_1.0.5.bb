require xorg-app-common.inc

SUMMARY = "X11 color name database"
DESCRIPTION = "This package includes both the list mapping X color names \
to RGB values (rgb.txt) and, if configured to use a database for color \
lookup, the rgb program to convert the text file into the binary database \
format."

DEPENDS += " xproto util-macros"
LIC_FILES_CHKSUM = "file://COPYING;md5=ef598adbe241bd0b0b9113831f6e249a"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[md5sum] = "bcd820d967eaa51bd25b86c0ee682d4e"
SRC_URI[sha256sum] = "8e936e648ffddce2e7184790efa15e4fa2bcb47f9da5469515d212c61bc8f857"

FILES_${PN} += "${datadir}/X11"
