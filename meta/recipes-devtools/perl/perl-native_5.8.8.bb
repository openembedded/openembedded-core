DESCRIPTION = "Perl is a popular scripting language."
HOMEPAGE = "http://www.perl.org/"
SECTION = "libs"
LICENSE = "Artistic|GPL"
DEPENDS = "virtual/db-native gdbm-native"
PR = "r14"

LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
                    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"

SRC_URI = "http://ftp.funet.fi/pub/CPAN/src/perl-${PV}.tar.gz \
           file://perl-5.8.8-gcc-4.2.patch;patch=1 \
           file://Configure-multilib.patch;patch=1 \
           file://perl-configpm-switch.patch;patch=1 \
           file://native-nopacklist.patch;patch=1 \
           file://native-no-gdbminc.patch;patch=1 \
           file://native-perlinc.patch;patch=1 \
           file://makedepend-dash.patch;patch=1 \
           file://asm-pageh-fix.patch;patch=1"

SRC_URI[md5sum] = "b8c118d4360846829beb30b02a6b91a7"
SRC_URI[sha256sum] = "e15d499321e003d12ed183601e37ee7ba5f64b278d1de30149ce01bd4a3f234d"

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

	install -d ${D}${libdir}/perl/${PV}/CORE \
	           ${D}${datadir}/perl/${PV}/ExtUtils

	# Save native config 
	install config.sh ${D}${libdir}/perl
	install lib/Config.pm ${D}${libdir}/perl/${PV}/
	install lib/ExtUtils/typemap ${D}${datadir}/perl/${PV}/ExtUtils/

	# perl shared library headers
	for i in av.h embed.h gv.h keywords.h op.h perlio.h pp.h regexp.h \
	         uconfig.h XSUB.h cc_runtime.h embedvar.h handy.h opnames.h \
	         perliol.h pp_proto.h regnodes.h unixish.h config.h EXTERN.h \
	         hv.h malloc_ctl.h pad.h perlsdio.h proto.h scope.h utf8.h \
	         cop.h fakesdio.h INTERN.h mg.h patchlevel.h perlsfio.h \
	         reentr.h sv.h utfebcdic.h cv.h fakethr.h intrpvar.h \
	         nostdio.h perlapi.h perlvars.h reentr.inc thrdvar.h util.h \
	         dosish.h form.h iperlsys.h opcode.h perl.h perly.h regcomp.h \
	         thread.h warnings.h; do
		install $i ${D}${libdir}/perl/${PV}/CORE
	done
}
do_install_append_nylon() {
	# get rid of definitions not supported by the gcc version we use for nylon...
	for i in ${D}${libdir}/perl/${PV}/Config_heavy.pl ${D}${libdir}/perl/config.sh; do
		perl -pi -e 's/-Wdeclaration-after-statement //g' ${i}
	done
}

PARALLEL_MAKE = ""

# Perl encodes the staging path into the perl binary so we have to depend on this
# as part of the checksum for now
HARDPATH := "${STAGING_BINDIR}"
do_populate_sysroot[vardeps] += "HARDPATH"
