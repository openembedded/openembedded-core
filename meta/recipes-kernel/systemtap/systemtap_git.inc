LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
SRCREV = "8f0fcd995f7f650a2ee0a94539f90c99e6d19e1d"
PV = "2.5+git${SRCPV}"

SRC_URI = "git://sourceware.org/git/systemtap.git \
           file://docproc-build-fix.patch \
           file://obsolete_automake_macros.patch \
           file://system_map_location.patch \
           file://tapset-linux-sendfile-syscall.patch \
           file://configure-allow-to-disable-libvirt.patch \
          "

# systemtap doesn't support mips
COMPATIBLE_HOST = '(x86_64|i.86|powerpc|arm|aarch64).*-linux'

S = "${WORKDIR}/git"

# systemtap can't be built without optimization, if someone tries to compile an
# entire image as -O0, we override it with -O2 here and give a note about it.
def get_optimization(d):
    selected_optimization = d.getVar("SELECTED_OPTIMIZATION", True)
    if bb.utils.contains("SELECTED_OPTIMIZATION", "-O0", "x", "", d) == "x":
        bb.note("systemtap can't be built with -O0, -O2 will be used instead.")
        return selected_optimization.replace("-O0", "-O2")
    return selected_optimization

SELECTED_OPTIMIZATION := "${@get_optimization(d)}"
