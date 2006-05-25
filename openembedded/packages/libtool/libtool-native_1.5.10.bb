SECTION = "devel"
include libtool_${PV}.bb

PR = "r4"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libtool-${PV}"
SRC_URI_append = " file://libdir-la.patch;patch=1 \
                   file://prefix.patch;patch=1 \
                   file://tag.patch;patch=1 \
                   file://tag1.patch;patch=1 \
                   file://install-path-check.patch;patch=1"
S = "${WORKDIR}/libtool-${PV}"

STAGING_DATADIR_safe := "${STAGING_DATADIR}"

inherit native

do_stage () {
	install -m 0755 ${HOST_SYS}-libtool ${STAGING_BINDIR}/${HOST_SYS}-libtool
	install -m 0755 libtoolize ${STAGING_BINDIR}/libtoolize
	oe_libinstall -a -so -C libltdl libltdl ${STAGING_LIBDIR}
	install -m 0644 libltdl/ltdl.h ${STAGING_INCDIR}/
	for dir in ${STAGING_DATADIR} ${STAGING_DATADIR_safe}; do
		install -d $dir/libtool \
			   $dir/aclocal
		install -c config.guess $dir/libtool/config.guess
		install -c config.sub $dir/libtool/config.sub
		install -c -m 0644 ltmain.sh $dir/libtool/
		install -c -m 0644 libtool.m4 $dir/aclocal/
		install -c -m 0644 ltdl.m4 $dir/aclocal/
	done
}

do_install () {
	:
}
