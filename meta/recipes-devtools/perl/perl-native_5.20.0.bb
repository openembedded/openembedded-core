SUMMARY = "Perl scripting language"
HOMEPAGE = "http://www.perl.org/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0"

LIC_FILES_CHKSUM = "file://Copying;md5=5b122a36d0f6dc55279a0ebc69f3c60b \
                    file://Artistic;md5=2e6fd2475335af892494fe1f7327baf3"

# 5.10.1 has Module::Build built-in
PROVIDES += "libmodule-build-perl-native"

SRC_URI = "http://www.cpan.org/src/5.0/perl-${PV}.tar.gz \
           file://Configure-multilib.patch \
           file://perl-configpm-switch.patch \
           file://native-nopacklist.patch \
           file://native-perlinc.patch \
           file://MM_Unix.pm.patch \
           file://debian/errno_ver.diff \
           file://dynaloaderhack.patch \
           file://perl-5.14.3-fix-CVE-2010-4777.patch "

SRC_URI[md5sum] = "406ec049ebe3afcc80d9c76ec78ca4f8"
SRC_URI[sha256sum] = "4e8c28ad6ecc89902f9cb2e76f2815bb1a8287ded278e15f7a36ca45f8bbcd02"

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
		-Dsed=/bin/sed \
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
	# reference perl 5.20.0-1 in debian:
	# https://packages.debian.org/experimental/i386/perl/filelist
	for i in av.h bitcount.h charclass_invlists.h config.h cop.h cv.h dosish.h \
		embed.h embedvar.h EXTERN.h fakesdio.h feature.h form.h git_version.h \
		gv.h handy.h hv_func.h hv.h inline.h INTERN.h intrpvar.h iperlsys.h \
		keywords.h l1_char_class_tab.h malloc_ctl.h metaconfig.h mg_data.h \
		mg.h mg_raw.h mg_vtable.h mydtrace.h nostdio.h opcode.h op.h \
		opnames.h op_reg_common.h overload.h pad.h parser.h patchlevel.h \
		perlapi.h perl.h perlio.h perliol.h perlsdio.h perlvars.h perly.h \
		pp.h pp_proto.h proto.h reentr.h regcharclass.h regcomp.h regexp.h \
		regnodes.h scope.h sv.h thread.h time64_config.h time64.h uconfig.h \
		unicode_constants.h unixish.h utf8.h utfebcdic.h util.h uudmap.h \
		vutil.h warnings.h XSUB.h
	do
		install $i ${D}${libdir}/perl/${PV}/CORE
	done

	create_wrapper ${D}${bindir}/perl PERL5LIB='$PERL5LIB:${STAGING_LIBDIR}/perl/${PV}:${STAGING_LIBDIR}/perl:${STAGING_LIBDIR}/perl/site_perl/${PV}:${STAGING_LIBDIR}/perl/vendor_perl/${PV}'
	create_wrapper ${D}${bindir}/perl${PV} PERL5LIB='$PERL5LIB:${STAGING_LIBDIR}/perl/${PV}:${STAGING_LIBDIR}/perl${STAGING_LIBDIR}/perl:${STAGING_LIBDIR}/perl/site_perl/${PV}:${STAGING_LIBDIR}/perl/vendor_perl/${PV}'

	# Use /usr/bin/env nativeperl for the perl script.
	for f in `grep -Il '#! *${bindir}/perl' ${D}/${bindir}/*`; do
		sed -i -e 's|${bindir}/perl|/usr/bin/env nativeperl|' $f
	done
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

# Fix the path in sstate
SSTATE_SCAN_FILES += "*.pm *.pod *.h *.pl *.sh"
