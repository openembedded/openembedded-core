DESCRIPTION = "Perl is a popular scripting language."
HOMEPAGE = "http://www.perl.org/"
SECTION = "libs"
LICENSE = "Artistic|GPL"
LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
		    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"
PR = "r2"

LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
                    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"

SRC_URI = "http://ftp.funet.fi/pub/CPAN/src/perl-${PV}.tar.gz \
           file://Configure-multilib.patch \
           file://perl-configpm-switch.patch \
           file://parallel_build_fix_1.patch \
           file://parallel_build_fix_2.patch \
           file://parallel_build_fix_3.patch \
           file://parallel_build_fix_4.patch \
           file://parallel_build_fix_5.patch \
           file://parallel_build_fix_6.patch \
           file://native-nopacklist.patch \
           file://native-perlinc.patch"

SRC_URI[md5sum] = "29975a69dce54e47fcd6331c085c6c99"
SRC_URI[sha256sum] = "5678bfd5c2cd59253a26171bf3e681235433b00c730eea8a8046e1b225c11d2f"

S = "${WORKDIR}/perl-${PV}"

inherit native

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
	for i in av.h embed.h gv.h keywords.h op.h perlio.h pp.h regexp.h \
	         uconfig.h XSUB.h cc_runtime.h embedvar.h handy.h opnames.h \
	         perliol.h pp_proto.h regnodes.h unixish.h config.h EXTERN.h \
	         hv.h malloc_ctl.h pad.h perlsdio.h proto.h scope.h utf8.h \
	         cop.h fakesdio.h INTERN.h mg.h patchlevel.h perlsfio.h \
	         reentr.h sv.h utfebcdic.h cv.h fakethr.h intrpvar.h \
	         nostdio.h perlapi.h perlvars.h util.h \
	         dosish.h form.h iperlsys.h opcode.h perl.h perly.h regcomp.h \
	         thread.h warnings.h; do
		install $i ${D}${libdir}/perl/${PV}/CORE
	done

	create_wrapper ${D}${bindir}/perl PERL5LIB='$PERL5LIB:${STAGING_LIBDIR}/perl/${PV}:${STAGING_LIBDIR}/perl/'
}
