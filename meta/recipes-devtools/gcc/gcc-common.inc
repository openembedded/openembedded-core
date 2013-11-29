DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"

NATIVEDEPS = ""

inherit autotools gettext

def get_gcc_fpu_setting(bb, d):
    if d.getVar('ARMPKGSFX_EABI', True) == "hf" and d.getVar('TRANSLATED_TARGET_ARCH', True) == "arm":
        return "--with-float=hard"
    if d.getVar('TARGET_FPU', True) in [ 'soft' ]:
        return "--with-float=soft"
    if d.getVar('TARGET_FPU', True) in [ 'ppc-efd' ]:
        return "--enable-e500_double"
    return ""

get_gcc_fpu_setting[vardepvalue] = "${@get_gcc_fpu_setting(bb, d)}"

def get_gcc_mips_plt_setting(bb, d):
    if d.getVar('TRANSLATED_TARGET_ARCH', True) in [ 'mips', 'mipsel' ] and 'mplt' in d.getVar('DISTRO_FEATURES',1).split() :
        return "--with-mips-plt"
    return ""

def get_gcc_multiarch_setting(bb, d):
    target_arch = d.getVar('TRANSLATED_TARGET_ARCH', True)
    multiarch_options = {
        "i586":    "--enable-targets=all",
        "powerpc": "--enable-targets=powerpc64",
        "sparc":   "--enable-targets=all",
    }

    if 'multiarch' in d.getVar('DISTRO_FEATURES', True).split() :
        if target_arch in multiarch_options :
            return multiarch_options[target_arch]
    return ""

# this is used by the multilib setup of gcc
def get_tune_parameters(tune, d):
    availtunes = d.getVar('AVAILTUNES', True)
    if tune not in availtunes.split():
        bb.error('The tune: %s is not one of the available tunes: %s', tune or None, availtunes)

    localdata = bb.data.createCopy(d)
    override = ':tune-' + tune
    localdata.setVar('OVERRIDES', localdata.getVar('OVERRIDES', False) + override)
    bb.data.update_data(localdata)

    retdict = {}
    retdict['tune'] = tune
    retdict['ccargs'] = localdata.getVar('TUNE_CCARGS', True)
    retdict['features'] = localdata.getVar('TUNE_FEATURES', True)
    # BASELIB is used by the multilib code to change library paths
    retdict['baselib'] = localdata.getVar('BASE_LIB', True) or localdata.getVar('BASELIB', True)
    retdict['arch'] = localdata.getVar('TUNE_ARCH', True)
    retdict['abiextension'] = localdata.getVar('ABIEXTENSION', True)
    retdict['target_fpu'] = localdata.getVar('TARGET_FPU', True)
    retdict['pkgarch'] = localdata.getVar('TUNE_PKGARCH', True)
    retdict['package_extra_archs'] = localdata.getVar('PACKAGE_EXTRA_ARCHS', True)
    return retdict

get_tune_parameters[vardepsexclude] = "AVAILTUNES"

DEBIANNAME_${MLPREFIX}libgcc = "libgcc1"

MIRRORS =+ "\
${GNU_MIRROR}/gcc    ftp://gcc.gnu.org/pub/gcc/releases/ \n \
${GNU_MIRROR}/gcc	ftp://gd.tuwien.ac.at/gnu/gcc/ \n \
${GNU_MIRROR}/gcc	http://mirrors.rcn.net/pub/sourceware/gcc/releases/ \n \
${GNU_MIRROR}/gcc   http://gcc.get-software.com/releases/ \n \
${GNU_MIRROR}/gcc	http://gcc.get-software.com/releases/ \n \
"

#
# Set some default values
#
gcclibdir = "${libdir}/gcc"
BINV = "${PV}"
#S = "${WORKDIR}/gcc-${PV}"
S = "${TMPDIR}/work-shared/gcc-${PV}-${PR}/gcc-${PV}"
B = "${WORKDIR}/gcc-${PV}/build.${HOST_SYS}.${TARGET_SYS}"

# SS means Shared Stamps directory
SS = "${TMPDIR}/stamps/work-shared/gcc-${PV}-${PR}"
do_fetch[stamp-base] = "${SS}"
do_unpack[stamp-base] = "${SS}"
do_patch[stamp-base] = "${SS}"
SSCLEAN = "${TMPDIR}/stamps/work-shared/gcc-[0-9]*-*"
do_fetch[stamp-base-clean] = "${SSCLEAN}"
do_unpack[stamp-base-clean] = "${SSCLEAN}"
do_unpack[umask] = "022"
do_patch[stamp-base-clean] = "${SSCLEAN}"

# SW means Shared Work directory
SW = "${TMPDIR}/work-shared/gcc-${PV}-${PR}"
WORKDIR_task-unpack = "${SW}"
WORKDIR_task-patch = "${SW}"

target_includedir ?= "${includedir}"
target_libdir ?= "${libdir}"
target_base_libdir ?= "${base_libdir}"
target_prefix ?= "${prefix}"

CLEANFUNCS += "workshared_clean"
# The do_clean should be exclusive since share ${S}
do_clean[lockfiles] = "${SW}.clean.lock"

python workshared_clean () {
	"""clear the source directory"""
	dir = d.expand("${SW}")
	bb.note("Removing " + dir)
	oe.path.remove(dir)

	"""clear the the stamps in work-shared"""
	dir = "%s.*" % bb.data.expand(d.getVarFlag('do_fetch', 'stamp-base', True), d)
	bb.note("Removing " + dir)
	oe.path.remove(dir)
}

# We need to ensure that for the shared work directory, the do_patch singatures match
# The real WORKDIR location isn't a dependency for the shared workdir.
src_patches[vardepsexclude] = "WORKDIR"
should_apply[vardepsexclude] += "PN"
