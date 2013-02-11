LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCREV = "48fa6b5e1d3e636c34cbd5650370e6f634efc294"
PV = "1.8+git${SRCPV}"

SRC_URI = "git://sourceware.org/git/systemtap.git;protocol=git \
           file://docproc-build-fix.patch \
           file://obsolete_automake_macros.patch \
          "

FILESPATH = "${FILE_DIRNAME}/systemtap"

SRC_URI[md5sum]    = "cb202866ed704c44a876d041f788bdee"
SRC_URI[sha256sum] = "8ffe35caec0d937bd23fd78a3a8d94b58907cc0de0330b35e38f9f764815c459"

# systemtap doesn't support mips
COMPATIBLE_HOST = '(x86_64.*|i.86.*|powerpc.*|arm.*)-linux'

S = "${WORKDIR}/git"

