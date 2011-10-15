DESCRIPTION = "Bellagio OpenMAX Integration Layer"
HOMEPAGE = "http://omxil.sourceforge.net/"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ae6f0f4dbc7ac193b50f323a6ae191cb \
                    file://src/omxcore.h;beginline=1;endline=27;md5=c2e37f68ba9652ca9b2431f466944174"
DEPENDS = "libvorbis libogg alsa-lib libmad"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/omxil/libomxil-B-${PV}.tar.gz"

S = "${WORKDIR}/${BPN}-B-${PV}"

inherit autotools

EXTRA_OECONF += "--disable-ffmpegcomponents"

FILES_${PN} += "${libdir}/omxilcomponents/*${SOLIBS} \
                ${datadir}/libomxil-B"
FILES_${PN}-staticdev += "${libdir}/omxilcomponents/*.a"
FILES_${PN}-dev += "${libdir}/omxilcomponents/*.la \
                    ${libdir}/omxilcomponents/*${SOLIBSDEV}"
FILES_${PN}-dbg += "${libdir}/omxilcomponents/.debug/"
