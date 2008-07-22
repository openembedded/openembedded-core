DESCRIPTION = "GSM 07.10 muxer userspace daemon"
HOMEPAGE = "http://www.freesmartphone.org/mediawiki/index.php/Implementations/gsm0710muxd"
AUTHOR = "M. Dietrich"
SECTION = "console/network"
DEPENDS = "dbus dbus-glib"
LICENSE = "GPL"
PV = "0.9.1+gitr${SRCREV}"

SRC_URI = "${FREESMARTPHONE_GIT}/gsm0710muxd.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

inherit autotools

# install init script for people who want to manually
# start/stop it, but don't add runlevels.
do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 data/gsm0710muxd ${D}${sysconfdir}/init.d/
}

FILES_${PN} += "${datadir} ${sysconfdir}"
