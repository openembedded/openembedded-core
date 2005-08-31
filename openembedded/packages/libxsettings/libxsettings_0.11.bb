DESCRIPTION = "Common code for XSETTINGS"
SECTION = "gpe/libs"
LICENSE = "BSD-X"
PRIORITY = "optional"
MAINTAINER = "Philip Blundell <pb@handhelds.org>"
DEPENDS = "x11"

inherit gpe

headers = "xsettings-common.h"

do_stage () {
        oe_libinstall -so libXsettings ${STAGING_LIBDIR}

	mkdir -p ${STAGING_INCDIR}/gpe
	for h in ${headers}; do
		install -m 0644 ${S}/$h ${STAGING_INCDIR}/$h
	done
}

do_install () {
	gpe_do_install
	oe_runmake PREFIX=${prefix} DESTDIR=${D} install-devel
}
