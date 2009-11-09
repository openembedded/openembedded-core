require libtool_${PV}.bb

PR = "r24"
PACKAGES = ""
SRC_URI_append = " file://cross_compile.patch;patch=1 \
                   file://prefix.patch;patch=1"

DEPENDS += "libtool-native"

DOLT_PATCH = ""
DOLT_PATCH_arm = " file://add_dolt.patch;patch=1"
DOLT_PATCH_i586 = " file://add_dolt.patch;patch=1"

#SRC_URI_append_linux = "${DOLT_PATCH}"
#SRC_URI_append_linux-gnueabi = "${DOLT_PATCH}"

prefix = "${STAGING_DIR_NATIVE}${prefix_native}"
exec_prefix = "${STAGING_DIR_NATIVE}${prefix_native}"
bindir = "${STAGING_BINDIR_NATIVE}"

do_compile () {
	:
}

do_stage () {
	install -m 0755 ${HOST_SYS}-libtool ${bindir}/${HOST_SYS}-libtool
	install -d ${STAGING_DIR_HOST}${target_datadir}/libtool ${STAGING_DIR_HOST}${target_datadir}/aclocal
	install -c ${S}/libltdl/config/config.guess ${STAGING_DIR_HOST}${target_datadir}/libtool/
	install -c ${S}/libltdl/config/config.sub ${STAGING_DIR_HOST}${target_datadir}/libtool/
	install -c -m 0644 ${S}/libltdl/config/ltmain.sh ${STAGING_DIR_HOST}${target_datadir}/libtool/
	install -c -m 0644 ${S}/libltdl/m4/libtool.m4 ${STAGING_DIR_HOST}${target_datadir}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltdl.m4 ${STAGING_DIR_HOST}${target_datadir}/aclocal/
	install -c -m 0644 ${WORKDIR}/dolt.m4 ${STAGING_DIR_HOST}${target_datadir}/aclocal/
}

do_install () {
	:
}
