#
# Configuration to use external CSL toolchain
#

EXTERNAL_TOOLCHAIN ?= "/usr/local/csl/${TARGET_ARCH}"

TOOLCHAIN_PATH_ADD = "${EXTERNAL_TOOLCHAIN}/bin:"
PATH =. "${TOOLCHAIN_PATH_ADD}"

CSL_TARGET_SYS_powerpc = "powerpc-linux-gnu"
CSL_TARGET_SYS_powerpc64 = "powerpc-linux-gnu"
CSL_TARGET_SYS_arm = "arm-none-linux-gnueabi"
CSL_TARGET_SYS_mips = "mips-linux-gnu"
CSL_TARGET_SYS_mipsel = "mips-linux-gnu"
CSL_TARGET_SYS_mips64 = "mips-linux-gnu"
CSL_TARGET_SYS_i686 = "i686-pc-linux-gnu"
CSL_TARGET_SYS_i586 = "i686-pc-linux-gnu"
CSL_TARGET_SYS = "${TARGET_SYS}"

TARGET_PREFIX = "${CSL_TARGET_SYS}-"

PREFERRED_PROVIDER_linux-libc-headers = "external-csl-toolchain"
PREFERRED_PROVIDER_linux-libc-headers-dev = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}compilerlibs = "external-csl-toolchain"
PREFERRED_PROVIDER_libgcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libintl = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libiconv = "external-csl-toolchain"
PREFERRED_PROVIDER_glibc-thread-db = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers = "external-csl-toolchain"

TARGET_CPPFLAGS_prepend = " -isystem${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/include "
TARGET_LDFLAGS_prepend = " -L${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib -Wl,-rpath-link,${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib "

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR_HOST}"

# Translate to CodeSourcery's names for their optimized files in the toolchain
def csl_target_core(d):
    coredata = {
        'armv7a-vfp-neon': 'armv7-a-neon',
        'i586': 'sgxx-glibc',
        'i686': 'sgxx-glibc',
        'mips': 'mips32',
        'mipsel': 'el',
        'powerpc-nf': 'nof',
        'ppce500': 'te500v1',
        'ppce500mc': 'te500mc',
        'ppce500v2': 'te500v2',
        'ppce600': 'te600'
    }
    return coredata.get(d.getVar('TUNE_PKGARCH', True), '')

CSL_TARGET_CORE = "${@csl_target_core(d)}"

# Unfortunately, the CSL ia32 toolchain has non-prefixed binaries in its
# bindir (e.g. gcc, ld). To avoid this messing up our build, we avoid adding
# this bindir to our PATH, and instead add symlinks to the prefixed binaries
# to our staging toolchain bindir.

python toolchain_metadata_setup () {
    if not isinstance(e, bb.event.ConfigParsed):
        return

    d = e.data

    if d.getVar('TUNE_PKGARCH', True) in ('i586', 'i686'):
        d.setVar('TOOLCHAIN_PATH_ADD', '')
}
addhandler toolchain_metadata_setup

python toolchain_setup () {
    if not isinstance(e, bb.event.BuildStarted):
        return

    d = e.data

    if d.getVar('TUNE_PKGARCH', True) in ('i586', 'i686'):
        populate_toolchain_links(d)
}
addhandler toolchain_setup

def populate_toolchain_links(d):
    import errno
    import os
    from glob import glob

    d = d.createCopy()
    d.finalize()

    pattern = d.expand('${EXTERNAL_TOOLCHAIN}/bin/${TARGET_PREFIX}*')
    files = glob(pattern)
    if not files:
        bb.fatal("Unable to populate toolchain binary symlinks")

    bindir = d.getVar('STAGING_BINDIR_TOOLCHAIN', True)
    bb.mkdirhier(bindir)
    for f in files:
        base = os.path.basename(f)
        newpath = os.path.join(bindir, base)
        try:
            os.symlink(f, newpath)
        except OSError as exc:
            if exc.errno == errno.EEXIST:
                break
            bb.fatal("Unable to populate toolchain binary symlink for %s: %s" % (newpath, exc))

require conf/distro/include/csl-versions.inc
