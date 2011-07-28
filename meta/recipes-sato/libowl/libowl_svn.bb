DESCRIPTION = "OpenedHand Widget Library"
HOMEPAGE = "http://www.o-hand.com"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2 & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://libowl/owlpaned.h;endline=20;md5=7fef844c4cc01b10541a7ab6ab5971af \
                    file://libowl/owltreemodelslice.h;endline=20;md5=a5421f2cdae8debe5e4c48c09a02beb9 \
                    file://libowl/owlcolourswatch.h;endline=24;md5=063c415c58719d536990ca8f606b5730"

SECTION = "libs"
DEPENDS = "gtk+"
SRCREV = "408"
PV = "0.0+svnr${SRCPV}"
PR = "r6"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${BPN};proto=http"

S = "${WORKDIR}/${BPN}"

inherit autotools pkgconfig
