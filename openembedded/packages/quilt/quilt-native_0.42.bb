include quilt.inc

INHIBIT_AUTOTOOLS_DEPS = "1"

SRC_URI = "cvs://anonymous@cvs.savannah.nongnu.org/cvsroot/quilt;method=pserver;module=quilt;tag=VER_${@(bb.data.getVar('PV', d, 1) or '').replace('.', '_')} \
	   file://install.patch;patch=1 \
	   file://nostrip.patch;patch=1 \
	   file://autoreconf.patch;patch=1"
S = "${WORKDIR}/quilt"

inherit autotools native

PATCHCLEANCMD = ""
PATCHCMD = "num='%s'; name='%s'; file='%s'; patch -p "$num" -i "$file""
EXTRA_OECONF = "--disable-nls"

do_configure () {
	chmod 755 configure
	oe_runconf
}
