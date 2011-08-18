DESCRIPTION = "libcurl python bindings."
HOMEPAGE = "http://pycurl.sourceforge.net/"
SECTION = "devel/python"
LICENSE = "LGPLv2.1+ | MIT"
LIC_FILES_CHKSUM = "file://README;endline=13;md5=fbfe545b1869617123a08c0983ef17b2 \
                    file://COPYING;md5=3579a9fd0221d49a237aaa33492f988c \
                    file://COPYING2;md5=ffaa1e283b7f9bf5aafd8d45db6f7518"

DEPENDS = "curl python"
RDEPENDS_${PN} = "python-core curl"
SRCNAME = "pycurl"
PR = "r1"

SRC_URI = "\
  http://${SRCNAME}.sourceforge.net/download/${SRCNAME}-${PV}.tar.gz;name=archive \
  file://no-static-link.patch \
"

SRC_URI[archive.md5sum] = "919d58fe37e69fe87ce4534d8b6a1c7b"
SRC_URI[archive.sha256sum] = "eb782dfcc5a7c023539a077462b83c167e178128ee9f7201665b9fbb1a8b0642"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

# need to export these variables for python-config to work
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

BBCLASSEXTEND = "native"
