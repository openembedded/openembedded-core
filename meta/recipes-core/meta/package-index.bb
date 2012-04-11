DESCRIPTION = "Rebuild the package index"
LICENSE = "MIT"

INHIBIT_DEFAULT_DEPS = "1"
ALLOW_EMPTY = "1"
PACKAGES = ""

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_package[noexec] = "1"
do_package_write[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_rpm[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_populate_sysroot[noexec] = "1"

do_package_index[nostamp] = "1"
do_package_index[depends] += "${PACKAGEINDEXDEPS}"

# Force NATIVE python to use modules from STAGING_DIR_NATIVE
export PYTHONHOME = "${STAGING_DIR_NATIVE}/usr"

do_package_index() {
	set -ex
	${PACKAGEINDEXES}
	set +ex
}
addtask do_package_index before do_build
EXCLUDE_FROM_WORLD = "1"
