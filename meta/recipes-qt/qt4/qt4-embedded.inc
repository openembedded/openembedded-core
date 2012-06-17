DESCRIPTION = "Qt is a versatile cross-platform application framework -- this is the embedded version."
SECTION = "libs"
HOMEPAGE = "http://qt.nokia.com"
DEPENDS += "directfb tslib"
INC_PR = "r46"

QT_BASE_LIB  ?= "libqt-embedded"

# Set necessary variables in the profile
SRC_URI += "file://qte.sh"

QT_CONFIG_FLAGS += " \
    -embedded $QT_ARCH \
    -qtlibinfix ${QT_LIBINFIX} \
    -plugin-gfx-transformed -plugin-gfx-qvfb -plugin-gfx-vnc -plugin-gfx-directfb \
    -plugin-mouse-tslib -qt-mouse-pc -qt-mouse-qvfb -qt-mouse-linuxinput \
    -qt-kbd-tty \
    -DQT_KEYPAD_NAVIGATION \
    "

require qt4.inc

do_install_append() {
    install -d ${D}${sysconfdir}/profile.d/
    install -m 0755 ${WORKDIR}/qte.sh ${D}${sysconfdir}/profile.d/
}

# We put the qte profile into it's own package as we don't want to install all qt stuff
# with depending on the global package qt4-embedded
PACKAGES += " ${PN}-conf"
FILES_${PN}-conf += " ${sysconfdir}/profile.d/qte.sh"
RRECOMMENDS_${PN} += " ${PN}-conf"

inherit qt4e

