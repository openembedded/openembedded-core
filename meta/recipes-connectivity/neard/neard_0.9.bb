SUMMARY  = "Linux NFC daemon"
DESCRIPTION = "A daemon for the Linux Near Field Communication stack"
HOMEPAGE = "http://01.org/linux-nfc"

LICENSE  = "GPLv2"

DEPENDS  = "dbus glib-2.0 libnl"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "neard"
INITSCRIPT_PARAMS = "defaults 64"

# This would copy neard start-stop shell and test scripts
do_install_append() {
  install -d ${D}${sysconfdir}/init.d/
  install -m 0755 ${WORKDIR}/neard ${D}${sysconfdir}/init.d/neard

  install -d ${D}${libdir}/neard
  install -m 0755 ${S}/test/* ${D}${libdir}/neard/
  install -m 0755 ${S}/tools/nfctool/nfctool ${D}${libdir}/neard/
}

RDEPENDS_${PN} = "dbus python python-dbus python-pygobject"

# Bluez & Wifi are not mandatory except for handover	"
RRECOMMENDS_${PN} = "\
	${@base_contains('DISTRO_FEATURES', 'bluetooth', 'bluez4', '', d)} \
	${@base_contains('DISTRO_FEATURES', 'wifi','wpa-supplicant', '', d)} \
	"

#Additional
PACKAGES =+ "${PN}-tests"

FILES_${PN}-tests = "${libdir}/neard/*-test"
RDEPENDS_${PN}-tests = "python python-dbus python-pygobject"

FILES_${PN}-dbg += "${bindir}/neard/*/.debug"

##=============================
# This is valid for 0.9+
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
 file://src/near.h;beginline=1;endline=20;md5=358e4deefef251a4761e1ffacc965d13 \
 "
S	= "${WORKDIR}/git"
SRCREV	= "7abdb13d106d496e1115fab49e6448a249dfb3c8"
PV	= "0.9-git${SRCPV}"
PR	= "r1"

SRC_URI  = "git://git.kernel.org/pub/scm/network/nfc/neard.git;protocol=git \
	file://neard \
	"

EXTRA_OECONF += "--enable-tools \
	"


