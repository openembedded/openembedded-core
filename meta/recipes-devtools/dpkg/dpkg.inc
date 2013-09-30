DESCRIPTION = "Package maintenance system for Debian."
LICENSE = "GPLv2.0+"
SECTION = "base"

SRC_URI = "${DEBIAN_MIRROR}/main/d/dpkg/dpkg_${PV}.tar.xz \
           file://ignore_extra_fields.patch"

DEPENDS = "zlib bzip2 perl ncurses"
DEPENDS_class-native = "bzip2-native zlib-native virtual/update-alternatives-native gettext-native perl-native"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives} xz"
RDEPENDS_${PN}_class-native = "xz"

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

DPKG_INIT_POSITION ?= "98"

do_install_append () {
	if [ "${PN}" = "dpkg-native" ]; then
		# update-alternatives doesn't have an offline mode
		rm ${D}${bindir}/update-alternatives
		sed -i -e 's|^#!.*${bindir}/perl-native.*/perl|#!/usr/bin/env nativeperl|' ${D}${bindir}/dpkg-*
	else
		mv ${D}${bindir}/update-alternatives ${D}${sbindir}
		sed -i -e 's|^#!.*${bindir}/perl-native.*/perl|#!/usr/bin/env perl|' ${D}${bindir}/dpkg-*
	fi
}

pkg_postinst_${PN} () {
#!/bin/sh
if [ "x$D" != "x" ] && [ -f $D/var/lib/dpkg/status ]; then
	install -d $D${sysconfdir}/rcS.d

	# this happens at S98 where our good 'ole packages script used to run
	echo "#!/bin/sh
[ -e ${sysconfdir}/default/postinst ] && . ${sysconfdir}/default/postinst
if [ \"\$POSTINST_LOGGING\" = \"1\" ]; then
    dpkg --configure -a >\$LOGFILE 2>&1
else
    dpkg --configure -a
fi
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


