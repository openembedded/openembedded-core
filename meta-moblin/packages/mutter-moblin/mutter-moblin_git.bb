require mutter-moblin.inc

PV = "0.43.8+git${SRCPV}"
PR = "r11"

SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://startup-notify.patch;patch=1 \
           file://88mutter-panelapps.sh \
           file://background-tile.png"

S = "${WORKDIR}/git"

