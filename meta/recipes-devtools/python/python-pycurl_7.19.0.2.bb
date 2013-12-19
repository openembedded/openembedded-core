SUMMARY = "Python bindings for libcurl"
HOMEPAGE = "http://pycurl.sourceforge.net/"
SECTION = "devel/python"
LICENSE = "LGPLv2.1+ | MIT"
LIC_FILES_CHKSUM = "file://README.rst;beginline=97;endline=111;md5=b5a5e531d80812bcbecbeb240fde63ef \
                    file://COPYING;md5=3579a9fd0221d49a237aaa33492f988c \
                    file://COPYING2;md5=ffaa1e283b7f9bf5aafd8d45db6f7518"

DEPENDS = "curl python"
RDEPENDS_${PN} = "python-core curl"
SRCNAME = "pycurl"

SRC_URI = "\
  http://${SRCNAME}.sourceforge.net/download/${SRCNAME}-${PV}.tar.gz;name=archive \
  file://no-static-link.patch \
"

SRC_URI[archive.md5sum] = "518be33976dbc6838e42495ada64b43f"
SRC_URI[archive.sha256sum] = "7a9e793b9181654d5eef3f6d22c244c57d2b51d38feb4c1b71d68efda99b0547"
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
