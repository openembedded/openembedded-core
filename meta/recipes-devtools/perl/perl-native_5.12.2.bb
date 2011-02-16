DESCRIPTION = "Perl is a popular scripting language."
HOMEPAGE = "http://www.perl.org/"
SECTION = "libs"
LICENSE = "Artistic|GPL"
LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
		    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"
PR = "r4"

LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
                    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"

SRC_URI = "http://ftp.funet.fi/pub/CPAN/src/perl-${PV}.tar.gz \
           file://Configure-multilib.patch;patch=1 \
           file://perl-configpm-switch.patch;patch=1 \
           file://native-nopacklist.patch;patch=1 \
           file://native-perlinc.patch;patch=1"

SRC_URI[md5sum] = "af2df531d46b77fdf0d97eecb03eddb2"
SRC_URI[sha256sum] = "cf888340021d5a2d1238bbd9b8b55aaf420a848d46e4d317cb8567f86ceb1022"

S = "${WORKDIR}/perl-${PV}"

inherit native

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

	create_wrapper ${D}${bindir}/perl PERL5LIB='$PERL5LIB:${STAGING_LIBDIR}/perl/5.12.2:${STAGING_LIBDIR}/perl/'
}
do_install_append_nylon() {
	# get rid of definitions not supported by the gcc version we use for nylon...
	for i in ${D}${libdir}/perl/${PV}/Config_heavy.pl ${D}${libdir}/perl/config.sh; do
		perl -pi -e 's/-Wdeclaration-after-statement //g' ${i}
	done
}
