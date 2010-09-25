DESCRIPTION = "Common code for XSETTINGS"
HOMEPAGE = "http://matchbox-project.org/sources/optional-dependencies/"
BUGTRACKER = "http://bugzilla.openedhand.com/"
SECTION = "x/libs"
LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=7cfac9d2d4dc3694cc7eb605cf32a69b \
                    file://xsettings-client.h;endline=22;md5=7cfac9d2d4dc3694cc7eb605cf32a69b \
                    file://xsettings-common.h;endline=22;md5=7cfac9d2d4dc3694cc7eb605cf32a69b"
PRIORITY = "optional"
DEPENDS = "virtual/libx11"

PR = "r4"

headers = "xsettings-common.h xsettings-client.h"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/optional-dependencies/Xsettings-client-0.10.tar.gz \
        file://MIT-style-license \
        file://link-x11.patch;apply=yes \
        file://disable_Os_option.patch"

S = "${WORKDIR}/Xsettings-client-0.10"

inherit autotools gettext

do_configure_prepend() {
    # This package doesn't ship with its own COPYING file and
    # autotools will install a GPLv2 one instead of the actual MIT-style license here.
    # Add the correct license here to avoid confusion.
    cp -f ${WORKDIR}/MIT-style-license ${S}/COPYING
}

