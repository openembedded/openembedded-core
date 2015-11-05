SUMMARY = "Python bindings for libcurl"
HOMEPAGE = "http://pycurl.sourceforge.net/"
SECTION = "devel/python"
LICENSE = "LGPLv2.1+ | MIT"
LIC_FILES_CHKSUM = "file://README.rst;beginline=166;endline=182;md5=e5c36c3ca7e9f0bf6e144267dd760861 \
                    file://COPYING-LGPL;md5=3579a9fd0221d49a237aaa33492f988c \
                    file://COPYING-MIT;md5=0c3eb58d2e99f3102e2ce2db4eb172f1"

DEPENDS = "curl python"
RDEPENDS_${PN} = "python-core curl"
SRCNAME = "pycurl"

SRC_URI = "\
  http://${SRCNAME}.sourceforge.net/download/${SRCNAME}-${PV}.tar.gz;name=archive \
  file://no-static-link.patch \
"

SRC_URI[archive.md5sum] = "59cea96cc3027a1a7ed5020e69d4009a"
SRC_URI[archive.sha256sum] = "98cc821555d152977fac99b6e5cb0f759275aabdf14e4928890c6fcc944ab736"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

# need to export these variables for python-config to work
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

BBCLASSEXTEND = "native"

# Ensure the docstrings are generated as make clean will remove them
do_compile_prepend() {
	${STAGING_BINDIR_NATIVE}/${PYTHON_PN}-native/${PYTHON_PN} setup.py docstrings
}

do_install_append() {
	rm -rf ${D}${datadir}/share
}
