SUMMARY = "AutoGen is a tool to manage programs that contain large amounts of repetitious text."
DESCRIPTION = "AutoGen is a tool designed to simplify the creation and\
 maintenance of programs that contain large amounts of repetitious text.\
 It is especially valuable in programs that have several blocks of text\
 that must be kept synchronized."
HOMEPAGE = "http://www.gnu.org/software/autogen/"
SECTION = "devel"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504" 

SRC_URI = "${GNU_MIRROR}/autogen/rel${PV}/autogen-${PV}.tar.gz"

SRC_URI[md5sum] = "6c6671b76021fb30dd43b0d5fdb7180b"
SRC_URI[sha256sum] = "37e885d6c8a58f33ab198d38bb972fa4c14236f951d628161dde6e20527d0df2"

PR = "r1"

DEPENDS = "guile-native libtool-native libxml2-native"
RDEPENDS = "automake pkgconfig"

inherit autotools native

# Following line will be needed for the non-native target recipe.
#CFLAGS += "-L${STAGING_LIBDIR} -lguile-2.0 -lgc -pthread -I${STAGING_INCDIR}/guile/2.0 -I${STAGING_INCDIR}"
