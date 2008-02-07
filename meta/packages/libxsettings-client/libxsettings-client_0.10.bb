DESCRIPTION = "Common code for XSETTINGS"
SECTION = "x/libs"
LICENSE = "BSD-X"
PRIORITY = "optional"
DEPENDS = "virtual/libx11"

PR = "r2"

headers = "xsettings-common.h xsettings-client.h"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/optional-dependencies/Xsettings-client-0.10.tar.gz \
        file://link-x11.patch;patch=1"

S = "${WORKDIR}/Xsettings-client-0.10"

inherit autotools gettext

do_stage () {
        # TODO: replace with autotools_stage_all?
        oe_libinstall -so libXsettings-client ${STAGING_LIBDIR}

	for h in ${headers}; do
		install -m 0644 ${S}/$h ${STAGING_INCDIR}/$h
	done
}
