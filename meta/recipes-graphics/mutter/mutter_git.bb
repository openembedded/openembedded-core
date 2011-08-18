require mutter.inc

SRCREV = "01d3bebe24d2a56bdf2d82c4a71923e1573f7b7c"
PV = "2.29.1+git${SRCPV}"
PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

# Gnome is the upstream but moblin is under more active development atm
# git://git.gnome.org/mutter.git;protocol=git;branch=master
#
SRC_URI = "git://git.moblin.org/mutter.git;protocol=git;branch=master \
           file://nodocs.patch \
           file://nozenity.patch \
           file://crosscompile.patch;rev=7adb574bb3fa3880eb85dbc86e580cf3452d57c4 \
           file://fix_pkgconfig-7adb574bb3fa3880eb85dbc86e580cf3452d57c4.patch;rev=7adb574bb3fa3880eb85dbc86e580cf3452d57c4 \
           file://fix_pkgconfig.patch;notrev=7adb574bb3fa3880eb85dbc86e580cf3452d57c4 \
           "
S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"
