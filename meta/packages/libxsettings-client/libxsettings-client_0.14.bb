SECTION = "gpe/libs"
LICENSE = "BSD-X"
PRIORITY = "optional"
DEPENDS = "libxsettings libx11 libxt"
# libxt is required to stop configure breaking builds by
# including system paths to find it if it isn't present.

inherit autotools pkgconfig gpe

SRC_URI = "${GPE_MIRROR}/xsettings-client-${PV}.tar.bz2"
S = ${WORKDIR}/xsettings-client-${PV}


headers = "xsettings-client.h xsettings-common.h"
do_stage () {
        oe_libinstall -so libXsettings-client ${STAGING_LIBDIR}
        mkdir -p ${STAGING_INCDIR}
        for h in ${headers}; do
                install -m 0644 ${S}/$h ${STAGING_INCDIR}/$h
        done
}
