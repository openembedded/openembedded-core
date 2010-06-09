DESCRIPTION = "libcurl python bindings."
HOMEPAGE = "http://pycurl.sourceforge.net/"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "LGPLv2.1+ | MIT"
LIC_FILES_CHKSUM = "file://README;endline=13;md5=fbfe545b1869617123a08c0983ef17b2 \
                    file://COPYING;md5=3579a9fd0221d49a237aaa33492f988c \
                    file://COPYING2;md5=ffaa1e283b7f9bf5aafd8d45db6f7518"

DEPENDS = "curl python"
RDEPENDS = "python-core curl"
SRCNAME = "pycurl"
PR = "r0"

SRC_URI = "\
  http://${SRCNAME}.sourceforge.net/download/${SRCNAME}-${PV}.tar.gz;name=archive \
  file://no-static-link.patch;patch=1 \
"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

# need to export these variables for python-config to work
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

NATIVE_INSTALL_WORKS = "1"

BBCLASSEXTEND = "native"
