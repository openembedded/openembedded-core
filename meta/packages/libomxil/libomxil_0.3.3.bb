DESCRIPTION = "Bellagio OpenMAX Integration Layer"
HOMEPAGE = "http://omxil.sourceforge.net/"
LICENSE = "LGPL"
DEPENDS = "libvorbis libogg alsa-lib libmad"

SRC_URI = "${SOURCEFORGE_MIRROR}/omxil/libomxil-B-${PV}.tar.gz"

S = "${WORKDIR}/${PN}-B-${PV}"

inherit autotools

EXTRA_OECONF += "--disable-ffmpegcomponents"

FILES_${PN} += "${libdir}/omxilcomponents/*.so*"
FILES_${PN}-dev += "${libdir}/omxilcomponents/*.*a"
FILES_${PN}-dbg += "${libdir}/omxilcomponents/.debug/"
