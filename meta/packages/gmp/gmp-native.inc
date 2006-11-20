require gmp_${PV}.bb

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gmp-${PV}"
S = "${WORKDIR}/gmp-${PV}"

inherit native

# darwin 'hacks'
EXTRA_OECONF_build-darwin = " --enable-shared "
OLD_STAGING := "${STAGING_BINDIR}"
OLD_TARGET  := "${TARGET_SYS}"
OLD_HOST    := "${HOST_SYS}"
OLD_BUILD   := "${BUILD_SYS}"

PATH_prepend_build-darwin = "${OLD_STAGING}/${OLD_HOST}:${OLD_STAGING}:"

TARGET_SYS_build-darwin   = "none-apple-darwin"
HOST_SYS_build-darwin     = "none-apple-darwin"
BUILD_SYS_build-darwin    = "none-apple-darwin"

do_compile_append_build-darwin() {
    oe_runmake check
}
