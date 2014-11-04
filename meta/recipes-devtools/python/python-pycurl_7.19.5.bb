SUMMARY = "Python bindings for libcurl"
HOMEPAGE = "http://pycurl.sourceforge.net/"
SECTION = "devel/python"
LICENSE = "LGPLv2.1+ | MIT"
LIC_FILES_CHKSUM = "file://README.rst;beginline=166;endline=181;md5=57e5ab0c0f964533fc59d93dec5695bb \
                    file://COPYING-LGPL;md5=3579a9fd0221d49a237aaa33492f988c \
                    file://COPYING-MIT;md5=e8200955c773b2a0fd6cea36ea5e87be"

DEPENDS = "curl python"
RDEPENDS_${PN} = "python-core curl"
SRCNAME = "pycurl"

SRC_URI = "\
  http://${SRCNAME}.sourceforge.net/download/${SRCNAME}-${PV}.tar.gz;name=archive \
  file://no-static-link.patch \
"

SRC_URI[archive.md5sum] = "47b4eac84118e2606658122104e62072"
SRC_URI[archive.sha256sum] = "69a0aa7c9dddbfe4cebf4d1f674c490faccf739fc930d85d8990ce2fd0551a43"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

# need to export these variables for python-config to work
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

BBCLASSEXTEND = "native"

do_install_append() {
	rm -rf ${D}${datadir}/share
}
