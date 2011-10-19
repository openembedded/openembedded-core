DESCRIPTION = "Perl is a popular scripting language."
HOMEPAGE = "http://www.perl.org/"
SECTION = "libs"
LICENSE = "Artistic|GPL"
LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
		    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"
PR = "r0"

LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
                    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"

SRC_URI = "http://www.cpan.org/src/5.0/perl-${PV}.tar.gz \
           file://Configure-multilib.patch \
           file://perl-configpm-switch.patch \
           file://native-nopacklist.patch \
           file://native-perlinc.patch \
           file://MM_Unix.pm.patch"

SRC_URI[md5sum] = "3306fbaf976dcebdcd49b2ac0be00eb9"
SRC_URI[sha256sum] = "6488359573bd7d41761bf935f66f827dc220fb3df961ef9b775d51fbd66548d3"

S = "${WORKDIR}/perl-${PV}"

inherit native

NATIVE_PACKAGE_PATH_SUFFIX = "/${PN}"

export LD="${CCLD}"

do_configure () {
	./Configure \
		-Dcc="${CC}" \
		-Dcflags="${CFLAGS}" \
		-Dldflags="${LDFLAGS}" \
		-Dcf_by="Open Embedded" \
		-Dprefix=${prefix} \
		-Dvendorprefix=${prefix} \
		-Dvendorprefix=${prefix} \
		-Dsiteprefix=${prefix} \
		\
		-Dbin=${STAGING_BINDIR}/${PN} \
		-Dprivlib=${STAGING_LIBDIR}/perl/${PV} \
		-Darchlib=${STAGING_LIBDIR}/perl/${PV} \
		-Dvendorlib=${STAGING_LIBDIR}/perl/${PV} \
		-Dvendorarch=${STAGING_LIBDIR}/perl/${PV} \
		-Dsitelib=${STAGING_LIBDIR}/perl/${PV} \
		-Dsitearch=${STAGING_LIBDIR}/perl/${PV} \
		\
		-Duseshrplib \
		-Dusethreads \
		-Duseithreads \
		-Duselargefiles \
		-Dnoextensions=ODBM_File \
		-Ud_dosuid \
		-Ui_db \
		-Ui_ndbm \
		-Ui_gdbm \
		-Di_shadow \
		-Di_syslog \
		-Duseperlio \
		-Dman3ext=3pm \
		-Uafs \
		-Ud_csh \
		-Uusesfio \
		-Uusenm -des
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	# We need a hostperl link for building perl
	ln -sf perl${PV} ${D}${bindir}/hostperl

        ln -sf perl ${D}${libdir}/perl5

	install -d ${D}${libdir}/perl/${PV}/CORE \
	           ${D}${datadir}/perl/${PV}/ExtUtils

	# Save native config 
	install config.sh ${D}${libdir}/perl
	install lib/Config.pm ${D}${libdir}/perl/${PV}/
	install lib/ExtUtils/typemap ${D}${libdir}/perl/${PV}/ExtUtils/

	# perl shared library headers
	for i in av.h bitcount.h config.h cop.h cv.h dosish.h embed.h embedvar.h \
		EXTERN.h fakesdio.h fakethr.h form.h gv.h handy.h hv.h INTERN.h \
		intrpvar.h iperlsys.h keywords.h l1_char_class_tab.h malloc_ctl.h \
		metaconfig.h mg.h mydtrace.h nostdio.h opcode.h op.h opnames.h \
		op_reg_common.h overload.h pad.h parser.h patchlevel.h perlapi.h \
		perl.h perlio.h perliol.h perlsdio.h perlsfio.h perlvars.h \
		perly.h pp.h pp_proto.h proto.h reentr.h regcharclass.h regcomp.h \
		regexp.h regnodes.h scope.h sv.h thread.h time64_config.h \
		time64.h uconfig.h unixish.h utf8.h utfebcdic.h util.h \
		uudmap.h warnings.h XSUB.h 
	do
		install $i ${D}${libdir}/perl/${PV}/CORE
	done

	create_wrapper ${D}${bindir}/perl PERL5LIB='$PERL5LIB:${STAGING_LIBDIR}/perl/${PV}:${STAGING_LIBDIR}/perl/'
	create_wrapper ${D}${bindir}/perl${PV} PERL5LIB='$PERL5LIB:${STAGING_LIBDIR}/perl/${PV}:${STAGING_LIBDIR}/perl/'
}

SYSROOT_PREPROCESS_FUNCS += "perl_sysroot_create_wrapper"

perl_sysroot_create_wrapper () {
	mkdir -p ${SYSROOT_DESTDIR}${bindir}
	# Create a wrapper that /usr/bin/env perl will use to get perl-native.
	# This MUST live in the normal bindir.
	cat > ${SYSROOT_DESTDIR}${bindir}/../nativeperl << EOF
#!/bin/sh
realpath=\`readlink -fn \$0\`
exec \`dirname \$realpath\`/perl-native/perl "\$@"
EOF
	chmod 0755 ${SYSROOT_DESTDIR}${bindir}/../nativeperl
	cat ${SYSROOT_DESTDIR}${bindir}/../nativeperl
}
