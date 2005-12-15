DESCRIPTION = "Alsa OSS Compatibility Package"
MAINTAINER = "Lorn Potter <lpotter@trolltech.com>"
SECTION = "libs/multimedia"
LICENSE = "GPL"
DEPENDS = "alsa-lib"

SRC_URI = "ftp://ftp.alsa-project.org/pub/oss-lib/alsa-oss-${PV}.tar.bz2"

inherit autotools 

do_configure_prepend () {
	touch NEWS README AUTHORS ChangeLog
}

do_stage () {
	oe_libinstall -C alsa -a -so libaoss ${STAGING_LIBDIR}
	oe_libinstall -C alsa -a -so libalsatoss ${STAGING_LIBDIR}
}
