DESCRIPTION = "Fluendo Streaming Server"
LICENSE = "GPL"
DEPENDS = "gstreamer python-gst twisted python-pygtk2"
RDEPENDS = "python-twisted-core python-twisted-web python-core python-gst"
RDEPENDS_${PN}-gui = "${PN} python-pygtk2"
PR = "r3"

SRC_URI = "http://www.flumotion.net/src/flumotion/flumotion-${PV}.tar.bz2 \
           file://python-path.patch;patch=1 \
	   file://no-check-for-python-stuff.patch;patch=1"

inherit autotools distutils-base pkgconfig

export EPYDOC = "no"

EXTRA_OECONF += "--with-python-includes=${STAGING_INCDIR}/../"

PACKAGES =+ "flumotion-gui"

FILES_${PN} = "${bindir} ${sbindir} ${libdir}/flumotion"
FILES_${PN}-dev += "${libdir}/pkgconfig"
FILES_${PN}-gui = "${bindir}/flumotion-admin ${bindir}/flumotion-tester \
                   ${libdir}/flumotion/python/flumotion/admin/gtk \
                   ${libdir}/flumotion/python/flumotion/component/*/admin_gtk* \
                   ${libdir}/flumotion/python/flumotion/component/*/*/admin_gtk* \
                   ${libdir}/flumotion/python/flumotion/extern \
                   ${libdir}/flumotion/python/flumotion/manager \
                   ${libdir}/flumotion/python/flumotion/ui \
                   ${libdir}/flumotion/python/flumotion/wizard \
                   ${datadir}/pixmaps ${datadir}/flumotion ${datadir}/applications"
