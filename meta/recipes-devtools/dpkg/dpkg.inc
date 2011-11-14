DESCRIPTION = "Package maintenance system for Debian."
LICENSE = "GPL"
SECTION = "base"

INC_PR = "r10"

SRC_URI = "${DEBIAN_MIRROR}/main/d/dpkg/dpkg_${PV}.tar.bz2 \
           file://ignore_extra_fields.patch"

DEPENDS = "zlib bzip2 perl"
DEPENDS_virtclass-native = "bzip2-native zlib-native virtual/update-alternatives-native gettext-native perl-native-runtime"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives}"
RDEPENDS_${PN}_virtclass-native = ""

S = "${WORKDIR}/${BPN}-${PV}"

PARALLEL_MAKE = ""

inherit autotools gettext perlnative

export PERL_LIBDIR = "${libdir}/perl"

EXTRA_OECONF = "--without-static-progs \
		--without-dselect \
		--with-start-stop-daemon \
		--with-zlib \
		--with-bz2lib \
		--without-selinux \
		--without-sgml-doc"

do_configure () {
    echo >> m4/compiler.m4
    sed -i -e 's#PERL_LIBDIR=.*$#PERL_LIBDIR="${libdir}/perl"#' ${S}/configure
    autotools_do_configure
}


DPKG_INIT_POSITION ?= "98"
do_install_prepend () {
	install -d ${D}/${sysconfdir}/rcS.d
	# this happens at S98 where our good 'ole packages script used to run
	printf "#!/bin/sh
dpkg --configure -a
rm -f ${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}configure
" > ${D}/${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}configure
	chmod 0755 ${D}/${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}configure
}

do_install_append () {
	mv ${D}${bindir}/update-alternatives ${D}${sbindir}
}

PROVIDES += "virtual/update-alternatives"

PACKAGES =+ "update-alternatives-dpkg"
FILES_update-alternatives-dpkg = "${sbindir}/update-alternatives ${localstatedir}/lib/dpkg/alternatives ${sysconfdir}/alternatives"
RPROVIDES_update-alternatives-dpkg += "update-alternatives"

PACKAGES += "${PN}-perl"
FILES_${PN}-perl = "${libdir}/perl"

BBCLASSEXTEND = "native"


