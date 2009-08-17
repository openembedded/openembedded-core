DESCRIPTION = "Moblin web browser (based on clutter + mozilla-headless/mozilla-offscreen)"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://xpidl-b55d523da7681febfabf9fc40dcd6fcee982b079.patch;patch=1;rev=b55d523da7681febfabf9fc40dcd6fcee982b079 \
           file://xpidl.patch;patch=1;notrev=b55d523da7681febfabf9fc40dcd6fcee982b079"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r7"

DEPENDS = "clutter-1.0 clutter-mozembed clutter-gtk libunique mozilla-headless-services libccss nbtk mozilla-headless mutter-moblin"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--with-idl-prefix=${STAGING_DIR_TARGET}/"

FILES_${PN} += "${datadir}/moblin-web-browser/chrome/* ${libdir}/xulrunner-*/chrome/*"
FILES_${PN} += "${datadir}/moblin-web-browser/components/* ${libdir}/xulrunner-*/components/*"
FILES_${PN}-dbg += "${datadir}/moblin-web-browser/components/.debug/* ${libdir}/xulrunner-*/components/.debug/*"

inherit autotools_stage
