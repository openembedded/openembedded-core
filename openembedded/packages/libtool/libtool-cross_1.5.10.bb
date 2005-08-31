SECTION = "devel"
include libtool_${PV}.bb

PACKAGES = ""
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libtool-${PV}"
SRC_URI_append = " file://libdir-la.patch;patch=1 \
                   file://prefix.patch;patch=1 \
                   file://tag.patch;patch=1 \
                   file://install-path-check.patch;patch=1"
S = "${WORKDIR}/libtool-${PV}"

prefix = "${STAGING_DIR}"
exec_prefix = "${prefix}/${BUILD_SYS}"

do_compile () {
	:
}

do_stage () {
        install -m 0755 ${HOST_SYS}-libtool ${bindir}/${HOST_SYS}-libtool
}

do_install () {
	:
}
