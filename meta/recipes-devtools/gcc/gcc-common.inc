DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"

NATIVEDEPS = ""

inherit autotools gettext

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"

def get_gcc_fpu_setting(bb, d):
    if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
        return "--with-float=soft"
    if bb.data.getVar('TARGET_FPU', d, 1) in [ 'ppc-efd' ]:
        return "--enable-e500_double"
    return ""

def get_gcc_mips_plt_setting(bb, d):
    if bb.data.getVar('TARGET_ARCH', d, 1) in [ 'mips', 'mipsel' ] and 'mplt' in bb.data.getVar('DISTRO_FEATURES',d,1).split() :
        return "--with-mips-plt"
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
S = "${TMPDIR}/work-shared/gcc-${PV}/gcc-${PV}-${PR}"
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
	dir = bb.data.expand("${SW}", d)
	bb.note("Removing " + dir)
	oe.path.remove(dir)

	"""clear the the stamps in work-shared"""
	dir = "%s.*" % bb.data.expand(d.getVarFlag('do_fetch', 'stamp-base', True), d)
	bb.note("Removing " + dir)
	oe.path.remove(dir)
}
