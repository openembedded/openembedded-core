DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"

NATIVEDEPS = ""

inherit autotools gettext

FILESDIR = "${@os.path.dirname(d.getVar('FILE',1))}/gcc-${PV}"

def get_gcc_fpu_setting(bb, d):
    if d.getVar('TARGET_FPU', True) in [ 'soft' ]:
        return "--with-float=soft"
    if d.getVar('TARGET_FPU', True) in [ 'ppc-efd' ]:
        return "--enable-e500_double"
    return ""

def get_gcc_mips_plt_setting(bb, d):
    if d.getVar('TARGET_ARCH', True) in [ 'mips', 'mipsel' ] and 'mplt' in d.getVar('DISTRO_FEATURES',1).split() :
        return "--with-mips-plt"
    return ""

def get_gcc_multiarch_setting(bb, d):
    target_arch = d.getVar('TARGET_ARCH', True)
    multiarch_options = {
        "i586":    "--enable-targets=all",
        "powerpc": "--enable-targets=powerpc64",
        "sparc":   "--enable-targets=all",
    }

    if 'multiarch' in d.getVar('DISTRO_FEATURES', True).split() :
        if target_arch in multiarch_options :
            return multiarch_options[target_arch]
    return ""

# We really need HOST_SYS here for some packages and TARGET_SYS for others.
# For now, libgcc is most important so we fix for that - RP.
SHLIBSDIR = "${STAGING_DIR_TARGET}/shlibs"

DEBIANNAME_libgcc = "libgcc1"

MIRRORS =+ "\
${GNU_MIRROR}/gcc/releases/    ftp://gcc.gnu.org/pub/gcc/releases/ \n \
${GNU_MIRROR}/gcc/	http://mirrors.rcn.net/pub/sourceware/gcc/releases/ \n \
${GNU_MIRROR}/gcc/releases/    http://gcc.get-software.com/releases/ \n \
${GNU_MIRROR}/gcc/	http://gcc.get-software.com/releases/ \n \
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
do_headerfix[stamp-base] = "${SS}"
do_patch[stamp-base] = "${SS}"

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

do_headerfix () {
	# Change the default dynamic linker path, in case $base_liddir is non-standard
	# (e.g. in multilib or sdk cases)
	#
	# We want something like the following:
	# 	#define GLIBC_DYNAMIC_LINKER64 "/lib64/ld-linux-x86-64.so.2"
	# becomes
	#	#define GLIBC_DYNAMIC_LINKER64 SYSTEMLIBS_DIR "ld-linux-x86-64.so.2"
	#
	sed -i ${S}/gcc/config/*/linux*.h -e \
		's#\(GLIBC_DYNAMIC_LINKER[^ ]*\) \( *"/lib.*\)/\(.*\)#\1 SYSTEMLIBS_DIR "\3#'
}

addtask headerfix after do_unpack before do_patch

do_headerfix[vardepvalue] = "PATH"

# We need to ensure that for the shared work directory, the do_patch singatures match
# The real WORKDIR location isn't a dependency for the shared workdir.
src_patches[vardepsexclude] = "WORKDIR"
should_apply[vardepsexclude] += "PN"
