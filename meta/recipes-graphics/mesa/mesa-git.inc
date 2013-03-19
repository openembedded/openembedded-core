# Current pre-9.1 commit
SRCREV = "1665af3066f3d58c42e9d5b13098f13615a7672c"
PV = "9.1~git${SRCPV}"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=42d77d95cba529a3637129be87d6555d"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://0002-cross-compile.patch \
           "

S = "${WORKDIR}/git"
