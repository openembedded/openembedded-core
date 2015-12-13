SRCREV = "d1b81113b43a6d26dec4e0e58a380895d121006e"

PV = "1.0.9+git${SRCPV}"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/uclibc-git:"

SRC_URI = "git://uclibc-ng.org/git/uclibc-ng;branch=1.0 \
        file://0001-Disable-lrount_tes-function.patch \
        file://0001-Revert-glibc-compat-bump-glibc-minor-version.patch \
        file://0002-Add-implementation-for-copysignl-for-ppc.patch \
        file://0003-Add-argp-implementation.patch \
        file://libc-stdlib-canonicalize_file_name-memory-leak.patch \
        file://uClibc.machine \
        file://uClibc.distro \
        file://obstack.cfg \
        file://locale.cfg \
"
S = "${WORKDIR}/git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.\d+)*)"

