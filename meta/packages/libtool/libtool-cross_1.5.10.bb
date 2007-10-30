SECTION = "devel"
require libtool_${PV}.bb

PR = "r6"
PACKAGES = ""
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libtool-${PV}"
SRC_URI_append = " file://libdir-la.patch;patch=1 \
                   file://prefix.patch;patch=1 \
                   file://tag.patch;patch=1 \
                   file://tag1.patch;patch=1 \
                   file://install-path-check.patch;patch=1"
S = "${WORKDIR}/libtool-${PV}"

prefix = "${STAGING_DIR_NATIVE}${layout_prefix}"
exec_prefix = "${STAGING_DIR_NATIVE}${layout_exec_prefix}"
bindir = "${STAGING_BINDIR_NATIVE}"

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
}

do_install () {
	:
}
