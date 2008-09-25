SECTION = "base"
DESCRIPTION = "PM hooks"
LICENSE="GPL"

PR = "r0"

SRC_URI = "http://git.fnordovax.org/pm-utils/snapshot/pm-utils-pm-utils-1.2.tar.gz"

inherit pkgconfig autotools

FILES_${PN}-dbg += "${libdir}/pm-utils/bin/.debug \
		    ${datadir}/doc/pm-utils/README.debugging"

do_configure_prepend () {
	autoreconf -f -i -s
}

S = "${WORKDIR}/pm-utils"
