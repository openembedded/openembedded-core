SUMMARY = "Mobile Broadband Service Provider Database"
SECTION = "network"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=87964579b2a8ece4bc6744d2dc9a8b04"
SRCREV = "c5e0139cdbb7d57399a804c1fddaa7759b7a4fa0"
PV = "20151106+gitr${SRCPV}"
PE = "1"

SRC_URI = "git://git.gnome.org/mobile-broadband-provider-info"
S = "${WORKDIR}/git"

inherit autotools
