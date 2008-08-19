LICENSE = "GPL"
DESCRIPTION = "Touchscreen calibration utility"
SECTION = "x11/base"

DEPENDS = "virtual/libx11 libxft libxcalibrate"

PR = "r10"

SRC_URI = "${GPE_MIRROR}/xtscal-${PV}.tar.bz2 \
           file://change-cross.patch;patch=1 \
	   file://cleanup.patch;patch=1 \
           file://30xTs_Calibrate.sh"

inherit autotools

do_install_append() {
    install -d ${D}${sysconfdir}/X11/Xsession.d/
    install -m 0755 ${WORKDIR}/30xTs_Calibrate.sh ${D}${sysconfdir}/X11/Xsession.d/
}
