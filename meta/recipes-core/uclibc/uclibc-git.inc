SRCREV = "0ad73077c230093ae004829da44418597f330c6a"

PV = "1.0.10+git${SRCPV}"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/uclibc-git:"

SRC_URI = "git://uclibc-ng.org/git/uclibc-ng;branch=1.0 \
        file://0001-Disable-lrount_tes-function.patch \
        file://0003-Add-argp-implementation.patch \
        file://uClibc.machine \
        file://uClibc.distro \
        file://obstack.cfg \
        file://locale.cfg \
"
S = "${WORKDIR}/git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.\d+)*)"

