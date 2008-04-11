require libtool.inc
require libtool_${PV}.bb

PR = "r12"
PACKAGES = ""
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libtool-${PV}"
SRC_URI_append = " file://libdir-la.patch;patch=1 \
                   file://libdir-la2.patch;patch=1 \
                   file://prefix.patch;patch=1 \
                   file://tag.patch;patch=1 \
                   file://install-path-check.patch;patch=1 \
		   file://nmedit_fix.patch;patch=1 \
		   file://nousrlib.patch;patch=1"

DOLT_PATCH = ""
DOLT_PATCH_arm = " file://add_dolt.patch;patch=1"
DOLT_PATCH_i586 = " file://add_dolt.patch;patch=1"

SRC_URI_append_linux = "${DOLT_PATCH}"
SRC_URI_append_linux-gnueabi = "${DOLT_PATCH}"

S = "${WORKDIR}/libtool-${PV}"

prefix = "${STAGING_DIR_NATIVE}${layout_prefix}"
exec_prefix = "${STAGING_DIR_NATIVE}${layout_exec_prefix}"
bindir = "${STAGING_BINDIR_NATIVE}"

do_configure_prepend () {
	rm -f ltmain.shT
	date=`/bin/sh ./mkstamp < ./ChangeLog` && \
        sed -e 's/@''PACKAGE@/libtool/' -e 's/@''VERSION@/1.5.10/' \
            -e "s%@""TIMESTAMP@%$date%" ./ltmain.in > ltmain.shT
	mv -f ltmain.shT ltmain.sh || \
	        (rm -f ltmain.sh && cp ltmain.shT ltmain.sh && rm -f ltmain.shT)
	cp ltmain.sh ./libltdl/
}

do_compile () {
	:
}

do_stage () {
        install -m 0755 ${HOST_SYS}-libtool ${bindir}/${HOST_SYS}-libtool
        install -m 0644 libltdl/ltdl.h ${STAGING_INCDIR}/
        install -d ${STAGING_DATADIR}/libtool ${STAGING_DATADIR}/aclocal
        install -c config.guess ${STAGING_DATADIR}/libtool/
        install -c config.sub ${STAGING_DATADIR}/libtool/
        install -c -m 0644 ltmain.sh ${STAGING_DATADIR}/libtool/
        install -c -m 0644 libtool.m4 ${STAGING_DATADIR}/aclocal/
        install -c -m 0644 ltdl.m4 ${STAGING_DATADIR}/aclocal/
        install -c -m 0644 ${WORKDIR}/dolt.m4 ${STAGING_DATADIR}/aclocal/
}

do_install () {
	:
}
