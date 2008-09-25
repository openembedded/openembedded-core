DESCRIPTION = "The Dialer"
DEPENDS = "pulseaudio eds-dbus libjana libnotify libgsmd"
LICENSE = "GPL"
PV = "0.0+gitr${SRCREV}"
PR = "r5"

inherit autotools pkgconfig

SRC_URI = "git://folks.o-hand.com/thomas/git/phone.git;protocol=http"
S = "${WORKDIR}/git"

EXTRA_OECONF = "--with-dbusbindir=${STAGING_BINDIR_NATIVE}"

FILES_${PN} += "${datadir}/openmoko-dialer/ ${datadir}/dbus-1/services/"
FILES_${PN} += "${datadir}/icons"