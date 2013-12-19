SUMMARY = "Mobile Broadband Service Provider Database"
SECTION = "network"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=87964579b2a8ece4bc6744d2dc9a8b04"
SRCREV = "4ed19e11c2975105b71b956440acdb25d46a347d"
PV = "20120614+gitr${SRCPV}"
PE = "1"

SRC_URI = "git://git.gnome.org/mobile-broadband-provider-info"
S = "${WORKDIR}/git"

inherit autotools
