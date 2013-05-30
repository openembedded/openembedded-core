DESCRIPTION = "Package maintenance system for Debian."
LICENSE = "GPLv2.0+"
SECTION = "base"

INC_PR = "r19"

SRC_URI = "${DEBIAN_MIRROR}/main/d/dpkg/dpkg_${PV}.tar.xz \
           file://ignore_extra_fields.patch"

DEPENDS = "zlib bzip2 perl ncurses"
DEPENDS_class-native = "bzip2-native zlib-native virtual/update-alternatives-native gettext-native perl-native"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives}"
RDEPENDS_${PN}_class-native = ""

S = "${WORKDIR}/${BPN}-${PV}"

PARALLEL_MAKE = ""

inherit autotools gettext perlnative pkgconfig

export PERL = "${bindir}/perl"
PERL_class-native = "${STAGING_BINDIR_NATIVE}/perl-native/perl"

export PERL_LIBDIR = "${libdir}/perl"
PERL_LIBDIR_class-native = "${libdir}/perl-native/perl"

EXTRA_OECONF = "--without-static-progs \
		--without-dselect \
		--with-start-stop-daemon \
		--with-zlib \
		--with-bz2lib \
		--without-liblzma \
		--without-selinux \
		--without-sgml-doc"

do_configure () {
    echo >> ${S}/m4/compiler.m4
    sed -i -e 's#PERL_LIBDIR=.*$#PERL_LIBDIR="${libdir}/perl"#' ${S}/configure
    autotools_do_configure
}

POSTLOG ?= "/var/log/postinstall.log"
REDIRECT_CMD = "${@base_contains('IMAGE_FEATURES', 'debug-tweaks', '>${POSTLOG} 2>&1', '', d)}"
REDIRECT_CMD[vardepsexclude] += "IMAGE_FEATURES POSTLOG"

DPKG_INIT_POSITION ?= "98"

do_install_append () {
	if [ "${PN}" = "dpkg-native" ]; then
		# update-alternatives doesn't have an offline mode
		rm ${D}${bindir}/update-alternatives
	else
		mv ${D}${bindir}/update-alternatives ${D}${sbindir}
		sed -i -e 's|^#!.*${bindir}/perl-native.*/perl|#!/usr/bin/env perl|' ${D}${bindir}/dpkg-*
	fi
}

# Create a wrapper for the perl script to avoid the "bad interpreter"
# error when the tmpdir is longer than 150.
do_install_append_class-native () {
	tmp=`find ${D}${bindir} -type f -exec grep -m 1 -l '#!/.*/perl-native/perl' {} \;`
	for i in $tmp; do
		create_wrapper $i ${STAGING_BINDIR_NATIVE}/perl-native/perl
	done
}

pkg_postinst_${PN} () {
#!/bin/sh
if [ "x$D" != "x" ] && [ -f $D/var/lib/dpkg/status ]; then
	install -d $D${sysconfdir}/rcS.d

	# this happens at S98 where our good 'ole packages script used to run
	echo "#!/bin/sh
dpkg --configure -a ${REDIRECT_CMD}
rm -f ${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}run-postinsts
" > $D${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}run-postinsts
	chmod 0755 $D${sysconfdir}/rcS.d/S${DPKG_INIT_POSITION}run-postinsts
fi
}

PROV = "virtual/update-alternatives"
PROV_class-native = ""

PROVIDES += "${PROV}"

PACKAGES =+ "update-alternatives-dpkg"
FILES_update-alternatives-dpkg = "${sbindir}/update-alternatives ${localstatedir}/lib/dpkg/alternatives ${sysconfdir}/alternatives"
RPROVIDES_update-alternatives-dpkg += "update-alternatives"

PACKAGES += "${PN}-perl"
FILES_${PN}-perl = "${libdir}/perl"

BBCLASSEXTEND = "native"


