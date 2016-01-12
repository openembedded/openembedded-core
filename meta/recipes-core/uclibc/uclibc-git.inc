SRCREV = "94493929f674f4410c2743fa3dd82fd37b70c58f"

PV = "1.0.11+git${SRCPV}"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/uclibc-git:"

SRC_URI = "git://uclibc-ng.org/git/uclibc-ng;branch=1.0 \
        file://0001-Disable-lrount_tes-function.patch \
        file://uClibc.machine \
        file://uClibc.distro \
        file://obstack.cfg \
        file://locale.cfg \
"
S = "${WORKDIR}/git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.\d+)*)"

