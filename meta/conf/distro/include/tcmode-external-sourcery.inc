#
# Configuration to use external Sourcery G++ toolchain
#

EXTERNAL_TOOLCHAIN ?= "/usr/local/csl/${TARGET_ARCH}"

TOOLCHAIN_PATH_ADD = "${EXTERNAL_TOOLCHAIN}/bin:"
PATH =. "${TOOLCHAIN_PATH_ADD}"

CSL_TARGET_SYS_powerpc ?= "powerpc-linux-gnu"
CSL_TARGET_SYS_powerpc64 ?= "powerpc-linux-gnu"
CSL_TARGET_SYS_arm ?= "arm-none-linux-gnueabi"
CSL_TARGET_SYS_mips ?= "mips-linux-gnu"
CSL_TARGET_SYS_mipsel ?= "mips-linux-gnu"
CSL_TARGET_SYS_mips64 ?= "mips-linux-gnu"
CSL_TARGET_SYS_i686 ?= "i686-pc-linux-gnu"
CSL_TARGET_SYS_i586 ?= "i686-pc-linux-gnu"
CSL_TARGET_SYS = "${TARGET_SYS}"

TARGET_PREFIX = "${CSL_TARGET_SYS}-"

PREFERRED_PROVIDER_linux-libc-headers = "external-sourcery-toolchain"
PREFERRED_PROVIDER_linux-libc-headers-dev = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-initial = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}compilerlibs = "external-sourcery-toolchain"
PREFERRED_PROVIDER_libgcc = "external-sourcery-toolchain"
PREFERRED_PROVIDER_eglibc = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/libc = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/libintl = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/libiconv = "external-sourcery-toolchain"
PREFERRED_PROVIDER_glibc-thread-db = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers = "external-sourcery-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers-dev = "external-sourcery-toolchain"
PREFERRED_PROVIDER_gdbserver ??= "external-sourcery-toolchain"

# No need to re-compile the locale files
GLIBC_INTERNAL_USE_BINARY_LOCALE = "precompiled"
ENABLE_BINARY_LOCALE_GENERATION = ""

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR_HOST}"

# Point to the appropriate multilib sysroot from the external toolchain, whose
# files will be extracted into the OE sysroot
def exttc_run(d, cmd):
    try:
        return bb.process.run(cmd, shell=True, env={'PATH': d.getVar('PATH', True)})[0].rstrip()
    except (OSError, bb.process.CmdError):
        return ''

EXTERNAL_TOOLCHAIN_SYSROOT_CMD = "${TARGET_PREFIX}gcc ${TARGET_CC_ARCH} -print-sysroot"
EXTERNAL_TOOLCHAIN_SYSROOT ??= "${@exttc_run(d, EXTERNAL_TOOLCHAIN_SYSROOT_CMD)}"

# These bits are here temporarily to sidestep the need to use a separate set
# of tune files to pass the appropriate multilib selection arguments to the
# sourcery toolchain, as is needed to extract the sysroot content.
TUNE_CCARGS_append_x86 = " -msgxx-glibc"

CSL_MULTILIB_ARGS[ppce500] ?= "-te500v1"
CSL_MULTILIB_ARGS[ppce500mc] ?= "-te500mc"
CSL_MULTILIB_ARGS[ppce500v2] ?= "-te500v2"
CSL_MULTILIB_ARGS[ppce600] ?= "-te600"

def csl_multilib_arg(d):
    argument = d.getVarFlag('CSL_MULTILIB_ARGS', d.getVar('DEFAULTTUNE', True) or '')
    if argument:
        return argument
    else:
        return ''

EXTERNAL_TOOLCHAIN_SYSROOT_CMD += "${@csl_multilib_arg(d)}"


# Unfortunately, the CSL ia32 toolchain has non-prefixed binaries in its
# bindir (e.g. gcc, ld). To avoid this messing up our build, we avoid adding
# this bindir to our PATH, and instead add symlinks to the prefixed binaries
# to our staging toolchain bindir.

python toolchain_metadata_setup () {
    d = e.data

    l = d.createCopy()
    l.finalize()
    if os.path.exists(bb.data.expand('${EXTERNAL_TOOLCHAIN}/bin/gcc', l)):
        d.setVar('TOOLCHAIN_PATH_ADD', '')
}
addhandler toolchain_metadata_setup
toolchain_metadata_setup[eventmask] = "bb.event.ConfigParsed"

python toolchain_setup () {
    d = e.data

    if not d.getVar('TOOLCHAIN_PATH_ADD', True):
        populate_toolchain_links(d)
}
addhandler toolchain_setup
toolchain_setup[eventmask] = "bb.event.BuildStarted"

def populate_toolchain_links(d):
    import errno
    from glob import glob

    d = d.createCopy()
    d.finalize()

    pattern = d.expand('${EXTERNAL_TOOLCHAIN}/bin/${TARGET_PREFIX}*')
    files = glob(pattern)
    if not files:
        bb.fatal("Unable to populate toolchain binary symlinks in %s" % pattern)

    bindir = d.getVar('STAGING_BINDIR_TOOLCHAIN', True)
    bb.utils.mkdirhier(bindir)
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
