DESCRIPTION = "Perl is a popular scripting language."
HOMEPAGE = "http://www.perl.org/"
SECTION = "libs"
LICENSE = "Artistic|GPL"
DEPENDS = "virtual/db-native gdbm-native"
PR = "r13"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/perl-${PV}"

SRC_URI = "http://ftp.funet.fi/pub/CPAN/src/perl-${PV}.tar.gz \
           file://perl-5.8.8-gcc-4.2.patch;patch=1 \
           file://Configure-multilib.patch;patch=1 \
           file://perl-configpm-switch.patch;patch=1 \
           file://native-nopacklist.patch;patch=1 \
           file://native-no-gdbminc.patch;patch=1 \
           file://native-perlinc.patch;patch=1 \
           file://makedepend-dash.patch;patch=1 \
           file://asm-pageh-fix.patch;patch=1"

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
    sed 's!${STAGING_DIR}/bin!${STAGING_BINDIR}!;
         s!${STAGING_DIR}/lib!${STAGING_LIBDIR}!' < config.sh > config.sh.new
    mv config.sh.new config.sh
}
do_stage_append() {
        # We need a hostperl link for building perl
        ln -sf ${STAGING_BINDIR_NATIVE}/perl${PV} ${STAGING_BINDIR_NATIVE}/hostperl
        # Store native config in non-versioned directory
        install -d ${STAGING_DIR}/${HOST_SYS}/perl
        install config.sh ${STAGING_DIR}/${HOST_SYS}/perl
}

PARALLEL_MAKE = ""
