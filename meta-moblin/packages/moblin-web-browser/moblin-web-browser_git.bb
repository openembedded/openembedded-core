DESCRIPTION = "Moblin web browser (based on clutter + mozilla-headless/mozilla-offscreen)"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://xpidl.patch;patch=1"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r2"

DEPENDS = "clutter clutter-mozembed clutter-gtk libunique mozilla-headless-services libccss nbtk mozilla-headless"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--with-idl-prefix=${STAGING_DIR_TARGET}/"

FILES_${PN} += "${libdir}/xulrunner-*/chrome/*"
FILES_${PN} += "${libdir}/xulrunner-*/components/*"
FILES_${PN}-dbg += "${libdir}/xulrunner-*/components/.debug/*"

inherit autotools_stage
