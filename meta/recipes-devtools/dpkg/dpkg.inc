DESCRIPTION = "Package maintenance system for Debian."
LICENSE = "GPL"
SECTION = "base"

INC_PR = "r5"

SRC_URI = "${DEBIAN_MIRROR}/main/d/dpkg/dpkg_${PV}.tar.bz2 \
           file://ignore_extra_fields.patch"

DEPENDS = "zlib bzip2 perl"
DEPENDS_virtclass-native = "bzip2-native zlib-native virtual/update-alternatives-native gettext-native perl-native-runtime"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives}"
RDEPENDS_${PN}_virtclass-native = ""

S = "${WORKDIR}/${BPN}-${PV}"

PARALLEL_MAKE = ""

inherit autotools gettext perlnative

DPKG_INIT_POSITION = "98"

do_install_prepend () {
	install -d ${D}/${sysconfdir}/rcS.d
	# this happens at S98 where our good 'ole packages script used to run
	printf "#!/bin/sh
dpkg --configure -a
rm -f ${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}configure
" > ${D}/${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}configure
	chmod 0755 ${D}/${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}configure
}

do_configure () {
    echo >> m4/compiler.m4
    autotools_do_configure
}
