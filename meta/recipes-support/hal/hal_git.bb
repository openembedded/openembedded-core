require hal.inc

PV = "0.5.9.1+git${SRCDATE}"
PR = "r8"

SRC_URI = "git://anongit.freedesktop.org/hal/;protocol=git \
        file://20hal \
        file://99_hal"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"
