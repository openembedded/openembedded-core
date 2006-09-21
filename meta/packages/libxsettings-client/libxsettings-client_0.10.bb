DESCRIPTION = "Common code for XSETTINGS"
SECTION = "x/libs"
LICENSE = "BSD-X"
PRIORITY = "optional"
DEPENDS = "libx11 libxt"
# libxt is required to stop configure breaking builds by
# including system paths to find it if it isn't present.

headers = "xsettings-common.h xsettings-client.h"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/optional-dependencies/Xsettings-client-0.10.tar.gz"
S = "${WORKDIR}/Xsettings-client-0.10"

inherit autotools gettext

do_stage () {
        oe_libinstall -so libXsettings-client ${STAGING_LIBDIR}

	for h in ${headers}; do
		install -m 0644 ${S}/$h ${STAGING_INCDIR}/$h
	done
}
