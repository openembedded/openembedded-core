DESCRIPTION = "Mobile Broadband Service Provider Database"
SECTION = "network"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=87964579b2a8ece4bc6744d2dc9a8b04"
SRCREV = "d9995ef693cb1ea7237f928df18e03cccba96f16"
PV = "1.0.0+gitr${SRCPV}"
PE = "1"
PR = "r0"

SRC_URI = "git://git.gnome.org/mobile-broadband-provider-info;protocol=git"
S = "${WORKDIR}/git"

inherit autotools
