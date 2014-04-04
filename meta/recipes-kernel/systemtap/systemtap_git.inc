LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
SRCREV = "ae91e3d552aff4f0e74662d055dae06ea55eb6bc"
PV = "2.5+git${SRCPV}"

SRC_URI = "git://sourceware.org/git/systemtap.git \
           file://docproc-build-fix.patch \
           file://obsolete_automake_macros.patch \
          "

FILESPATH = "${FILE_DIRNAME}/systemtap"

# systemtap doesn't support mips
COMPATIBLE_HOST = '(x86_64|i.86|powerpc|arm|aarch64).*-linux'

S = "${WORKDIR}/git"

# systemtap can't be built without optimization, if someone tries to compile an
# entire image as -O0, we override it with -O2 here and give a note about it.
def get_optimization(d):
    selected_optimization = d.getVar("SELECTED_OPTIMIZATION", True)
    if base_contains("SELECTED_OPTIMIZATION", "-O0", "x", "", d) == "x":
        bb.note("systemtap can't be built with -O0, -O2 will be used instead.")
        return selected_optimization.replace("-O0", "-O2")
    return selected_optimization

SELECTED_OPTIMIZATION := "${@get_optimization(d)}"
