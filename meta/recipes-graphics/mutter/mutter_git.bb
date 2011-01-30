require mutter.inc

PV = "2.29.1+git${SRCPV}"
PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

# Gnome is the upstream but moblin is under more active development atm
# git://git.gnome.org/mutter.git;protocol=git;branch=master
#
SRC_URI = "git://git.moblin.org/mutter.git;protocol=git;branch=master \
           file://nodocs.patch;patch=1 \
           file://nozenity.patch;patch=1 \
           file://crosscompile.patch;patch=1;rev=7adb574bb3fa3880eb85dbc86e580cf3452d57c4 \
           file://fix_pkgconfig-7adb574bb3fa3880eb85dbc86e580cf3452d57c4.patch;patch=1;rev=7adb574bb3fa3880eb85dbc86e580cf3452d57c4 \
           file://fix_pkgconfig.patch;patch=1;notrev=7adb574bb3fa3880eb85dbc86e580cf3452d57c4 \
           "
S = "${WORKDIR}/git"

